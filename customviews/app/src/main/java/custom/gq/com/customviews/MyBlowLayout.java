package custom.gq.com.customviews;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

    private LinearLayout linearLayout;

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

    public void reset() {
        if (linearLayout != null) {
            scrollTo(0, getScreenHeight() / 8);
            invalidate();
            mScrollerCompat.setFinalY(getScreenHeight() / 8);
        }
    }

    private int getDefautHeight() {
        Log.d("****", getScreenHeight() / 8 + "");
        return getScreenHeight() / 8;
    }

    public int getScreenHeight() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
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
        linearLayout = (LinearLayout) getChildAt(0);
        header = linearLayout.findViewById(R.id.header);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getScreenHeight() / 8);
        header.setLayoutParams(layoutParams);
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
//                    float damp = 1 - offsetY / (getScreenHeight()/8);
//                    if (damp>=0){
//                        offsetY = offsetY * damp;
                    endY = mScrollerCompat.getFinalY() - (int) offsetY;
                    Log.d("endY", "" + endY);
                    scrollTo(0, -(int) endY);
//                    }
                } else if (offsetY < 0) {
                    Log.d(mTAG, "pull down");
                    mTYPE = TYPE.DRAGdOWN;
                    //设置阻尼效果
//                    float damp = 1 + offsetY / (getScreenHeight()/8);
//                    if (damp>=0){
//                        offsetY = offsetY * damp;
                    endY = mScrollerCompat.getFinalY() + (int) offsetY;
                    scrollTo(0, (int) endY);
//                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                switch (mTYPE) {
                    case DRAGUP:
                        mScrollerCompat.setFinalY((int) endY);
                        int finalY = mScrollerCompat.getFinalY();
                        this.mPullDownToRefresh.refresh(false);
                        mScrollerCompat.startScroll(0, -finalY, 0, finalY, 300);
                        break;
                    case DRAGdOWN:
                        mScrollerCompat.setFinalY((int) endY);
                        finalY = mScrollerCompat.getFinalY();
                        mScrollerCompat.startScroll(0, finalY, 0, -(finalY - getDefautHeight()), 300);
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
