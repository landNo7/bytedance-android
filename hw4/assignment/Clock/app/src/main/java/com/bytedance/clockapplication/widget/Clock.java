package com.bytedance.clockapplication.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.View;

import java.util.Calendar;
import java.util.Locale;

public class Clock extends View {

    private final static String TAG = Clock.class.getSimpleName();

    private static final int FULL_ANGLE = 360;

    private static final int CUSTOM_ALPHA = 140;
    private static final int FULL_ALPHA = 255;

    private static final int DEFAULT_PRIMARY_COLOR = Color.WHITE;
    private static final int DEFAULT_SECONDARY_COLOR = Color.LTGRAY;

    private static final float DEFAULT_DEGREE_STROKE_WIDTH = 0.010f;

    public final static int AM = 0;

    private static final int RIGHT_ANGLE = 90;

    private int mWidth, mCenterX, mCenterY, mRadius;

    /**
     * properties
     */
    private int centerInnerColor;
    private int centerOuterColor;

    private int secondsNeedleColor;
    private int hoursNeedleColor;
    private int minutesNeedleColor;

    private int degreesColor;

    private int hoursValuesColor;

    private int numbersColor;

    private boolean mShowAnalog = true;

    public Clock(Context context) {
        super(context);
        init(context, null);
    }

    public Clock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Clock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int size;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
        int heightWithoutPadding = height - getPaddingTop() - getPaddingBottom();

        if (widthWithoutPadding > heightWithoutPadding) {
            size = heightWithoutPadding;
        } else {
            size = widthWithoutPadding;
        }

        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(), size + getPaddingTop() + getPaddingBottom());
    }

    private void init(Context context, AttributeSet attrs) {

        this.centerInnerColor = Color.LTGRAY;
        this.centerOuterColor = DEFAULT_PRIMARY_COLOR;

        this.secondsNeedleColor = DEFAULT_SECONDARY_COLOR;
        this.hoursNeedleColor = DEFAULT_PRIMARY_COLOR;
        this.minutesNeedleColor = DEFAULT_PRIMARY_COLOR;

        this.degreesColor = DEFAULT_PRIMARY_COLOR;

        this.hoursValuesColor = DEFAULT_PRIMARY_COLOR;

        numbersColor = Color.WHITE;
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        mWidth = getHeight() > getWidth() ? getWidth() : getHeight();

        int halfWidth = mWidth / 2;
        mCenterX = halfWidth;
        mCenterY = halfWidth;
        mRadius = halfWidth;

        if (mShowAnalog) {
            drawDegrees(canvas);
            drawHoursValues(canvas);
            drawNeedles(canvas);
            drawCenter(canvas);
        } else {
            drawNumbers(canvas);
        }

    }

    private void drawDegrees(Canvas canvas) {

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(mWidth * DEFAULT_DEGREE_STROKE_WIDTH);
        paint.setColor(degreesColor);

        int rPadded = mCenterX - (int) (mWidth * 0.01f);
        int rEnd = mCenterX - (int) (mWidth * 0.05f);

        for (int i = 0; i < FULL_ANGLE; i += 6 /* Step */) {

            if ((i % RIGHT_ANGLE) != 0 && (i % 15) != 0)
                paint.setAlpha(CUSTOM_ALPHA);
            else {
                paint.setAlpha(FULL_ALPHA);
            }

            int startX = (int) (mCenterX + rPadded * Math.cos(Math.toRadians(i)));
            int startY = (int) (mCenterX - rPadded * Math.sin(Math.toRadians(i)));

            int stopX = (int) (mCenterX + rEnd * Math.cos(Math.toRadians(i)));
            int stopY = (int) (mCenterX - rEnd * Math.sin(Math.toRadians(i)));

            canvas.drawLine(startX, startY, stopX, stopY, paint);

        }
    }

    /**
     * @param canvas
     */
    private void drawNumbers(Canvas canvas) {

        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(mWidth * 0.2f);
        textPaint.setColor(numbersColor);
        textPaint.setColor(numbersColor);
        textPaint.setAntiAlias(true);

        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int amPm = calendar.get(Calendar.AM_PM);

        String time = String.format("%s:%s:%s%s",
                String.format(Locale.getDefault(), "%02d", hour),
                String.format(Locale.getDefault(), "%02d", minute),
                String.format(Locale.getDefault(), "%02d", second),
                amPm == AM ? "AM" : "PM");

        SpannableStringBuilder spannableString = new SpannableStringBuilder(time);
        spannableString.setSpan(new RelativeSizeSpan(0.3f), spannableString.toString().length() - 2, spannableString.toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // se superscript percent

        StaticLayout layout = new StaticLayout(spannableString, textPaint, canvas.getWidth(), Layout.Alignment.ALIGN_CENTER, 1, 1, true);
        canvas.translate(mCenterX - layout.getWidth() / 2f, mCenterY - layout.getHeight() / 2f);
        layout.draw(canvas);
    }

    /**
     * Draw Hour Text Values, such as 1 2 3 ...
     *
     * @param canvas
     */
    private void drawHoursValues(Canvas canvas) {
        // Default Color:
        // - hoursValuesColor
        float nRadius = mCenterX*0.725f;
        String[] Hours = {"06","05","04","03","02","01","12","11","10","09","08","07"};

        Paint textPaint = new Paint();
        textPaint.setColor(minutesNeedleColor);
        textPaint.setTextSize(100);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;
        //canvas.drawTextRun();
        for (int i = 0;i < FULL_ANGLE;i += 30) {

            float baseLineX = (float) (mCenterX + nRadius * Math.sin(Math.toRadians(i)));
            float baseLineY = (float) (mCenterY + nRadius * Math.cos(Math.toRadians(i)) - top / 2 - bottom / 2);
            canvas.drawText(Hours[i/30],baseLineX, baseLineY, textPaint);
            //canvas.drawPosText("巴扎黑", new float[]{100, 100, 100, 200, 100, 300}, paint);
        }
    }

    /**
     * Draw hours, minutes needles
     * Draw progress that indicates hours needle disposition.
     *
     * @param canvas
     */
    private void drawNeedles(final Canvas canvas) {
        // Default Color:
        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int amPm = calendar.get(Calendar.AM_PM);

        // - secondsNeedleColor
        Paint secondsLinePaint = new Paint();
        secondsLinePaint.setColor(secondsNeedleColor);
        secondsLinePaint.setStyle(Paint.Style.FILL);
        secondsLinePaint.setStrokeWidth(8f);
        secondsLinePaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawLine(mCenterX,mCenterY,
                (float) (mCenterX + mRadius * 0.6f * Math.sin(Math.toRadians((30 - second) * 6.0))),
                (float) (mCenterX + mRadius * 0.6f * Math.cos(Math.toRadians((30 - second) * 6.0)))
                ,secondsLinePaint);

        // - hoursNeedleColor
        Paint hoursLinePaint = new Paint();
        hoursLinePaint.setColor(hoursNeedleColor);
        hoursLinePaint.setStyle(Paint.Style.FILL);
        hoursLinePaint.setStrokeWidth(14f);
        hoursLinePaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawLine(mCenterX,mCenterY,
                (float) (mCenterX + mRadius * 0.3f * Math.sin(Math.toRadians((6 - hour) * 30.0))),
                (float) (mCenterX + mRadius * 0.3f * Math.cos(Math.toRadians((6 - hour) * 30.0)))
                ,hoursLinePaint);

        // - minutesNeedleColor
        Paint minutesLinePaint = new Paint();
        //Point minutesE = new Point();       //分针点
        minutesLinePaint.setColor(minutesNeedleColor);
        minutesLinePaint.setStyle(Paint.Style.FILL);
        minutesLinePaint.setStrokeWidth(12f);
        minutesLinePaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawLine(mCenterX,mCenterY,
                (float) (mCenterX + mRadius * 0.45f * Math.sin(Math.toRadians((30 - minute) * 6.0))),
                (float) (mCenterX + mRadius * 0.45f * Math.cos(Math.toRadians((30 - minute) * 6.0)))
                ,minutesLinePaint);
    }

    /**
     * Draw Center Dot
     *
     * @param canvas
     */
    private void drawCenter(Canvas canvas) {
        // Default Color:
        // - centerInnerColor
        Paint centerInnerPaint = new Paint();
        centerInnerPaint.setColor(centerInnerColor);
        centerInnerPaint.setStrokeWidth(20);
        centerInnerPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mCenterX,mCenterY,20,centerInnerPaint);

        // - centerOuterColor
        Paint centerOuterPaint = new Paint();
        centerOuterPaint.setColor(centerOuterColor);
        centerOuterPaint.setStrokeWidth(20);
        centerOuterPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(mCenterX,mCenterY,20,centerOuterPaint);

    }

    public void setShowAnalog(boolean showAnalog) {
        mShowAnalog = showAnalog;
        invalidate();
    }

    public boolean isShowAnalog() {
        return mShowAnalog;
    }

}