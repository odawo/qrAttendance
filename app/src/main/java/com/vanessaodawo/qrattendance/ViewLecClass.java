package com.vanessaodawo.qrattendance;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import Fragments.ScanningFragment;
import POJO.RecyclerAdapterLecClasses;
import POJO.RecyclerAdapterVLC;
import POJO.Students;
import POJO.Units;

public class ViewLecClass extends AppCompatActivity {

    EditText studentEmail;
    Button addButton;
    ImageView imgvQR;
    Spinner classPeriod;
    TextView classDate, className, classID;

    RecyclerView recycle;
    List<Students> list;

    FloatingActionButton addStudent;
    FloatingActionButton markRegister;
    FloatingActionButton scanQR;

    FirebaseAuth firebaseAuth;
    DatabaseReference studReference;
    DatabaseReference dateReference;

    String[] hours_per_unit;
    String TAG;

    Uri URI = null;

    IntentIntegrator integrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_lec_class);

        addStudent = findViewById(R.id.fabaddStudent);
        markRegister = findViewById(R.id.fabmarkRegister);
        scanQR = findViewById(R.id.fabscanQRCode);

        integrator = new IntentIntegrator(this);

        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewStudent();
            }
        });

        markRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeAttendance();
            }
        });

        scanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewLecClass.this, ScanningFragment.class));
                finish();
            }
        });

        studReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                list = new ArrayList<Students>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Students value = dataSnapshot1.getValue(Students.class);
                    Students students1 = new Students();

                    String student_email = value.getStudent_class_email();

                    students1.setStudent_class_email(student_email);

                    list.add(students1);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled: Unable to obtain values. Refresh", databaseError.toException());
            }
        });

        classID =findViewById(R.id.tvclassId);
        String iddata = getIntent().getExtras().getString("classid");
        classID.setText(iddata);

        RecyclerAdapterVLC recyclerAdapter = new RecyclerAdapterVLC(list, ViewLecClass.this);
        RecyclerView.LayoutManager recyce = new LinearLayoutManager(ViewLecClass.this);  //new GridLayoutManager(viewproduct.this, 2) ... 2 is number or rowns
        recycle.setLayoutManager(recyce);
        recycle.setItemAnimator(new DefaultItemAnimator()); //recycclerview.itemanimator..
        recycle.setAdapter(recyclerAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri qrimage = data.getData();

    }

    /**
     * GENERATE QR CODE
     * SEND THE QR TO EMAILS
     * SAVE THIS PART TO THE DATABASE
     * **/
    private void takeAttendance() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_take_attendance, null);
        builder.setView(view);

        imgvQR = findViewById(R.id.qrCodeImg);
        classPeriod = findViewById(R.id.unitPeriod);
        classDate = findViewById(R.id.classunitName);
        className = findViewById(R.id.classDate);

        hours_per_unit = getResources().getStringArray(R.array.unit_periods);
        classPeriod.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_item, hours_per_unit));

        builder.setPositiveButton("SEND", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

//                generate unique qr code for the class and send to the email.

                String dataemails = getIntent().getStringExtra("emails");

                String aname  = className.getText().toString().trim();
                String bdate  = classDate.getText().toString().trim();
                String cperiod = classPeriod.getSelectedItem().toString().trim();

                Students stud_student = new Students();
                stud_student.setStudent_class_name(aname);
                stud_student.setStudent_class_date(Date.valueOf(bdate));
                stud_student.setStudent_class_period(Time.valueOf(cperiod));

                if (aname.isEmpty() || bdate.isEmpty() || cperiod.isEmpty()) {
                    Toast.makeText(ViewLecClass.this, "fill empty fields", Toast.LENGTH_SHORT).show();
                } else {

                    /**
                     * generate qr code per class selection with specific data
                     * **/
                    String datasent = "Class : " + aname + ", Date : " + bdate + ", Period : " + cperiod ;
                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    try {
                        Log.d(TAG, "onClick: LOADING THE QR POINT REACHED");
                        BitMatrix bitMatrix = multiFormatWriter.encode(datasent, BarcodeFormat.QR_CODE,200,200);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        imgvQR.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }

                    /**
                     * send email to the student emails..
                     * obtain email names from the recycler adapter class
                     *
                     * **/
//                send email to the emails on recycler view. or in database for the class
                    try {
                        Log.d(TAG, "onClick: EMAIL POINT REACHED");
                        Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {dataemails});
                        if (URI != null) {
                            emailIntent.putExtra(Intent.EXTRA_STREAM, URI);
                        }
                        startActivity(Intent.createChooser(emailIntent, "SENDING EMAIL."));

                    } catch (Throwable t) {
                        Toast.makeText(ViewLecClass.this, "PROCESS FAILURE. RETRY!", Toast.LENGTH_SHORT).show();
                    }

                    /**
                     * save the data: date, unit data, indivual emails
                     * **/
//                save all this data to db, date, , unitid, unit name, period hours, plus each individual email sent with it
                    if (firebaseAuth.getCurrentUser() != null) {
                        dateReference = FirebaseDatabase.getInstance().getReference();
                        dateReference.child("LECTURERS").child(firebaseAuth.getCurrentUser().getUid()).child("CLASSES")
                                .child("SENTEMAILS").setValue(stud_student, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    Log.d(TAG, "onComplete: DATA SAVING POINT");
                                    Toast.makeText(ViewLecClass.this, "DATA SAVED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(ViewLecClass.this, "failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }

            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                startActivity(new Intent(ViewLecClass.this, ViewLecClass.class));
                finish();
            }
        });
    }

    private void addNewStudent() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_add_student, null);
        builder.setView(view);

        studentEmail = findViewById(R.id.studentEmail);
        addButton = findViewById(R.id.btnAddStud);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String studmail = studentEmail.getText().toString().trim();

                Students students2 = new Students();
                students2.setStudent_class_email(studmail);

                if (studmail.isEmpty()) {
                    Toast.makeText(ViewLecClass.this, "fill empty field", Toast.LENGTH_SHORT).show();
                } else {
                    studReference = FirebaseDatabase.getInstance().getReference();

                    studReference.child("LECTURERS").child(firebaseAuth.getCurrentUser().getUid()).child("CLASSES")
                            .child("STUDENTS").setValue(students2, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                Toast.makeText(ViewLecClass.this, "STUDENT ADDED", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ViewLecClass.this, "ERROR OCCURRED. RETRY!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }

}
