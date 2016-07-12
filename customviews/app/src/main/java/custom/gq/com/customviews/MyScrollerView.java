package custom.gq.com.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * Created by gaoqun on 2016/7/12.
 */
public class MyScrollerView extends ScrollView {


    private Scroller mScroller;
    private View header;
    private View contentView;
    private LinearLayout parent;
    private View footer;
    private boolean addContent = false;//is add content view
    private State mState = State.NOMAL;
    private LinearLayout.LayoutParams layoutParams;
    private static final String mATG = "MyScrollerView";

    private float startY;
    private float currentY;
    private float endY;
    private float offset;

    public MyScrollerView(Context context) {
        super(context);
        init();
    }

    public MyScrollerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyScrollerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mScroller = new Scroller(getContext(), new AccelerateInterpolator());
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getScreenHeight() / 8);
    }

    public LinearLayout getParentView() {
        return parent;
    }

    public View getContentView() {
        return contentView;
    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
        if (contentView == null) {
            new IllegalArgumentException("view can be null!");
        } else {
            addContent = true;
            this.parent.addView(contentView);
        }
    }

    public void setFooterView(View footer) {
        this.footer = footer;
        if (footer == null) {
            new IllegalArgumentException("footer can be null!");
        } else if (addContent) {
            footer.setLayoutParams(layoutParams);
            this.parent.addView(footer);
        }

    }

    public void resetState() {
        parent.setFocusable(true);
        scrollTo(0, 240);
        invalidate();
        mScroller.setFinalY(240);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
        }
        postInvalidate();
        super.computeScroll();
    }

    @Override
    protected void onFinishInflate() {
        parent = (LinearLayout) getChildAt(0);
        header = parent.getChildAt(0);
        header.setLayoutParams(layoutParams);
        super.onFinishInflate();
    }

    /**
     * there three cases
     * 1,如果是下拉刷新  屏蔽scrollerview
     * 2,如果是上拉加载  屏蔽scrollerview
     * 3，如果是正常状态  启动scrollerview滚动机制
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mState == State.PULL_DOWN) {
            return false;
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                offset = 0;
                currentY = ev.getY();
                offset = startY - currentY;
                if (offset < 0) {
                    mState = State.PULL_DOWN;
                    endY = mScroller.getFinalY() + (int) offset;
                    scrollTo(0, (int) endY);
                } else if (offset >= 0) {
                    mState = State.PULL_UP;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                switch (mState) {
                    case PULL_DOWN:
                        Log.d("STATE", "PULL_DOWN");
                        mScroller.setFinalY((int) endY);
                        int finalY = mScroller.getFinalY();
                        mScroller.startScroll(0, finalY, 0, -finalY, 1000);
                        mScroller.setFinalY(getScreenHeight() / 8);
                        break;
                    case PULL_UP:

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

    public int getScreenHeight() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

}
