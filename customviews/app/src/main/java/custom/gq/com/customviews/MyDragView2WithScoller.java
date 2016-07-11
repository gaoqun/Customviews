package custom.gq.com.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by gaoqun on 2016/6/30.
 */
public class MyDragView2WithScoller extends LinearLayout implements View.OnTouchListener {

    private Scroller mScroller;
    private View mContentView;
    private static final String MATG = "MyDragView2WithScoller";

    private float startX = 0;
    private float startY = 0;

    public MyDragView2WithScoller(Context context) {
        super(context);
        init(context);
    }

    public MyDragView2WithScoller(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyDragView2WithScoller(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mScroller = new Scroller(context);//, new BounceInterpolator(), true);
    }

/*    public void smoothScrollTo(int fx, int fy) {
        int dx = fx - mScroller.getFinalX();
        int dy = fy - mScroller.getFinalY();
        smoothScrollBy(dx, dy);

    }

    public void smoothScrollBy(int dx, int dy) {
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy, 2000);
        invalidate();
    }*/

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            postInvalidate();
        }
        super.computeScroll();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentView = getChildAt(0);
        mContentView.setOnTouchListener(this);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        mContentView.scrollTo(-100,-100);
//        Log.d("MATG","onTouchEvent");
//        invalidate();
        return super.onTouchEvent(event);
    }


    /**
     * ontouch 的优先级高于ontouchevent  如果执行了ontouch 就不执行ontouchevent
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        float currentX;
        float currentY;
        float offsetX;
        float offsetY;
        float endX = 0, endY = 0;
        int action = event.getAction();
//        Log.d("MATG_ontouch", event.getX()+"___"+event.getY());
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
//                mScroller.startScroll(-(int) startX, -(int) startY, 0, 0);
            case MotionEvent.ACTION_MOVE:
                currentX = event.getX();
                currentY = event.getY();
                offsetX = startX - currentX;
                offsetY = startY - currentY;

                mScroller.startScroll(-(int)currentX,-(int)currentY,(int)offsetX,(int)offsetY);

                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                endX = event.getX();
                endY = event.getY();


                break;
            default:
                break;
        }
        invalidate();
        return false;
    }
}
