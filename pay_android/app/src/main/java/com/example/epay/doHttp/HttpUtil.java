package com.example.epay.doHttp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import com.example.epay.BaseApplication;
import com.example.epay.activity.LoginActivity;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;


/**
 * Created by liujin on 2018/6/17.
 */

public class HttpUtil {

    //登录
    private String appMessage = "";
    private String message = "";
    private String RespBody = "";
    private String prompt = "";

    String payUrl = "http://pay.jqepay.com/";//api.jqwise.cn
    String testPayUrl = "http://pay.jqepay.cn/";//api.jqwise.cn

    public void HttpServer(final Activity context, final String data, final int action,
                           boolean isLoading,
                           final HttpCallBack httpCallBack) {
        if (isLoading) {
            prompt = "正在登陆……";
        } else {
            prompt = null;
        }

        Server server = new Server(context, prompt) {
            @Override
            protected Integer doInBackground(String... params) {
                ReturnResquest returnResquest = new ReturnResquest();
                try {
                    CuncResponse resp = returnResquest.request(context, data, action);
                    appMessage = "";
                    message = resp.errorMsg;
                    RespBody = resp.RespBody;
                    return resp.RespCode;
                } catch (IOException e) {
                    if (e instanceof SocketTimeoutException) {
                        //判断超时异常
                        return 5000;
                    }
                    if (e instanceof ConnectException) {
                        //判断连接异常，我这里是报Failed to connect to 10.7.5.144
                        return 4040;
                    }
                    return -1;
                }
            }

            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
                if (result == 0) {
                    httpCallBack.back(RespBody);
                } else {
//                    Toast.makeText(context,message,Toast.LENGTH_LONG).show();
                    if (result == 1) {
                        appMessage = message;
                    } else if (result == 2) {
                        context.startActivity(new Intent(context, LoginActivity.class));
                    } else if (result == 3) {
                        appMessage = message;
                    } else if (result == 4) {
                        appMessage = message;
                    } else if (result == 5) {
                        appMessage = message;
                    } else if (result == 7) {
                        appMessage = message;
                    } else if (result == 5000) {
                        appMessage = "请求超时，请刷新数据";
                    } else if (result == 4040) {
                        appMessage = "连接出错，请检查网络";
                    } else {
                        appMessage = message;
                    }
                    httpCallBack.fail(appMessage, result, RespBody);
                }
            }
        };
        server.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    //表单请求
    public void HttpServer(final Activity context, String url, final String data, boolean isLoading, final HttpCallBack httpCallBack) {
        if (BaseApplication.isTest()) {
            url = testPayUrl + url;
        } else {
            url = payUrl + url;
        }
        if (isLoading) {
            prompt = "正在登陆……";
        } else {
            prompt = null;
        }
        final String finalUrl = url;
        Server server = new Server(context, prompt) {
            @Override
            protected Integer doInBackground(String... params) {
                ReturnResquest returnResquest = new ReturnResquest();
                try {
                    CuncResponse resp = returnResquest.request(finalUrl, data);
                    message = resp.errorMsg;
                    RespBody = resp.RespBody;
                    return resp.RespCode;
                } catch (IOException e) {
                    if (e instanceof SocketTimeoutException) {
                        //判断超时异常
                        return 5000;
                    }
                    if (e instanceof ConnectException) {
                        //判断连接异常，我这里是报Failed to connect to 10.7.5.144
                        return 4040;
                    }
                    return -1;
                }
            }

            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
                if (result == 0) {
                    httpCallBack.back(RespBody);
                } else {
                    if (result == 1) {
                        appMessage = message;
                    } else if (result == 2) {
                        appMessage = message;
                    } else if (result == 3) {
                        appMessage = message;
                    } else if (result == 4) {
                        appMessage = message;
                    } else if (result == 5) {
                        appMessage = message;
                    } else if (result == 7) {
                        appMessage = message;
                    } else if (result == 5000) {
                        appMessage = "请求超时，请刷新数据";
                    } else if (result == 4040) {
                        appMessage = "连接出错，请检查网络";
                    } else {
                        appMessage = message;
                    }
                    httpCallBack.fail(appMessage, result, RespBody);
                }
            }
        };
        server.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}
