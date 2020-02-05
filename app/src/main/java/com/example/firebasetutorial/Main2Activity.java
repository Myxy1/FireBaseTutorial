package com.example.firebasetutorial;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class Main2Activity extends AppCompatActivity {

    private Button buttonFeltoltes, buttonKepValasztas;
    private EditText editTextFileNev;
    private ImageView imageView;
    private ProgressBar progressBar;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private Uri imageUri;

    private static final int request = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        init();
        buttonKepValasztas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KepValasztas();
            }
        });
        buttonFeltoltes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Feltoltes();
            }
        });
    }
    //ezzel mondjuk meg, hogy milyen típusú a kép formátumunk
    private String getFileExtension(Uri uri)
    {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void Feltoltes() {

        if (imageUri != null)
        {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." +
                    getFileExtension(imageUri));

            StorageTask storageTask = fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(Main2Activity.this, "Feltöltés sikeres", Toast.LENGTH_SHORT).show();
                            File file = new File(editTextFileNev.getText().toString(),
                                    "URL");
                            String egyediAzonosito = databaseReference.push().getKey();
                            databaseReference.child(egyediAzonosito).setValue(file);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);
                        }
                    });
        }else
        {
            Toast.makeText(this, "Nincsen fájl kijelölve", Toast.LENGTH_SHORT).show();
        }
    }

    private void KepValasztas() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == request && resultCode == RESULT_OK && data != null
         && data.getData() != null)
        {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    public void init()
    {
        buttonFeltoltes = findViewById(R.id.buttonFeltoltes);
        buttonKepValasztas = findViewById(R.id.buttonKepValasztas);
        editTextFileNev = findViewById(R.id.editTextFileNev);
        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progress_horizontal);

        databaseReference = FirebaseDatabase.getInstance().getReference("Feltöltések");
        storageReference = FirebaseStorage.getInstance().getReference("Feltöltések");
    }
}
