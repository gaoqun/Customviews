package custom.gq.com.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by gaoqun on 2016/6/16.
 */
public class CubicCustomView extends View {

    private Paint mPaint;
    private int centerX, centerY;
    private boolean flag = false;//up:true  down/false

    private PointF start, end, control, control2;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public CubicCustomView(final Context context) {
        super(context);

        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(8);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(60);

        start = new PointF(0, 0);
        end = new PointF(0, 0);
        control = new PointF(0, 0);
        control2 = new PointF(0, 0);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (control.y >= centerY * 2) {
                        control.y = control.y - 10;
                        if (flag) flag = false;
                        else flag = true;
                    } else if (control.y <= 0) {
                        control.y = control.y + 10;
                        if (flag) flag = false;
                        else flag = true;
                    } else {
                        if (flag) {
                            control.y = control.y - 10;
                        } else {
                            control.y = control.y + 10;
                        }
                    }
                    control2.y = control.y;
                    try {
                        // 为了保证效果的同时，尽可能将cpu空出来，供其他部分使用
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                    }
                    postInvalidate();
                }
            }
        }).start();
    }

    /**
     * view current width & height
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;

        // 初始化数据点和控制点的位置
        start.x = centerX - 200;
        start.y = centerY;
        end.x = centerX + 200;
        end.y = centerY;
        control.x = centerX - 100;
        control.y = centerY - 100;
        control2.x = centerX + 100;
        control2.y = centerY - 100;


    }

    /**
     * if your hand up the animation will return back
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        control.x = event.getX() - 100;
        control.y = event.getY();
        control2.x = event.getX() + 100;
        control2.y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:


                break;
            case MotionEvent.ACTION_UP:
//                animationReset();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            default:
                break;
        }
        invalidate();


        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制数据点和控制点
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(20);
        mPaint.setDither(true);//防抖动
        canvas.drawPoint(start.x, start.y, mPaint);
        canvas.drawPoint(end.x, end.y, mPaint);
        canvas.drawPoint(control.x, control.y, mPaint);
        canvas.drawPoint(control2.x, control2.y, mPaint);

//        // 绘制辅助线
        mPaint.setStrokeWidth(4);
        canvas.drawLine(start.x, start.y, control.x, control.y, mPaint);
        canvas.drawLine(control.x, control.y, control2.x, control2.y, mPaint);
        canvas.drawLine(control2.x, control2.y, end.x, end.y, mPaint);

        // 绘制贝塞尔曲线
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(8);
        mPaint.setStyle(Paint.Style.FILL);

        Path path = new Path();

        path.moveTo(start.x, start.y);
//        path.quadTo(control.x,control.y,end.x,end.y);
        path.cubicTo(control.x, control.y, control2.x, control2.y, end.x, end.y);

        canvas.drawPath(path, mPaint);
    }
}

