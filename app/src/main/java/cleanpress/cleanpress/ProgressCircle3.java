package cleanpress.cleanpress;

/**
 * Created by VanessaM on 8/17/2015.
 */

import android.animation.ValueAnimator;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import cleanpress.cleanpress.R;

public class ProgressCircle3 extends View {

    private final RectF mOval = new RectF();
    private float mSweepAngle = 0;
    private int startAngle = 194;
    private int angleGap = 1;

    float mEndAngle = 1.0f;

    Paint progressPaint = new Paint();
    TextPaint textPaint = new TextPaint();
    Paint incompletePaint = new Paint();
    Paint percentagePaint = new Paint();
    Paint checkPaint = new Paint();

    private int maxProgress;

    private long animDuration = 50;

    private float strokeWidth = 5.0f;

    public ProgressCircle3(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ProgressCircle,
                0, 0);

        int textColor;
        float textSize;

        int progressColor;
        int incompleteColor;
        int checkColor;

        try {
            textColor = a.getColor(R.styleable.ProgressCircle_android_textColor, getResources().getColor(android.R.color.holo_red_dark));
            textSize = a.getDimension(R.styleable.ProgressCircle_android_textSize, 100);

            strokeWidth = a.getDimension(R.styleable.ProgressCircle_strokeWidth, 5.0f);

            progressColor = a.getColor(R.styleable.ProgressCircle_progressColor, getResources().getColor(android.R.color.holo_blue_bright));
            incompleteColor = a.getColor(R.styleable.ProgressCircle_incompleteProgressColor, getResources().getColor(android.R.color.darker_gray));
            checkColor = a.getColor(R.styleable.ProgressCircle_incompleteProgressColor, getResources().getColor(android.R.color.darker_gray));
        } finally {
            a.recycle();
        }

        progressPaint.setColor(progressColor);
        progressPaint.setStrokeWidth(strokeWidth);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        Typeface tf = Typeface.create("Roboto Condensed Light", Typeface.BOLD);
        textPaint.setTypeface(tf);

        percentagePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        percentagePaint.setColor(textColor);
        percentagePaint.setTextSize(0);

        incompletePaint.setColor(incompleteColor);
        incompletePaint.setStrokeWidth(strokeWidth);
        incompletePaint.setStyle(Paint.Style.STROKE);
        incompletePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        checkPaint.setColor(checkColor);
        checkPaint.setStrokeWidth(strokeWidth);
        checkPaint.setStyle(Paint.Style.FILL);
        checkPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float currentAngleGap = mSweepAngle == 1.0f || mSweepAngle == 0 ? 0 : angleGap;
        mOval.set(strokeWidth / 2, strokeWidth + 10, getWidth() - (strokeWidth / 2), getWidth() - (strokeWidth / 2));
        canvas.drawArc(mOval, -startAngle + currentAngleGap, (mSweepAngle * 98) - currentAngleGap, false,
                progressPaint);

        mOval.set(strokeWidth / 2, strokeWidth + 10, getWidth() - (strokeWidth / 2), getWidth() - (strokeWidth / 2));
        canvas.drawArc(mOval, mSweepAngle * 98 - startAngle + currentAngleGap, 98 - (mSweepAngle * 98) - currentAngleGap, false,
                incompletePaint);

        drawText(canvas, textPaint, String.valueOf((int) (mSweepAngle * 98)), percentagePaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    private void drawText(Canvas canvas, Paint paint, String text, Paint percentagePaint) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        Rect percentageBounds = new Rect();
        percentagePaint.getTextBounds("%", 0, 1, percentageBounds);
        int x = (canvas.getWidth() / 2) - (bounds.width() / 2) - (percentageBounds.width() / 2);
        int y = (canvas.getHeight() / 2) + (bounds.height() / 2);
        canvas.drawText(text, x, y, paint);


    }

    public void setTextColor(int color) {
        textPaint.setColor(color);
    }

    public void setProgressColor(int color) {
        progressPaint.setColor(color);
    }

    public void setIncompleteColor(int color) {
        incompletePaint.setColor(color);
    }

    public void setCheckColor(int color) {        checkPaint.setColor(color);    }

    public void setMax(int max){
        maxProgress = max;

    }

    public int getMax(){
        return maxProgress;
    }

    public void setAnimDuration(long animDuration) {
        this.animDuration = animDuration;
    }

    public void setProgress(float progress) {
        if (progress > 100.0f || progress < 0) {
            throw new RuntimeException("Value must be between 0 and 1: " + progress);
        }

        mEndAngle = progress;

        this.invalidate();
    }

    public void startAnimation() {
        ValueAnimator anim = ValueAnimator.ofFloat(mSweepAngle, mEndAngle);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ProgressCircle3.this.mSweepAngle = (Float) valueAnimator.getAnimatedValue();
                ProgressCircle3.this.invalidate();
            }
        });
        anim.setDuration(animDuration);
        //anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();

    }

}

