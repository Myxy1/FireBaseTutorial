package com.example.firebasetutorial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Button buttonKuldes, buttonKep, buttonBejelentkez;
    private EditText editFelhasznaloNev, editJelszo, editEmail;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private Tagok tagok;

    private long maxid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        buttonKuldes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editFelhasznaloNev.getText().toString().isEmpty() ||
                        editJelszo.getText().toString().isEmpty() ||
                        editEmail.getText().toString().isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Minden mezőt ki kell tölteni", Toast.LENGTH_SHORT).show();
                }else
                {
                    tagok.setFelhasznalonev(editFelhasznaloNev.getText().toString());
                    tagok.setJelszo(editJelszo.getText().toString());
                    tagok.setEmail(editEmail.getText().toString());
                    databaseReference.child(String.valueOf(maxid+1)).setValue(tagok);

                    firebaseAuth.createUserWithEmailAndPassword(editEmail.getText().toString(),editJelszo.getText().toString())
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful())
                                    {
                                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                        if (!firebaseUser.isEmailVerified())
                                        {
                                            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(MainActivity.this, "Erősítsd meg az emailcímed", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(MainActivity.this,Main3Activity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                        }else
                                            Toast.makeText(MainActivity.this, "Sikertelen regisztráció", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        buttonKep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonBejelentkez.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Main3Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void init()
    {
        buttonKuldes = findViewById(R.id.buttonKuldes);
        buttonKep = findViewById(R.id.buttonKep);
        buttonBejelentkez = findViewById(R.id.buttonBejelentkez);
        editFelhasznaloNev = findViewById(R.id.editFelhasznaloNev);
        editJelszo = findViewById(R.id.editJelszo);
        editEmail = findViewById(R.id.editEmail);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Felhaszálók");
        firebaseAuth = FirebaseAuth.getInstance();
        tagok = new Tagok();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    maxid = dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
