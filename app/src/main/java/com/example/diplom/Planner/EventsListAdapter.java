package com.example.diplom.Planner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diplom.CalendarUtils;
import com.example.diplom.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventsListAdapter extends RecyclerView.Adapter<EventsViewHolder> {

    private Context context;
    private List<Events> list;
    private EventsClickListener listener;

    public EventsListAdapter(Context context, List<Events> list, EventsClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventsViewHolder(LayoutInflater.from(context).inflate(R.layout.events_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventsViewHolder holder, int position) {

        holder.eventTask.setText(list.get(position).getTask());
        holder.eventTask.setSelected(true);

        holder.eventDate.setText(CalendarUtils.formattedDateUa(CalendarUtils.formattedDateReverse(list.get(position).getDate())));
        holder.eventDate.setSelected(true);

        holder.eventCheckBox.setChecked(list.get(position).getState());
        holder.eventCheckBox.setSelected(true);

        holder.events_container.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.white, null));

        holder.events_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(list.get(holder.getAdapterPosition()));
            }
        });

        holder.events_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onLongClick(list.get(holder.getAdapterPosition()), holder.events_container);
                return true;
            }
        });
        holder.eventCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    //Toast.makeText(context, holder.eventTask.getText() + "+++", Toast.LENGTH_SHORT).show();
                    listener.onCheckboxClick(list.get(holder.getAdapterPosition()), holder.eventCheckBox);
                } else {
                    //Toast.makeText(context, holder.eventTask.getText() + "---", Toast.LENGTH_SHORT).show();
                    listener.onCheckboxClick(list.get(holder.getAdapterPosition()), holder.eventCheckBox);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList (List<Events> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

}

class EventsViewHolder extends RecyclerView.ViewHolder {

    CardView events_container;
    CheckBox eventCheckBox;
    TextView eventTask, eventDate;


    public EventsViewHolder(@NonNull View itemView) {
        super(itemView);

        events_container = itemView.findViewById(R.id.events_container);
        eventCheckBox = itemView.findViewById(R.id.eventCheckBox);
        eventTask = itemView.findViewById(R.id.eventTask);
        eventDate = itemView.findViewById(R.id.eventDate);

    }


}
