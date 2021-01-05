package com.example.proyectoindividual.cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.proyectoindividual.Producto;
import com.example.proyectoindividual.R;
import com.example.proyectoindividual.RecyclerViewAdaptador;
import com.example.proyectoindividual.Venta;
import com.example.proyectoindividual.admin.DetalleVentas;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class HistorialCliente extends AppCompatActivity {

    Activity activity = this;
    Context context = this;
    private RecyclerView recyclerViewProducto;
    private RecyclerViewAdaptador recyclerViewAdaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_cliente);

        setTitle("Historial de mis ventas");

        List<Producto> productoLista = new ArrayList<>();
        List<Venta> ventaLista = new ArrayList<>();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("ventas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productoLista.clear();
                for(DataSnapshot children : snapshot.getChildren()){
                    Venta venta = children.getValue(Venta.class);
                    Producto producto = venta.getProducto();
                    StorageReference reference = FirebaseStorage.getInstance().getReference().child(producto.getKey()).child("imagen");
                    if(venta.getUidVendedor().contentEquals(currentUser.getUid())){
                        productoLista.add(new Producto(producto.getMarca(),producto.getTipo(),producto.getDescripcion(),"Cliente: "+venta.getNombreCliente(),reference,children.getKey(),producto.getPrecio()));
                        ventaLista.add(venta);
                    }
                }

                recyclerViewProducto = (RecyclerView)findViewById(R.id.recyclerViewHistCliente);
                recyclerViewProducto.setLayoutManager(new LinearLayoutManager(context));
                recyclerViewAdaptador = new RecyclerViewAdaptador(productoLista,activity);


                recyclerViewAdaptador.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Venta ven = new Venta();
                        ven.setProducto(ventaLista.get(recyclerViewProducto.getChildAdapterPosition(v)).getProducto());
                        ven.setCantidad(ventaLista.get(recyclerViewProducto.getChildAdapterPosition(v)).getCantidad());
                        ven.setDni(ventaLista.get(recyclerViewProducto.getChildAdapterPosition(v)).getDni());
                        ven.setFacbol(ventaLista.get(recyclerViewProducto.getChildAdapterPosition(v)).getFacbol());
                        ven.setNombreCliente(ventaLista.get(recyclerViewProducto.getChildAdapterPosition(v)).getNombreCliente());
                        ven.setUidVendedor(ventaLista.get(recyclerViewProducto.getChildAdapterPosition(v)).getUidVendedor());
                        ven.setKey(ventaLista.get(recyclerViewProducto.getChildAdapterPosition(v)).getKey());


                        Intent intent = new Intent(activity, DetallesVentasCliente.class);
                        intent.putExtra("venta", ven);
                        startActivity(intent);
                        //finish();
                    }
                });

                recyclerViewProducto.setAdapter(recyclerViewAdaptador);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}