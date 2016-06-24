package custom.gq.com.customviews;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ObjectAnimator mObjectAnimator;
    private BottomSheetDialog bottomSheetDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rl);
//        CubicCustomView cubicCustomView = new CubicCustomView(this);
//        relativeLayout.addView(cubicCustomView);
        final MyCircleImageView myCircleImageView = (MyCircleImageView)findViewById(R.id.mycircle_image_view);
//        Glide.with(this).load("http://h.hiphotos.baidu.com/image/pic/item/f9dcd100baa1cd11dd1855cebd12c8fcc2ce2db5.jpg")
//                .centerCrop().into(myCircleImageView);

        final ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.mipmap.ic_eg);
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(imageView);

        myCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCircleImageView.setBorderColor(Color.BLACK);
                myCircleImageView.setBorderWidth(20f);
                if (bottomSheetDialog!=null)
                {
                    if (!bottomSheetDialog.isShowing())
                    bottomSheetDialog.show();
                    else bottomSheetDialog.dismiss();
                }
            }
        });

        mObjectAnimator = ObjectAnimator.ofFloat(myCircleImageView,"rotation",0,360).setDuration(5000);
        mObjectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mObjectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float percent = animation.getAnimatedFraction();
                myCircleImageView.setAlpha(percent);
            }
        });
        mObjectAnimator.setRepeatCount(100);
        mObjectAnimator.setInterpolator(new DecelerateInterpolator());
        mObjectAnimator.start();


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE,null);
        intent.putExtra("EXTRA_QUICK_CAPTURE",true);

//        AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(MainActivity.this,R.animator.circle_view_animator);
//        animatorSet.setTarget(myCircleImageView);
//        animatorSet.start();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                bottomSheetDialog.show();
                            }
                        }).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mObjectAnimator.resume();
        }else {
            if (!mObjectAnimator.isRunning()&&!mObjectAnimator.isStarted())mObjectAnimator.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mObjectAnimator.isRunning()) if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mObjectAnimator.pause();
        }
    }

    @Override
    protected void onDestroy() {
        mObjectAnimator.removeAllListeners();
        mObjectAnimator.removeAllUpdateListeners();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
