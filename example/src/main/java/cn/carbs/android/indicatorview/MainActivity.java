package cn.carbs.android.indicatorview;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import cn.carbs.android.library.indicatorview.IndicatorView;
import cn.carbs.android.library.indicatorview.SimpleIndicatorView;

public class MainActivity extends AppCompatActivity {

    private SimpleIndicatorView mSimpleIndicatorViewH;
    private SimpleIndicatorView mSimpleIndicatorViewV;
    private IndicatorView mIndicatorView1;
    private IndicatorView mIndicatorView2;
    private Button mButton;
    private ViewPager viewpager;
    private int mIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSimpleIndicatorViewH = (SimpleIndicatorView)this.findViewById(R.id.simple_indicator_view_h);
        mSimpleIndicatorViewV = (SimpleIndicatorView)this.findViewById(R.id.simple_indicator_view_v);
        mIndicatorView1 = (IndicatorView)this.findViewById(R.id.indicator_view_1);
        mIndicatorView2 = (IndicatorView)this.findViewById(R.id.indicator_view_2);

        viewpager = (ViewPager)this.findViewById(R.id.vp);
        viewpager.setAdapter(new ThePagerAdapter(5));
        viewpager.setOffscreenPageLimit(5);

        mIndicatorView2.setViewPager(viewpager);

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

    private class ThePagerAdapter extends PagerAdapter {

        private ArrayList<TextView> pages = new ArrayList<TextView>();
        private int pageNumber;
        private ViewPager.LayoutParams params = new ViewPager.LayoutParams();

        public ThePagerAdapter(int pageNumber) {
            this.pageNumber = pageNumber;
            for(int i = 0; i < pageNumber; i++){
                pages.add(null);
            }
        }

        @Override
        public int getCount() {
            return pageNumber;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TextView page = pages.get(position);
            if(page == null){
                page = new TextView(MainActivity.this);
                page.setBackgroundColor(0x99990000);
                page.setLayoutParams(params);
                page.setGravity(Gravity.CENTER);
                page.setTextColor(0xffffffff);
                pages.set(position, page);
            }
            if (page != null) {
                container.addView(page);

                switch (position) {
                    case 0:
                        page.setBackgroundColor(0x99014984);
                        page.setText("ViewPagerItem-0");
                        break;
                    case 1:
                        page.setBackgroundColor(0x990a5ea0);
                        page.setText("ViewPagerItem-1");
                        break;
                    case 2:
                        page.setBackgroundColor(0x991775b7);
                        page.setText("ViewPagerItem-2");
                        break;
                    case 3:
                        page.setBackgroundColor(0x992a99cf);
                        page.setText("ViewPagerItem-3");
                        break;
                    case 4:
                        page.setBackgroundColor(0x990a93a6);
                        page.setText("ViewPagerItem-4");
                        break;
                }
            }
            return page;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }
    }
}
