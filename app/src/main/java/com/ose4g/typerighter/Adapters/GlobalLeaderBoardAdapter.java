package com.ose4g.typerighter.Adapters;

import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ose4g.typerighter.Models.User;
import com.ose4g.typerighter.R;

import java.util.ArrayList;

public class GlobalLeaderBoardAdapter extends RecyclerView.Adapter<GlobalLeaderBoardAdapter.ViewHolder> {
    Context mContext;
    ArrayList<User> mUsers;
    LayoutInflater mLayoutInflater;

    public GlobalLeaderBoardAdapter(Context context, ArrayList<User> users) {
        mContext = context;
        mUsers = users;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.global_leaderboard_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mUsers.get(position),position);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public  void clear()
    {
        int size = mUsers.size();
        mUsers.clear();
        notifyItemRangeRemoved(0, size);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView rank, username, bestScore;
        LinearLayout mLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rank = itemView.findViewById(R.id.rank);
            username = itemView.findViewById(R.id.userName);
            bestScore = itemView.findViewById(R.id.bestScore);
            mLayout = itemView.findViewById(R.id.bkg_list_item);

        }

        public void bind(User user,int position)
        {
            rank.setText((position+1)+"");
            username.setText(user.getUserName());
            bestScore.setText((user.getBestScore()*-1)+"");
            if((position+1)%2==1)
                changeColor(R.color.light_grey);
            else
            {
                changeColor(android.R.color.transparent);
            }
        }

        public void changeColor(int color)
        {
            mLayout.setBackgroundColor(mContext.getResources().getColor(color));
        }


    }
}
