package com.example.proyectoindividual.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.proyectoindividual.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.UUID;

public class AgregarImagen extends AppCompatActivity {

    Activity activity = this;
    Context context = this;

    ImageView selectImage;
    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_imagen);

        setTitle("Producto");

        Button btnSubir= findViewById(R.id.buttonSubirFoto);
        selectImage= findViewById(R.id.imageViewFoto);
        Button btnTomarFoto= findViewById(R.id.buttonTomarFoto);


        btnTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int permission = ContextCompat.checkSelfPermission(context,
                        Manifest.permission.CAMERA);

                if (permission == PackageManager.PERMISSION_GRANTED){
                    Intent camara= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camara,0);
                }else{
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.CAMERA},0);
                }
            }
        });


        btnSubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });


    }


    public void agregarImagen(View view){

        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission == PackageManager.PERMISSION_GRANTED) {

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(Intent.createChooser(intent, "Choose File"), 1);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            //Cancelado por el usuario
        }


        if ((resultCode == RESULT_OK) && (requestCode == 1)) {

            Intent intent = getIntent();
            String key = intent.getStringExtra("key");

            //Procesar el resultado
            Uri uri = data.getData();//obtener el uri content
            String fileName = "imagen";

            int permission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);

            if (permission == PackageManager.PERMISSION_GRANTED) {
                //Subir archivo a FireStorage
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();


                storageReference.child(key).child(fileName).putFile(uri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.d("infoapp", "Subida exitosa");
                                Toast.makeText(context, "Imagen Subida", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(context, MainAdmin.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("infoapp", "Error en Subida");
                        Toast.makeText(context, "Error en Subida de Imagen", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                });


            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            }


        }else if(requestCode==0 && resultCode==RESULT_OK){

            int permission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA);
            if (permission == PackageManager.PERMISSION_GRANTED) {

                image = (Bitmap) data.getExtras().get("data");
                selectImage.setImageBitmap(image);
                selectImage.setVisibility(View.VISIBLE);


            }else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, 1);

            }


            Button btnSubir = findViewById(R.id.buttonSubirFoto);
            btnSubir.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == 1) {
                agregarImagen(null);
            }else if (requestCode == 0){
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Intent camara= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camara,0);
                }else{
                    Toast.makeText(this, "Se requiere permiso para usar la cámara", Toast.LENGTH_SHORT).show();
                }

            }

        }
    }

    private  void upload(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG,100,stream);


        String fileName = "imagen";
        Intent intent = getIntent();
        String key = intent.getStringExtra("key");


        final String random = UUID.randomUUID().toString();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageReference.child(key).child(fileName);

        byte[] b = stream.toByteArray();
        imageRef.putBytes(b).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri downloadUri = uri;
                    }
                });

                startActivity(new Intent(context, MainAdmin.class));
                Toast.makeText(context, "Se subió la  captura  exitosamente", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("infoapp", "Error en Subida");
                Toast.makeText(context, "Error en Subida de Imagen", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });
    }

}