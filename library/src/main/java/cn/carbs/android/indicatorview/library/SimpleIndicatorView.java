package cn.carbs.android.indicatorview.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;


/**
 * Created by Carbs.Wang on 2016/6/21.
 */
public class SimpleIndicatorView extends View {

    private static final int DEFAULT_INDICATOR_COLOR = 0xff3388ff;
    private static final int DEFAULT_INDICATOR_LINE_COLOR = 0xffaaaaaa;
    private static final int DEFAULT_INDICATOR_COUNT = 2;
    private static final int DEFAULT_INDICATOR_DURATION = 200;
    private static final int DEFAULT_INDICATOR_INDEX = 0;
    private static final float DEFAULT_INDICATOR_WIDTH_RATION = 1f;
    private static final boolean DEFAULT_INDICATOR_LINE_SHOW = true;
    private static final int DEFAULT_INDICATOR_LINE_THICKNESS_PX = 1;
    private static final boolean DEFAULT_INDICATOR_COLOR_GRADIENT = false;

    private static final int ORIENTATION_H = 0;
    private static final int ORIENTATION_V = 1;

    private static final int LINE_SHOW_POSITION_START = 0;
    private static final int LINE_SHOW_POSITION_END = 1;

    private int mIndicatorColor = DEFAULT_INDICATOR_COLOR;
    private int mIndicatorColorStart = DEFAULT_INDICATOR_COLOR;
    private int mIndicatorColorEnd = DEFAULT_INDICATOR_COLOR;
    private boolean mIndicatorColorGradient = DEFAULT_INDICATOR_COLOR_GRADIENT;
    private int mIndicatorCount = DEFAULT_INDICATOR_COUNT;
    private int mIndicatorOrientation = ORIENTATION_H;
    private int mIndicatorDuration = DEFAULT_INDICATOR_DURATION;
    private int mIndicatorIndex = DEFAULT_INDICATOR_INDEX;
    private float mIndicatorWidthRation = DEFAULT_INDICATOR_WIDTH_RATION;
    private boolean mIndicatorLineShow = DEFAULT_INDICATOR_LINE_SHOW;
    private int mIndicatorLineThickness = DEFAULT_INDICATOR_LINE_THICKNESS_PX;
    private int mIndicatorLineShowPosition = LINE_SHOW_POSITION_END;
    private int mIndicatorLineColor = DEFAULT_INDICATOR_LINE_COLOR;

    private Paint mPaintIndicator = new Paint();
    private Paint mPaintLine = new Paint();
    private ValueGeneratorAnim mAnim = new ValueGeneratorAnim();

    public SimpleIndicatorView(Context context) {
        super(context);
        init();
    }
    public SimpleIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        init();
    }
    public SimpleIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        init();
    }

    private void initAttr(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SimpleIndicatorView);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if(attr == R.styleable.SimpleIndicatorView_iv_SimpleIndicatorColor){
                mIndicatorColor = a.getColor(attr, DEFAULT_INDICATOR_COLOR);
            }else if(attr == R.styleable.SimpleIndicatorView_iv_SimpleIndicatorCount){
                mIndicatorCount = a.getInt(attr, DEFAULT_INDICATOR_COUNT);
            }else if(attr == R.styleable.SimpleIndicatorView_iv_SimpleIndicatorWidthRation){
                mIndicatorWidthRation = a.getFloat(attr, DEFAULT_INDICATOR_WIDTH_RATION);
            }else if(attr == R.styleable.SimpleIndicatorView_iv_SimpleIndicatorOrientation){
                mIndicatorOrientation = a.getInt(attr, ORIENTATION_H);
            }else if(attr == R.styleable.SimpleIndicatorView_iv_SimpleIndicatorDuration){
                mIndicatorDuration = a.getInt(attr, DEFAULT_INDICATOR_DURATION);
            }else if(attr == R.styleable.SimpleIndicatorView_iv_SimpleIndicatorDefaultIndex){
                mIndicatorIndex = a.getInt(attr, DEFAULT_INDICATOR_INDEX);
            }else if(attr == R.styleable.SimpleIndicatorView_iv_SimpleIndicatorLineShow){
                mIndicatorLineShow = a.getBoolean(attr, DEFAULT_INDICATOR_LINE_SHOW);
            }else if(attr == R.styleable.SimpleIndicatorView_iv_SimpleIndicatorLinePosition){
                mIndicatorLineShowPosition = a.getInt(attr, LINE_SHOW_POSITION_END);
            }else if(attr == R.styleable.SimpleIndicatorView_iv_SimpleIndicatorLineThickness){
                mIndicatorLineThickness = a.getDimensionPixelSize(attr, DEFAULT_INDICATOR_LINE_THICKNESS_PX);
            }else if(attr == R.styleable.SimpleIndicatorView_iv_SimpleIndicatorLineColor){
                mIndicatorLineColor = a.getColor(attr, DEFAULT_INDICATOR_LINE_COLOR);
            }else if(attr == R.styleable.SimpleIndicatorView_iv_SimpleIndicatorColorStart){
                mIndicatorColorStart = a.getColor(attr, DEFAULT_INDICATOR_LINE_COLOR);
            }else if(attr == R.styleable.SimpleIndicatorView_iv_SimpleIndicatorColorEnd){
                mIndicatorColorEnd = a.getColor(attr, DEFAULT_INDICATOR_LINE_COLOR);
            }else if(attr == R.styleable.SimpleIndicatorView_iv_SimpleIndicatorColorGradient){
                mIndicatorColorGradient = a.getBoolean(attr, DEFAULT_INDICATOR_COLOR_GRADIENT);
            }
        }
        a.recycle();
    }

    public void setIndicatorColor(int color){
        mIndicatorColorGradient = false;
        mIndicatorColor = color;
        mPaintIndicator.setColor(mIndicatorColor);
        invalidate();
    }

    public void setIndicatorColor(int colorStart, int colorEnd){
        mIndicatorColorGradient = true;
        mIndicatorColorStart = colorStart;
        mIndicatorColorEnd = colorEnd;
        refreshCurrColor();
        mPaintIndicator.setColor(mIndicatorColor);
        invalidate();
    }

    private int refreshCurrColor(){
        if(mIndicatorColorGradient){
            float fraction;
            if(mIndicatorOrientation == ORIENTATION_H){
                fraction = mRectFIndicator.left / (mWidth - mRectFIndicator.width());
            }else{
                fraction = mRectFIndicator.top / (mHeight - mRectFIndicator.height());
            }
            mIndicatorColor = getEvaluateColor(fraction, mIndicatorColorStart, mIndicatorColorEnd);
        }
        return mIndicatorColor;
    }

    private void init(){
        if(mIndicatorWidthRation > 1 || mIndicatorWidthRation == 0)
            mIndicatorWidthRation = 1;

        mPaintIndicator.setAntiAlias(true);
        mPaintIndicator.setColor(mIndicatorColor);
        mPaintIndicator.setStyle(Paint.Style.FILL);
        mPaintLine.setAntiAlias(true);
        mPaintLine.setColor(mIndicatorLineColor);
        mPaintLine.setStyle(Paint.Style.FILL);
    }

    private float getTextCenterYOffset(Paint.FontMetrics fontMetrics){
        if(fontMetrics == null) return 0;
        return Math.abs(fontMetrics.top + fontMetrics.bottom)/2;
    }

    private float mWidth = 0;
    private float mHeight = 0;
    private float mItemWidth = 0;
    private float mItemHeight = 0;

    private RectF mRectFFrame = new RectF();
    private RectF mRectFIndicator = new RectF();
    private RectF mRectFLine = new RectF();

    private PointF mDestP = new PointF();
    private PointF mOrigP = new PointF();
    private PointF mCurrP = new PointF();

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mRectFFrame.left = 0;
        mRectFFrame.top = 0;
        mRectFFrame.right = w;
        mRectFFrame.bottom = h;
        if(mIndicatorOrientation == ORIENTATION_H){
            mItemWidth = mWidth * mIndicatorWidthRation / mIndicatorCount;
            float paddingH = mWidth * (1 - mIndicatorWidthRation) / (2 * mIndicatorCount);
            mRectFIndicator.left = 0;
            mRectFIndicator.top = 0;
            mRectFIndicator.right = mItemWidth;
            mRectFIndicator.bottom = h;
            mCurrP.x = mWidth * (0.5f + mIndicatorIndex) / mIndicatorCount;
            mCurrP.y = mHeight / 2f;
            if(mIndicatorLineShowPosition == LINE_SHOW_POSITION_START){
                mRectFLine.left = paddingH;
                mRectFLine.top = 0;
                mRectFLine.right = mWidth - paddingH;
                mRectFLine.bottom = mIndicatorLineThickness;
            }else{
                mRectFLine.left = paddingH;
                mRectFLine.top = mHeight - mIndicatorLineThickness;
                mRectFLine.right = mWidth - paddingH;
                mRectFLine.bottom = mHeight;
            }
        }else{
            mItemHeight = mHeight * mIndicatorWidthRation / mIndicatorCount;
            float paddingV = mHeight * (1 - mIndicatorWidthRation) / (2 * mIndicatorCount);
            mRectFIndicator.left = 0;
            mRectFIndicator.top = 0;
            mRectFIndicator.right = w;
            mRectFIndicator.bottom = mItemHeight;
            mCurrP.x = mWidth / 2f;
            mCurrP.y = mHeight * (0.5f + mIndicatorIndex) / mIndicatorCount;
            if(mIndicatorLineShowPosition == LINE_SHOW_POSITION_START){
                mRectFLine.left = 0;
                mRectFLine.top = paddingV;
                mRectFLine.right = mIndicatorLineThickness;
                mRectFLine.bottom = mHeight - paddingV;
            }else{
                mRectFLine.left = mWidth - mIndicatorLineThickness;
                mRectFLine.top = paddingV;
                mRectFLine.right = mWidth;
                mRectFLine.bottom = mHeight - paddingV;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawIndicator(canvas);
        drawHintLine(canvas);
    }

    private void drawIndicator(Canvas canvas){
        mRectFIndicator.offsetTo(mCurrP.x - mRectFIndicator.width()/2 ,
                mCurrP.y - mRectFIndicator.height()/2);

        mPaintIndicator.setColor(refreshCurrColor());
        canvas.drawRect(mRectFIndicator, mPaintIndicator);
    }

    private void drawHintLine(Canvas canvas){
        if(mIndicatorLineShow){
            canvas.drawRect(mRectFLine, mPaintLine);
        }
    }

    public void setIndex(int indexDest){
        if(indexDest < 0 || indexDest > mIndicatorCount - 1){
            throw new IllegalArgumentException("indexDest should less than (mIndicatorCount) and larger than -1," +
                    " now indexDest is " + indexDest );
        }

        float destX, destY;

        if(mIndicatorOrientation == ORIENTATION_H){
            destX = mWidth * (0.5f + indexDest) / mIndicatorCount;
            destY = mHeight / 2f;
        }else{
            destX = mWidth / 2f;
            destY = mHeight * (0.5f + indexDest) / mIndicatorCount;
        }

        mAnim.cancel();
        mOrigP.set(mCurrP);
        mDestP.set(destX, destY);
        if(mOrigP.x == mDestP.x && mOrigP.y == mDestP.y){
            return;
        }
        startAnim(mOrigP, mDestP, mCurrP);
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

    public void setPosition(){

    }

    private void refreshCurrPointByIndexAndOffsetRation(int index, float offsetRation){
        if(index < 0 || index > mIndicatorCount - 1){
            throw new IllegalArgumentException("index should be larger than -1 and less than (mIndicatorCount - 1),"
                    + " now index is " + index);
        }
        if(offsetRation < 0 || offsetRation >= 1){
            throw new IllegalArgumentException("offsetRation should be in [0,1),"
                    + " now offsetRation is " + index);
        }

        if(index == mIndicatorCount - 1){
            offsetRation = 0;
        }

        float destX;
        float destY;

        if(mIndicatorOrientation == ORIENTATION_H){
            destX = mWidth * (0.5f + index + offsetRation) / mIndicatorCount;
            destY = mHeight / 2f;
        }else{
            destX = mWidth / 2f;
            destY = mHeight * (0.5f + index + offsetRation) / mIndicatorCount;
        }
        mCurrP.set(destX, destY);
    }

}