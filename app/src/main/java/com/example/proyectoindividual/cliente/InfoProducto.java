package com.example.proyectoindividual.cliente;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.proyectoindividual.Producto;
import com.example.proyectoindividual.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class InfoProducto extends AppCompatActivity {

    Activity activity = this;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_producto);

        setTitle("Producto");

        Intent intent = getIntent();
        Producto producto = (Producto) intent.getSerializableExtra("producto");

        TextView marca = findViewById(R.id.textViewInfoMarca);
        TextView tipo = findViewById(R.id.textViewInfoTipo);
        TextView descripcion = findViewById(R.id.textViewInfoDescripcion);
        TextView stock = findViewById(R.id.textViewInfoStock);
        TextView precio = findViewById(R.id.textViewInfoPrecio);

        marca.setText(producto.getMarca());
        tipo.setText(producto.getTipo());
        descripcion.setText(producto.getDescripcion());
        stock.setText("Stock: "+producto.getStock());
        precio.setText("S/."+producto.getPrecio());

        StorageReference reference = FirebaseStorage.getInstance().getReference().child(producto.getKey()).child("imagen");
        ImageView imageView = findViewById(R.id.imageViewInfoProducto);
        Glide.with(this).load(reference).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(imageView);
    }

    public void vender(View view){

        Intent intent = getIntent();
        Producto producto = (Producto) intent.getSerializableExtra("producto");

        Intent intent2 = new Intent(activity, VenderProducto.class);
        intent2.putExtra("producto", producto);
        startActivity(intent2);
        finish();

    }
}