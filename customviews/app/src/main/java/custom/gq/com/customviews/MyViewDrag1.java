package custom.gq.com.customviews;

import android.content.Context;
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
    private Point autoBackPoint = new Point();

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

    private void init() {
        final float density = getResources().getDisplayMetrics().density;
        final float minVel = 400 * density;

        mViewDragHelper = ViewDragHelper.create(this, 0.01f, new ViewDragHelper.Callback() {

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
//                final int newTop = Math.min(getHeight()/2,Math.max(top,child.getHeight()/2));
                return top;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (releasedChild==contentView){
                    mViewDragHelper.settleCapturedViewAt(autoBackPoint.x,autoBackPoint.y);
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
                mViewDragHelper.captureChildView(contentView,pointerId);
            }

            @Override
            public void onEdgeTouched(int edgeFlags, int pointerId) {
                super.onEdgeTouched(edgeFlags, pointerId);
            }
        });

        mViewDragHelper.setMinVelocity(minVel);
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_TOP|ViewDragHelper.EDGE_BOTTOM);


    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mViewDragHelper.continueSettling(true))invalidate();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mViewDragHelper.shouldInterceptTouchEvent(ev))return true;
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
        autoBackPoint.y= contentView.getTop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        backGroundView = getChildAt(0);
        contentView = getChildAt(1);
    }
}
