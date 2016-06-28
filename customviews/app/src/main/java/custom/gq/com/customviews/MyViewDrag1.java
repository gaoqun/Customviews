package custom.gq.com.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
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
    private Point mPoint;

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

    @Override
    protected void onDraw(Canvas canvas) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);




    }

    private void init() {
        autoBackPoint = new Point();
        mPoint = new Point();
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {

            /**
             * only capture the content view
             * @param child
             * @param pointerId
             * @return
             */
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == contentView;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                final int topBound = getPaddingTop();
                final int bottomBound = getHeight()/3;
                int newTop = Math.min(Math.max(top, topBound), bottomBound);
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
           /* @Override
            public int getViewHorizontalDragRange(View child)
            {
                return getMeasuredWidth()-child.getMeasuredWidth();
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return getMeasuredHeight()-child.getMeasuredHeight();
            }*/
            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                mViewDragHelper.captureChildView(contentView, pointerId);
            }

            @Override
            public void onEdgeTouched(int edgeFlags, int pointerId) {
                super.onEdgeTouched(edgeFlags, pointerId);
            }
        });
//        mViewDragHelper.setMinVelocity(minVel);
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_TOP);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setDither(true);//递色
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
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
    protected void onFinishInflate() {
        super.onFinishInflate();
        backGroundView = getChildAt(0);
        contentView = getChildAt(1);
    }

}
