package com.example.epay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.adapter.SetMealAdapter;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.MealListBean;
import com.example.epay.bean.OptionBean;
import com.example.epay.doHttp.HttpCallBack;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetMealActivity extends BaseActivity {

    @Bind(R.id.set_meal_name)
    TextView setMealName;
    @Bind(R.id.set_meal_listView)
    ListView setMealListView;

    ArrayList<OptionBean> list=new ArrayList<>();
    ArrayList<MealListBean.MealRight> list2=new ArrayList<>();

    SetMealAdapter setMealAdapter;
    MealListBean.MealRight Mealright1;
    private double price=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_meal);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        setMealAdapter=new SetMealAdapter(this,list);
        setMealListView.setAdapter(setMealAdapter);

        doHttp();
    }

    private void doHttp(){
        httpUtil.HttpServer(this, "{\"ID\":"+getIntent().getIntExtra("id",0)+"}", 3, false, new HttpCallBack() {
            @Override
            public void back(String data) {
                Mealright1= gson.fromJson(data, MealListBean.MealRight.class);
                list=Mealright1.getOptionItems();
                list2=Mealright1.getSubItems();
                for (MealListBean.MealRight f:list2){
                    OptionBean optionBean=new OptionBean();
                    optionBean.setCount(0);
                    optionBean.setMax(0);
                    optionBean.setName(f.getName());
                    list.add(optionBean);
                }
                setMealAdapter.setList(list);
            }

            @Override
            public void fail(String Message, int code, String data) {
                showMessage(Message);
            }
        });
    }

    @OnClick(R.id.set_meal_sure)
    public void sure(){
       ArrayList<MealListBean.MealRight> lists=(ArrayList<MealListBean.MealRight>)getIntent().getSerializableExtra("lists");
       int num1= getIntent().getIntExtra("num1", 0);
       double price1= getIntent().getDoubleExtra("price1", 0);
        MealListBean.MealRight mealRight=new MealListBean.MealRight();
        mealRight.setID(getIntent().getIntExtra("id",0));
        mealRight.setName(Mealright1.getName());
        mealRight.setNumber(1);
        ArrayList<MealListBean.MealRight> jk=new ArrayList<>();
        for(int i=0;i<list.size();i++){
            if(list.get(i).getCount()>0){
                for (int j=0;j<list.get(i).getItems().size();j++) {
                    if(list.get(i).getItems().get(j).isChoose()){
                        MealListBean.MealRight mealRight1 = new MealListBean.MealRight();
                        mealRight1.setID(list.get(i).getItems().get(j).getID());
                        mealRight1.setCount(1);
                        jk.add(mealRight1);
                        price=price+list.get(i).getItems().get(j).getPrice();
                    }
                }
            }
        }
        mealRight.setSubItems(jk);
        mealRight.setPrice(Mealright1.getPrice()+price);
        num1++;
        price1=price1+Mealright1.getPrice()+price;
        lists.add(mealRight);
        Intent intent=new Intent(this,OrderMealActivity.class);
        intent.putExtra("list",lists);
        intent.putExtra("num",num1);
        intent.putExtra("price",price1);
        setResult(1001,intent);
        finish();
    }
}
