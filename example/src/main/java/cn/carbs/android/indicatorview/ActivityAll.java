package cn.carbs.android.indicatorview;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import cn.carbs.android.library.indicatorview.IndicatorView;
import cn.carbs.android.library.indicatorview.SimpleIndicatorView;

public class ActivityAll extends AppCompatActivity {

    private SimpleIndicatorView mSimpleIndicatorViewH;
    private SimpleIndicatorView mSimpleIndicatorViewV;
    private IndicatorView mIndicatorView1;
    private IndicatorView mIndicatorView2;
    private ViewPager mViewpager;
    private Button mButton;

    private int mIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all);
        mSimpleIndicatorViewH = (SimpleIndicatorView)this.findViewById(R.id.simple_indicator_view_h);
        mSimpleIndicatorViewV = (SimpleIndicatorView)this.findViewById(R.id.simple_indicator_view_v);
        mIndicatorView1 = (IndicatorView)this.findViewById(R.id.indicator_view_1);
        mIndicatorView2 = (IndicatorView)this.findViewById(R.id.indicator_view_2);

        mViewpager = (ViewPager)this.findViewById(R.id.view_pager);
        mViewpager.setAdapter(new ThePagerAdapter(this, 5));
        mViewpager.setOffscreenPageLimit(5);

        mIndicatorView2.setViewPager(mViewpager);

        mButton = (Button)this.findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIndex = mIndex + 1;
                mSimpleIndicatorViewH.setIndex(mIndex % 3);
                mSimpleIndicatorViewV.setIndex(mIndex % 4);
//                mIndicatorView1.increaseSelectedIndex();
//                mIndicatorView2.increaseSelectedIndex();
                mIndicatorView1.increaseSelectedIndexWithViewPager();
                mIndicatorView2.increaseSelectedIndexWithViewPager();
            }
        });
    }
}