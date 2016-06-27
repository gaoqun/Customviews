package custom.gq.com.customviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by gaoqun on 2016/6/23.
 */
public class MyLinearLayout extends LinearLayout {
    private ViewDragHelper mViewDragHelper;
    private View mDragView;
    private View mAutoBackView;
    private View mEdgeTrackerView;
    private Point mAutoBackOriginPos = new Point();

    public MyLinearLayout(Context context) {
        super(context);
        initViewDragHelper();
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViewDragHelper();

    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViewDragHelper();

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViewDragHelper();

    }

    private void initViewDragHelper() {
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {

            /**
             *是否能滑动  true代表能滑动  边界值 禁止拖拽
             * @param child
             * @param pointerId
             * @return  返回true表示可以表捕获该view
             */
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == mDragView || child == mAutoBackView;
            }


            /**
             * 圈出一块可以滑动的区域
             * @param child
             * @param left
             * @param dx
             * @return
             */
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return top;
            }


            /**
             * 松开手 回弹 滑动越快 回弹越快  开始用Point讲开始的坐标记录下来  因为调用的时候mScroller.startScroll 所以还得结合调用computerscroll方法
             * @param releasedChild
             * @param xvel
             * @param yvel
             */
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (releasedChild == mAutoBackView) {
                    mViewDragHelper.settleCapturedViewAt(mAutoBackOriginPos.x, mAutoBackOriginPos.y);
                    invalidate();
                }
            }

            /**
             * 在边界拖拽是回调
             * @param edgeFlags
             * @param pointerId
             */
            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                mViewDragHelper.captureChildView(mEdgeTrackerView,pointerId);
            }

            /**
             * 解决由于可以点击拦截不能对view进行捕获的问题
             * @param child
             * @return
             */
            @Override
            public int getViewHorizontalDragRange(View child)
            {
                return getMeasuredWidth()-child.getMeasuredWidth();
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return getMeasuredHeight()-child.getMeasuredHeight();
            }
        });
        /**
         * 边界检测
         */
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);

    }


    /**
     * intercept touchEvent
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mViewDragHelper.shouldInterceptTouchEvent(ev)) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * viewdraghelper will dispatch parent event
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mAutoBackOriginPos.x = mAutoBackView.getLeft();
        mAutoBackOriginPos.y = mAutoBackView.getTop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mDragView = getChildAt(0);
        mAutoBackView = getChildAt(1);
        mEdgeTrackerView = getChildAt(2);

    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) invalidate();
    }

}
