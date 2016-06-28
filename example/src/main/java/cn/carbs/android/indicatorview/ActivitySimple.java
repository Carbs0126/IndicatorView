package cn.carbs.android.indicatorview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import cn.carbs.android.indicatorview.library.SimpleIndicatorView;

public class ActivitySimple extends AppCompatActivity {

    private SimpleIndicatorView mSimpleIndicatorViewH;
    private SimpleIndicatorView mSimpleIndicatorViewV;
    private Button mButton;
    
    private int mIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        mSimpleIndicatorViewH = (SimpleIndicatorView)this.findViewById(R.id.simple_indicator_view_h);
        mSimpleIndicatorViewV = (SimpleIndicatorView)this.findViewById(R.id.simple_indicator_view_v);

        mButton = (Button)this.findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIndex = mIndex + 1;
                mSimpleIndicatorViewH.setIndex(mIndex % 3);
                mSimpleIndicatorViewV.setIndex(mIndex % 4);
            }
        });
    }
}
