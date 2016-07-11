package custom.gq.com.customviews;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class Main5Activity extends AppCompatActivity implements MyBlowLayout.PullDownToRefresh{

    /**
     * witch view want to scroll please find it's parent
     */
    View mViewBg;
    ImageView mImageview;
    MyBlowLayout mMyBlowLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        mImageview = (ImageView) findViewById(R.id.imageview);
        mMyBlowLayout = (MyBlowLayout) findViewById(R.id.parent_view);
//        mImageview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("imageview_","*********");
//            }
//        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mMyBlowLayout.refreshReset();

        mMyBlowLayout.setPullDownToRefresh(this);
    }

    @Override
    public void redresh(boolean isSuccess) {
        Toast.makeText(this,"Refresh......"+isSuccess,Toast.LENGTH_SHORT).show();
    }
}
