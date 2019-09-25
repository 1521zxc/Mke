package com.example.epay.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.example.epay.R;
import com.example.epay.adapter.AddressAdapter;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.AddressBean;
import com.example.epay.bean.UserBean;
import com.example.epay.cache.CacheData;
import com.example.epay.view.PxListView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class AddressActivity extends BaseActivity implements OnGetPoiSearchResultListener, OnGetGeoCoderResultListener,PxListView.PxListViewListener {
    @Bind(R.id.bmapView)
    MapView mMapView;
    @Bind(R.id.address_listView)
    PxListView listView;
    @Bind(R.id.et_text)
    EditText searchText;
    @Bind(R.id.bt_nutton)
    TextView exit;

    private AddressAdapter adapter;
    private ArrayList<AddressBean> list;
    private ArrayList<AddressBean> arraylist;
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    BaiduMap mBaiduMap;
    boolean isFirstLoc = true; // 是否首次定位

    private PoiSearch mPoiSearch = null;
    LatLng center = null;
    int radius = 50000;
    Marker marker;
    GeoCoder geocoder;
    LocationClientOption option;
    MapStatusUpdate u;
    int page=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
        initView();
        initListenr();
    }

    @OnClick(R.id.et_text)
    public void search()
    {
        exit.setVisibility(View.VISIBLE);
        mMapView.setVisibility(View.GONE);
        arraylist=list;
    }

    @OnClick(R.id.bt_nutton)
    public void exit()
    {
        exit.setVisibility(View.GONE);
        mMapView.setVisibility(View.VISIBLE);
        list.clear();
        list=arraylist;
        adapter.setList(list);
        page=1;
    }

    @Override
    public void initView() {
        super.initView();
        //关闭输入框
        list=new ArrayList<AddressBean>();
        arraylist=new ArrayList<AddressBean>();
        adapter=new AddressAdapter(this,list);
        listView.setAdapter(adapter);
        listView.setPxListViewListener(this);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        geocoder=GeoCoder.newInstance();
        geocoder.setOnGetGeoCodeResultListener(this);
        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);

        u=MapStatusUpdateFactory.newLatLngZoom(center,17.0f);
        mBaiduMap.animateMapStatus(u);
    }



    public void initListenr(){

        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {}
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {}
            @Override
            public void onMapStatusChange(MapStatus mapStatus) {}
            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                marker.setPosition(mapStatus.target);
                geocoder.reverseGeoCode(new ReverseGeoCodeOption().location(mapStatus.target));
            }
        });


        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听
                page=1;
                mPoiSearch.searchInCity(new PoiCitySearchOption()
                        .city("北京")
                        .pageCapacity(10)
                        .keyword(searchText.getText().toString())
                        .pageNum(page));
            }
        });
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                //当i == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (i == EditorInfo.IME_ACTION_SEND
                        || i == EditorInfo.IME_ACTION_DONE
                        || (keyEvent != null && KeyEvent.KEYCODE_ENTER == keyEvent.getKeyCode() && KeyEvent.ACTION_DOWN == keyEvent.getAction())) {
                    //处理事件
                    page=1;
                    mPoiSearch.searchInCity(new PoiCitySearchOption()
                            .city("北京")
                            .pageCapacity(10)
                            .keyword(searchText.getText().toString())
                            .pageNum(page));
                    return true;
                }
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UserBean userBean= CacheData.getMyBeans(AddressActivity.this);
                userBean.setAddress(list.get(i-1).getAddress());
                userBean.setUp(true);
                CacheData.cacheMyBeans(AddressActivity.this,userBean);
                startActivity(AddressActivity.this,AccoutActivity.class);
                finish();
            }
        });
    }


    //地图移动后中心点的监听1
    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }
    //2
    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            //没有找到检索结果
            showMessage("meiyoudedao");
        }

        //获取反向地理编码结果
        List<PoiInfo> arrayList=reverseGeoCodeResult.getPoiList();
        if(arrayList!=null&&arrayList.size()>0){
            if(page==1) {
                list.clear();
            }
            for (int i = 0; i < arrayList.size(); i++) {
                AddressBean bean=new AddressBean();
                bean.setName(arrayList.get(i).name);
                bean.setAddress(arrayList.get(i).address);
                //对需要的信息设置适配器，如果想在其他界面用，可以自己创建回调接口
                list.add(bean);
            }
            adapter.setList(list);
        }
    }

    @Override
    public void onRefresh() {
        Log.i(TAG, "onRefresh: ");
    }

    @Override
    public void onLoadMore() {
        page++;
        mPoiSearch.searchInCity(new PoiCitySearchOption()
                .city("北京")
                .keyword(searchText.getText().toString())
                .pageCapacity(10)
                .pageNum(page));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }

            center=new LatLng(location.getLatitude(), location.getLongitude());

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                OverlayOptions options = new MarkerOptions()
                        .position(center)  //设置Marker的位置
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_mark))  //设置Marker图标
                        .zIndex(9)  //设置Marker所在层级
                        .draggable(true);  //设置手势拖拽
                //将Marker添加到地图上
                marker = (Marker) (mBaiduMap.addOverlay(options));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
    //搜索的三个接口1
    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if(poiResult.error!= SearchResult.ERRORNO.NO_ERROR){
            //BaseApp.getInstance().showToast("检索fail");
            showMessage("没有数据");

        }else {
            mBaiduMap.clear();
            if(page==1) {
                list.clear();
            }
            if(poiResult.getAllPoi()!=null&&poiResult.getAllPoi().size()>0){
                List<PoiInfo> mPoiInfos=poiResult.getAllPoi();
                for (int i = 0; i < mPoiInfos.size(); i++) {
                    AddressBean bean=new AddressBean();
                    bean.setName(mPoiInfos.get(i).name);
                    bean.setAddress(mPoiInfos.get(i).address);
                    //对需要的信息设置适配器，如果想在其他界面用，可以自己创建回调接口
                    list.add(bean);
                }
                adapter.setList(list);
            }
        }
    }
//2
    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

    }

    //3
    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        listView=null;
        searchText=null;
        exit=null;
        mPoiSearch = null;
        adapter=null;
        list=null;
        myListener=null;
        center = null;
        marker=null;
        option=null;
        u=null;
        arraylist=null;

    }

    /**
     * 必须要实现
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPoiSearch!=null){
            mPoiSearch.destroy();
        }
        adapter=null;
        list=null;
        myListener=null;
        center = null;
        marker=null;
        geocoder.destroy();
        geocoder=null;
        option=null;
        u=null;
        arraylist=null;
        // 退出时销毁定位
        mLocClient=null;
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
    }

    /**
     * 必须要实现
     */
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("位置"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    /**
     * 必须要实现
     */
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
        MobclickAgent.onPageEnd("位置"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}