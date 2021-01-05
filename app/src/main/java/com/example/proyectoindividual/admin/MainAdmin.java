package com.example.proyectoindividual.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.proyectoindividual.MainActivity;
import com.example.proyectoindividual.Producto;
import com.example.proyectoindividual.R;
import com.example.proyectoindividual.RecyclerViewAdaptador;
import com.example.proyectoindividual.Venta;
import com.example.proyectoindividual.cliente.MainCliente;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MainAdmin extends AppCompatActivity {

    Activity activity = this;
    Context context = this;
    private RecyclerView recyclerViewProducto;
    private RecyclerViewAdaptador recyclerViewAdaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        setTitle("Lista de Productos");

        List<Producto> productoLista = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("productos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productoLista.clear();
                for(DataSnapshot children : snapshot.getChildren()){
                    Producto producto = children.getValue(Producto.class);
                    StorageReference reference = FirebaseStorage.getInstance().getReference().child(children.getKey()).child("imagen");
                    productoLista.add(new Producto(producto.getMarca(),producto.getTipo(),producto.getDescripcion(),producto.getStock(),reference,children.getKey(),producto.getPrecio()));
                }

                recyclerViewProducto = (RecyclerView)findViewById(R.id.recyclerViewAdmin);
                recyclerViewProducto.setLayoutManager(new LinearLayoutManager(context));
                recyclerViewAdaptador = new RecyclerViewAdaptador(productoLista,activity);


                recyclerViewAdaptador.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Producto pro = new Producto();
                        pro.setKey(productoLista.get(recyclerViewProducto.getChildAdapterPosition(v)).getKey());
                        pro.setMarca(productoLista.get(recyclerViewProducto.getChildAdapterPosition(v)).getMarca());
                        pro.setTipo(productoLista.get(recyclerViewProducto.getChildAdapterPosition(v)).getTipo());
                        pro.setStock(productoLista.get(recyclerViewProducto.getChildAdapterPosition(v)).getStock());
                        pro.setDescripcion(productoLista.get(recyclerViewProducto.getChildAdapterPosition(v)).getDescripcion());
                        pro.setPrecio(productoLista.get(recyclerViewProducto.getChildAdapterPosition(v)).getPrecio());

                        //intent.putExtra("producto", productoLista.get(recyclerViewProducto.getChildAdapterPosition(v)));

                        Intent intent = new Intent(activity, DetallesProducto.class);
                        intent.putExtra("producto", pro);
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

    public void AgregarProducto(View view){

        startActivity(new Intent(context, AgregarProducto.class));
        //finish();

    }

    public void cerrarSesion(View view){

        AuthUI.getInstance().signOut(this).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(context, MainActivity.class));
                finish();
            }
        });

    }

    public void historial(View view){
        startActivity(new Intent(context, HistorialAdmin.class));
        //finish();
    }
}