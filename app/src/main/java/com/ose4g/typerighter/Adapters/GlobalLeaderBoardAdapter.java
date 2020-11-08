package com.ose4g.typerighter.Adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ose4g.typerighter.GlobalLeaderboard.GlobalLeaderboardViewModel;
import com.ose4g.typerighter.Models.User;
import com.ose4g.typerighter.R;

import java.util.ArrayList;

public class GlobalLeaderBoardAdapter extends RecyclerView.Adapter<GlobalLeaderBoardAdapter.ViewHolder> {
    Context mContext;
    public ArrayList<User> mUsers;
    LayoutInflater mLayoutInflater;
    GlobalLeaderboardViewModel mViewModel;
    public GlobalLeaderBoardAdapter(Context context, ArrayList<User> users, GlobalLeaderboardViewModel viewModel) {
        mContext = context;
        mUsers = users;
        mLayoutInflater = LayoutInflater.from(mContext);
        mViewModel = viewModel;
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
        ConstraintLayout mConstraintLayout;
        CardView mCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rank = itemView.findViewById(R.id.rank);
            username = itemView.findViewById(R.id.userName);
            bestScore = itemView.findViewById(R.id.bestScore);
            mLayout = itemView.findViewById(R.id.bkg_list_item);
            mConstraintLayout = itemView.findViewById(R.id.layout);
            mCardView = itemView.findViewById(R.id.cardView);

        }

        public void bind(User user,int position)
        {
            rank.setText((position+1)+"");
            username.setText(user.getUserName());
            bestScore.setText((user.getBestScore()*-1)+"");


            if(mViewModel.mFirebaseUser.getUid().equals(user.getUserId()))
            {
                mCardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.darker_transparent));
                mConstraintLayout.setPadding(16,16,16,16);
                setImportant(rank,true);
                setImportant(username,true);
                setImportant(bestScore,true);
            }
            else
            {
                mCardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.dark_transparent));
                mConstraintLayout.setPadding(40,0,40,4);
                setImportant(rank,false);
                setImportant(username,false);
                setImportant(bestScore,false);
            }
        }

        public void changeColor(int color)
        {
            mLayout.setBackgroundColor(mContext.getResources().getColor(color));
        }

        public void setImportant(TextView view, boolean important)
        {
            if(important)
            {
                view.setTextColor(mContext.getResources().getColor(android.R.color.white));

            }
            else
            {
                view.setTextColor(mContext.getResources().getColor(R.color.batman_grey));
            }
        }

    }
}
