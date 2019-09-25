package com.example.epay.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.bean.MemberStoredBean;
import com.example.epay.util.DateUtil;

import java.util.List;


public class MemberStoredValueAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MemberStoredBean.ItemsBean> memberStoredBeanList;
    private Context mContext;

    //声明自定义的监听接口
    private OnRecyclerItemClickListener monItemClickListener;

    //提供set方法供Activity或Fragment调用
    public void setRecyclerItemClickListener(OnRecyclerItemClickListener listener){
        monItemClickListener=listener;
    }

    public MemberStoredValueAdapter(List<MemberStoredBean.ItemsBean> memberStoredBeanList, Context mContext) {
        this.memberStoredBeanList = memberStoredBeanList;
        this.mContext = mContext;
    }
    public void setList(List<MemberStoredBean.ItemsBean> memberStoredBeanList){
        this.memberStoredBeanList=memberStoredBeanList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if(position == memberStoredBeanList.size()){
            return 0;
        }else{
            return 1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0){
            return new CustomAddition(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_custom_addition,parent,false));
        }else{
            return new MemberStoredValueHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_member_stored_value,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MemberStoredValueHolder){
            MemberStoredValueHolder memberStoredValueHolder = (MemberStoredValueHolder) holder;
            memberStoredValueHolder.load(memberStoredBeanList.get(position));
            memberStoredValueHolder.ll_recharge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i=0;i<memberStoredBeanList.size();i++){
                        if(i==position){
                            memberStoredBeanList.get(i).setIsCheck(true);
                        }else{
                            memberStoredBeanList.get(i).setIsCheck(false);
                        }
                    }
                    monItemClickListener.onItemClick(position,memberStoredBeanList);
                }
            });
        }else{
            CustomAddition customAddition = (CustomAddition) holder;
            boolean isHave=false;
            for (int i=0;i<memberStoredBeanList.size();i++){
               if(memberStoredBeanList.get(i).getIsCheck()){
                   isHave=true;
               }
            }
            if(isHave){
                customAddition.tv_charge.setVisibility(View.VISIBLE);
                customAddition.tv_edit.setVisibility(View.GONE);
                customAddition.ll_custom.setFocusable(true);
                customAddition.ll_custom.setBackgroundColor(Color.rgb(255,255,255));
            }
            if (customAddition.tv_edit != null) {
                customAddition.tv_edit.setFocusableInTouchMode(true);
                customAddition.tv_edit.setFocusable(true);
                customAddition.tv_edit.requestFocus();
            }
            customAddition.ll_custom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        for (int i = 0; i < memberStoredBeanList.size(); i++) {
                            memberStoredBeanList.get(i).setIsCheck(false);
                        }
                        customAddition.tv_charge.setVisibility(View.GONE);
                        customAddition.tv_edit.setVisibility(View.VISIBLE);
                        customAddition.tv_edit.setText("");
                        customAddition.tv_edit.requestFocus();
                        customAddition.tv_edit.findFocus();
                        customAddition.ll_custom.setBackgroundColor(Color.rgb(49, 92, 194));
                        monItemClickListener.onItemClick(position, memberStoredBeanList);
                    }
            });
            customAddition.tv_edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if(DateUtil.isNumeric00(editable.toString())){
                        monItemClickListener.onText(Double.parseDouble(editable.toString()));
                       // monItemClickListener.onItemClick(position,memberStoredBeanList,Double.parseDouble(editable.toString()));
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return memberStoredBeanList == null ? 1: memberStoredBeanList.size()+1;
    }

    public class MemberStoredValueHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll_recharge;
        private TextView tv_charge;

        public MemberStoredValueHolder(View view) {
            super(view);
            ll_recharge = view.findViewById(R.id.ll_recharge);
            tv_charge = view.findViewById(R.id.tv_charge);

//            ll_recharge.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    ll_recharge.setBackgroundColor(Color.rgb(49,92,194));
//                    tv_charge.setTextColor(Color.rgb(255,255,255));
//                    tv_give.setTextColor(Color.rgb(255,255,255));
//                }
//            });
        }
        public void load(MemberStoredBean.ItemsBean itemsBean){
            tv_charge.setText("￥"+itemsBean.getName()+"");
            if(itemsBean.getIsCheck()){

                ll_recharge.setBackgroundColor(Color.rgb(49,92,194));
                tv_charge.setTextColor(Color.rgb(255,255,255));

            }else{
                ll_recharge.setBackgroundColor(Color.rgb(255,255,255));
                tv_charge.setTextColor(Color.rgb(33,33,33));
            }
        }
    }

    public class CustomAddition extends RecyclerView.ViewHolder{
        private LinearLayout ll_custom;
        private TextView tv_charge;
        private EditText tv_edit;
        public CustomAddition(View view) {
            super(view);
            ll_custom = view.findViewById(R.id.ll_custom);
            tv_charge = view.findViewById(R.id.tv_custom);
            tv_edit = view.findViewById(R.id.et_j);

        }

    }

    public interface OnRecyclerItemClickListener {
        //RecyclerView的点击事件，将信息回调给view
        void onItemClick(int Position, List<MemberStoredBean.ItemsBean> dataBeanList);
        void onText(double money);
    }

}
