package com.vanessaodawo.qrattendance;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import POJO.Lecturer;

public class Registration extends AppCompatActivity {

    EditText lecID, lecName, lecPassword, lecEmail;
    Button register;
    TextView login;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    DatabaseReference lecReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        lecID = findViewById(R.id.lecID);
        lecName = findViewById(R.id.lecName);
        lecPassword = findViewById(R.id.lecPassword);
        lecEmail = findViewById(R.id.lecEmail);
        register = findViewById(R.id.btnRegister);
        login = findViewById(R.id.tvLogin);

        firebaseAuth = FirebaseAuth.getInstance();
        lecReference = FirebaseDatabase.getInstance().getReference();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this, Login.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerLec();
            }
        });
    }

    private void registerLec() {

        String id = lecID.getText().toString().trim();
        String name = lecName.getText().toString().trim();
        String email = lecEmail.getText().toString().trim();
        String password = lecPassword.getText().toString().trim();

        if (id.isEmpty()||name.isEmpty()||email.isEmpty()||password.isEmpty()) {

            Toast.makeText(this, "Kindly fill all empty spaces", Toast.LENGTH_SHORT).show();

        } else {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Registration.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        savetoDB();
                        firebaseUser = firebaseAuth.getCurrentUser();
                        updateUI(firebaseUser);

                        startActivity(new Intent(Registration.this, MainActivity.class));

                    } else {
                        Toast.makeText(Registration.this, "Registration process failed. Retry", Toast.LENGTH_SHORT).show();

                        updateUI(null);
                        lecID.setText("");
                        lecName.setText("");
                        lecName.setText("");
                        lecPassword.setText("");

                    }
                }
            });
        }

    }

    private void savetoDB() {

        String email = lecEmail.getText().toString().trim();
        String name = lecName.getText().toString().trim();
        int id = Integer.parseInt(lecID.getText().toString().trim());

        Lecturer lecturer = new Lecturer();
        lecturer.setEmail(email);
        lecturer.setName(name);
        lecturer.setId(id);

        lecReference.child("LECTURER").setValue(lecturer, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {

                    Toast.makeText(Registration.this, "USER ADDED",Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    Toast.makeText(Registration.this, "USER NOT ADDED!",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }

    private void updateUI(FirebaseUser firebaseUser) {
        if(firebaseUser != null){
            startActivity(new Intent(Registration.this, MainActivity.class));
        }
    }
}
