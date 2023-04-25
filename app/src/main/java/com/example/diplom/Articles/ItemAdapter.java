package com.example.diplom.Articles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diplom.R;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private final Context context;
    private final List<DataModel> mList;
    private List<Articles> list = new ArrayList<>();

    public ItemAdapter(Context context, List<DataModel> mList){
        this.context = context;
        this.mList  = mList;
    }
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_item , parent , false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        DataModel model = mList.get(position);
        holder.mTextView.setText(model.getItemText());

        boolean isExpandable = model.isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);

        if (isExpandable){
            holder.mArrowImage.setImageResource(R.drawable.arrow_up);
        }else{
            holder.mArrowImage.setImageResource(R.drawable.arrow_down);
        }

        ArticlesAdapter articlesAdapter = new ArticlesAdapter(context, list);

        holder.articlesRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.articlesRecyclerView.setHasFixedSize(true);
        holder.articlesRecyclerView.setAdapter(articlesAdapter);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setExpandable(!model.isExpandable());
                list = model.getArticlesList();
                notifyItemChanged(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        private final LinearLayout linearLayout;
        private final RelativeLayout expandableLayout;
        private final TextView mTextView;
        private final ImageView mArrowImage;
        private final RecyclerView articlesRecyclerView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.linear_layout);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            mTextView = itemView.findViewById(R.id.itemTv);
            mArrowImage = itemView.findViewById(R.id.arro_imageview);
            articlesRecyclerView = itemView.findViewById(R.id.child_rv);
        }
    }
}
