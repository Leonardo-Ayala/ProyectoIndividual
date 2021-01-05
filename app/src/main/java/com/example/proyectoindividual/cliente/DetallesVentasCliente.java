package com.example.proyectoindividual.cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.proyectoindividual.Cliente;
import com.example.proyectoindividual.R;
import com.example.proyectoindividual.Venta;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetallesVentasCliente extends AppCompatActivity {

    Activity activity = this;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_ventas_cliente);

        setTitle("Venta");

        Intent intent = getIntent();
        Venta venta = (Venta) intent.getSerializableExtra("venta");

        TextView producto = findViewById(R.id.textViewProDetVentaCl);
        TextView cliente = findViewById(R.id.textViewClienteDetVentaCl);
        TextView facbol = findViewById(R.id.textViewFacBolDetVentaCl);
        TextView cantidad = findViewById(R.id.textViewCantidadDetVentaCl);
        TextView uidVendedor = findViewById(R.id.textViewUidVenDetVentaCl);
        TextView precioTotal = findViewById(R.id.textViewPrecioTotalDetVentaCl);
        ImageView imageView = findViewById(R.id.imageViewDetVentaCl);

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
}