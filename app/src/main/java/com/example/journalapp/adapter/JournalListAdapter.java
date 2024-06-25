package com.example.journalapp.adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.journalapp.R;
import com.example.journalapp.model.Journal;

import java.util.List;

public class JournalListAdapter extends RecyclerView.Adapter<JournalListAdapter.MyViewHolder> {
    private Context context;
    private List<Journal> journalList;

    public JournalListAdapter(Context context, List<Journal> journalList) {
        this.context = context;
        this.journalList = journalList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.journal_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Journal currentJournal = journalList.get(position);
        holder.title.setText(currentJournal.getTitle());
        holder.thougths.setText(currentJournal.getThoughts());
        holder.name.setText(currentJournal.getUserName());

        String imageUrl = currentJournal.getImageUrl();

        String timeAgo = (String) DateUtils.getRelativeTimeSpanString(currentJournal.getTimeAdded().getSeconds()*1000);

        holder.dateAdded.setText(timeAgo);
        //Glide

        Glide.with(context).load(imageUrl).fitCenter().into(holder.image);

    }

    @Override
    public int getItemCount() {
        return journalList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title,thougths,dateAdded,name;
        public ImageView image,sharebuton;
        public String userId,userName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.journal_title_list);
            thougths = itemView.findViewById(R.id.journal_thought_list);
            dateAdded= itemView.findViewById(R.id.journal_timestamp_list);
            image= itemView.findViewById(R.id.journal_image_list);
            name = itemView.findViewById(R.id.jounal_row_username);
            sharebuton = itemView.findViewById(R.id.jorunal_row_share_button);
            sharebuton.setOnClickListener(v -> {

            });
        }
    }
}
