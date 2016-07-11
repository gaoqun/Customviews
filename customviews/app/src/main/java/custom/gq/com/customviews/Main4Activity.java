package custom.gq.com.customviews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class Main4Activity extends AppCompatActivity {

    LinearLayout mAdasd;
    MyDragView2WithScoller mAsdasdasd;
    ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        mAdasd = (LinearLayout) findViewById(R.id.adasd);
        mAsdasdasd = (MyDragView2WithScoller) findViewById(R.id.asdasdasd);
        mAsdasdasd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onclick_parent","******************");
            }
        });
        mAdasd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mAsdasdasd.smoothScrollBy(-300, 600);
                Log.d("onclick_child","******************");
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
