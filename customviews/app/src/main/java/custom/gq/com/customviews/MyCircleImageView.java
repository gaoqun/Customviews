package custom.gq.com.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

/**
 * Created by gaoqun on 2016/6/20.
 */
public class MyCircleImageView extends ImageView {

    /**
     * 图片类型
     */
    private static final int TYPE_CIRCLE = 0;
    private static final int TYPE_ROUND = 1;
    private int mType;

    /**
     * 图片
     */
    private Bitmap mSrc;

    /**
     * 控件的宽高
     */
    private int mWidth;
    private int mHeight;

    /**
     * 圆角的大小
     */
    private int mRadius;

    /**
     * 圆边宽度
     */
    private static final float DEFAULT_BORDER_WIDTH = 10f;
    private float borderWidth;

    /**
     * 圆边颜色
     */
    private int borderColor = Color.RED;

    public MyCircleImageView(Context context) {
        super(context);
        init(null, 0);
    }

    public MyCircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MyCircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        final TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MyCircleImageView, defStyleAttr, 0);
        int typeArrayCount = typedArray.getIndexCount();
        for (int i = 0; i < typeArrayCount; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {

                case R.styleable.MyCircleImageView_borderRadius:
                    mRadius = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.MyCircleImageView_src:
                    mSrc = BitmapFactory.decodeResource(getResources(), typedArray.getResourceId(attr, 0));
                    break;
                case R.styleable.MyCircleImageView_type:
                    mType = typedArray.getInt(attr, TYPE_CIRCLE);
                    break;
                case R.styleable.MyCircleImageView_border_color:
                    borderColor = typedArray.getColor(attr, Color.RED);
                    break;
                case R.styleable.MyCircleImageView_border_width:
//                    borderWidth = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()));
                    borderWidth = typedArray.getFloat(attr, DEFAULT_BORDER_WIDTH);
                    break;
                default:
                    break;
            }
        }
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int specWidth = MeasureSpec.getSize(widthMeasureSpec);
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {//match_parent
            mWidth = specWidth;
        } else {
            /**
             * 根据图片宽度决定
             */
            int desireImageWidth = getPaddingLeft() + getPaddingRight() + mSrc.getWidth();
            if (specMode == MeasureSpec.AT_MOST) {//warp_content
                mWidth = Math.min(desireImageWidth, specWidth);
            } else mWidth = desireImageWidth;
        }

        int specHeight = MeasureSpec.getSize(heightMeasureSpec);
        int specHeightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (specHeightMode == MeasureSpec.EXACTLY) {
            mHeight = specHeight;
        } else {
            int desireHeight = getPaddingTop() + getPaddingBottom() + mSrc.getHeight();
            if (specHeightMode == MeasureSpec.AT_MOST) {
                mHeight = Math.min(desireHeight, specHeight);
            } else
                mHeight = specHeight;
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        Drawable drawable = getDrawable();
        if (drawable==null)return;
        if (drawable.getClass()== NinePatchDrawable.class)return;

        switch (mType) {

            case TYPE_CIRCLE:
                final Paint paint = new Paint();
                paint.setAntiAlias(true);
                int min = Math.min(mWidth, mHeight);
                //压缩
                mSrc = Bitmap.createScaledBitmap(mSrc, min, min, false);
                //创建期望的bitmap的大小
                Bitmap bitmap = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_4444);
                //创建一个跟bitmap一样大小的画布，这种方式创建的canvas，其内容都会被画到其所传入的bitmap上
                Canvas desireCanvas = new Canvas(bitmap);
                //画圆
                desireCanvas.drawCircle(min / 2, min / 2, min / 2, paint);
                //设置模式
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                //画图
                desireCanvas.drawBitmap(mSrc, 0, 0, paint);
                canvas.drawBitmap(bitmap, 0, 0, null);

                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(borderColor);
                paint.setStrokeWidth(borderWidth);
                RectF rectF = new RectF(0, 0, min, min);
                canvas.drawArc(rectF, 0, 360, false, paint);
                break;
            case TYPE_ROUND:
                final Paint roundPaint = new Paint();
                roundPaint.setAntiAlias(true);
                mSrc = Bitmap.createScaledBitmap(mSrc, mWidth, mHeight, false);
                Bitmap roundBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_4444);
                Canvas roundCanvas = new Canvas(roundBitmap);
                roundCanvas.drawRoundRect(new RectF(0, 0, mWidth, mHeight), mRadius, mRadius, roundPaint);
                //设置模式
                roundPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                roundCanvas.drawBitmap(mSrc, 0, 0, roundPaint);
                canvas.drawBitmap(roundBitmap, 0, 0, null);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
        invalidate();
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        invalidate();
    }

}

