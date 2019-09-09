package com.lenovo.smarttraffic.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.bean.RecBean;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Item2Activity extends AppCompatActivity {
    @BindView(R.id.reclyvie)
    RecyclerView reclyvie;
    private ArrayList<RecBean> list =new ArrayList();
    private int[] data={R.string.one,R.string.two,R.string.three,R.string.four,R.string.five,R.string.six};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item2_layout);
        ButterKnife.bind(this);
        init();
        initview();
    }
    private void init() {
        for (int i = 0; i <data.length; i++) {
            RecBean bean = new RecBean();
        }
    }

    private void initview() {

        //实例化linearlayout对象，指定linear layout方式
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);//指定linearlayout是垂直还是水平，这里是垂直
        reclyvie.setLayoutManager(layoutManager);
        RecAdapter adapter = new RecAdapter(getApplicationContext(),list);
        reclyvie.setAdapter(adapter);
    }

    private class RecAdapter extends RecyclerView.Adapter<RecAdapter.ViewHolder>{
        private final Context context;
        private final ArrayList list;

        public RecAdapter(Context context, ArrayList list) {
            this.context=context;
            this.list=list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(context).inflate(R.layout.rec_adapter,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder( ViewHolder holder, int position) {

//            holder.rec_txt.setText();
        }


        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView rec_txt;

            public ViewHolder( View itemView) {
                super(itemView);
                rec_txt =itemView.findViewById(R.id.rec_ada_txt);

            }
        }
    }

}
