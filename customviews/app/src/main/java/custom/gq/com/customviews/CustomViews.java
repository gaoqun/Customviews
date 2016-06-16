package custom.gq.com.customviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by gaoqun on 2016/6/14.
 */
public class CustomViews extends View{

    private Paint mPaint;
    private Picture mPicture;

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
        /**
         * draw(canvas) / drawpicture() / drawdrawable()
         * draw()方法在低版本不兼容 不推荐使用
         */


        mPaint = new Paint();
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        //抗锯齿
        mPaint.setAntiAlias(true);
        /**
         * 填充
         * e.g. Paint.Style.FILL/STROKE/STROKE&&FILL
         */
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(10);
        //picture 讲canvas绘制的内容录制到Picture中，不会显示
        mPicture = new Picture();

        Canvas canvas1 = mPicture.beginRecording(500,500);
        canvas1.drawColor(Color.BLUE);
        mPaint.setColor(Color.BLACK);
        canvas1.drawRect(0,0,500,500,mPaint);
        mPicture.endRecording();


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(final Canvas canvas) {
//        mPicture.draw(canvas);不推荐使用 低版本不兼容
//        canvas.drawPicture(mPicture);

        mPaint.setStyle(Paint.Style.STROKE);



        Rect sRect = new Rect(0, 0, 100, 100);
        canvas.drawLine(0, 0, 100, 100, mPaint);

        canvas.save();
        canvas.translate(200, 200);
        /*canvas.drawRect(sRect, mPaint);
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

//        canvas.drawPicture(mPicture);
        PictureDrawable pictureDrawable = new PictureDrawable(mPicture);
        pictureDrawable.draw(canvas);*/
        Bitmap bitmap = null;
        //读取资源文件
//        bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_eg);
        //        canvas.drawBitmap(bitmap,10,10,mPaint);

        //读取assets图片
//        try {
//            InputStream inputStream = getContext().getAssets().open("ic_eg.jpg");
//            bitmap = BitmapFactory.decodeStream(inputStream);
////            bitmap.setConfig(Bitmap.Config.RGB_565);
////            bitmap.getScaledWidth(canvas);
//            canvas.drawBitmap(bitmap,10,10,mPaint);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //读取内存卡读片
        bitmap = BitmapFactory.decodeFile("/storage/emulated/0/Download/190550.jpg");
//        canvas.drawBitmap(bitmap,200,200,mPaint);
        //网络资源读取 略

        Rect src = new Rect(0,0,bitmap.getWidth()/2,bitmap.getHeight()/2);
        Rect dst = new Rect(0,0,300,300);
        canvas.drawBitmap(bitmap,src,dst,mPaint);
        canvas.save();
        canvas.restore();
        canvas.translate(300,300);
        mPaint.setColor(Color.RED);
        mPaint.setAlpha(150);
        mPaint.setARGB(100,100,100,100);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        mPaint.setTextSize(100);
        mPaint.setTypeface(Typeface.MONOSPACE);//字体样式
        mPaint.setTextAlign(Paint.Align.CENTER);//设置对其样式


        canvas.drawText("fuck",0,0,mPaint);
        canvas.drawText("abccdefg",2,5,100,100,mPaint);

        Path path = new Path();
        path.lineTo(0,300);
        if(!path.getFillType().equals(Path.FillType.WINDING)){
            path.setFillType(Path.FillType.INVERSE_EVEN_ODD);
        }
        path.rLineTo(200,100);
        path.close();
        canvas.drawPath(path,mPaint);
        mPaint.setAlpha(255);
        path.moveTo(100,100);
        path.setLastPoint(0,0);
        path.addOval(0,0,300,300, Path.Direction.CW);
        canvas.drawPath(path,mPaint);
        mPaint.setTextAlign(Paint.Align.RIGHT);
        mPaint.setTypeface(Typeface.MONOSPACE);
        path.addRect(200,200,500,500, Path.Direction.CCW);
        canvas.drawPath(path,mPaint);
//        if (!path.isEmpty())
//        path.addPath(path);
        mPaint.setStrokeWidth(10);
        path.addArc(new RectF(0,0,100,100),30,160);
        canvas.drawPath(path,mPaint);
        Path path1 = new Path();
        RectF rect = new RectF(-200,-199,400,500);
        path1.addRect(rect, Path.Direction.CW);
        path1.rCubicTo(100,100,300,300,500,300);
        path1.incReserve(5);
//        path.set(path1);
//        path.op(path1, Path.Op.UNION);//op combine xor
        path.op(path1, Path.Op.REVERSE_DIFFERENCE);//op combine xor
        canvas.drawPath(path,mPaint);
        path.reset();
        path.lineTo(300,0);
        path.lineTo(200,0);
        path.close();
        path.computeBounds(rect,true);//极端边界值
        path.transform(new Matrix());
        canvas.drawPath(path,mPaint);
        path.setLastPoint(200,100);//reset last point
        path.addRect(rect, Path.Direction.CCW);//逆时针  CW顺指针

        //贝塞尔曲线






        super.onDraw(canvas);
    }

}
