package cn.carbs.android.indicatorview;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import cn.carbs.android.library.indicatorview.IndicatorView;

public class ActivityNormalEven extends AppCompatActivity {

    private IndicatorView mIndicatorViewEven;
    private ViewPager mViewpager;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_even);
        mIndicatorViewEven = (IndicatorView)this.findViewById(R.id.indicator_view);

        mViewpager = (ViewPager)this.findViewById(R.id.view_pager);
        mViewpager.setAdapter(new ThePagerAdapter(this, 5));
        mViewpager.setOffscreenPageLimit(5);

        mIndicatorViewEven.setViewPager(mViewpager);

        mButton = (Button)this.findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIndicatorViewEven.increaseSelectedIndexWithViewPager();
            }
        });
    }
}
