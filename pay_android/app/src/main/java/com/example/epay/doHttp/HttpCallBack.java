package com.example.epay.doHttp;

/**
 * Created by liujin on 2018/6/17.
 */

public interface  HttpCallBack {
    public void back(String data);
    public void fail(String Message,int code,String data);
}
