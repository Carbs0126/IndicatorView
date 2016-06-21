package cn.carbs.android.library.indicatorview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import java.lang.reflect.Field;

import cn.carbs.android.library.R;

/**
 * Created by Carbs.Wang on 2016/6/21.
 */
public class IndicatorView extends View {

    private static final String TAG = "IndicatorView";

    private static final int DEFAULT_INDICATOR_COLOR = 0xff3388ff;
    private static final int DEFAULT_INDICATOR_DURATION = 200;
    private static final int DEFAULT_INDICATOR_SELECTED_INDEX = 0;
    private static final int DEFAULT_INDICATOR_TEXT_COLOR_NORMAL = 0x55555555;
    private static final int DEFAULT_INDICATOR_TEXT_COLOR_SELECTED = DEFAULT_INDICATOR_COLOR;
    private static final int DEFAULT_INDICATOR_TEXT_SIZE_SP = 14;
    private static final int DEFAULT_INDICATOR_HEIGHT_DP = 3;
    private static final int DEFAULT_INDICATOR_GAP_DP = 24;
    private static final int DEFAULT_INDICATOR_EXTRA_DP = 4;
    private static final int DEFAULT_INDICATOR_BG_TOUCHED_COLOR = 0x22666666;
    private static final boolean DEFAULT_INDICATOR_COLOR_GRADIENT = false;
    private static final boolean DEFAULT_EVEN = false;
    private static final boolean DEFAULT_VIEW_PAGER_ANIM = true;

    private int mIndicatorColor = DEFAULT_INDICATOR_COLOR;
    private int mIndicatorColorStart = DEFAULT_INDICATOR_COLOR;
    private int mIndicatorColorEnd = DEFAULT_INDICATOR_COLOR;
    private int mIndicatorDuration = DEFAULT_INDICATOR_DURATION;
    private int mIndicatorSelectedIndex = DEFAULT_INDICATOR_SELECTED_INDEX;
    private int mTextColorNormal = DEFAULT_INDICATOR_TEXT_COLOR_NORMAL;
    private int mTextColorSelected = DEFAULT_INDICATOR_TEXT_COLOR_SELECTED;
    private int mBgTouchedColor = DEFAULT_INDICATOR_BG_TOUCHED_COLOR;
    private boolean mEven = DEFAULT_EVEN;//default value is uneven mode
    private boolean mIndicatorColorGradient = DEFAULT_INDICATOR_COLOR_GRADIENT;
    private boolean mViewPagerAnim = DEFAULT_VIEW_PAGER_ANIM;

    private int mIndicatorTextSize;
    private float mIndicatorSelectedIndexOffsetRation;
    private float textCenterYOffset;

    private CharSequence[] mIndicatorTextArray;
    private int[] mIndicatorTextArrayWidths;
    private PointF[] mIndicatorTextArrayCenterPoints;

    private Paint mPaintIndicator = new Paint();
    private Paint mPaintText = new Paint();
    private Paint mPaintTouch = new Paint();

    private RectF mRectFIndicator = new RectF();
    private RectF mRectFTouchEffect = new RectF();

    private PointF mDestP = new PointF();
    private PointF mOrigP = new PointF();
    private PointF mCurrP = new PointF();

    private ValueGeneratorAnim mAnim = new ValueGeneratorAnim();

    private ViewPager mViewPager;

    private int mIndicatorGap;//px
    private int mIndicatorExtra;//px
    private int mIndicatorHeight;//px

    private int mWidth = 0;
    private int mHeight = 0;
    private int mNetWidth = 0;

    public IndicatorView(Context context) {
        super(context);
        init();
    }
    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        init();
    }
    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        init();
    }

    private void initAttr(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IndicatorView);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if(attr == R.styleable.IndicatorView_iv_IndicatorColor){
                mIndicatorColor = a.getColor(attr, DEFAULT_INDICATOR_COLOR);
            }else if(attr == R.styleable.IndicatorView_iv_IndicatorDuration){
                mIndicatorDuration = a.getInt(attr, DEFAULT_INDICATOR_DURATION);
            }else if(attr == R.styleable.IndicatorView_iv_IndicatorSelectedIndex){
                mIndicatorSelectedIndex = a.getInt(attr, DEFAULT_INDICATOR_SELECTED_INDEX);
            }else if(attr == R.styleable.IndicatorView_iv_IndicatorColorStart){
                mIndicatorColorStart = a.getColor(attr, DEFAULT_INDICATOR_COLOR);
            }else if(attr == R.styleable.IndicatorView_iv_IndicatorColorEnd){
                mIndicatorColorEnd = a.getColor(attr, DEFAULT_INDICATOR_COLOR);
            }else if(attr == R.styleable.IndicatorView_iv_IndicatorColorGradient){
                mIndicatorColorGradient = a.getBoolean(attr, DEFAULT_INDICATOR_COLOR_GRADIENT);
            }else if(attr == R.styleable.IndicatorView_iv_IndicatorTextColorNormal){
                mTextColorNormal = a.getColor(attr, DEFAULT_INDICATOR_TEXT_COLOR_NORMAL);
            }else if(attr == R.styleable.IndicatorView_iv_IndicatorTextColorSelected){
                mTextColorSelected = a.getColor(attr, DEFAULT_INDICATOR_TEXT_COLOR_SELECTED);
            }else if(attr == R.styleable.IndicatorView_iv_IndicatorTextSize){
                mIndicatorTextSize = a.getDimensionPixelSize(attr, sp2px(getContext(), DEFAULT_INDICATOR_TEXT_SIZE_SP));
            }else if(attr == R.styleable.IndicatorView_iv_IndicatorTextArray){
                mIndicatorTextArray = a.getTextArray(attr);
            }else if(attr == R.styleable.IndicatorView_iv_IndicatorHeight){
                mIndicatorHeight = a.getDimensionPixelSize(attr, dp2px(getContext(), DEFAULT_INDICATOR_HEIGHT_DP));
            }else if(attr == R.styleable.IndicatorView_iv_IndicatorTextGap){
                mIndicatorGap = a.getDimensionPixelSize(attr, dp2px(getContext(), DEFAULT_INDICATOR_GAP_DP));
            }else if(attr == R.styleable.IndicatorView_iv_IndicatorLengthExtra){
                mIndicatorExtra = a.getDimensionPixelSize(attr, dp2px(getContext(), DEFAULT_INDICATOR_EXTRA_DP));
            }else if(attr == R.styleable.IndicatorView_iv_IndicatorEven){
                mEven = a.getBoolean(attr, DEFAULT_EVEN);
            }else if(attr == R.styleable.IndicatorView_iv_IndicatorBgTouchedColor){
                mBgTouchedColor = a.getColor(attr, DEFAULT_INDICATOR_BG_TOUCHED_COLOR);
            }else if(attr == R.styleable.IndicatorView_iv_IndicatorViewPagerAnim){
                mViewPagerAnim = a.getBoolean(attr, DEFAULT_VIEW_PAGER_ANIM);
            }
        }
        a.recycle();
    }

    private void init(){

        if(mIndicatorTextSize == 0)
            mIndicatorTextSize = sp2px(getContext(), DEFAULT_INDICATOR_TEXT_SIZE_SP);

        if(mIndicatorHeight == 0)
            mIndicatorHeight = dp2px(getContext(), DEFAULT_INDICATOR_HEIGHT_DP);

        if(mIndicatorGap == 0)
            mIndicatorGap = dp2px(getContext(), DEFAULT_INDICATOR_GAP_DP);

        mPaintIndicator.setAntiAlias(true);
        mPaintIndicator.setColor(mIndicatorColor);
        mPaintIndicator.setStyle(Paint.Style.FILL);

        mPaintTouch.setAntiAlias(true);
        mPaintTouch.setColor(mBgTouchedColor);
        mPaintTouch.setStyle(Paint.Style.FILL);

        mPaintText.setAntiAlias(true);
        mPaintText.setTextAlign(Paint.Align.CENTER);
        mPaintText.setTextSize(mIndicatorTextSize);
        textCenterYOffset = getTextCenterYOffset(mPaintText.getFontMetrics());

        this.setClickable(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mNetWidth = mWidth - getPaddingLeft() - getPaddingRight();
        refreshTextArrayWidthsAndCenterPoints();
        refreshCurrPointByIndexAndOffsetRation(mIndicatorSelectedIndex, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!refresh()) return;
        drawTouchedEffect(canvas);
        drawIndicator(canvas);
        drawText(canvas);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        mNetWidth = mWidth - left - right;
    }

    private int mPreTouchedIndex = -1;
    private int mCurTouchedIndex = -1;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mPreTouchedIndex = mCurTouchedIndex;
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mCurTouchedIndex = getTouchedIndex(event.getX(), event.getY());
                if(mPreTouchedIndex != mCurTouchedIndex){
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                mCurTouchedIndex = getTouchedIndex(event.getX(), event.getY());
                if(mPreTouchedIndex != mCurTouchedIndex){
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                mCurTouchedIndex = getTouchedIndex(event.getX(), event.getY());
                if(mCurTouchedIndex != -1){
                    if(mOnIndicatorChangedListener != null && mIndicatorSelectedIndex != mCurTouchedIndex){
                        mOnIndicatorChangedListener.onIndicatorChanged(mIndicatorSelectedIndex, mCurTouchedIndex);
                    }
                }
                if(checkViewPagerOnPageChangeListener(mViewPager)){
                    mViewPager.setCurrentItem(mCurTouchedIndex, mViewPagerAnim);
                }else {
                    setIndex(mCurTouchedIndex);
                }
                invalidate();
                mCurTouchedIndex = -1;
                break;
            case MotionEvent.ACTION_CANCEL:
                mCurTouchedIndex = -1;
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    private void refreshCurrColorByCurrPoint(){
        if(mIndicatorColorGradient && mIndicatorTextArrayCenterPoints != null && mIndicatorTextArrayCenterPoints.length > 1){
            float fraction = (mCurrP.x - mIndicatorTextArrayCenterPoints[0].x) /
                    (mIndicatorTextArrayCenterPoints[mIndicatorTextArrayCenterPoints.length - 1].x - mIndicatorTextArrayCenterPoints[0].x);
            mIndicatorColor = getEvaluateColor(fraction, mIndicatorColorStart, mIndicatorColorEnd);
        }
    }

    private float getTextCenterYOffset(Paint.FontMetrics fontMetrics){
        if(fontMetrics == null) return 0;
        return Math.abs(fontMetrics.top + fontMetrics.bottom)/2;
    }

    private void refreshTextArrayWidthsAndCenterPoints(){
        if(mIndicatorTextArray == null){
            mIndicatorTextArrayWidths = null;
            mIndicatorTextArrayCenterPoints = null;
        }else{
            mIndicatorTextArrayWidths = new int[mIndicatorTextArray.length];
            mIndicatorTextArrayCenterPoints = new PointF[mIndicatorTextArray.length];
            float centerY = (mHeight - mIndicatorHeight) / 2;

            if(mEven){
                for (int i = 0; i < mIndicatorTextArray.length; i++) {
//                    mIndicatorTextArrayWidths[i] = mNetWidth / mIndicatorTextArray.length + 2 * mIndicatorExtra;
//                    mIndicatorTextArrayCenterPoints[i] = new PointF(getPaddingLeft() + mNetWidth * (i + 0.5f) / mIndicatorTextArray.length, centerY);
                    mIndicatorTextArrayWidths[i] = (mNetWidth - 2 * mIndicatorExtra) / mIndicatorTextArray.length;
                    mIndicatorTextArrayCenterPoints[i] = new PointF(getPaddingLeft()
                            + mIndicatorExtra + (mNetWidth - 2 * mIndicatorExtra) * (i + 0.5f) / mIndicatorTextArray.length, centerY);
                }
            }else {
                for (int i = 0; i < mIndicatorTextArray.length; i++) {
                    mIndicatorTextArrayWidths[i] = getTextWidth(mIndicatorTextArray[i].toString(), mPaintText);
                    if (i == 0) {
                        mIndicatorTextArrayCenterPoints[i] =
                                new PointF(getPaddingLeft() + ((float) mIndicatorTextArrayWidths[i]) / 2 + (mIndicatorExtra > 0 ? mIndicatorExtra : 0),
                                        centerY);
                    } else {
                        mIndicatorTextArrayCenterPoints[i] =
                                new PointF(mIndicatorTextArrayCenterPoints[i - 1].x
                                        + ((float) mIndicatorTextArrayWidths[i - 1]) / 2
                                        + mIndicatorGap
                                        + ((float) mIndicatorTextArrayWidths[i]) / 2,
                                        centerY);
                    }
                }
            }
        }
    }

    private void refreshCurrPointByIndexAndOffsetRation(int index, float offsetRation){
        if(mIndicatorTextArray == null) return;
        if(index < 0 || index > mIndicatorTextArray.length - 1){
            throw new IllegalArgumentException("index should be larger than -1 and less than (mIndicatorTextArray.length),"
                    + " now index is " + index );
        }
        if(offsetRation < 0 || offsetRation >= 1){
            throw new IllegalArgumentException("offsetRation should be in [0,1),"
                    + " now offsetRation is " + index);
        }
        if(index != mIndicatorTextArray.length - 1){
            mCurrP.x = mIndicatorTextArrayCenterPoints[index].x
                    + offsetRation * (mIndicatorTextArrayCenterPoints[index + 1].x - mIndicatorTextArrayCenterPoints[index].x);
            mCurrP.y = mIndicatorTextArrayCenterPoints[index].y;
        }else{
            mCurrP.x = mIndicatorTextArrayCenterPoints[index].x;
            mCurrP.y = mIndicatorTextArrayCenterPoints[index].y;
            offsetRation = 0;
        }
        mIndicatorSelectedIndex = index;
        mIndicatorSelectedIndexOffsetRation = offsetRation;
    }

    private boolean refresh(){
        if(mIndicatorTextArray == null || mIndicatorTextArray.length == 0
                || mIndicatorTextArrayCenterPoints == null
                || mIndicatorTextArrayCenterPoints.length == 0) return false;

        refreshCurrColorByCurrPoint();
        refreshSpringIndicatorRectByCurrPoint();
        return true;
    }

    private void drawIndicator(Canvas canvas){
        mPaintIndicator.setColor(mIndicatorColor);
        canvas.drawRect(mRectFIndicator, mPaintIndicator);
    }

    private void drawText(Canvas canvas){
        for(int i = 0; i < mIndicatorTextArray.length; i++){

            if(mIndicatorSelectedIndex == i){
                mPaintText.setColor(getEvaluateColor(mIndicatorSelectedIndexOffsetRation,
                        (mIndicatorColorGradient ? mIndicatorColor : mTextColorSelected), mTextColorNormal));
            }else if(mIndicatorSelectedIndex == i - 1){
                mPaintText.setColor(getEvaluateColor(mIndicatorSelectedIndexOffsetRation, mTextColorNormal,
                        (mIndicatorColorGradient ? mIndicatorColor : mTextColorSelected)));
            }else{
                mPaintText.setColor(mTextColorNormal);
            }

            canvas.drawText(mIndicatorTextArray[i].toString(),
                    mIndicatorTextArrayCenterPoints[i].x,
                    mIndicatorTextArrayCenterPoints[i].y + textCenterYOffset,
                    mPaintText);
        }
    }

    private void drawTouchedEffect(Canvas canvas){
        if(mCurTouchedIndex > -1 && mCurTouchedIndex < mIndicatorTextArrayCenterPoints.length){
            mRectFTouchEffect.left = mIndicatorTextArrayCenterPoints[mCurTouchedIndex].x
                    - (mIndicatorTextArrayWidths[mCurTouchedIndex] / 2 + mIndicatorExtra);
            mRectFTouchEffect.right = mIndicatorTextArrayCenterPoints[mCurTouchedIndex].x
                    + (mIndicatorTextArrayWidths[mCurTouchedIndex] / 2 + mIndicatorExtra);
            mRectFTouchEffect.top = 0;
            mRectFTouchEffect.bottom = mHeight;
            canvas.drawRect(mRectFTouchEffect, mPaintTouch);
        }
    }

    private void refreshSpringIndicatorRectByCurrPoint(){
        if(mIndicatorTextArrayCenterPoints == null) return;

        for(int i = 0; i < mIndicatorTextArrayCenterPoints.length - 1; i++){
            if(mIndicatorTextArrayCenterPoints[i].x <= mCurrP.x
                    && mCurrP.x <= mIndicatorTextArrayCenterPoints[i+1].x){

                float offsetRation = (mCurrP.x - mIndicatorTextArrayCenterPoints[i].x)/
                        (mIndicatorTextArrayCenterPoints[i+1].x - mIndicatorTextArrayCenterPoints[i].x);
                float springHalfWidth = mIndicatorExtra + mIndicatorTextArrayWidths[i] / 2
                        + (mIndicatorTextArrayWidths[i+1] - mIndicatorTextArrayWidths[i]) * offsetRation / 2;

                mRectFIndicator.left = mCurrP.x - springHalfWidth;
                mRectFIndicator.right = mCurrP.x + springHalfWidth;
                mRectFIndicator.top = mHeight - mIndicatorHeight;
                mRectFIndicator.bottom = mHeight;
                if(offsetRation < 1f){
                    mIndicatorSelectedIndex = i;
                    mIndicatorSelectedIndexOffsetRation = offsetRation;
                }else{
                    mIndicatorSelectedIndex = i + 1;
                    mIndicatorSelectedIndexOffsetRation = 0;
                }
                return;
            }
        }
        Log.d(TAG, "refreshSpringIndicatorRectByCurrPoint() wrong");
    }

    public int getItemCount(){
        if(mIndicatorTextArray == null) return 0;
        return mIndicatorTextArray.length;
    }


    public void increaseSelectedIndex(){
        Object[] currState = getCurrIndexAndOffset();
        if(currState == null){
            throw new IllegalArgumentException("increaseSelectedIndex wrong! currState == null");
        }
        setIndex(((int)currState[0] + 1) % mIndicatorTextArray.length);
    }

    public void increaseSelectedIndexWithViewPager(){
        Object[] currState = getCurrIndexAndOffset();
        if(currState == null){
            throw new IllegalArgumentException("increaseSelectedIndex wrong! currState == null");
        }
        setIndexWithViewPager(((int)currState[0] + 1) % mIndicatorTextArray.length);
    }

    public void decreaseSelectedIndex(){
        Object[] currState = getCurrIndexAndOffset();
        if(currState == null){
            throw new IllegalArgumentException("decreaseSelectedIndex wrong! currState == null");
        }
        setIndex(((int)currState[0] - 1) % mIndicatorTextArray.length);
    }

    public void decreaseSelectedIndexWithViewPager(){
        Object[] currState = getCurrIndexAndOffset();
        if(currState == null){
            throw new IllegalArgumentException("decreaseSelectedIndex wrong! currState == null");
        }
        setIndexWithViewPager(((int)currState[0] - 1) % mIndicatorTextArray.length);
    }

    public void setIndexWithViewPager(int indexDest){
        if(checkViewPagerOnPageChangeListener(mViewPager)){
            mViewPager.setCurrentItem(indexDest, mViewPagerAnim);
        }else {
            setIndex(indexDest);
            invalidate();
        }
    }

    public void setIndex(int indexDest){

        if(mIndicatorTextArrayCenterPoints == null){
            throw new IllegalArgumentException("you should set textarray first");
        }
        if(indexDest < 0 || indexDest > mIndicatorTextArrayCenterPoints.length - 1){
            throw new IllegalArgumentException("indexDest should less than (mIndicatorTextArrayCenterPoints.length - 1)" +
                    " and larger than -1, now indexDest is " + indexDest );
        }

        float destCenterX, destCenterY;

        destCenterX = mIndicatorTextArrayCenterPoints[indexDest].x;
        destCenterY = mIndicatorTextArrayCenterPoints[indexDest].y;

        mAnim.cancel();
        mOrigP.set(mCurrP);
        mDestP.set(destCenterX, destCenterY);
        if(mOrigP.x == mDestP.x && mOrigP.y == mDestP.y){
            return;
        }
        startAnim(mOrigP, mDestP, mCurrP);
    }

    /**
     * setViewPager(viewpager) to response to the change of ViewPager
     * @param viewPager the viewPager you want IndicatorView to respond with
     */
    public void setViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
        if (viewPager != null) {
            viewPager.setOnPageChangeListener(new InternalViewPagerListener());
        }
    }

    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            refreshCurrPointByIndexAndOffsetRation(position, positionOffset);
            IndicatorView.this.invalidate();
        }

        @Override
        public void onPageScrollStateChanged(int state) {}
        @Override
        public void onPageSelected(int position) {}
    }

    private boolean checkViewPagerOnPageChangeListener(ViewPager viewPager){
        if(viewPager == null) return false;
        Field field;
        try {
            field = ViewPager.class.getDeclaredField("mOnPageChangeListener");
            if(field == null) return false;
            field.setAccessible(true);
            Object o = field.get(viewPager);
            if(o != null && o instanceof InternalViewPagerListener){
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public Object[] getCurrIndexAndOffset(){
        if(mIndicatorTextArray == null) return null;

        Object[] ret = new Object[2];
        for(int i = 0; i < mIndicatorTextArray.length - 1; i++){
            if(mIndicatorTextArrayCenterPoints[i].x <= mCurrP.x
                    && mCurrP.x < mIndicatorTextArrayCenterPoints[i+1].x){

                float rationOffset = (mCurrP.x - mIndicatorTextArrayCenterPoints[i].x)/
                        (mIndicatorTextArrayCenterPoints[i+1].x - mIndicatorTextArrayCenterPoints[i].x);
                ret[0] = i;
                ret[1] = rationOffset;
                return ret;
            }else if(mCurrP.x == mIndicatorTextArrayCenterPoints[i+1].x){
                ret[0] = i + 1;
                ret[1] = 0f;
                return ret;
            }
        }
        Log.d(TAG, "getCurrIndexAndOffset() wrong");
        return null;
    }

    private void startAnim(final PointF origP, final PointF destP, final PointF currP){
        mAnim.reset();
        mAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnim.setDuration(mIndicatorDuration);
        InterpolatedTimeCallback callback = new InterpolatedTimeCallback(){
            public void onTimeUpdate(float interpolatedTime){
                getCurrPoint(interpolatedTime, origP, destP, currP);
                invalidate();
            }
        };
        mAnim.setCallback(callback);
        this.startAnimation(mAnim);
    }

    private void getCurrPoint(float fraction, PointF origP, PointF destP, PointF currP){
        currP.x = origP.x + (destP.x - origP.x) * fraction;
        currP.y = origP.y + (destP.y - origP.y) * fraction;
    }

    class ValueGeneratorAnim extends Animation {

        private InterpolatedTimeCallback interpolatedTimeCallback;

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            if(interpolatedTimeCallback != null){
                this.interpolatedTimeCallback.onTimeUpdate(interpolatedTime);
            }
        }

        public void setCallback(InterpolatedTimeCallback interpolatedTimeCallback){
            this.interpolatedTimeCallback = interpolatedTimeCallback;
        }
    }

    interface InterpolatedTimeCallback {
        void onTimeUpdate(float interpolatedTime);
    }

    private int getEvaluateColor(float fraction, int startColor, int endColor){

        int a, r, g, b;

        int sA = (startColor & 0xff000000) >>> 24;
        int sR = (startColor & 0x00ff0000) >>> 16;
        int sG = (startColor & 0x0000ff00) >>> 8;
        int sB = (startColor & 0x000000ff) >>> 0;

        int eA = (endColor & 0xff000000) >>> 24;
        int eR = (endColor & 0x00ff0000) >>> 16;
        int eG = (endColor & 0x0000ff00) >>> 8;
        int eB = (endColor & 0x000000ff) >>> 0;

        a = (int)(sA + (eA - sA) * fraction);
        r = (int)(sR + (eR - sR) * fraction);
        g = (int)(sG + (eG - sG) * fraction);
        b = (int)(sB + (eB - sB) * fraction);

        return a << 24 | r << 16 | g << 8 | b;
    }

    private int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    private int dp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().density;
        return (int) (spValue * fontScale + 0.5f);
    }

    private int getTextWidth(String text, Paint paint){
        if(!TextUtils.isEmpty(text)){
            return (int)(paint.measureText(text) + 0.5f);
        }
        return -1;
    }

    private OnIndicatorChangedListener mOnIndicatorChangedListener;

    public interface OnIndicatorChangedListener{
        void onIndicatorChanged(int oldSelectedIndex, int newSelectedIndex);
    }

    public void setOnIndicatorChangedListener(OnIndicatorChangedListener listener){
        mOnIndicatorChangedListener = listener;
    }

    private int getTouchedIndex(float x, float y){
        if(mIndicatorTextArrayCenterPoints != null) {
            for (int i = 0; i < mIndicatorTextArrayCenterPoints.length; i++) {

                if (mIndicatorTextArrayCenterPoints[i].x - (mIndicatorTextArrayWidths[i] + mIndicatorGap) / 2 <= x
                        && x < mIndicatorTextArrayCenterPoints[i].x + (mIndicatorTextArrayWidths[i] + mIndicatorGap) / 2) {
                    return i;
                }
            }
        }
        return -1;
    }
}