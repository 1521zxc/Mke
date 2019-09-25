package com.example.epay.activity;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.UserBean;
import com.example.epay.cache.CacheData;
import com.example.epay.doHttp.HttpCallBack;
import com.qiniu.android.storage.UploadManager;
import com.umeng.analytics.MobclickAgent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import androidx.annotation.NonNull;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import org.json.JSONException;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class AccoutActivity extends BaseActivity {

    @Bind(R.id.ac_logo) ImageView logo;
    @Bind(R.id.ac_icon) ImageView icon;
    @Bind(R.id.ac_name) TextView name;
    @Bind(R.id.ac_phone) TextView phone;
    @Bind(R.id.ac_address) TextView address;
    @Bind(R.id.account_icon) LinearLayout LayoutIcon;
    @Bind(R.id.account_logo) LinearLayout LayoutLogo;

    public static final int TAKE_PHOTO = 1;//启动相机标识
    public static final int SELECT_PHOTO = 2;//启动相册标识
    private File outputImagepath;//存储拍完照后的图片
    private File outputImagepath2;//存储拍完照后的图片
    private Uri getImageUri;
    private Uri getImageUri2;
    private String token = "";
    private int type = 3;
    UserBean userBean;
    private int sum = 0;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accout);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        //获取系統版本
        int currentapiVersion = Build.VERSION.SDK_INT;
        SimpleDateFormat timeStampFormat = new SimpleDateFormat(
                "yyyy_MM_dd_HH_mm_ss");
        String filename2 = timeStampFormat.format(new Date());
        outputImagepath2 = new File("file://" + "/" + Environment.getExternalStorageDirectory(),
                filename2 + ".jpg");
        userBean = CacheData.getMyBeans(this);
        if (!userBean.getCoverURL().equals("")) {
            loadBitmap(userBean.getCoverURL(), icon);
        }
        if (!userBean.getIconURL().equals("")) {
            loadBitmap(userBean.getIconURL(), logo);
        }

        name.setText(userBean.getBrandName());
        phone.setText(userBean.getMobile());
        address.setText(userBean.getAddress());
        token = CacheData.getUser(this, String.valueOf(CacheData.getId(this))).getQiniuToken();
    }

    @OnClick(R.id.account_logo)
    public void UpLogo() {
        type = 0;
        showChoosePicDialog();
    }

    @OnClick(R.id.account_icon)
    public void UpIcon() {
        type = 1;
        showChoosePicDialog();
    }

    @OnClick(R.id.account_layout)
    public void Upname() {
        startActivity(AccoutActivity.this, AccountNameActivity.class);
    }

    @Override
    public void onBackPressed() {
        if (!phone.getText().toString().equals(userBean.getMobile())) {
            userBean.setUp(true);
            userBean.setMobile(phone.getText().toString());
        }
        if (userBean.isUp()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AccoutActivity.this);
            builder.setTitle("提示");
            builder.setMessage("确定更改信息");
            builder.setNegativeButton("取消", (dialog, which) -> {
                dialog.dismiss();
                startActivity(AccoutActivity.this, MainActivity.class);
            });
            builder.setPositiveButton("确定", (dialog, which) -> {
                userBean.setUp(false);
                String objectStr = gson.toJson(userBean);
                doUp(objectStr);
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            super.onBackPressed();
        }
    }

    //更新
    private void doUp(final String data) {
        httpUtil.HttpServer(this, data, 42, true, new HttpCallBack() {
            @Override
            public void back(String data) {
                if (type == 3) {
                    CacheData.cacheMyBeans(AccoutActivity.this, userBean);
                    startActivity(AccoutActivity.this, MainActivity.class);
                } else {
                    return;
                }
            }
            @Override
            public void fail(String Message, int code, String data) {
                showMessage(Message);
            }
        });
    }

    @OnClick(R.id.ac_address)
    public void setAddress() {
        sum++;
        if (sum == 1) {
            startActivity(AccoutActivity.this, AddressActivity.class);
        } else {
            return;
        }
    }

    protected void showChoosePicDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");
        String[] items = {"拍照", "相册", "取消"};
        builder.setNegativeButton("", null);
        builder.setItems(items, (dialogInterface, i) -> {
            switch (i) {
                case 0: // 拍照
                    take_photo();
                    break;
                case 1: // 相册
                    openAlbum();
                    break;
                case 2: // 取消
                    builder.create().dismiss();
                    return;
            }
        });
        builder.show();
        Window window = builder.create().getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); //为获取屏幕宽、高
        WindowManager.LayoutParams p = builder.create().getWindow().getAttributes(); //获取对话框当前的参数值
        p.width = d.getWidth(); //宽度设置为屏幕
        builder.create().getWindow().setAttributes(p); //设置生效
    }



    public void crop() {
        String headImageFilePath = String.format("image%d.jpg", System.currentTimeMillis());
        File file = new File(getCacheDir(), headImageFilePath);
        imagePath = file.getAbsolutePath();
        Uri destinationUri = Uri.fromFile(file);
        UCrop.Options options = new UCrop.Options();
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        if (type == 0) {
            // 设置x,y的比例，截图方框就按照这个比例来截 若设置为0,0，或者不设置 则自由比例截图
            options.withAspectRatio(1.0f, 1.0f);
            options.withMaxResultSize(110, 110);
        } else if (type == 1) {
            options.withAspectRatio(27.0f, 9.0f);
            options.withMaxResultSize(270, 90);
        }
        UCrop.of(getImageUri, destinationUri)
            .withOptions(options)
            .start(this);
    }

    /**
     * 拍照获取图片
     **/
    public void take_photo() {
        //获取系統版本
        int currentapiVersion = Build.VERSION.SDK_INT;
        // 激活相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            SimpleDateFormat timeStampFormat = new SimpleDateFormat(
                    "yyyy_MM_dd_HH_mm_ss");
            String filename = timeStampFormat.format(new Date());
            outputImagepath = new File(Environment.getExternalStorageDirectory(),
                    filename + ".jpg");
            if (currentapiVersion < 24) {
                // 从文件中创建uri
                getImageUri = Uri.fromFile(outputImagepath);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri);
            } else {
                //兼容android7.0 使用共享文件的形式
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, outputImagepath.getAbsolutePath());
                getImageUri = getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri);
            }
            if (currentapiVersion < 24) {
                // 从文件中创建uri
                getImageUri2 = Uri.fromFile(outputImagepath2);
            } else {
                //兼容android7.0 使用共享文件的形式
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, outputImagepath2.getAbsolutePath());
                getImageUri2 = getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            }
        }
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
        startActivityForResult(intent, TAKE_PHOTO);
    }

    /**
     * 判断sdcard是否被挂载
     */
    public static boolean hasSdcard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }


    /**
     * 打开相册的方法
     */
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_PHOTO);
    }

    /**
     * 4.4以下系统处理图片的方法
     */
    private void handleImageBeforeKitKat(Intent data) {
        getImageUri = data.getData();
        crop();
    }

    /**
     * 4.4及以上系统处理图片的方法
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void handleImgeOnKitKat(Intent data) {
        getImageUri = data.getData();
        crop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //打开相机后返回
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    /**
                     * 这种方法是通过内存卡的路径进行读取图片，所以的到的图片是拍摄的原图
                     */
                    crop();
                }
                break;

            //打开相册后返回
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT > 19) {
                        //4.4及以上系统使用这个方法处理图片
                        handleImgeOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            //剪裁后后返回
            case UCrop.REQUEST_CROP:
                if (resultCode == RESULT_OK){
                  upImage();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                //判断是否有权限
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();//打开相册
                    take_photo();//打开相机
                } else {
                    showMessage("你需要许可");
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void upImage() {
        new Thread(() -> {
            UploadManager uploadManager = new UploadManager();
            uploadManager.put(imagePath, null, token,
                    (key, info, response) -> {
                        if (info.isOK()) {
                            Log.i(TAG, "complete: " + response.toString());
                            try {
                                String path = "http://file.jqepay.com/" + String.valueOf(response.get("key"));
                                if (type == 0) {
                                    loadBitmap(path, logo);
                                    userBean.setIconURL(path);
                                } else {
                                    loadBitmap(path, icon);
                                    userBean.setCoverURL(path);
                                }
                                CacheData.cacheMyBeans(AccoutActivity.this, userBean);
                                String objectStr = gson.toJson(userBean);
                                doUp(objectStr);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                        }
                    }, null);
        }).start();
    }

    public void onResume() {
        super.onResume();
        initView();
        sum = 0;
        MobclickAgent.onPageStart("账户信息"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("账户信息"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
