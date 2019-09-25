package com.example.epay.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.epay.R;

import butterknife.Bind;

public class MembershipListAdapter extends RecyclerView.Adapter<MembershipListAdapter.MembershipListViewHolder> {


    @Override
    public MembershipListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_membership_list, parent, false);
        return new MembershipListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MembershipListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MembershipListViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPortrait;
        LinearLayout llMembershipList;
        TextView tvName;
        TextView tvTotalConsumption;
        TextView tvBalance;

        public MembershipListViewHolder(View view) {
            super(view);
            ivPortrait = view.findViewById(R.id.iv_portrait);
            llMembershipList = view.findViewById(R.id.ll_membership_list);
            tvName = view.findViewById(R.id.tv_name);
            tvTotalConsumption = view.findViewById(R.id.tv_total_consumption);
            tvBalance = view.findViewById(R.id.tv_balance);
        }
    }
}
