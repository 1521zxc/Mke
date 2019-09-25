package com.example.epay.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.epay.doHttp.HttpUtil;
import com.example.epay.util.ImageViewLoader;
import com.google.gson.Gson;


/**
 * Created by liujin on 2018/1/17.
 */

public  abstract class BaseFragment extends Fragment {
    public String TAG;
    public  int width;
    public  int height;
    public Gson gson = new Gson();
    public HttpUtil  httpUtil = new HttpUtil();

    /**
     * 布局文件对应的id
     */
    public abstract int initViewId();

    /**
     * 用户手动调用
     */
    public BaseFragment() { }

    public abstract void initView();
    public long time;
    protected View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        width=getActivity().getWindowManager().getDefaultDisplay().getWidth();
        height=getActivity().getWindowManager().getDefaultDisplay().getHeight();
        TAG = this.getClass().getSimpleName();
        log(TAG
                + " onCreate------------------------------------------------------------------------------------");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        log(TAG
                + " onActivityCreated------------------------------------------------------------------------------------");
        initView();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        log(this.getClass().getSimpleName()
                + " onAttach------------------------------------------------------------------------------------");
        super.onAttach(activity);
    }

    @Override
    public void onDestroy() {
        log(TAG
                + " onDestroy------------------------------------------------------------------------------------");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        log(TAG
                + " onDestroyView------------------------------------------------------------------------------------");
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        log(TAG
                + " onPause------------------------------------------------------------------------------------");
        super.onPause();
    }

    @Override
    public void onResume() {
        log(TAG
                + " onResume------------------------------------------------------------------------------------");
        super.onResume();
    }

    @Override
    public void onStop() {
        log(TAG
                + " onStop------------------------------------------------------------------------------------");
        super.onStop();
    }

    @Override
    public void onStart() {
        log(TAG
                + " onStart------------------------------------------------------------------------------------");
        super.onStart();
    }

    @Override
    public void onDetach() {
        log(TAG
                + " onDetach------------------------------------------------------------------------------------");
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        log(TAG
                + " onSaveInstanceState------------------------------------------------------------------------------------");
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int id = initViewId();
        log(TAG + " onCreateView------------------------------------------------------------------------------------");
        view = inflater.inflate(id, container, false);
        return view;
    }

    public void loadBitmap(String uri, ImageView imageView) {
        ImageViewLoader.load(uri, imageView);

    }
    public void load(String uri, ImageView imageView,int round) {
        ImageViewLoader.load(uri, imageView,round);

    }
    public View findViewById(int id) {
        if (view != null)
            return view.findViewById(id);
        return null;
    }

    @Override
    public void startActivity(Intent intent) {
        try {
            super.startActivity(intent);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void log(String log) {
        Log.i("epay", TAG + "    " + log);
        Log.i(TAG, log);
    }

    public void startActivity(Class<?> actClass) {
        Intent intent = new Intent(this.getActivity(), actClass);
        startActivity(intent);
    }

    public void toast(String message) {
        Activity act = this.getActivity();
        while (act.getParent() != null) {
            act = act.getParent();
        }
        showMessage(act,message);
    }
    private void showMessage(Context context, String message) {
        if (context == null || message == null||message.trim().equals(""))
            return;
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}