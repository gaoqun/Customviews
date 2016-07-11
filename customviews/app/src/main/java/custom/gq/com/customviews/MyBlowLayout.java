package custom.gq.com.customviews;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by gaoqun on 2016/7/11.
 */
public class MyBlowLayout extends FrameLayout {

    private Scroller mScrollerCompat;
    //开始滑动的x坐标
//    private float startX;
    //开始滑动的y坐标
    private float startY;
    //当前滑动的x
//    private float currentX;
    //当前滑动的y
    private float currentY;
    //结束滑动的x
//    private float endX;
    //结束滑动y
    private float endY;

    //位移
//    private float offsetX;
    private float offsetY;

    //是否滑动完毕
    private boolean isScrollOver;

    //是否正在滑动
    private boolean isScrolling;

    //状态
    enum State {
        OPENNING, HANDLING, CLOSING;
    }

    //上拉/下拉
    enum TYPE {
        DRAGUP, DRAGdOWN
    }

    private View header;
    private View content;
    private int headerHeight;

    //状态高度
    private static final int StateViewHeight = 288;

    private static Handler sHandler = new Handler(Looper.getMainLooper());

    private PullDownToRefresh mPullDownToRefresh;

    public void setPullDownToRefresh(PullDownToRefresh pullDownToRefresh) {
        mPullDownToRefresh = pullDownToRefresh;
    }

    public MyBlowLayout(Context context) {
        super(context);
        init();
    }

    public MyBlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyBlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mScrollerCompat = new Scroller(getContext(), new AccelerateInterpolator());

    }

    @Override
    public void computeScroll() {
        if (mScrollerCompat.computeScrollOffset()) {
            scrollTo(mScrollerCompat.getCurrX(), mScrollerCompat.getCurrY());
        }
        postInvalidate();
        super.computeScroll();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LinearLayout linearLayout = (LinearLayout) getChildAt(0);
        header = linearLayout.findViewById(R.id.header);
        content = linearLayout.findViewById(R.id.imageview);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mScrollerCompat.isFinished()) {
            mScrollerCompat.forceFinished(true);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                currentY = event.getY();
                offsetY = startY - currentY;
                //设置阻尼效果
                float damp = 1 + offsetY / getMeasuredHeight();
                offsetY = -offsetY * damp;
                endY = mScrollerCompat.getFinalY() - (int) offsetY;
                if (offsetY != 0) {
                    scrollTo(0, (int) endY);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mScrollerCompat.setFinalY((int) endY);
                int finalY = mScrollerCompat.getFinalY();
                if (finalY < -StateViewHeight) {
                    mScrollerCompat.startScroll(0, finalY, 0, finalY + StateViewHeight, 300);
                    mScrollerCompat.setFinalY(-StateViewHeight);
                    sHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mScrollerCompat.startScroll(0, -StateViewHeight, 0, StateViewHeight, 300);
                            mPullDownToRefresh.redresh(true);
                        }
                    }, 500);
                } else {
                    this.mPullDownToRefresh.redresh(false);
                    mScrollerCompat.startScroll(0, finalY, 0, -finalY, 300);
                }
                break;
            default:
                break;
        }
        invalidate();
        return true;
    }

    public void refreshReset(){
        if (header != null) {
            headerHeight = content.getHeight();
            Log.d("headerHeight",headerHeight+"");
            scrollTo(0,headerHeight);
            invalidate();
        } else {
            new IllegalAccessException("header is null");
        }
    }

    public interface PullDownToRefresh{
        void redresh(boolean isSuccess);

        void finish();
    }

}
