package com.example.proyectoindividual.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.proyectoindividual.Cliente;
import com.example.proyectoindividual.Producto;
import com.example.proyectoindividual.R;
import com.example.proyectoindividual.Venta;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetalleVentas extends AppCompatActivity {

    Activity activity = this;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_ventas);

        setTitle("Venta");

        Intent intent = getIntent();
        Venta venta = (Venta) intent.getSerializableExtra("venta");

        TextView producto = findViewById(R.id.textViewProDetVenta);
        TextView cliente = findViewById(R.id.textViewClienteDetVenta);
        TextView facbol = findViewById(R.id.textViewFacBolDetVenta);
        TextView cantidad = findViewById(R.id.textViewCantidadDetVenta);
        TextView uidVendedor = findViewById(R.id.textViewUidVenDetVenta);
        TextView precioTotal = findViewById(R.id.textViewPrecioTotalDetVenta);
        ImageView imageView = findViewById(R.id.imageViewDetVenta);

        StorageReference reference = FirebaseStorage.getInstance().getReference().child(venta.getProducto().getKey()).child("imagen");
        Glide.with(this).load(reference).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(imageView);

        producto.setText(venta.getProducto().getMarca()+" "+venta.getProducto().getTipo());
        cliente.setText("Cliente: "+venta.getNombreCliente());
        facbol.setText("N° Factura o Boleta: "+venta.getFacbol());
        cantidad.setText("Cantidad : "+venta.getCantidad());

        Integer cant = Integer.valueOf(venta.getCantidad());
        Integer precio = Integer.valueOf(venta.getProducto().getPrecio());
        precioTotal.setText("Precio Total S/."+(cant*precio));

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("clientes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot children : snapshot.getChildren()){
                    Cliente cliente = children.getValue(Cliente.class);
                    String key = children.getKey();
                    if(key.contentEquals(venta.getUidVendedor())){
                        uidVendedor.setText("Vendedor: "+cliente.getNombre());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void eliminarVenta(View view){
        Intent intent = getIntent();
        Venta venta = (Venta) intent.getSerializableExtra("venta");

        Log.d("infoapp", venta.getKey());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("ventas").child(venta.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Venta cancelada", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(activity, HistorialAdmin.class));
                finish();
            }
        });

    }
}