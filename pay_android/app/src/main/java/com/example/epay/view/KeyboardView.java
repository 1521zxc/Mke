package com.example.epay.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.epay.R;

/**
 * Created by liujin on 2018/3/5.
 */

public class KeyboardView extends View{

    private Paint mPaint;
    private Bitmap mBpDelete ,mBpOff;
    private float clickX, clickY;   //点击时的x,y坐标
    private float mWidth, mHeight;   //屏幕的宽高
    private float mRectWidth, mRectHeight;   //单个按键的宽高
    private float mRightHeight;  //右边一列的高度
    private float mWidthOfBp, mHeightOfBp;
    private float mWidthOfBpOff, mHeightOfBpOff;
    private boolean isInit = false;   //view是否已经初始化
    private String number;//点击的数字
    private float[] xs = new float[4];//声明数组保存每一列的矩形中心的横坐标
    private float[] ys = new float[4];//声明数组保存每一排的矩形中心的纵坐标
    private OnNumberClickListener onNumberClickListener;
    private float x1, y1, x2, y2;  //按下的时候所处的矩形的左上和右下的坐标
    /**
     * 判断刷新数据
     * -1 不进行数据刷新
     * 0  按下刷新
     * 1  弹起刷新
     */
    private int type = -1;



    public KeyboardView(Context context) {
        super(context);
    }



    public KeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    public KeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    public OnNumberClickListener getOnNumberClickListener() {
        return onNumberClickListener;
    }



    public void setOnNumberClickListener(OnNumberClickListener onNumberClickListener) {
        this.onNumberClickListener = onNumberClickListener;
    }





    @Override

    protected void onDraw(Canvas canvas) {
        if (!isInit) {
            initData();
        }

        //画键盘
        drawKeyboard(canvas);

        //判断是否点击数字
        if (clickX > 0 && clickY > 0) {
            if (type == 0) {  //按下刷新
                if ("intent".equals(number)) {
                    mPaint.setColor(Color.WHITE);
                    canvas.drawRect(new RectF(x1, y1, x2, y2), mPaint);
                } else  if("delete".equals(number)){
                    mPaint.setColor(Color.WHITE);
                    canvas.drawRect(new RectF(4+ 3* mRectWidth, mHeight *2/ 3+1 , 4 + 4 * mRectWidth, mHeight *2/ 3+ 1+ 2 * mRectHeight),  mPaint);
                } else  if("action".equals(number)){
                    mPaint.setColor(Color.WHITE);
                    canvas.drawRect(new RectF(4 + 3 * mRectWidth, mHeight *2/ 3 + 3 + 2 * mRectHeight, 4 + 4 * mRectWidth, mHeight *2/ 3 + 4 + 4 * mRectHeight),  mPaint);
                } else {
                    mPaint.setColor(Color.GRAY);
                    canvas.drawRect(new RectF(x1, y1, x2, y2), mPaint);
                    mPaint.setColor(Color.BLACK);
                    mPaint.setTextSize(getResources().getDimension(R.dimen.sp_28));// 设置字体大小
                    mPaint.setStrokeWidth(2);
                    canvas.drawText(number, clickX, clickY, mPaint);
                }
            } else if (type == 1) {  //抬起刷新
//                if ("intent".equals(number)) {
////                    mPaint.setColor(Color.GRAY);
////                    canvas.drawRect(new RectF(x1, y1, x2, y2), mPaint);
//                } else  if("delete".equals(number)){
////                    mPaint.setColor(Color.GRAY);
////                    canvas.drawRect(new RectF(4 + 3 * mRectWidth, mHeight / 2 + 1, 2 + 4 * mRectWidth, mHeight / 2 + 1 + mRightHeight), mPaint);
//                } else  if("action".equals(number)){
////                    mPaint.setColor(Color.BLUE);
////                    canvas.drawRect(new RectF(4 + 3 * mRectWidth, mHeight / 2 + 2 + mRightHeight, 2+ 4 * mRectWidth, mHeight / 2 + 1 + 2 * mRightHeight), mPaint);
//                } else {
//                    mPaint.setColor(Color.WHITE);
//                    canvas.drawRect(new RectF(x1, y1, x2, y2),  mPaint);
//                    mPaint.setColor(Color.BLACK);
//                    mPaint.setTextSize(60);// 设置字体大小
//                    mPaint.setStrokeWidth(2);
//                    canvas.drawText(number, clickX, clickY, mPaint);
//                }

                //绘制完成后,重置
                clickX = 0;

                clickY = 0;

            }

        }

    }



    private void initData() {

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWidth = getWidth();
        mHeight = getHeight();
        mBpDelete = BitmapFactory.decodeResource(getResources(), R.drawable.ic_backspace);
        mWidthOfBp = mBpDelete.getWidth();
        mHeightOfBp = mBpDelete.getHeight();
        mBpOff = BitmapFactory.decodeResource(getResources(), R.drawable.ic_jp);
        mWidthOfBpOff=mBpOff.getWidth();
        mHeightOfBp = mBpOff.getHeight();
        mRectWidth = (mWidth - 4) / 4;   //每个按键左右间距1  每个按键的宽度
        mRectHeight = mHeight/12-1;//每个按键上下间距1 每个按键的高度
        mRightHeight=(mHeight - 3) / 6;

        xs[0] = mRectWidth / 2;
        xs[1] = (mRectWidth * 3-3) /2;
        xs[2] = (mRectWidth * 5-5) / 2;
        xs[3] =(mRectWidth * 7-7) / 2;

        ys[0] = mRectHeight *3/ 4 + mHeight*2 / 3;
        ys[1] = (mRectHeight * 7+3) / 4  + mHeight *2/ 3;
        ys[2] = (mRectHeight * 11+5) / 4  + mHeight*2 / 3;
        ys[3] = (mRectHeight * 15+7) / 4  + mHeight *2/ 3;

        isInit = true;

    }



    /**
     * drawRoundRect(float left, float top, float right, float bottom, float rx, float ry, Paint paint)这种方式在5.0以下的机器上会报错，
     * 需要换成drawRoundRect(RectF rect, float rx, float ry, Paint paint)
     *
     * @param canvas
     */


    private void drawKeyboard(Canvas canvas) {

        mPaint.setColor(Color.WHITE);
        //画宫格

        //第一列
        canvas.drawRect(new RectF(1, mHeight *2/ 3+1 , 1+ mRectWidth, mHeight *2/ 3  + mRectHeight), mPaint);
        canvas.drawRect(new RectF(1, mHeight *2/ 3+ 2 + mRectHeight, 1 + mRectWidth, mHeight *2/ 3 + 1 + 2 * mRectHeight),  mPaint);
        canvas.drawRect(new RectF(1, mHeight *2/ 3 + 3 + 2 * mRectHeight, 1 + mRectWidth, mHeight *2/ 3 + 2 + 3 * mRectHeight), mPaint);
        canvas.drawRect(new RectF(1 , mHeight *2/ 3 +4 + 3 * mRectHeight, 1 +  mRectWidth, mHeight *2/ 3 + 3 + 4 * mRectHeight),  mPaint);



        //第二列
        mPaint.setColor(Color.WHITE);
        canvas.drawRect(new RectF(2 + mRectWidth, mHeight *2/ 3+1, 2 + 2 * mRectWidth, mHeight *2/ 3  + mRectHeight),mPaint);
        canvas.drawRect(new RectF(2 + mRectWidth, mHeight *2/ 3 + 2 + mRectHeight, 2+ 2 * mRectWidth, mHeight *2/ 3 + 1 + 2 * mRectHeight), mPaint);
        canvas.drawRect(new RectF(2 + mRectWidth, mHeight *2/ 3 + 3+ 2* mRectHeight, 2 + 2 * mRectWidth, mHeight *2/ 3 + 2 + 3 * mRectHeight),  mPaint);
        canvas.drawRect(new RectF(2 + mRectWidth, mHeight *2/ 3 + 4 + 3 * mRectHeight, 2 + 2 * mRectWidth, mHeight *2/ 3 + 3 + 4 * mRectHeight),  mPaint);

        //第三列
        mPaint.setColor(Color.WHITE);
        canvas.drawRect(new RectF(3+ 2 * mRectWidth, mHeight *2/ 3+1 , 3 + 3 * mRectWidth, mHeight *2/ 3 + mRectHeight),  mPaint);
        canvas.drawRect(new RectF(3 + 2 * mRectWidth, mHeight *2/ 3 + 2 + mRectHeight, 3 + 3 * mRectWidth, mHeight *2/ 3+ 1+ 2 * mRectHeight), mPaint);
        canvas.drawRect(new RectF(3 + 2 * mRectWidth, mHeight *2/ 3 + 3 + 2* mRectHeight, 3 + 3 * mRectWidth, mHeight *2/ 3 + 2+ 3 * mRectHeight),  mPaint);
        canvas.drawRect(new RectF(3 + 2 * mRectWidth, mHeight *2/ 3 + 4+ 3 * mRectHeight, 3 + 3 * mRectWidth, mHeight *2/ 3 + 3 + 4 * mRectHeight), mPaint);

        //第四列
        mPaint.setColor(Color.WHITE);
        canvas.drawRect(new RectF(4+ 3* mRectWidth, mHeight *2/ 3+1 , 4 + 4 * mRectWidth, mHeight *2/ 3+ 1+ 2 * mRectHeight),  mPaint);
        mPaint.setColor(Color.BLUE);
        canvas.drawRect(new RectF(4 + 3 * mRectWidth, mHeight *2/ 3 + 3 + 2 * mRectHeight, 4 + 4 * mRectWidth, mHeight *2/ 3 + 4 + 4 * mRectHeight),  mPaint);

        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(getResources().getDimension(R.dimen.sp_28));// 设置字体大小
        mPaint.setStrokeWidth(2);
        //画数字

        //第一列
        canvas.drawText("1", xs[0], ys[0], mPaint);
        canvas.drawText("4", xs[0], ys[1], mPaint);
        canvas.drawText("7", xs[0], ys[2], mPaint);
       // canvas.drawBitmap(mBpOff, xs[0] - mWidthOfBpOff / 2 -20, ys[3] - mHeightOfBpOff / 2 + 5, mPaint);
        drawImage(canvas,mBpOff,(int)(xs[0]-mWidthOfBpOff/3),(int)(ys[3]-mHeightOfBp*2/3),(int)mWidthOfBp*2/3,(int)mHeightOfBp*2/3,0,0);
        //第二列
        canvas.drawText("2", xs[1], ys[0], mPaint);
        canvas.drawText("5", xs[1], ys[1], mPaint);
        canvas.drawText("8", xs[1], ys[2], mPaint);
        canvas.drawText("0", xs[1], ys[3], mPaint);

        //第三列
        canvas.drawText("3", xs[2], ys[0], mPaint);
        canvas.drawText("6", xs[2], ys[1], mPaint);
        canvas.drawText("9", xs[2], ys[2], mPaint);
        canvas.drawText(".", xs[2], ys[3], mPaint);


        //第四列
      //【  canvas.drawText("9", xs[3],(ys[0]+ys[1])/2, mPaint);

       // canvas.drawBitmap();
        drawImage(canvas,mBpDelete,(int)(xs[3]-mWidthOfBp/3),(int)((ys[0]+ys[1])/2-mHeightOfBp*2/3),(int)mWidthOfBp*2/3,(int)mHeightOfBp*2/3,0,0);
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(getResources().getDimension(R.dimen.sp_16));// 设置字体大小
        mPaint.setStrokeWidth(2);
        canvas.drawText("去收款", xs[3]-mRectWidth/5,(ys[2]+ys[3])/2, mPaint);
    }

    /*---------------------------------
        * 绘制图片
        * @param       x屏幕上的x坐标
        * @param       y屏幕上的y坐标
        * @param       w要绘制的图片的宽度
        * @param       h要绘制的图片的高度
        * @param       bx图片上的x坐标
        * @param       by图片上的y坐标
        *
        * @return      null
        ------------------------------------*/
    public static void drawImage(Canvas canvas, Bitmap blt, int x, int y,
                                 int w, int h, int bx, int by) {
        Rect src = new Rect();// 图片 >>原矩形
        Rect dst = new Rect();// 屏幕 >>目标矩形

        src.left = bx;
        src.top = by;
        src.right = bx + w;
        src.bottom = by + h;

        dst.left = x;
        dst.top = y;
        dst.right = x + w;
        dst.bottom = y + h;
        // 画出指定的位图，位图将自动--》缩放/自动转换，以填补目标矩形
        // 这个方法的意思就像 将一个位图按照需求重画一遍，画后的位图就是我们需要的了
        canvas.drawBitmap(blt, null, dst, null);
        src = null;
        dst = null;
    }

    @Override

    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: //按下
                setDefault();
                handleDown(x, y);
                return true;
            case MotionEvent.ACTION_UP: //弹起
                type = 1;//弹起刷新
                invalidate();//刷新界面
                //一次按下结束,返回点击的数字
                if (onNumberClickListener != null) {
                    if (number != null) {
                        if (number.equals("intent")) {
                            onNumberClickListener.onNumberOn();
                        } else if(number.equals("delete")){
                            onNumberClickListener.onNumberDelete();
                        } else if(number.equals("action")){
                            onNumberClickListener.onNumber();
                        }else{
                            onNumberClickListener.onNumberReturn(number);
                        }
                    }
                }

                //恢复默认
                setDefault();
                return true;
            case MotionEvent.ACTION_CANCEL:  //取消
                //恢复默认值
                setDefault();
                return true;
            default:
                break;

        }
        return false;
    }



    private void setDefault() {

        clickX = 0;
        clickY = 0;
        number = null;
        type = -1;

    }



    private void handleDown(float x, float y) {

        if (y < mHeight / 2) {
            return;
        }
        if (x >= 2 && x <= 2 + mRectWidth) {   //第一列
            clickX = xs[0];
            if (y >= mHeight *2/ 3+1 && y <= mHeight *2/ 3 + mRectHeight) {  //第一排(1)
                clickY = ys[0];
                x1 = 1;
                y1 = mHeight *2/3 + 1;
                x2 = 1 + mRectWidth;
                y2 = mHeight *2/3 + 1 + mRectHeight;
                number = "1";
            } else if (y >= mHeight *2/3 + 4 + mRectHeight && y <= mHeight *2/3+ 4 + 2 * mRectHeight) {  //第二排(4)
                x1 = 1;
                y1 = mHeight *2/3 + 2 + mRectHeight;
                x2 = 1 + mRectWidth;
                y2 = mHeight *2/3 + 2 + 2 * mRectHeight;
                clickY = ys[1];
                number = "4";
            } else if (y >= mHeight *2/3 + 6 + 2 * mRectHeight && y <= mHeight *2/3 + 6 + 3 * mRectHeight) {  //第三排(7)
                x1 = 1;
                y1 = mHeight*2/3 + 3+ 2 * mRectHeight;
                x2 = 1 + mRectWidth;
                y2 = mHeight *2/3 + 3 + 3 * mRectHeight;
                clickY = ys[2];
                number = "7";
            } else if (y >= mHeight *2/3 + 8 + 3 * mRectHeight && y <= mHeight *2/3 + 8 + 4 * mRectHeight) { //第四排(0)
                x1 = 1;
                y1 = mHeight *2/3 + 4 + 3 * mRectHeight;
                x2 = 1 + mRectWidth;
                y2 = mHeight *2/3 + 4 + 4 * mRectHeight;
                clickY = ys[3];
                number = "intent";

            }

        } else if (x >= 4 + mRectWidth && x <= 4 + 2 * mRectWidth) {  //第二列
            clickX = xs[1];
            if (y >= mHeight *2/3 + 2 && y <= mHeight *2/3 + 2 + mRectHeight) {  //第一排(2)
                x1 = 2 + mRectWidth;
                y1 = mHeight *2/3 + 1;
                x2 = 2 + 2 * mRectWidth;
                y2 = mHeight *2/3 + 1 + mRectHeight;
                clickY = ys[0];
                number = "2";
            } else if (y >= mHeight *2/3 + 4 + mRectHeight && y <= mHeight*2/3+ 4 + 2 * mRectHeight) {  //第二排(5)
                x1 = 2 + mRectWidth;
                y1 = mHeight *2/3 + 2 + mRectHeight;
                x2 = 2 + 2 * mRectWidth;
                y2 = mHeight *2/3+ 2 + 2 * mRectHeight;
                clickY = ys[1];
                number = "5";
            } else if (y >= mHeight*2/3 + 6 + 2 * mRectHeight && y <= mHeight*2/3 + 6 + 3 * mRectHeight) {  //第三排(8)
                x1 = 2 + mRectWidth;
                y1 = mHeight *2/3 + 3 + 2 * mRectHeight;
                x2 = 2+ 2 * mRectWidth;
                y2 = mHeight *2/3 + 3 + 3 * mRectHeight;
                clickY = ys[2];
                number = "8";
            } else if (y >= mHeight *2/3 + 8 + 3 * mRectHeight && y <= mHeight*2/3 + 8 + 4 * mRectHeight) { //第四排(0)
                x1 = 2+ mRectWidth;
                y1 = mHeight *2/3 +4 + 3 * mRectHeight;
                x2 = 2 + 2 * mRectWidth;
                y2 = mHeight *2/3 + 4+ 4 * mRectHeight;
                clickY = ys[3];
                number = "0";
            }

        } else if (x >= 6 + 2 * mRectWidth && x <= 6 + 3 * mRectWidth) {   //第三列
            clickX = xs[2];
            if (y >= mHeight*2/3 + 2 && y <= mHeight *2/3 + 2 + mRectHeight) {  //第一排(3)
                x1 = 3 + 2 * mRectWidth;
                y1 = mHeight *2/3 + 1;
                x2 = 3 + 3 * mRectWidth;
                y2 = mHeight *2/3 + 1 + mRectHeight;
                clickY = ys[0];
                number = "3";
            } else if (y >= mHeight *2/3+ 4 + mRectHeight && y <= mHeight*2/3 + 4 + 2 * mRectHeight) {  //第二排(6)
                x1 = 3+ 2 * mRectWidth;
                y1 = mHeight*2/3 + 2 + mRectHeight;
                x2 = 3 + 3 * mRectWidth;
                y2 = mHeight *2/3 + 2 + 2 * mRectHeight;
                clickY = ys[1];
                number = "6";
            } else if (y >= mHeight *2/3 + 6 + 2 * mRectHeight && y <= mHeight*2/3 + 6 + 3 * mRectHeight) {  //第三排(9)
                x1 = 3 + 2 * mRectWidth;
                y1 = mHeight*2/3 + 3 + 2 * mRectHeight;
                x2 = 3+ 3 * mRectWidth;
                y2 = mHeight *2/3 + 3 + 3 * mRectHeight;
                clickY = ys[2];
                number = "9";
            } else if (y >= mHeight *2/3 + 8 + 3 * mRectHeight && y <= mHeight *2/3 + 8 + 4 * mRectHeight) { //第四排(删除键)
                x1 = 3 + 2 * mRectWidth;
                y1 = mHeight*2/3 + 4 + 3 * mRectHeight;
                x2 = 3 + 3 * mRectWidth;
                y2 = mHeight *2/3 + 4 + 4 * mRectHeight;
                clickY = ys[3];
                number = ".";
            }
        }else if(x >= 8 + 2 * mRectWidth && x <=4 * mRectWidth)
        {
            clickX = xs[2];
            if (y >= mHeight*2/3+ 2 && y <= mHeight*2/3 + 4 + 2 * mRectHeight) {  //第一排(3)
                x1 = 6 + 2 * mRectWidth;
                y1 = mHeight*2/3+ 2;
                x2 = 6 + 3 * mRectWidth;
                y2 = mHeight *2/3+ 2 + mRectHeight;
                clickY = ys[0];
                number = "delete";
            } else if (y >= mHeight*2/3 + 6 + 2 * mRectHeight && y <= mHeight *2/3 + 8 + 4 * mRectHeight) {  //第三排(9)
                x1 = 6 + 2 * mRectWidth;
                y1 = mHeight*2/3+ 6 + 2 * mRectHeight;
                x2 = 6 + 3 * mRectWidth;
                y2 = mHeight *2/3 + 6 + 3 * mRectHeight;
                clickY = ys[2];
                number = "action";
            }
        }
        type = 0;   //按下刷新
        invalidate();
    }



    public interface OnNumberClickListener {
        //回调点击的数字
        public void onNumberReturn(String number);
        //删除键的回调
        public void onNumberDelete();
        //关闭键的回调
        public void onNumberOn();
        //跳转的回调
        public void onNumber();
    }

}
