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

    private static final String mTAG = "MyBlowLayout";

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
        DRAGUP, DRAGdOWN, NORMAL
    }

    private TYPE mTYPE = TYPE.NORMAL;

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
        return super.onInterceptTouchEvent(ev);
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
                if (offsetY > 0) {
                    Log.d(mTAG, "pull up");
                    mTYPE = TYPE.DRAGUP;
                    float damp = 1 - offsetY / getMeasuredHeight();
                    offsetY = offsetY * damp;
                    endY = mScrollerCompat.getFinalY() - (int) offsetY;
                    Log.d("endY",""+endY);
                    if (offsetY != 0) {
                        scrollTo(0, -(int) endY);
                    }
                } else {
                    Log.d(mTAG, "pull down");
                    mTYPE = TYPE.DRAGdOWN;
                    //设置阻尼效果
                    float damp = 1 + offsetY / getMeasuredHeight();
                    offsetY = offsetY * damp;
                    endY = mScrollerCompat.getFinalY() + (int) offsetY;
                    if (offsetY != 0) {
                        scrollTo(0, (int) endY);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mScrollerCompat.setFinalY((int) endY);
                int finalY = mScrollerCompat.getFinalY();
                switch (mTYPE) {
                    case DRAGUP:
                        if (finalY>StateViewHeight)
                        {
                            mScrollerCompat.startScroll(0,finalY,0,finalY-StateViewHeight,300);
                            mScrollerCompat.setFinalY(StateViewHeight);
                            sHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mScrollerCompat.startScroll(0, StateViewHeight, 0, StateViewHeight, 300);
                                    mPullDownToRefresh.refresh(true);
                                }
                            },300);
                        }else {
                            this.mPullDownToRefresh.refresh(false);
                            mScrollerCompat.startScroll(0, -finalY, 0, finalY, 300);
                        }

                        break;
                    case DRAGdOWN:
                        if (finalY < -StateViewHeight) {
                            mScrollerCompat.startScroll(0, finalY, 0, finalY + StateViewHeight, 300);
                            mScrollerCompat.setFinalY(-StateViewHeight);
                            sHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mScrollerCompat.startScroll(0, -StateViewHeight, 0, StateViewHeight, 300);
                                    mPullDownToRefresh.refresh(true);
                                }
                            }, 500);
                        } else {
                            this.mPullDownToRefresh.refresh(false);
                            mScrollerCompat.startScroll(0, finalY, 0, -finalY, 300);
                        }
                        break;
                    default:
                        break;
                }

                break;
            default:
                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        super.onLayout(changed, left, top, right, bottom);
    }


    public interface PullDownToRefresh {
        void refresh(boolean isSuccess);

        void finish();
    }

}
