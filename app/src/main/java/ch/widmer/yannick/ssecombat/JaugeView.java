package ch.widmer.yannick.ssecombat;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Yannick on 21.07.2015.
 */
public class JaugeView extends View {

    private static String LOG = "View Jauge";
    private int textSize, mHeight, mWidth;
    private int max=1,moment=1;
    private Paint foregroundColor, backgroundColor,textPaint;

    @TargetApi(21)
    public JaugeView(Context c, AttributeSet attr, int defStyleAttr, int defStyleRes) {
        super(c, attr, defStyleAttr, defStyleRes);
        TypedArray a = c.getTheme().obtainStyledAttributes(
                attr,
                R.styleable.JaugeView,0,0);
        init(a);
    }

    public JaugeView(Context c, AttributeSet attr, int defStyle){
        super(c,attr,defStyle);
        Log.d(LOG, "Constructor(c,attr)");
        TypedArray a = c.getTheme().obtainStyledAttributes(
                attr,
                R.styleable.JaugeView,0,0);
        init(a);
    }

    public JaugeView(Context c, AttributeSet attr){
        super(c,attr);
        Log.d(LOG, "Constructor(c,attr)");
        TypedArray a = c.getTheme().obtainStyledAttributes(
                attr,
                R.styleable.JaugeView,0,0);
        init(a);
    }

    public JaugeView(Context c){
        super(c);
        Log.d(LOG,"Constructor(c)");
        TypedArray a = c.getTheme().obtainStyledAttributes(R.styleable.JaugeView);
        init(a);
    }

    private void init(TypedArray tp){
        foregroundColor = new Paint();
        foregroundColor.setStyle(Paint.Style.FILL);
        backgroundColor = new Paint();
        backgroundColor.setStyle(Paint.Style.FILL);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.DKGRAY);

        try {
            textSize = tp.getDimensionPixelOffset(R.styleable.JaugeView_android_textSize,10);
            textPaint.setTextSize(textSize);
            textPaint.setColor(getResources().getColor(R.color.white));
            LinearGradient shader = new LinearGradient(0,0,0,textSize,
                    Color.WHITE,tp.getColor(R.styleable.JaugeView_foreground_color, Color.WHITE),
                    Shader.TileMode.MIRROR);
            foregroundColor.setShader(shader);
            shader = new LinearGradient(0,0,0,textSize,
                    Color.WHITE,tp.getColor(R.styleable.JaugeView_background_color, Color.BLACK),
                    Shader.TileMode.CLAMP);
            backgroundColor.setShader(shader);

            Log.d(LOG, "resolved values");
            Log.d(LOG, "textSize "+textSize);
        }catch(Exception e) {
            Log.d(LOG, "wasn't able to resolve xml values");
        }finally{
            tp.recycle();
        }
    }

    public void setValues(int max, int moment){
        this.max = max;
        this.moment = moment;
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canv){
        int top = getPaddingTop();
        int left = getPaddingLeft()+mHeight/2;
        if(max == 0)
            max=1;
        int momentWidth = mWidth * moment / max;
        if(momentWidth>mWidth)
            momentWidth = mWidth;

        canv.drawRect(left, top, left + mWidth , top + mHeight, backgroundColor);
        canv.drawCircle(left, top + mHeight / 2, mHeight/2, backgroundColor);
        canv.drawCircle(left+mWidth, top + mHeight / 2, mHeight/2, backgroundColor);

        if(moment!=0) {
            canv.drawRect(left, top, left + momentWidth, top + mHeight, foregroundColor);
            canv.drawCircle(left, top + mHeight / 2, mHeight / 2, foregroundColor);
            canv.drawCircle(left + momentWidth, top + mHeight / 2, mHeight / 2, foregroundColor);
        }

        canv.drawText("" + moment, left, top + mHeight - 5, textPaint);
        canv.drawText(""+max,left+mWidth-textSize/(max<10?3:1),top+mHeight-5,textPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = resolveMeasureSpec(widthMeasureSpec,100);
        mHeight = resolveMeasureSpec(heightMeasureSpec,textSize + getPaddingTop()+getPaddingBottom());
        setMeasuredDimension(mWidth,mHeight);
        mWidth -= getPaddingLeft()+getPaddingRight();
        mHeight -= getPaddingBottom()+getPaddingRight();
        mWidth -= mHeight;
    }

    private int resolveMeasureSpec(int measureSpec, int desiredSize){
        int given = MeasureSpec.getSize(measureSpec);
        switch(MeasureSpec.getMode(measureSpec)){
            case MeasureSpec.AT_MOST:
                return Math.min(given,desiredSize);
            case MeasureSpec.EXACTLY:
                return given;
            default: return desiredSize;
        }
    }
}
