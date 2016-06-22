# IndicatorView
IndicatorView on Android platform to indicator the current index of pages

## 概述
Android IndicatorView的灵感来源于SlidingTabView，虽然有句“不重复”造轮子在先，本着练手的目的，还是写了一个功能较为简单的类似view。
其比SlidingTabView在功能上欠缺的一点是：暂时没有添加“当内容显示不开时自动向左右滑动”的功能。

## 应用场景
可以胜任一屏显示所有tab标题的情况。

## 效果图

![Example Image][1]

所有效果如上图所示。

![Example Image][2]

左侧图片为所有效果的截图包括IndicatorView和SimpleIndicatorView，右侧图片为SimpleIndicatorView单独示例。

## 功能
1. 均等Indicator显示；
2. 不均等Indicator显示，根据tab标题的字宽度自动适配；
3. Indicator可以添加indicator两端相对多增加的长度；
4. 可以设置颜色渐变，一般可用来做tab切换，增加美感；
5. 文字也可以随Indicator的颜色变化而变化，具有渐变效果；
6. SimpleIndicatorView可以设置indicator的宽度相对每个等长单元的宽度比值；
7. SimpleIndicatorView可以设置滑动方向，即水平方向或者竖直方向，同时可以设置底部线条的位置；
7. 可与ViewPager联动；
8. 可首尾循环；
9. 支持padding，支持自定义点击效果等；
10. 可一同设置文字；
11. java代码控制非常简单，如果和ViewPager联动，那么只需要一行代码。

## 加入项目
```
  compile 'cn.carbs.android:IndicatorView:1.0.0'
```

## 使用方法
### xml布局文件中声明：
SimpleIndicatorView和IndicatorView采用了两种不同的属性，原因是两者差距稍大，索性采用两套属性。由于属性较多，因此xml中的声明稍微复杂。之所以写了两个view，是因为竖直方向显示时，一般没有文字描述，另外SimpleIndicatorView较为轻量化，便于降低耦合。

1.对于IndicatorView，xml中这样声明：
```
  <cn.carbs.android.library.indicatorview.IndicatorView
        android:id="@+id/indicator_view"
        android:layout_width="match_parent"
        android:layout_height="60dp"
        android:layout_marginTop="20dp"
        android:background="#ffeeeeee"
        android:paddingLeft="2dp"
        android:paddingRight="2dp"
        app:iv_IndicatorColorEnd="#ffee5544"
        app:iv_IndicatorColorGradient="true"
        app:iv_IndicatorColorStart="#ff3388ff"
        app:iv_IndicatorEven="false"
        app:iv_IndicatorLengthExtra="3dp"
        app:iv_IndicatorTextArray="@array/text_arrays_2"
        app:iv_IndicatorTextGap="20dp"
        app:iv_IndicatorTextSize="20dp"/>
```
2.对于SimpleIndicatorView，xml中这样声明：
```
<cn.carbs.android.library.indicatorview.SimpleIndicatorView
        android:id="@+id/simple_indicator_view_v"
        android:layout_width="4dp"
        android:layout_height="match_parent"
        android:layout_alignParentLeft="true"
        app:iv_SimpleIndicatorColorEnd="#ffee5544"
        app:iv_SimpleIndicatorColorGradient="true"
        app:iv_SimpleIndicatorColorStart="#ff3388ff"
        app:iv_SimpleIndicatorCount="4"
        app:iv_SimpleIndicatorLinePosition="start"
        app:iv_SimpleIndicatorOrientation="vertical"
        app:iv_SimpleIndicatorWidthRation="0.8"/>
```
各个属性作用，见说明末尾。

### java文件中的控制：

如果想和ViewPager联动，那么Java代码只有一句：
```
  mIndicatorView.setViewPager(mViewpager);
```
如果想主动改变其显示的索引值，可通过多种方式：
```
  //使IndicatorView当前指示的索引值加1
  mIndicatorView.increaseSelectedIndexWithViewPager();
  
  //使IndicatorView当前指示的索引值减1
  mIndicatorView.decreaseSelectedIndexWithViewPager();
  
  //使IndicatorView指向任意的索引值
  mIndicatorView.setIndexWithViewPager(int indexDest);
```

IndicatorView状态改变后的回调函数：
```
  mIndicatorView.setOnIndicatorChangedListener(new IndicatorView.OnIndicatorChangedListener() {
            @Override
            public void onIndicatorChanged(int oldSelectedIndex, int newSelectedIndex) {
                //如果已经和ViewPager关联了，那么不要在此处改变ViewPager的状态
            }
        });
```

## 注意事项
1. IndicatorLengthExtra属性请暂时使用大于等于0的dimension值，因为我暂时没有对负值做限制，如果负值过小，可能显示会有问题;
2. 不支持在Java代码中生成此view;
3. IndicatorView只能水平方向显示，而SimpleIndicatorView可以竖直方向显示。
4. IndicatorView能与ViewPager联动，而SimpleIndicatorView不能。

## 属性说明
### IndicatorView的相关属性
```
<declare-styleable name="IndicatorView">
        <attr name="iv_IndicatorEven" format="reference|boolean" /> //每个单元是否是等宽的
        <attr name="iv_IndicatorColor" format="reference|color" />  //indicator的颜色
        <attr name="iv_IndicatorColorStart" format="reference|color" /> //indicator的起始颜色，indicator的颜色随着滑动而进行改变，iv_IndicatorColorGradient属性开启后有效
        <attr name="iv_IndicatorColorEnd" format="reference|color" />   //indicator的终止颜色，iv_IndicatorColorGradient属性开启后有效
        <attr name="iv_IndicatorColorGradient" format="reference|boolean" />//是否允许indicator随着滑动而改变颜色
        <attr name="iv_IndicatorTextGap" format="reference|dimension" />  //每个text之间的间距，在单元非等宽的条件下有效(iv_IndicatorEven="false")
        <attr name="iv_IndicatorTextArray" format="reference" />//显示的tab标题
        <attr name="iv_IndicatorTextSize" format="reference|dimension" />//显示的tab标题的文字大小
        <attr name="iv_IndicatorTextColorNormal" format="reference|color" />//显示的tab标题的非选中状态文字颜色(iv_IndicatorColorGradient="false"条件下有效)
        <attr name="iv_IndicatorTextColorSelected" format="reference|color" />//显示的tab标题的选中状态文字颜色(iv_IndicatorColorGradient="false"条件下有效)
        <attr name="iv_IndicatorLengthExtra" format="reference|dimension" />//indicator长于每个tab标题的长度的一半
        <attr name="iv_IndicatorDuration" format="reference|integer" />//indicator切换时的动画持续时间
        <attr name="iv_IndicatorSelectedIndex" format="reference|integer" />//默认的选中索引
        <attr name="iv_IndicatorHeight" format="reference|dimension" />//indicator的高度
        <attr name="iv_IndicatorBgTouchedColor" format="reference|color" />//按下某个tab标题时的背景颜色
        <attr name="iv_IndicatorViewPagerAnim" format="reference|boolean" />//按下tab后，与之联动的ViewPager是否需要有动画效果，默认true
    </declare-styleable>
    
```

### SimpleIndicatorView的相关属性
```
    <declare-styleable name="SimpleIndicatorView">
        <attr name="iv_SimpleIndicatorColor" format="reference|color" />//indicator的颜色
        <attr name="iv_SimpleIndicatorColorStart" format="reference|color" />//indicator的起始颜色，indicator的颜色随着滑动而进行改变，iv_IndicatorColorGradient属性开启后有效
        <attr name="iv_SimpleIndicatorColorEnd" format="reference|color" />//indicator的终止颜色，iv_IndicatorColorGradient属性开启后有效
        <attr name="iv_SimpleIndicatorColorGradient" format="reference|boolean" />//是否允许indicator随着滑动而改变颜色
        <attr name="iv_SimpleIndicatorCount" format="reference|integer" />//一共包含的单元数目
        <attr name="iv_SimpleIndicatorWidthRation" format="reference|float" />//indicator宽度与均分的单元的宽度比值
        <attr name="iv_SimpleIndicatorDuration" format="reference|integer" />//indicator切换时的动画持续时间
        <attr name="iv_SimpleIndicatorDefaultIndex" format="reference|integer" />//默认的选中索引
        <attr name="iv_SimpleIndicatorLineColor" format="reference|color" />//底部的线条的颜色
        <attr name="iv_SimpleIndicatorLineShow" format="reference|boolean" />//底部线条是否显示
        <attr name="iv_SimpleIndicatorLineThickness" format="reference|dimension" />//底部线条的高度
        <attr name="iv_SimpleIndicatorOrientation" format="enum">//indicator水平显示还是竖直显示
            <enum name="horizontal" value="0" />
            <enum name="vertical" value="1" />
        </attr>
        <attr name="iv_SimpleIndicatorLinePosition" format="enum">//底部线条的位置：水平方向，可以在顶部，可以在底部。竖直方向，可以在左侧，可以在右侧。
            <enum name="start" value="0" />
            <enum name="end" value="1" />
        </attr>
    </declare-styleable>
```

## License

    Copyright 2016 Carbs.Wang (IndicatorView)

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


 [1]: https://github.com/Carbs0126/Screenshot/blob/master/indicator_view.gif
 [2]: https://github.com/Carbs0126/Screenshot/blob/master/indicator_view_uneven.png
 [3]: https://github.com/Carbs0126/Screenshot/blob/master/indicator_view_sample.jpg
