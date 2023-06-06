package com.example.diplom.Articles;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diplom.R;

import java.util.List;
import java.util.Objects;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticlesViewHolder>{

    private final Context context;
    private final List<Articles> mList;
    private Articles selectedArticle;
    private ArticlesDB database;
    private ArticlesAdapter articlesAdapter;

    public ArticlesAdapter(Context context, List<Articles> mList){
        this.context = context;
        this.mList = mList;
    }
    @NonNull
    @Override
    public ArticlesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item , parent , false);
        return new ArticlesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticlesViewHolder holder, int position) {

        database = ArticlesDB.getInstance(context);
        articlesAdapter = new ArticlesAdapter(context, mList);

        holder.articleItemTv.setText(mList.get(position).getTitle());
        holder.articleItemTv.setSelected(true);

        if (Objects.equals(mList.get(position).getState(), "1")) {
            holder.readState.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.state_1));
        } else if (Objects.equals(mList.get(position).getState(), "2")) {
            holder.readState.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.state_2));
        } else {
            holder.readState.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.state_0_white));
        }
        holder.readState.setSelected(true);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class ArticlesViewHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {

        private final CardView article_containers;
        private final TextView articleItemTv;
        private final ImageView readState;

        public ArticlesViewHolder(@NonNull View itemView) {

            super(itemView);
            article_containers = itemView.findViewById(R.id.article_containers);
            articleItemTv = itemView.findViewById(R.id.articleItemTv);
            readState = itemView.findViewById(R.id.readState);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ArticleTextActivity.class);
                    intent.putExtra("id", mList.get(getAdapterPosition()).getID());
                    context.startActivity(intent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    selectedArticle = mList.get(getAdapterPosition());
                    showPopup (article_containers);
                    return false;
                }
            });
        }

        private void showPopup(CardView cardView) {
            PopupMenu popupMenu = new PopupMenu(context, cardView);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.inflate(R.menu.article_popup_menu);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.state0:
                    this.readState.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.state_0_white));
                    database.articlesDAO().stateChange(selectedArticle.getID(), "0");
                    Toast.makeText(context, "Читання не роозпочато", Toast.LENGTH_SHORT).show();
                    articlesAdapter.notifyDataSetChanged();
                    return true;
                case R.id.state1:
                    this.readState.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.state_1));
                    database.articlesDAO().stateChange(selectedArticle.getID(), "1");
                    Toast.makeText(context, "Читаю", Toast.LENGTH_SHORT).show();
                    articlesAdapter.notifyDataSetChanged();
                    return true;
                case R.id.state2:
                    this.readState.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.state_2));
                    database.articlesDAO().stateChange(selectedArticle.getID(), "2");
                    Toast.makeText(context, "Прочитано", Toast.LENGTH_SHORT).show();
                    articlesAdapter.notifyDataSetChanged();
                    return true;
                default:
                    return false;
            }
        }
    }
}