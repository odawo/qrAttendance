package POJO;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vanessaodawo.qrattendance.R;
import com.vanessaodawo.qrattendance.ViewLecClass;

import java.util.List;

/**
 * Created by Vanessa on 22/04/2018.
 */

public class RecyclerAdapterReports extends RecyclerView.Adapter<RecyclerAdapterReports.MyHolder> {

    List<Students> list;
    Context context;

    FirebaseAuth mAuth;

    public RecyclerAdapterReports(List<Students> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerAdapterReports.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_reports, parent, false);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapterReports.MyHolder holder, int position) {

        Students myStudList = list.get(position);
        holder.emailReports.setText(myStudList.getStudent_class_email());
        holder.percentage.setText(myStudList.getPercentage());

    }

    @Override
    public int getItemCount() {
        int arr = 0;

        try {
            if (list.size() == 0) {
                arr = 0;
            }else {
                arr = list.size();
            }
        }catch (Exception ignored) { }

        return arr;
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView emailReports, percentage;

        public MyHolder(View itemView) {
            super(itemView);

            emailReports = itemView.findViewById(R.id.tvemailReport);
            percentage = itemView.findViewById(R.id.tvPercentage);

        }
    }
}
