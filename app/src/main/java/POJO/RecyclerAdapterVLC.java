package POJO;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.vanessaodawo.qrattendance.R;
import com.vanessaodawo.qrattendance.ViewLecClass;

import java.util.List;

/**
 * Created by Vanessa on 22/04/2018.
 */

public class RecyclerAdapterVLC extends RecyclerView.Adapter<RecyclerAdapterVLC.MyHolder> {

    List<Students> list;
    Context context;

    public RecyclerAdapterVLC(List<Students> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerAdapterVLC.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_student, parent, false);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapterVLC.MyHolder holder, int position) {

        Students myStudList = list.get(position);
        holder.studEmail.setText(myStudList.getStudent_class_email());

        Intent intent = new Intent(context, ViewLecClass.class);
        intent.putExtra("emails",myStudList.getStudent_class_email()); //Any getter of your class you want

        context.startActivity(intent);

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

        TextView studEmail;

        public MyHolder(View itemView) {
            super(itemView);

            studEmail = itemView.findViewById(R.id.studEmail);

        }
    }
}
