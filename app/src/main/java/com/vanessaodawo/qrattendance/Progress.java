package com.vanessaodawo.qrattendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import Fragments.AttendanceFragment;
import Fragments.ReportsFragment;
import POJO.RecyclerAdapterLecClasses;
import POJO.RecyclerAdapterProgress;
import POJO.Students;
import POJO.Units;

public class Progress extends AppCompatActivity {

    RecyclerView recycle;
    List<Units> list;

    DatabaseReference studentReference;

    String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        studentReference = FirebaseDatabase.getInstance().getReference();
        studentReference.addValueEventListener(new ValueEventListener() {
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

        RecyclerAdapterProgress recyclerAdapter = new RecyclerAdapterProgress(list, Progress.this);
        RecyclerView.LayoutManager recyce = new GridLayoutManager(Progress.this, 2);  //new GridLayoutManager(viewproduct.this, 2) ... 2 is number or rows
        recycle.setLayoutManager(recyce);
        recycle.setItemAnimator(new DefaultItemAnimator()); //recycclerview.itemanimator..
        recycle.setAdapter(recyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.overflow_menu_attendance, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_unitattendance:
                startActivity(new Intent(Progress.this, AttendanceFragment.class));
                finish();
                break;
            case R.id.action_unitreport:
                startActivity(new Intent(Progress.this, ReportsFragment.class));
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
