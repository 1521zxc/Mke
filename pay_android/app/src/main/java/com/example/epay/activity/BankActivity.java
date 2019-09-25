package com.example.epay.activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.example.epay.R;
import com.example.epay.adapter.BankCardListAdapter;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.BeanBankCard;
import com.example.epay.doHttp.CuncResponse;
import com.example.epay.doHttp.ReturnResquest;
import com.example.epay.doHttp.Server;
import com.example.epay.view.EPayDialog;
import com.umeng.analytics.MobclickAgent;
import java.util.ArrayList;
import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class BankActivity extends BaseActivity{
    @Bind(R.id.bank_listView)
    ListView listView;
    BankCardListAdapter adapter;
    ArrayList<BeanBankCard> bankCards;
    EPayDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);
        ButterKnife.bind(this);
        dialog=new EPayDialog(this,R.layout.bank_dialog);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        BeanBankCard bankCard=new BeanBankCard();
        bankCards=new ArrayList<BeanBankCard>();
        bankCards.add(bankCard);
        adapter=new BankCardListAdapter(this,bankCards);
        listView.setAdapter(adapter);
        //doBank();
    }

    @OnClick(R.id.banK_up)
    public void up(View view) {
        dialog.show();
        View v=dialog.getView();
        final TextView textView=v.findViewById(R.id.bank_up);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(BankActivity.this,VerificationActivity.class);
                dialog.dismiss();
            }
        });
    }
    //银行卡列表
    String message = "";
    private void doBank() {
        Server server = new Server(BankActivity.this, "正在登陆……") {
            @Override
            protected Integer doInBackground(String... params) {
                ReturnResquest returnResquest = new ReturnResquest();
                try {
                    CuncResponse resp = returnResquest.request(BankActivity.this, "", 61);
                    message = resp.errorMsg;
                    return resp.RespCode;
                } catch (Exception e) {
                    return -1;
                }
            }

            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
            }
        };
        server.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("收款银行账户"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("收款银行账户"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
