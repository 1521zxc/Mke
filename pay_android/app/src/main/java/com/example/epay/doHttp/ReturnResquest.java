package com.example.epay.doHttp;

import android.content.Context;
import android.util.Log;

import com.example.epay.BaseApplication;
import com.example.epay.activity.ServicePayActivity;
import com.example.epay.cache.CacheData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;


/**
 * Created by zyf on 2015/8/18.
 * 登陆接口
 */
public class ReturnResquest {
    String url ="https://api.jqepay.com";//api.jqwise.cn
    String testUrl="http://api.jqepay.cn";
    String TAG="epayaaaaa";

    //请求
    public CuncResponse request(Context context, String data, int action)throws IOException {
        CuncResponse response = new CuncResponse();
        Response resp2;
        if(BaseApplication.isTest())
        {
            resp2= OkHttpClientManager.postSafe(testUrl, this.encoding(context, data, action));
        }else {
            resp2= OkHttpClientManager.postSafe(url, this.encoding(context, data, action));
        }
        String resp = resp2.body().string();

        try {
            JSONObject tespJo = new JSONObject(resp);
            response.RespCode = tespJo.getInt("errorCode");
            if(tespJo.has("message")) {
                response.errorMsg = tespJo.getString("message");
            }

            if(tespJo.has("data")) {
                response.RespBody = tespJo.getString("data");
            }
            Log.e(TAG, "request:RespBody: "+response.RespBody);
            if (action==31)
            {
                String token = tespJo.getString("token");
                CacheData.setToken(context,token);
                CacheData.setLoginstate(context,true);
            } else{

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }

    //封装请求body
    private String encoding(Context context,String data,int action) {
        JSONObject jsonObject = new JSONObject();
        String token=CacheData.getToken(context);

        try {//{"op":1,"seq":0,"ver":0}
            jsonObject.put("seq", 0);
            jsonObject.put("ver", 0);
            //jsonObject.put("token","18610039992");

            jsonObject.put("token",token);
            if(!data.equals("")) {
                jsonObject.put("data", data);
            }
            if(action==1){
                jsonObject.put("debug",-1);
            }
            if(data.contains("muuid")) {
                jsonObject.put("muuid",CacheData.getUser(context, String.valueOf(CacheData.getId(context))).getMuuid());
            }else{
                jsonObject.put("muuid", CacheData.getUser(context, String.valueOf(CacheData.getId(context))).getMuuid());
            }
            jsonObject.put("op", action);
            Log.e(TAG, "jsonObject: "+jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    //表单请求
    public CuncResponse request(Context context,String orderID,String money, String url,String authCode)throws IOException {
        CuncResponse response = new CuncResponse();
        Response resp2 = OkHttpClientManager.postSafe(url, encoding(context,orderID,money,authCode));
        String resp = resp2.body().string();
        try {
            Log.i(TAG, "request:uuuuuu"+resp );
            JSONObject tespJo = new JSONObject(resp);
            response.RespCode = tespJo.getInt("errorCode");
            response.errorMsg = tespJo.getString("message");
            response.RespBody = tespJo.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }
    //表单请求
    public CuncResponse request( String url,String data)throws IOException {
        Log.e(TAG, "url: "+url+",,,,data："+data);
        CuncResponse response = new CuncResponse();
        Response resp2 = OkHttpClientManager.postSafe(url, encoding(data));

        String resp = resp2.body().string();
        try {
            Log.e(TAG, "request:uuuuuu"+resp );
            JSONObject tespJo = new JSONObject(resp);
            response.RespCode = tespJo.getInt("errorCode");
            if(tespJo.has("message")) {
                response.errorMsg = tespJo.getString("message");
            }
            if(tespJo.has("data")) {
                response.RespBody = tespJo.getString("data");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }
    //封装请求body
    private String encoding(String data) {
        return data;
    }

    //封装请求body
    private String encoding(Context context,String orderNO,String money,String authCode) {
        StringBuilder sb = new StringBuilder();
        sb.append("orderNO=").append(orderNO);
        sb.append("&");
        sb.append("money=").append(money);
        sb.append("&");
        sb.append("muuid=").append( CacheData.getUser(context, String.valueOf(CacheData.getId(context))).getMuuid());
        if (authCode.equals("")||authCode==null)
        {
        }else{
            sb.append("&");
            sb.append("authCode=").append(authCode);
        }
        Log.e(TAG, "encoding: "+sb.toString() );
        return sb.toString();
    }
}
