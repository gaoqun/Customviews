package custom.gq.com.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by gaoqun on 2016/6/24.
 */
public class MyViewDrag1 extends FrameLayout {
    private ViewDragHelper mViewDragHelper;
    private View backGroundView;
    private View contentView;
    private Point autoBackPoint;
    private Paint mPaint;
    private PointF start, end, control;
    private int mMaxVerticalRange;

    public MyViewDrag1(Context context) {
        super(context);
        init();
    }


    public MyViewDrag1(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyViewDrag1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        Log.d("onDraw","Ondraw.............");
//
//    }


    /**
     * viewgroup中执行画图
     * @param canvas
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        Path path = new Path();
        path.moveTo(start.x, start.y);
        path.quadTo(control.x, control.y, end.x, end.y);
        canvas.drawPath(path, mPaint);
    }

    private void init() {
//        setWillNotDraw(false);//保证执行ondraw

        autoBackPoint = new Point();
        start = new PointF(0, 0);
        end = new PointF(0, 0);
        control = new PointF(0, 0);
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {

            /**
             * only capture the content view
             * @param child
             * @param pointerId
             * @return
             */
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                mMaxVerticalRange = contentView.getHeight()*2/3;
                return child == contentView;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                final int topBound = getPaddingTop();
                int newTop = Math.min(Math.max(top, topBound), mMaxVerticalRange);
                if (newTop>mMaxVerticalRange){
                    return mMaxVerticalRange;
                }
                return newTop;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (releasedChild == contentView) {
                    mViewDragHelper.settleCapturedViewAt(autoBackPoint.x, autoBackPoint.y);
                    invalidate();
                }
            }

            /**
             * 解决由于可以点击拦截不能对view进行捕获的问题
             * @param
             * @return
             */
            @Override
            public int getViewHorizontalDragRange(View child) {
                return getMeasuredWidth() - child.getMeasuredWidth();
            }

            @Override
            public int getViewVerticalDragRange(View child) {
//                return getMeasuredHeight() - child.getMeasuredHeight();
                return mMaxVerticalRange;
            }
            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                mViewDragHelper.captureChildView(contentView, pointerId);
            }

            @Override
            public void onEdgeTouched(int edgeFlags, int pointerId) {
                super.onEdgeTouched(edgeFlags, pointerId);
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
//                super.onViewPositionChanged(changedView, left, top, dx, dy);
                control.x = getWidth()/2;
                control.y = top;
                invalidate();
                Log.d("control.X_Y",control.x+"_"+control.y);
            }
        });
//        mViewDragHelper.setMinVelocity(minVel);
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_TOP);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setDither(true);//递色
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(30);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //控制点画在contentview顶部中间
        end.x = getWidth();
        end.y = 0;
        control.x = getWidth() / 2;
        control.y = 0;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mViewDragHelper.continueSettling(true)) invalidate();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mViewDragHelper.shouldInterceptTouchEvent(ev)) return true;
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        autoBackPoint.x = contentView.getLeft();
        autoBackPoint.y = contentView.getTop();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        backGroundView = getChildAt(0);
        contentView = getChildAt(1);
    }

}
