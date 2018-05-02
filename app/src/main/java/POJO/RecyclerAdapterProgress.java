package POJO;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.vanessaodawo.qrattendance.R;

import java.util.List;

/**
 * Created by Vanessa on 24/04/2018.
 */

public class RecyclerAdapterProgress extends RecyclerView.Adapter<RecyclerAdapterProgress.MyHolder> {

    List<Units> list;
    Context context;

    public RecyclerAdapterProgress(List<Units> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerAdapterProgress.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_progress, parent, false);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapterProgress.MyHolder holder, int position) {

        Units myList = list.get(position);
        holder.idU.setText(myList.getUnitID());
        holder.nameU.setText(myList.getUnitName());
        holder.hoursU.setText((CharSequence) myList.getUnitHours());

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

        TextView idU, nameU, hoursU;
        ImageButton omenuOverf;

        public MyHolder(View itemView) {
            super(itemView);

            idU = itemView.findViewById(R.id.tvidProgress);
            nameU = itemView.findViewById(R.id.tvnameProgress);
            hoursU = itemView.findViewById(R.id.tvhoursProgress);
            omenuOverf = itemView.findViewById(R.id.btnOverflow);
        }
    }
}
