package POJO;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.vanessaodawo.qrattendance.R;
import com.vanessaodawo.qrattendance.ViewLecClass;

import java.util.List;

/**
 * Created by Vanessa on 05/04/2018.
 */

public class RecyclerAdapterLecClasses extends  RecyclerView.Adapter<RecyclerAdapterLecClasses.MyHolder>{

    List<Units> list;
    Context context;
    Context mContext;


    public RecyclerAdapterLecClasses(List<Units> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerAdapterLecClasses.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_class, parent, false);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapterLecClasses.MyHolder holder, int position) {
        Units myList = list.get(position);
        holder.unitidentifier.setText(myList.getUnitID());
        holder.unitnaming.setText(myList.getUnitName());
        holder.unithourstime.setText((CharSequence) myList.getUnitHours()); //changed due to Time form

        holder.viewClassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, ViewLecClass.class);
                intent.putExtra("classid", "uID");
                mContext.startActivity(intent);
            }
        });
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

        TextView unitidentifier, unitnaming, unithourstime;
        Button viewClassbtn;

        public MyHolder(View itemView) {
            super(itemView);

            unitidentifier = itemView.findViewById(R.id.unitidentifier);
            unitnaming = itemView.findViewById(R.id.unitnaming);
            unithourstime = itemView.findViewById(R.id.unitHoursTime);
            viewClassbtn = itemView.findViewById(R.id.viewClassBtn);

        }
    }

}
