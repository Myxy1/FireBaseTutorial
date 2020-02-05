package com.example.firebasetutorial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class Main3Activity extends AppCompatActivity {

    private Button buttonBe;
    private EditText editBeEmail, editBeJelszo;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        init();

        buttonBe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editBeEmail.getText().toString().isEmpty() ||
                        editBeJelszo.getText().toString().isEmpty())
                {
                    Toast.makeText(Main3Activity.this, "Minden mezőt ki kell tölteni", Toast.LENGTH_SHORT).show();
                }else
                {
                    mAuth.signInWithEmailAndPassword(editBeEmail.getText().toString(),editBeJelszo.getText().toString())
                            .addOnCompleteListener(Main3Activity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful())
                                    {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if (!user.isEmailVerified())
                                        {
                                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(Main3Activity.this, "Erősítsd meg az emailed", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }else
                                        {
                                            Toast.makeText(Main3Activity.this, "Hello " + user.getEmail(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else
                                        Toast.makeText(Main3Activity.this, "Hibás email cím vagy jelszo!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }

    public void init()
    {
        buttonBe = findViewById(R.id.buttonBe);
        editBeEmail = findViewById(R.id.editBeEmail);
        editBeJelszo = findViewById(R.id.editBeJelszo);

        mAuth = FirebaseAuth.getInstance();
    }
}
