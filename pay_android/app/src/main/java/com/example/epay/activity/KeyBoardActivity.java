package com.example.epay.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.OrderBean;
import com.example.epay.doHttp.HttpCallBack;
import com.example.epay.view.KeyboardView;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class KeyBoardActivity extends BaseActivity {


    @Bind(R.id.money_editText)
    EditText edit;
    @Bind(R.id.keyBoardView)
    KeyboardView keyView;

    OrderBean listBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_board);
        getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        edit.setInputType(InputType.TYPE_NULL);
        keyView.setOnNumberClickListener(new KeyboardView.OnNumberClickListener() {

            @Override
            public void onNumberReturn(String number) {
                if(edit.getText().toString().contains("."))
                {
                    if(edit.getText().toString().indexOf(".")==edit.getText().toString().length()-2&&number=="0")
                    {
                            showMessage("最少0.01元");
                            return;
                    }
                    if(edit.getText().toString().indexOf(".")==edit.getText().toString().length()-3)
                    {
                        showMessage("最多有两位小数");
                        return;
                    }
                }
                if(number.equals("0")&&edit.getText().toString().isEmpty()) {
                        edit.setText("0.");
                        return;
                }
                if(number.equals(".")) {
                    if (edit.getText().toString().contains(".")) {
                        return;
                    } else if (edit.getText().toString().isEmpty()) {
                        edit.setText("0.");
                        return;
                    }
                }
                 edit.setText(edit.getText().append(number));
            }
            //删除
            @Override
            public void onNumberDelete() {
                if (edit.getText().toString().equals("0."))
                {
                    edit.setText("");
                    return;
                }
                if(edit.getText().length()>0)
                {
                    edit.setText(edit.getText().toString().substring(0,edit.getText().length()-1));
                }


            }
            //关闭控件
            @Override
            public void onNumberOn() {
                if (edit.getText().toString()==null||edit.getText().toString().equals("")||edit.getText().toString().equals("0."))
                {
                    edit.setText("");
                    showMessage("请输入大于0.01的金额");
                }
                InVisibleView(R.id.keyBoardView);
            }
            //去收款
            @Override
            public void onNumber() {
                if(edit.getText().toString()==null||edit.getText().toString().equals("")||edit.getText().toString().equals("0."))
                {
                    showMessage("请输入大于0.01的金额");
                    InVisibleView(R.id.keyBoardView);
                }else {
                    if(Double.parseDouble(edit.getText().toString())<1000) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("remark", "");
                        jsonObject.put("money", edit.getText().toString());
                        jsonObject.put("needPrint", 0);
                        doDetail(JSON.toJSONString(jsonObject));
                    }else{
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(KeyBoardActivity.this);
                        builder.setMessage("确定收款￥"+edit.getText().toString()+"吗？？？");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("remark", "");
                                jsonObject.put("money", edit.getText().toString());
                                jsonObject.put("needPrint", 0);
                                doDetail(JSON.toJSONString(jsonObject));
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                    }
                }
            }
        });
    }
    //下单
    private void doDetail(final String data) {
        httpUtil.HttpServer(this, data, 80, true, new HttpCallBack() {
            @Override
            public void back(String data1) {
                listBean=gson.fromJson(data1,OrderBean.class);
                if(listBean!=null) {
                    Intent intent = new Intent(KeyBoardActivity.this, CollectCodeActivity.class);
                    intent.putExtra("orderID", listBean.getOrderNO());
                    intent.putExtra("sum", listBean.getSaleMoney());
                    intent.putExtra("dis", "0");
                    intent.putExtra("isisis", true);
                    startActivity(intent);
                }else{
                    showMessage("没有数据");
                }
            }

            @Override
            public void fail(String Message, int code,String data) {
                showMessage(Message);
            }
        });
    }


    @OnClick(R.id.key_out)
    public void out(View view)
    {
       startActivity(this,MainActivity.class);
    }
    @OnClick(R.id.money_editText)
    public void edit(View view)
    {
        if(edit.isFocused())
        {
            showView(R.id.keyBoardView);
        }
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("数字键盘"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("数字键盘"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
