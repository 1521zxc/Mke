package com.example.epay.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;


import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;

import android.widget.AdapterView;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.epay.adapter.MealLeftAdapter;
import com.example.epay.adapter.MealRightAdapter;


import java.util.ArrayList;

import com.example.epay.R;
import com.example.epay.adapter.OrderMealMenuAdapter;
import com.example.epay.adapter.SearchMealAdapter;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.MealListBean;
import com.example.epay.bean.OrderMealAttrBean;
import com.example.epay.bean.OrderMealLeftBean;
import com.example.epay.bean.OrderMealListBean;
import com.example.epay.bean.OrderMealRightBean;
import com.example.epay.bean.RemarkBean;
import com.example.epay.cache.CacheData;
import com.example.epay.doHttp.HttpCallBack;
import com.example.epay.util.JsonParser;
import com.example.epay.view.EPayProgressDialog;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.util.ResourceUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.ui.RecognizerDialogListener;

//点餐
public class OrderMealActivity extends BaseActivity {

    private ArrayList<OrderMealLeftBean> menuList = new ArrayList<>();
    private ArrayList<MealListBean> homeList = new ArrayList<>();
    ArrayList<RemarkBean> remarklists = new ArrayList<>();

    private ArrayList<Integer> showTitle;

    @Bind(R.id.order_meal_listView)
    ListView lv_menu;
    @Bind(R.id.order_meal_listView2)
    ListView lv_home;
    @Bind(R.id.order_meal_search_listView)
    ListView searchListView;

    private MealLeftAdapter MealLeftAdapter;
    private MealRightAdapter rightAdapter;
    private SearchMealAdapter searchAdapter;
    private int currentItem;

    @Bind(R.id.order_meal_titile)
    TextView tv_title;
    @Bind(R.id.order_meal_price)
    TextView tv_price;
    @Bind(R.id.order_meal_cata)
    LinearLayout cataFr;
    @Bind(R.id.order_meal_num)
    TextView cataNum;

    @Bind(R.id.order_meal_layout1)
    LinearLayout searchLayout;
    @Bind(R.id.order_meal_layout)
    LinearLayout getSearchLayout;

    @Bind(R.id.search_edit)
    EditText search;
    @Bind(R.id.order_meal_speech_text)
    TextView speechText;
    @Bind(R.id.order_meal_speech)
    TextView speechButton;

    OrderMealListBean bean;
    private double price = 0;
    int num1 = 0;
    ArrayList<MealListBean.MealRight> lists = new ArrayList<>();
    ArrayList<MealListBean.MealRight> searchlists = new ArrayList<>();
    // 函数调用返回值
    // 语音听写对象
    private SpeechRecognizer mIat;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order_meal);

        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
//
        // 创建语音听写对象
        mIat = SpeechRecognizer.createRecognizer(this, mInitListener);
        // 初始化听写Dialog,如果只使用有UI听写功能,无需创建SpeechRecognizer
        // 创建语音听写UI


        showTitle = new ArrayList<>();
        MealLeftAdapter = new MealLeftAdapter(this, menuList);
        lv_menu.setAdapter(MealLeftAdapter);
        rightAdapter = new MealRightAdapter(this, homeList, remarklists, lists, price, num1);
        lv_home.setAdapter(rightAdapter);
        searchAdapter = new SearchMealAdapter(this, searchlists, remarklists, price, lists, num1);
        searchListView.setAdapter(searchAdapter);
        search.setCursorVisible(false);
        doHttp();
        lv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MealLeftAdapter.setSelectItem(position);
                MealLeftAdapter.notifyDataSetInvalidated();
                tv_title.setText(menuList.get(position).getName());
                lv_home.setSelection(showTitle.get(position));
            }
        });

        lv_home.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int scrollState;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.scrollState = scrollState;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    return;
                }
                int current = showTitle.indexOf(firstVisibleItem);
                if (currentItem != current && current >= 0) {
                    currentItem = current;
                    tv_title.setText(menuList.get(currentItem).getName());
                    MealLeftAdapter.setSelectItem(currentItem);
                    MealLeftAdapter.notifyDataSetInvalidated();
                }
            }
        });

        rightAdapter.setOnNtClickListener(new MealRightAdapter.OnNtClickListener() {
            @Override
            public void onNtClick(ArrayList<MealListBean.MealRight> list, int num, double price) {
                lists = list;
                num1 = num;
                OrderMealActivity.this.price = price;
                tv_price.setText("总价：￥" + price);
                cataNum.setText(num1 + "");
                rightAdapter.setData(lists, remarklists, price, num1);
            }
        });

        searchAdapter.setOnNtClickListener(new SearchMealAdapter.OnNtClickListener() {
            @Override
            public void onNtClick(ArrayList<MealListBean.MealRight> list, int num, double price) {
                lists = list;
                num1 = num;
                OrderMealActivity.this.price = price;
                tv_price.setText("总价：￥" + price);
                cataNum.setText(num1 + "");
                rightAdapter.setData(lists, remarklists, price, num1);
                searchAdapter.setData(lists, remarklists, price, num1);
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                search();
            }
        });

        if(CacheData.getUser(this,CacheData.getId(this)+"").getIsClose()==1) {
            showMessage("打烊中");
        }
    }

    public void doHttp() {
        httpUtil.HttpServer(this, "{\"cataIndex\":-1}", 4, true, new HttpCallBack() {
            @Override
            public void back(String data) {
                bean = gson.fromJson(data, OrderMealListBean.class);
                menuList = bean.getItems();
                tv_title.setText(menuList.get(0).getName());
                initdata();
            }

            @Override
            public void fail(String Message, int code, String data) {
                showMessage(Message);
            }
        });
    }

    public void initdata() {
        ArrayList<OrderMealRightBean> list = bean.getProducts();
        ArrayList<OrderMealAttrBean> lists1 = bean.getProductAttrs();
        remarklists = bean.getProductRemarks();
        for (int i = 0; i < menuList.size(); i++) {
            showTitle.add(i);
            MealListBean listBean = new MealListBean();
            ArrayList<MealListBean.MealRight> list1 = new ArrayList<>();
            listBean.setTitle(menuList.get(i).getName());
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).getCataID() == menuList.get(i).getID()) {
                    MealListBean.MealRight right = new MealListBean.MealRight();
                    right.setCataID(list.get(j).getCataID());
                    right.setSoldCount(list.get(j).getSoldCount());
                    right.setSetMeal(list.get(j).getSetMeal());
                    right.setSellStatus(list.get(j).getSellStatus());
                    right.setPrice(list.get(j).getPrice());
                    right.setIconURL(list.get(j).getIconURL());
                    right.setName(list.get(j).getName());
                    right.setID(list.get(j).getID());
                    right.setVipPrice(list.get(j).getVipPrice());
                    right.setLowSell(list.get(j).getLowSell());
                    right.setSpell(list.get(j).getSpell());
                    right.setChoose(list.get(j).getChoose());
                    ArrayList<OrderMealAttrBean> list2 = new ArrayList<>();
                    if (lists1.size() > 0) {
                        for (int n = 0; n < lists1.size(); n++) {
                            if (lists1.get(n).getProductID() == list.get(j).getID()) {
                                OrderMealAttrBean bean1 = lists1.get(n);
                                list2.add(bean1);
                            }
                        }
                    }
                    right.setAttrs(list2);
                    list1.add(right);
                }
            }
            listBean.setMealRights(list1);
            homeList.add(listBean);
        }
        MealLeftAdapter.setList(menuList);
        rightAdapter.setData(lists, remarklists, price, num1);
    }

    @OnClick(R.id.order_meal_ok)
    public void ok() {
        if (CacheData.getUser(this, CacheData.getId(this) + "").getIsClose() != 1) {
            if (lists.size() > 0) {
                CacheData.cacheMealBean(this, lists);
                Intent intent = new Intent(this, ConfirmOrderActivity.class);
                intent.putExtra("price", price);
                intent.putExtra("num", num1);
                intent.putExtra("meal", getIntent().getBooleanExtra("meal", true));
                intent.putExtra("deskNo", getIntent().getStringExtra("deskNo"));
                intent.putExtra("deskName", getIntent().getStringExtra("deskName"));
                if (!getIntent().getBooleanExtra("meal", true)) {
                    intent.putExtra("orderID", getIntent().getIntExtra("orderID", 0));
                    intent.putExtra("remark", getIntent().getStringExtra("remark"));
                    intent.putExtra("people", getIntent().getIntExtra("people", 0));
                }
                startActivity(intent);
            } else {
                showMessage("请选菜品");
            }
        } else {
            showMessage("打烊中");
        }
    }

    @OnClick({R.id.order_meal_speech_text,R.id.search_speech})
    public void speech(){
        speechText.setText("点菜中");
        setParam();
        // 显示听写对话框
        mIat.startListening(recognizerListener);

    }
    @OnClick(R.id.order_meal_speech)
    public void speechButton(){
        if(speechButton.getText().toString().equals("语音")) {
            speechText.setVisibility(View.VISIBLE);
            cataFr.setVisibility(View.GONE);
            speechButton.setText("返回");
            speechText.setText("语音点菜");
        }else{
            speechButton.setText("语音");
            cataFr.setVisibility(View.VISIBLE);
            speechText.setVisibility(View.GONE);
        }
    }




    @OnClick(R.id.order_meal_reach)
    public void reach() {
        searchAdapter.setList(searchlists);
        getSearchLayout.setVisibility(View.GONE);
        searchLayout.setVisibility(View.VISIBLE);
    }

    public void search() {
        searchlists.clear();
        for (int j = 0; j < homeList.size(); j++) {
            for(int m=0;m<homeList.get(j).getMealRights().size();m++) {
                if(homeList.get(j).getMealRights().get(m).getSpell().contains(search.getText().toString())) {
                    MealListBean.MealRight right = new MealListBean.MealRight();
                    right.setCataID(homeList.get(j).getMealRights().get(m).getCataID());
                    right.setSoldCount(homeList.get(j).getMealRights().get(m).getSoldCount());
                    right.setSetMeal(homeList.get(j).getMealRights().get(m).getSetMeal());
                    right.setSellStatus(homeList.get(j).getMealRights().get(m).getSellStatus());
                    right.setPrice(homeList.get(j).getMealRights().get(m).getPrice());
                    right.setIconURL(homeList.get(j).getMealRights().get(m).getIconURL());
                    right.setName(homeList.get(j).getMealRights().get(m).getName());
                    right.setID(homeList.get(j).getMealRights().get(m).getID());
                    right.setVipPrice(homeList.get(j).getMealRights().get(m).getVipPrice());
                    right.setLowSell(homeList.get(j).getMealRights().get(m).getLowSell());
                    right.setAttrs(homeList.get(j).getMealRights().get(m).getAttrs());
                    searchlists.add(right);
                    break;
                }

                if(homeList.get(j).getMealRights().get(m).getName().contains(search.getText().toString())) {
                    MealListBean.MealRight right = new MealListBean.MealRight();
                    right.setCataID(homeList.get(j).getMealRights().get(m).getCataID());
                    right.setSoldCount(homeList.get(j).getMealRights().get(m).getSoldCount());
                    right.setSetMeal(homeList.get(j).getMealRights().get(m).getSetMeal());
                    right.setSellStatus(homeList.get(j).getMealRights().get(m).getSellStatus());
                    right.setPrice(homeList.get(j).getMealRights().get(m).getPrice());
                    right.setIconURL(homeList.get(j).getMealRights().get(m).getIconURL());
                    right.setName(homeList.get(j).getMealRights().get(m).getName());
                    right.setID(homeList.get(j).getMealRights().get(m).getID());
                    right.setVipPrice(homeList.get(j).getMealRights().get(m).getVipPrice());
                    right.setLowSell(homeList.get(j).getMealRights().get(m).getLowSell());
                    right.setAttrs(homeList.get(j).getMealRights().get(m).getAttrs());
                    searchlists.add(right);
                    break;
                }
            }
        }
        searchAdapter.setList(searchlists);
        searchAdapter.setData(lists, remarklists, price, num1);
        if(searchlists.size()<1){
            showMessage("没有相关菜品");
        }
    }

    @Override
    public void onBackPressed() {
        if (getSearchLayout.getVisibility() == View.GONE) {
            searchLayout.setVisibility(View.GONE);
            getSearchLayout.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.order_meal_cata)
    public void popo() {
        int[] location = new int[2];
        cataFr.getLocationOnScreen(location);
        if (cataNum.getText().equals("0")){
            showMessage("请添加菜品");
        }else {
            initListPopuptWindow();
        }
    }

    private PopupWindow mPopupWindow;
    OrderMealMenuAdapter adapter;

    public PopupWindow initListPopuptWindow() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View popupWindow = layoutInflater.inflate(R.layout.order_meal_popup_list, null);
        ListView listView = (ListView) popupWindow.findViewById(R.id.popup_list);
        LinearLayout order_meal_layout_window = popupWindow.findViewById(R.id.order_meal_layout_window);
        TextView clear = (TextView) popupWindow.findViewById(R.id.order_meal_popup_clear);
        TextView on = (TextView) popupWindow.findViewById(R.id.order_meal_popup_on);
        adapter = new OrderMealMenuAdapter(this, lists, price, num1);
        listView.setAdapter(adapter);
        mPopupWindow = new PopupWindow(popupWindow, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, false);
        mPopupWindow.setOutsideTouchable(true);// 设置外部触摸会关闭窗口
        mPopupWindow.setFocusable(true);
        mPopupWindow.setAnimationStyle(R.style.animTranslate);

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1.0f;
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(params);
            }
        });

        mPopupWindow.showAtLocation(getSearchLayout, Gravity.BOTTOM, 0, 152);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(params);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderMealActivity.this.onClick(view);
            }
        });

        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });

        adapter.setOnNtClickListener(new OrderMealMenuAdapter.OnNtClickListener() {
            @Override
            public void onNtClick(ArrayList<MealListBean.MealRight> list, int num, double price) {
                lists = list;
                num1 = num;
                OrderMealActivity.this.price = price;
                tv_price.setText("总价：￥" + price);
                cataNum.setText(num1 + "");
                rightAdapter.setData(lists, remarklists, OrderMealActivity.this.price, num1);
                searchAdapter.setData(lists, remarklists, OrderMealActivity.this.price, num1);
                adapter.setData(lists, OrderMealActivity.this.price, num1);
            }
        });
        return mPopupWindow;
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("点餐"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("点餐"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    private void onClick(View view) {
        mPopupWindow.dismiss();
        lists = new ArrayList<>();
        num1 = 0;
        cataNum.setText(num1 + "");
        OrderMealActivity.this.price = 0;
        tv_price.setText("总价：￥" + 0.0);
        rightAdapter.setData(lists, remarklists, OrderMealActivity.this.price, num1);
        searchAdapter.setData(lists, remarklists, OrderMealActivity.this.price, num1);
        adapter.setData(lists, OrderMealActivity.this.price, num1);
    }





    public void setParam() {
        Log.e(TAG, "setParam");
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);
        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
       // if (mEngineType.equals(SpeechConstant.TYPE_LOCAL)) {
            // 设置本地识别资源
            mIat.setParameter(ResourceUtil.ASR_RES_PATH, getResourcePath());
       // }

        // 设置语言
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
        // 设置语音前端点
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
        // 设置语音后端点
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
        // 设置标点符号 1为有标点 0为没标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");
        // 设置音频保存路径
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH,
                Environment.getExternalStorageDirectory()
                        + "/xiaobailong24/xunfeiyun");
    }

    private String getResourcePath(){
        StringBuffer tempBuffer = new StringBuffer();
        //识别通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(this, ResourceUtil.RESOURCE_TYPE.assets, "iat/common.jet"));
        tempBuffer.append(";");
        tempBuffer.append(ResourceUtil.generateResourcePath(this, ResourceUtil.RESOURCE_TYPE.assets, "iat/sms_16k.jet"));
        //识别8k资源-使用8k的时候请解开注释
        return tempBuffer.toString();
    }
    /**
     * 听写监听器。
     */
    private RecognizerListener recognizerListener = new RecognizerListener() {

        @Override
        public void onVolumeChanged(int i, byte[] bytes) {
            Log.e(TAG, "recognizerListener-->onVolumeChanged");

        }

        @Override
        public void onBeginOfSpeech() {
            Log.e(TAG, "recognizerListener-->onBeginOfSpeech");
        }

        @Override
        public void onEndOfSpeech() {
            Log.e(TAG, "recognizerListener-->onEndOfSpeech");
            speechText.setText("语音点菜");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.e(TAG, "recognizerListener-->onResult");
            String text = JsonParser.parseIatResult(results.getResultString());
            if (isLast) {
                // TODO 最后的结果
                searchAdapter.setList(searchlists);
                getSearchLayout.setVisibility(View.GONE);
                searchLayout.setVisibility(View.VISIBLE);
                search.setText(text);
                search();
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            Log.e(TAG, "recognizerListener-->onEvent");
        }

        @Override
        public void onError(SpeechError arg0) {
            speechText.setText("语音点菜"+arg0.getErrorDescription());
            Log.e(TAG, "recognizerListener-->onError"+arg0.getErrorDescription());
            // TODO Auto-generated method stub
        }
    };
    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.e(TAG, "mInitListener-->onInit");
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            speechText.setText("语音点菜");
            if (code != ErrorCode.SUCCESS) {
                showMessage("初始化失败,错误码：" );
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出时释放连接
        if(mIat!=null) {
            mIat.cancel();
            mIat.destroy();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1001&&resultCode==1001){
            lists = (ArrayList<MealListBean.MealRight>) data.getSerializableExtra("list");
            num1 = data.getIntExtra("num",0);
            OrderMealActivity.this.price = data.getDoubleExtra("price",0);
            tv_price.setText("总价：￥" + price);
            cataNum.setText(num1 + "");
            rightAdapter.setData(lists, remarklists, price, num1);
        }
    }
}
