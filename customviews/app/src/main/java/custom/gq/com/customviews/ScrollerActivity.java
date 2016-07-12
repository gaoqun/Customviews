package custom.gq.com.customviews;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScrollerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_myscoller);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2000);
        TextView button = new TextView(this);
        button.setLayoutParams(layoutParams);
        button.setText("content");
        button.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        final MyScrollerView myScrollerView = (MyScrollerView) findViewById(R.id.parent_view);
        myScrollerView.setContentView(button);

        View footer = LayoutInflater.from(this).inflate(R.layout.layout_header,myScrollerView.getParentView(),false);
        TextView footerText = (TextView)footer.findViewById(R.id.header);
        footerText.setText("Footer");
        myScrollerView.setFooterView(footer);
        myScrollerView.resetState();
     }

}
