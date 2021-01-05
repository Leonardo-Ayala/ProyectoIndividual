package com.example.proyectoindividual;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.proyectoindividual.admin.MainAdmin;
import com.example.proyectoindividual.cliente.MainCliente;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Activity activity = this;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        validarUsuario();
        ImageView imageView = findViewById(R.id.imageViewMain);
        imageView.setImageResource(R.drawable.tienda);
    }


    public void IniciarSession (View view){

        List<AuthUI.IdpConfig> proveedores = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );


        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setLogo(R.drawable.tienda)
                .setAvailableProviders(proveedores)
                .build(),1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1){
            validarUsuario();
        }
    }

    public void validarUsuario(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


        if(currentUser!=null){
            currentUser.reload();
            if(currentUser.isEmailVerified()){


                String uid = currentUser.getUid();

                if (currentUser.getEmail().contentEquals("a20125980@pucp.edu.pe")){
                    startActivity(new Intent(context, MainAdmin.class));
                    finish();
                }else{
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("clientes").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //Log.d("infoapp",snapshot.getValue().toString());
                            if (snapshot.getValue() != null) {

                                Cliente clienteDTO = snapshot.getValue(Cliente.class);

                                Log.d("infoapp","Nombre: "+clienteDTO.getNombre());

                                startActivity(new Intent(context, MainCliente.class));
                                finish();

                            }else{
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                Cliente clienteDTO = new Cliente();
                                clienteDTO.setNombre(currentUser.getDisplayName());
                                clienteDTO.setEmail(currentUser.getEmail());

                                databaseReference.child("clientes").child(currentUser.getUid()).setValue(clienteDTO)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("infoapp","Guardado de usuario exitoso");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                e.printStackTrace();
                                            }
                                        });

                                startActivity(new Intent(context, MainCliente.class));
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }




            }else{
                currentUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context,"Se le ha enviado un correo para verificar su cuenta.",Toast.LENGTH_LONG).show();
                    }
                });



            }

        }
    }
}

