package com.example.epay.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.adapter.RoyaltyAdapter;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.AddressBean;
import com.example.epay.bean.RoyaltyBean;
import com.example.epay.bean.User;
import com.example.epay.cache.CacheData;
import com.example.epay.doHttp.HttpCallBack;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RoyaltyActivity extends BaseActivity {
    @Bind(R.id.royalty_listView)
    ListView listView;
    @Bind(R.id.scan_top)
    FrameLayout scanTop;
    @Bind(R.id.royalty_img)
    ImageView royaltyImg;
    @Bind(R.id.royalty_name)
    TextView royaltyName;
    @Bind(R.id.royalty_phone)
    TextView royaltyPhone;
    @Bind(R.id.royalty_month)
    TextView royaltyMonth;
    @Bind(R.id.royalty_day)
    TextView royaltyDay;
    @Bind(R.id.royalty_type)
    TextView royaltyType;


    RoyaltyAdapter adapter;
    ArrayList<RoyaltyBean.RoyaltyItem> list = new ArrayList<>();



    private int type = 1;
    User userBean;
    private String data="";

    RoyaltyBean royaltyBean1=new RoyaltyBean();

    private int index=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_royalty);
        ButterKnife.bind(this);

        royaltyBean1.setImgUrl("https://file.jqepay.com/jq96c56a1144cf45e7997c9153e998005c.jpg");
        royaltyBean1.setName("扫码体验");
        royaltyBean1.setPhone("13011276195");
        royaltyBean1.setMonthRoyalty(2291.61);
        royaltyBean1.setDayRoyalty(142.05);
        ArrayList<RoyaltyBean.RoyaltyItem> items=new ArrayList<>();
        RoyaltyBean.RoyaltyItem item=new RoyaltyBean.RoyaltyItem();
        item.setImgUrl("https://file.jqepay.com/jq4092ee6ce58d4a4f9e8de29450e32aa1.jpg");
        item.setName("扫码体验");
        item.setPhone("13011276195");
        item.setPrice(10133);
        item.setRoyalty(1295.61);
        item.setDayRoyalty(79.05);
            ArrayList<RoyaltyBean.RoyaltyItem> itemItems=new ArrayList<>();
            RoyaltyBean.RoyaltyItem Item=new RoyaltyBean.RoyaltyItem();
            Item.setImgUrl("https://file.jqepay.com/jq7ca2d08e43764c50b5d7710012be935f.blob");
            Item.setName("原切西冷牛排");
            Item.setPrice(3256);
            Item.setRoyalty(137.11);
            itemItems.add(Item);
            RoyaltyBean.RoyaltyItem Item1=new RoyaltyBean.RoyaltyItem();
            Item1.setImgUrl("https://file.jqepay.com/jq2fa897f29fa74e03b3aecefca08a62e5.blob");
            Item1.setName("雪花牛排");
            Item1.setPrice(2432);
            Item1.setRoyalty(290);
            itemItems.add(Item1);
            RoyaltyBean.RoyaltyItem Item2=new RoyaltyBean.RoyaltyItem();
            Item2.setImgUrl("https://file.jqepay.com/jqd2fe9fe988ae4a979387c5d0c5ec1af1.blob");
            Item2.setName("花之冠");
            Item2.setPrice(2990);
            Item2.setRoyalty(300);
            itemItems.add(Item2);
            RoyaltyBean.RoyaltyItem Item3=new RoyaltyBean.RoyaltyItem();
            Item3.setImgUrl("https://file.jqepay.com/jq87b13666f88f488aaf95a4546c8fc62a.blob");
            Item3.setName("油闷大虾");
            Item3.setPrice(1728);
            Item3.setRoyalty(312);
            itemItems.add(Item3);
            RoyaltyBean.RoyaltyItem Item4=new RoyaltyBean.RoyaltyItem();
            Item4.setImgUrl("https://file.jqepay.com/jq563ec2b20c554d4a9cad79d454fd399f.blob");
            Item4.setName("原切T骨牛排");
            Item4.setPrice(1092);
            Item4.setRoyalty(224);
            itemItems.add(Item4);
            RoyaltyBean.RoyaltyItem Item5=new RoyaltyBean.RoyaltyItem();
            Item5.setImgUrl("https://file.jqepay.com/jq294dacd2c62f4281a5d4fc5702c2ff90.blob");
            Item5.setName("啤酒");
            Item5.setPrice(1625);
            Item5.setRoyalty(32.5);
            itemItems.add(Item5);
        item.setItem(itemItems);
        items.add(item);
        RoyaltyBean.RoyaltyItem item1=new RoyaltyBean.RoyaltyItem();
        item1.setImgUrl("https://file.jqepay.com/jq6fa70a07c33e47a38526bf385287eb48.png");
        item1.setName("刘瑾");
        item1.setPhone("15201365138");
        item1.setPrice(12937);
        item1.setRoyalty(996);
        item1.setDayRoyalty(63);

        ArrayList<RoyaltyBean.RoyaltyItem> itemItems2=new ArrayList<>();
        RoyaltyBean.RoyaltyItem Item10=new RoyaltyBean.RoyaltyItem();
        Item10.setImgUrl("https://file.jqepay.com/jq7ca2d08e43764c50b5d7710012be935f.blob");
        Item10.setName("原切西冷牛排");
        Item10.setPrice(2992);
        Item10.setRoyalty(126);
        itemItems2.add(Item10);
        RoyaltyBean.RoyaltyItem Item11=new RoyaltyBean.RoyaltyItem();
        Item11.setImgUrl("https://file.jqepay.com/jq2fa897f29fa74e03b3aecefca08a62e5.blob");
        Item11.setName("雪花牛排");
        Item11.setPrice(2944);
        Item11.setRoyalty(230);
        itemItems2.add(Item11);
        RoyaltyBean.RoyaltyItem Item12=new RoyaltyBean.RoyaltyItem();
        Item12.setImgUrl("https://file.jqepay.com/jqd2fe9fe988ae4a979387c5d0c5ec1af1.blob");
        Item12.setName("花之冠");
        Item12.setPrice(2093);
        Item12.setRoyalty(210);
        itemItems2.add(Item12);
        RoyaltyBean.RoyaltyItem Item13=new RoyaltyBean.RoyaltyItem();
        Item13.setImgUrl("https://file.jqepay.com/jq87b13666f88f488aaf95a4546c8fc62a.blob");
        Item13.setName("油闷大虾");
        Item13.setPrice(2160);
        Item13.setRoyalty(240);
        itemItems2.add(Item13);
        RoyaltyBean.RoyaltyItem Item14=new RoyaltyBean.RoyaltyItem();
        Item14.setImgUrl("https://file.jqepay.com/jq563ec2b20c554d4a9cad79d454fd399f.blob");
        Item14.setName("原切T骨牛排");
        Item14.setPrice(1248);
        Item14.setRoyalty(160);
        itemItems2.add(Item14);
        RoyaltyBean.RoyaltyItem Item15=new RoyaltyBean.RoyaltyItem();
        Item15.setImgUrl("https://file.jqepay.com/jq294dacd2c62f4281a5d4fc5702c2ff90.blob");
        Item15.setName("啤酒");
        Item15.setPrice(1500);
        Item15.setRoyalty(30);
        itemItems2.add(Item15);
        item1.setItem(itemItems2);
        items.add(item1);
        royaltyBean1.setItem(items);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        userBean = CacheData.getUser(this, CacheData.getId(this) + "");
        adapter = new RoyaltyAdapter(this, list);
        listView.setAdapter(adapter);
        if (userBean.getRoleType() == 2 || userBean.getRoleType() == 7) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if(type==1){
                        type = 2;
                        index=i;
                        http();
                    }
                }
            });
        }else{
            type = 2;
        }
        http();
    }

    public void http(){
        if(type==1){
            list=royaltyBean1.getItem();
            loadCircle(royaltyBean1.getImgUrl(),royaltyImg,30);
            royaltyName.setText(royaltyBean1.getName());
             royaltyPhone.setText(royaltyBean1.getPhone());
             royaltyMonth.setText(royaltyBean1.getMonthRoyalty()+"");
             royaltyDay.setText(royaltyBean1.getDayRoyalty()+"");
            // royaltyType.setText(royaltyBean1.getName());
        }else if(type==2){
            list=royaltyBean1.getItem().get(index).getItem();
            loadCircle(royaltyBean1.getItem().get(index).getImgUrl(),royaltyImg,30);
            royaltyName.setText(royaltyBean1.getItem().get(index).getName());
            royaltyPhone.setText(royaltyBean1.getItem().get(index).getPhone());
            royaltyMonth.setText(royaltyBean1.getItem().get(index).getRoyalty()+"");
            royaltyDay.setText(royaltyBean1.getItem().get(index).getDayRoyalty()+"");
        }
        adapter.setList(list);
//        httpUtil.HttpServer(this, data, 12, true, new HttpCallBack() {
//            @Override
//            public void back(String data) {
//
//            }
//
//            @Override
//            public void fail(String Message, int code, String data) {
//
//            }
//        });
    }


    @Override
    public void onBackPressed() {
        if (userBean.getRoleType() == 2 || userBean.getRoleType() == 7) {
            if (type == 1) {
                super.onBackPressed();
            } else {
                type=1;
                http();
            }
        } else {
            super.onBackPressed();
        }
    }
}
