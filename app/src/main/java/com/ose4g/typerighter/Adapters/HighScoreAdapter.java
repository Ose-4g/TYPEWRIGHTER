package com.ose4g.typerighter.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ose4g.typerighter.R;

import java.util.ArrayList;

public class HighScoreAdapter extends RecyclerView.Adapter<HighScoreAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Long> ScoresList;
    private LayoutInflater mLayoutInflater;

    public HighScoreAdapter(Context context, ArrayList<Long> scoresList) {
        mContext = context;
        ScoresList = scoresList;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.high_score_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position,ScoresList.get(position));
    }

    @Override
    public int getItemCount() {
        return ScoresList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView rank,score;
        ImageView badge;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rank = itemView.findViewById(R.id.rank);
            score = itemView.findViewById(R.id.score);
            badge = itemView.findViewById(R.id.no_1);
        }
        public void bind(int position, long score)
        {
            if(position!=0)
                badge.setVisibility(View.INVISIBLE);
            else
                badge.setVisibility(View.VISIBLE);

            rank.setText((position+1)+"");
            this.score.setText(score+"");
        }
    }
}
