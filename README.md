# IndicatorView
IndicatorView on Android platform to indicator the current index of pages

## 概述
Android IndicatorView的灵感来源于SlidingTabView，虽然有句“不重复”造轮子在先，本着练手的目的，还是写了一个功能较为简单的类似view。
其比SlidingTabView在功能上欠缺的一点是：暂时没有添加“当内容显示不开时自动向左右滑动”的功能。

## 应用场景
可以胜任一屏显示所有tab标题的情况。

## 功能
1. 均等Indicator显示；
2. 不均等Indicator显示，根据tab标题的字宽度自动适配；
3. Indicator可以添加indicator两端相对多增加的长度；
4. 可以设置颜色渐变，一般可用来做tab切换，增加美感；
5. SimpleIndicator可以设置indicator的宽度相对每个等长单元的宽度比值；
6. 可与ViewPager联动；

## 加入项目
```
  compile 'cn.carbs.android:IndicatorView:1.0.0'
```

## 注意事项
IndicatorLengthExtra属性请暂时使用大于等于0的dimension值，因为我暂时没有对负值做限制，如果负值过小，可能显示会有问题
