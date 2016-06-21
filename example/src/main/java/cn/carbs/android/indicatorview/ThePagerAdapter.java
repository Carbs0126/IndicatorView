package cn.carbs.android.indicatorview;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Rick.Wang on 2016/6/21.
 */
public class ThePagerAdapter extends PagerAdapter {

    private ArrayList<TextView> pages = new ArrayList<TextView>();
    private int pageNumber;
    private Context context;
//    private ViewPager.LayoutParams params = new ViewPager.LayoutParams();

    public ThePagerAdapter(Context context, int pageNumber) {
        this.context = context;
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
            page = new TextView(context);
            page.setBackgroundColor(0x99990000);
//            page.setLayoutParams(params);
            page.setGravity(Gravity.CENTER);
            page.setTextColor(0xffffffff);
            pages.set(position, page);
        }
        if (page != null) {
            container.addView(page);

            switch (position) {
                case 0:
                    page.setBackgroundColor(0x99165578);
                    page.setText("ViewPagerItem-0");
                    break;
                case 1:
                    page.setBackgroundColor(0x9919637e);
                    page.setText("ViewPagerItem-1");
                    break;
                case 2:
                    page.setBackgroundColor(0x992e7a92);
                    page.setText("ViewPagerItem-2");
                    break;
                case 3:
                    page.setBackgroundColor(0x993f8c9c);
                    page.setText("ViewPagerItem-3");
                    break;
                case 4:
                    page.setBackgroundColor(0x9959a8af);
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
