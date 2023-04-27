package com.example.diplom.Diary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diplom.R;

import java.util.List;

public class ChecksListAdapter extends RecyclerView.Adapter<ChecksViewHolder> {

    private Context context;
    private List<Checks> list;
    private ChecksClickListener listener;

    public ChecksListAdapter(Context context, List<Checks> list, ChecksClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChecksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChecksViewHolder(LayoutInflater.from(context).inflate(R.layout.checks_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChecksViewHolder holder, int position) {

        holder.checkTask.setText(list.get(position).getTask());
        holder.checkTask.setSelected(true);

        holder.checkCheckBox.setChecked(list.get(position).getState());
        holder.checkCheckBox.setSelected(true);

        holder.checks_container.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.white, null));

        holder.checks_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onLongClick(list.get(holder.getAdapterPosition()), holder.checks_container);
                return true;
            }
        });
        holder.checkCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    listener.onCheckboxClick(list.get(holder.getAdapterPosition()), holder.checkCheckBox);
                } else {
                    listener.onCheckboxClick(list.get(holder.getAdapterPosition()), holder.checkCheckBox);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}

class ChecksViewHolder extends RecyclerView.ViewHolder {

    CardView checks_container;
    CheckBox checkCheckBox;
    TextView checkTask;


    public ChecksViewHolder(@NonNull View itemView) {
        super(itemView);

        checks_container = itemView.findViewById(R.id.checks_container);
        checkCheckBox = itemView.findViewById(R.id.checkCheckBox);
        checkTask = itemView.findViewById(R.id.checkTask);

    }

}