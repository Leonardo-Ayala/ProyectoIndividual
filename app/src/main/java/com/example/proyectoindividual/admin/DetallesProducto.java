package com.example.proyectoindividual.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.proyectoindividual.Producto;
import com.example.proyectoindividual.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetallesProducto extends AppCompatActivity {

    Activity activity = this;
    Context context = this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_producto);

        setTitle("Producto");

        Intent intent = getIntent();
        Producto producto = (Producto) intent.getSerializableExtra("producto");

        Log.d("infoapp", producto.getMarca());

        EditText marca = findViewById(R.id.editTextMarcaDet);
        EditText tipo = findViewById(R.id.editTextTipoDet);
        EditText descripcion = findViewById(R.id.editTextDescripcionDet);
        EditText stock = findViewById(R.id.editTextStockDet);
        EditText precio = findViewById(R.id.editTextPrecioDet);

        marca.setText(producto.getMarca());
        tipo.setText(producto.getTipo());
        descripcion.setText(producto.getDescripcion());
        stock.setText(producto.getStock());
        precio.setText(producto.getPrecio());

        StorageReference reference = FirebaseStorage.getInstance().getReference().child(producto.getKey()).child("imagen");
        ImageView imageView = findViewById(R.id.imageViewDetalles);
        Glide.with(this).load(reference).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(imageView);

    }

    public void actualizar(View view){

        Intent inte = getIntent();
        Producto product = (Producto) inte.getSerializableExtra("producto");

        EditText marca = findViewById(R.id.editTextMarcaDet);
        EditText tipo = findViewById(R.id.editTextTipoDet);
        EditText descripcion = findViewById(R.id.editTextDescripcionDet);
        EditText stock = findViewById(R.id.editTextStockDet);
        EditText precio = findViewById(R.id.editTextPrecioDet);
        Intent intent = getIntent();
        Producto pro = (Producto) intent.getSerializableExtra("producto");

        Producto producto = new Producto(marca.getText().toString(),tipo.getText().toString(),descripcion.getText().toString(),stock.getText().toString(),precio.getText().toString(),product.getKey());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("productos").child(pro.getKey()).setValue(producto).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Producto actualizado", Toast.LENGTH_SHORT).show();
            }
        });

        startActivity(new Intent(activity,MainAdmin.class));
        finish();

    }

    public void actualizarfoto(View view){
        Intent intent = getIntent();
        Producto pro = (Producto) intent.getSerializableExtra("producto");

        Intent intent2 = new Intent(activity, AgregarImagen.class);
        intent2.putExtra("key", pro.getKey());
        startActivity(intent2);
        finish();

    }

    public void eliminarProducto(View view){

        Intent intent = getIntent();
        Producto pro = (Producto) intent.getSerializableExtra("producto");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("productos").child(pro.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Producto eliminado", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(activity, MainAdmin.class));
                finish();
            }
        });


    }

}