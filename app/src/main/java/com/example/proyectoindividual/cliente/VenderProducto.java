package com.example.proyectoindividual.cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.controls.templates.TemperatureControlTemplate;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectoindividual.Producto;
import com.example.proyectoindividual.R;
import com.example.proyectoindividual.Venta;
import com.example.proyectoindividual.admin.AgregarImagen;
import com.example.proyectoindividual.admin.AgregarProducto;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VenderProducto extends AppCompatActivity {

    Activity activity = this;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vender_producto);

        setTitle("Producto");

        Intent intent = getIntent();
        Producto producto = (Producto) intent.getSerializableExtra("producto");

        TextView info = findViewById(R.id.textViewProductoVender);
        info.setText(producto.getMarca() + " " + producto.getTipo());
        TextView precio = findViewById(R.id.textViewProductoPrecio);
        precio.setText("Precio Unit. S/." + producto.getPrecio());
        TextView stock = findViewById(R.id.textViewProductoStockVender);
        stock.setText("Stock: " + producto.getStock());

    }

    public void confimar(View view) {

        Intent intent = getIntent();
        Producto producto = (Producto) intent.getSerializableExtra("producto");

        EditText nombre = findViewById(R.id.editTextNombre);
        EditText dni = findViewById(R.id.editTextDNI);
        EditText facBol = findViewById(R.id.editTextFacturaBoleta);
        EditText cantidad = findViewById(R.id.editTextCantidad);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        boolean validar = true;

        if((nombre.getText().toString()).isEmpty()){
            nombre.setError("No puede estar vacio");
            validar = false;
        }
        if(dni.getText().toString().isEmpty()){
            dni.setError("No puede estar vacio");
            validar = false;
        }
        if(facBol.getText().toString().isEmpty()){
            facBol.setError("No puede estar vacio");
            validar = false;
        }
        if(cantidad.getText().toString().isEmpty()){
            cantidad.setError("No puede estar vacio");
            validar = false;
        }else{
            Integer disponible = Integer.valueOf(producto.getStock());
            Integer cant = Integer.valueOf(cantidad.getText().toString());
            if((disponible-cant) < 0){
                cantidad.setError("Cantidad NO disponible");
                validar = false;
            }
        }


        if(validar){

            Integer disponible = Integer.valueOf(producto.getStock());
            Integer cant = Integer.valueOf(cantidad.getText().toString());

            producto.setStock(String.valueOf(disponible-cant));

            databaseReference.child("productos").child(producto.getKey()).setValue(producto);



            String key = databaseReference.child("ventas").push().getKey();

            Log.d("infoapp", key);

            Venta venta = new Venta(producto, nombre.getText().toString(), dni.getText().toString(), facBol.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(),cantidad.getText().toString(),key);


            databaseReference.child("ventas").child(key).setValue(venta).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(context, "Venta exitosa", Toast.LENGTH_SHORT).show();
                }
            });

            startActivity(new Intent(activity, MainCliente.class));
            finish();

        }

    }
}