package Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vanessaodawo.qrattendance.R;
import com.vanessaodawo.qrattendance.ViewLecClass;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import POJO.RecyclerAdapterReports;
import POJO.RecyclerAdapterVLC;
import POJO.Students;
import POJO.Units;


public class ReportsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    RecyclerView recycle;
    List<Students> list;

    DatabaseReference reportReference;
    FirebaseAuth firebaseAuth;

    String TAG;

    public ReportsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_reports, container, false);



        reportReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                list = new ArrayList<Students>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Students value = dataSnapshot1.getValue(Students.class);
                    Students students1 = new Students();

                    final String student_email = value.getStudent_class_email();
                    final int student_percentage = value.getPercentage();

                    students1.setStudent_class_email(student_email);
                    students1.setPercentage(student_percentage);

                    list.add(students1);

                    Units uval = dataSnapshot1.getValue(Units.class);
                    Units units = new Units();
                    final Time unithrs = uval.getUnitHours();
                    units.setUnitHours(unithrs);


                    /**
                     * obtain the emails,
                     * compare the email in the list to what's in the db of the "PRESENT" node: string a = b.compareto(c)
                     * count how many times it appears within the db : totatimes
                     * place it against the hours : int perc = (totaltimes/totalhours) * 100%
                     * int perc set to the percentage in listview
                     * **/
                    FirebaseApp.initializeApp(getContext());

                    DatabaseReference percRef = FirebaseDatabase.getInstance().getReference();
                    percRef.child("LECTURERS").child(firebaseAuth.getCurrentUser().getUid()).child("CLASSES").child("PRESENT").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

//                no of times email is in the db
//                int vals = (int) dataSnapshot.child("email").getChildrenCount();

//                actual emails in the db but i don't need this
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    nums = (String) data.child("email").getValue();
////                    a = nums;
//                }
//                            count of emails per student
                            String vals = dataSnapshot.child("email").getValue().toString();
                            boolean comp = vals.equalsIgnoreCase(student_email);
                            int count = 0;

                            while (vals.indexOf(String.valueOf(comp)) != 1) {
                                count += 1;
                                vals = vals.substring(vals.indexOf(String.valueOf(comp))+1);
                            }
//                            math bit

                            int total_hours_per_unit = Integer.parseInt(String.valueOf(unithrs));
                            int stud_percentage = ((count / total_hours_per_unit) * 100);

                            stud_percentage = student_percentage;
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled: Unable to obtain values. Refresh", databaseError.toException());
            }
        });

        RecyclerAdapterReports recyclerAdapter = new RecyclerAdapterReports(list, getActivity());
        RecyclerView.LayoutManager recyce = new LinearLayoutManager(getActivity());  //new GridLayoutManager(viewproduct.this, 2) ... 2 is number or rowns
        recycle.setLayoutManager(recyce);
        recycle.setItemAnimator(new DefaultItemAnimator()); //recycclerview.itemanimator..
        recycle.setAdapter(recyclerAdapter);

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
}
