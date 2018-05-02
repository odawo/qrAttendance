package Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.vanessaodawo.qrattendance.R;
import com.vanessaodawo.qrattendance.ViewLecClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

import POJO.Students;

public class ScanningFragment extends Fragment {


    private OnFragmentInteractionListener mListener;

    TextView email, name, date, period;
    Button submit;

    IntentIntegrator integrator;

    DatabaseReference presentReference;
    DatabaseReference studentReference;
    FirebaseAuth firebaseAuth;

    String TAG;
    String nums;
//    boolean compareEmails = true;

    public ScanningFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scanning, container, false);
        email = view.findViewById(R.id.qrStudEmail);
        name = view.findViewById(R.id.qrUnitName);
        date = view.findViewById(R.id.qrUnitDate);
        period = view.findViewById(R.id.qrUnitPeriod);
        submit = view.findViewById(R.id.btnSubmit);

        integrator = new IntentIntegrator(getActivity());

        presentReference = FirebaseDatabase.getInstance().getReference();
        
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRegQR();
            }
        });

        integratorofScan();

        return view;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
//            if qr is empty or cannot be read ..or is not equal to what was sent
            if (result.getContents() == null) {
                Toast.makeText(getContext(), "EMPTY QR!", Toast.LENGTH_SHORT).show();
            } else {
//                conversion to JSON
                try {
                    JSONObject jsonObject = new JSONObject(result.getContents());
                    email.setText(jsonObject.getString("email"));
                    name.setText(jsonObject.getString("name"));
                    date.setText(jsonObject.getString("date"));
                    period.setText(jsonObject.getString("period"));

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), result.getContents(), Toast.LENGTH_SHORT).show();
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void integratorofScan() {
//        initiates a qr scan
        integrator.initiateScan();
    }

    private void submitRegQR() {

        final String uemail = email.getText().toString().trim();
        String uname = email.getText().toString().trim();
        String udate = email.getText().toString().trim();
        String uperiod = email.getText().toString().trim();

        final Students studs = new Students();
        studs.setStudent_class_email(uemail);
        studs.setStudent_class_name(uname);
        studs.setStudent_class_date(Date.valueOf(udate));
        studs.setStudent_class_period(Time.valueOf(uperiod));

//        if uemail == to email in db then saved
        studentReference = FirebaseDatabase.getInstance().getReference().child("STUDENTS");
        studentReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    if (data.child(uemail).exists()) {
                        presentReference.child("LECTURERS").child(firebaseAuth.getCurrentUser().getUid()).child("CLASSES").child("PRESENT").setValue(studs, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    Log.d(TAG, "onComplete: PRESENT POINT REACHED");
                                    Toast.makeText(getActivity(), "PRESENT STUDENT SAVED", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "FAILURE. RETRY.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } else {
                        Toast.makeText(getActivity(), "STUDENT NOT REGISTERED TO YOUR CLASS", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
