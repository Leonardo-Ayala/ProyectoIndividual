package com.example.proyectoindividual.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyectoindividual.Producto;
import com.example.proyectoindividual.R;
import com.example.proyectoindividual.cliente.MainCliente;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AgregarProducto extends AppCompatActivity {

    Activity activity = this;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto);

        setTitle("Producto");
    }

    public void AgregarProducto(View view){

        EditText marca = findViewById(R.id.editTextMarca);
        EditText tipo = findViewById(R.id.editTextTipo);
        EditText descripcion = findViewById(R.id.editTextDescripcion);
        EditText stock = findViewById(R.id.editTextStock);
        EditText precio = findViewById(R.id.editTextPrecio);

        boolean validar = true;

        if((marca.getText().toString()).isEmpty()){
            marca.setError("No puede estar vacio");
            validar = false;
        }
        if(tipo.getText().toString().isEmpty()){
            tipo.setError("No puede estar vacio");
            validar = false;
        }
        if(descripcion.getText().toString().isEmpty()){
            descripcion.setError("No puede estar vacio");
            validar = false;
        }
        if(stock.getText().toString().isEmpty()){
            stock.setError("No puede estar vacio");
            validar = false;
        }
        if(precio.getText().toString().isEmpty()){
            precio.setError("No puede estar vacio");
            validar = false;
        }

        if(validar){

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

            String key = databaseReference.child("productos").push().getKey();

            Log.d("infoapp",key);

            Producto producto = new Producto(marca.getText().toString(),tipo.getText().toString(),descripcion.getText().toString(),stock.getText().toString(),precio.getText().toString(),key);


            databaseReference.child("productos").child(key).setValue(producto).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(AgregarProducto.this, "Producto agregado", Toast.LENGTH_SHORT).show();
                }
            });

            Intent intent = new Intent(activity, AgregarImagen.class);
            intent.putExtra("key", key);
            startActivity(intent);
            finish();

        }

    }
}