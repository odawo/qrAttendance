package com.vanessaodawo.qrattendance;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import POJO.RecyclerAdapterLecClasses;
import POJO.Units;

public class LecClasses extends AppCompatActivity {

    EditText id, name, start, end, hours;
    FloatingActionButton fabAddNewClass;

    RecyclerView recycle;
    List<Units> list;

    FirebaseAuth mAuth;
    DatabaseReference classReference;

    String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lec_classes);


        fabAddNewClass = findViewById(R.id.fab_classes);
        fabAddNewClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Animation animation = AnimationUtils.loadAnimation(R.anim.);   bounce animator from evgenii
                addNewClass();
            }
        });

        classReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                list = new ArrayList<Units>();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Units value = dataSnapshot1.getValue(Units.class);
                    Units units1 = new Units();

                    int uunitid = value.getUnitID();
                    String uunitname = value.getUnitName();
                    Time uunithrs = value.getUnitHours();

                    units1.setUnitID(uunitid);
                    units1.setUnitName(uunitname);
                    units1.setUnitHours(uunithrs);

                    list.add(units1);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled: Unable to obtain values. Refresh", databaseError.toException());
            }
        });

        RecyclerAdapterLecClasses recyclerAdapter = new RecyclerAdapterLecClasses(list, LecClasses.this);
        RecyclerView.LayoutManager recyce = new LinearLayoutManager(LecClasses.this);  //new GridLayoutManager(viewproduct.this, 2) ... 2 is number or rowns
        recycle.setLayoutManager(recyce);
        recycle.setItemAnimator(new DefaultItemAnimator()); //recycclerview.itemanimator..
        recycle.setAdapter(recyclerAdapter);

    }

    private void addNewClass() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater  inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.popup_add_class, null);
        builder.setView(view);

        id = findViewById(R.id.unitId);
        name = findViewById(R.id.unitName);
        start = findViewById(R.id.startDate);
        end = findViewById(R.id.endDate);
        hours = findViewById(R.id.noHours);

        builder.setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                final int unid = Integer.parseInt(id.getText().toString().trim());
                final String unname = name.getText().toString().trim();
                long unstart = Date.parse(start.getText().toString().trim());
                long unend = Date.parse(end.getText().toString().trim());
                Time unhours = Time.valueOf(hours.getText().toString().trim());

                Units units = new Units();
                units.setUnitID(unid);
                units.setUnitName(unname);
                units.setUnitStart(unstart);
                units.setUnitEnd(unend);
                units.setUnitHours(unhours);

                if (unid == Integer.parseInt(null) || unname.isEmpty() ||
                        unstart == Date.parse(null) || unend == Date.parse(null) ||
                        unhours == Time.valueOf(null) ) {
                    Toast.makeText(LecClasses.this, "Fill all fieLds", Toast.LENGTH_SHORT).show();
                } else {
                    if (mAuth.getCurrentUser() != null) {
                        classReference = FirebaseDatabase.getInstance().getReference();
                        classReference.child("LECTURERS").child(mAuth.getCurrentUser().getUid()).child("CLASSES").setValue(units, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    Toast.makeText(LecClasses.this, "Unit " + unname + " created.", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(LecClasses.this, "Unable to create new unit. RETRY", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Snackbar.make(view, "ERROR DURING PROCESS", Snackbar.LENGTH_SHORT).setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addNewClass();
                    }
                }).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.overflow_menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete :
                deletePost();
                break;
            default:
                break;
        }
        return true;
    }

    private void deletePost() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_warning_black_24dp)
                .setTitle("DELETE CLASS")
                .setMessage("Are you sure you want to delete this class?");

        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                classReference.child("LECTURERS").child(mAuth.getCurrentUser().getUid()).child("CLASSES").removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        Toast.makeText(LecClasses.this, "CLASS DELETED!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        }).setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
//                startActivity(new Intent(LecClasses.this, LecClasses.class));
            }
        });
    }
}
