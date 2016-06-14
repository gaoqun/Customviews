package custom.gq.com.customviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

/**
 * Created by gaoqun on 2016/6/14.
 */
public class CustomViews extends View{

    private Paint mPaint;

    public CustomViews(Context context) {
        super(context, null);
        init();
    }

    public CustomViews(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init();
    }

    public CustomViews(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        //抗锯齿
        mPaint.setAntiAlias(true);
        /**
         * 填充
         * e.g. Paint.Style.FILL/STROKE/STROKE&&FILL
         */
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(final Canvas canvas) {
        canvas.drawColor(Color.BLUE);


        Rect sRect = new Rect(0, 0, 100, 100);
        canvas.drawLine(0, 0, 100, 100, mPaint);

        canvas.save();
        canvas.translate(200, 200);
        canvas.drawRect(sRect, mPaint);
        for (int i = 0; i < 200; i++) {
            if (i % 2 == 0)
                canvas.drawPoint(0, i, mPaint);
        }

        canvas.drawLine(0, 0, 100, 100, mPaint);

        Path path = new Path();
        path.lineTo(200, 200);
        path.lineTo(300, 100);
        path.lineTo(100, 400);
        path.setLastPoint(300, 300);
        path.close();
        canvas.drawPath(path, mPaint);

        RectF rectF = new RectF(200, 200, 400, 300);
        canvas.drawRoundRect(rectF, 20, 20, mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        canvas.drawOval(rectF, mPaint);
        canvas.drawCircle(100, 100, 200, mPaint);
        canvas.drawArc(150, 150, 500, 500, 30, 60, false, mPaint);
        canvas.drawPoints(
                new float[]{100, 100,
                        100, 200,
                        100, 300,
                        100, 400}, mPaint);
        canvas.translate(300, 300);

        canvas.drawLines(new float[]{
                100, 100, 200, 200,
                100, 200, 300, 300,
                300, 300, 400, 400
        }, mPaint);
//        for (int i=0;i<20;i++){
//            //缩放
//            canvas.scale(0.5f,0.5f);
//            canvas.drawRoundRect(0,0,400,400,10,10,mPaint);
//            //缩放+中心点
//            canvas.scale(0.5f,0.5f,100,100);
//            canvas.drawRoundRect(0,0,400,400,10,10,mPaint);
//            //缩放反转
//            canvas.scale(-0.5f,-0.5f);
//            canvas.drawRoundRect(0,0,400,400,30,30,mPaint);
//        }

        //save 当前canvas状态 restore后会恢复到之前save的状态  存储方式为栈方式 最上边的是最新的  可用         canvas.restoreToCount(2); 恢复到指定的canvas

        canvas.save();
        //rotate 旋转
        canvas.rotate(30);
        canvas.drawRoundRect(100, 100, 400, 500, 40, 40, mPaint);
        canvas.restore();

        canvas.rotate(-30, 100, 100);
        canvas.drawRoundRect(100, 100, 400, 500, 40, 40, mPaint);
        //倾斜  tan=1 45度
        canvas.skew(1, 0);
        mPaint.setColor(Color.YELLOW);
        canvas.drawLine(100, 100, 300, 300, mPaint);
        canvas.drawRoundRect(100, 100, 400, 400, 30, 30, mPaint);
        Toast.makeText(getContext(), "" + canvas.getSaveCount(), Toast.LENGTH_LONG).show();

        super.onDraw(canvas);
    }

}
