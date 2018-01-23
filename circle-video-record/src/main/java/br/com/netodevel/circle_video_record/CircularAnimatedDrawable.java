package br.com.netodevel.circle_video_record;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * @author NetoDevel
 */
public class CircularAnimatedDrawable extends Drawable implements Animatable {

    private ValueAnimator mValueAnimatorSweep;
    private static final Interpolator SWEEP_INTERPOLATOR = new DecelerateInterpolator();
    private static final int SWEEP_ANIMATOR_DURATION = 10000;
    private static final Float MIN_SWEEP_ANGLE = 30f;

    private final RectF fBounds = new RectF();
    private Paint mPaint;
    private View mAnimatedView;

    private float mBorderWidth;
    private float mCurrentGlobalAngle;
    private float mCurrentSweepAngle = 1;
    private float mCurrentGlobalAngleOffset;

    private boolean mModeAppearing;
    private boolean mRunning;

    private ProgressListener progressListener;

    public CircularAnimatedDrawable(View view, float borderWidth, int arcColor) {
        mAnimatedView = view;

        mBorderWidth = borderWidth;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(borderWidth);
        mPaint.setColor(arcColor);

        setupAnimations();
    }

    public void setOnProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        fBounds.left = bounds.left + mBorderWidth / 2f + .5f;
        fBounds.right = bounds.right - mBorderWidth / 2f - .5f;
        fBounds.top = bounds.top + mBorderWidth / 2f + .5f;
        fBounds.bottom = bounds.bottom - mBorderWidth / 2f - .5f;
    }

    /**
     * Start the animation
     */
    @Override
    public void start() {
        if (isRunning()) {
            return;
        }
        mRunning = true;
        mValueAnimatorSweep.start();
    }

    /**
     * Stops the animation
     */
    @Override
    public void stop() {
        if (!isRunning()) {
            return;
        }
        mRunning = false;
        mValueAnimatorSweep.cancel();
    }

    public void startAnimation() {
        mValueAnimatorSweep.start();
    }

    public void restartAnimation() {
        mValueAnimatorSweep.cancel();
        mValueAnimatorSweep.end();
    }

    @Override
    public boolean isRunning() {
        return mRunning;
    }

    @Override
    public void draw(Canvas canvas) {
        if (isRunning()) {
            float startAngle = mCurrentGlobalAngle - mCurrentGlobalAngleOffset ;
            float sweepAngle = mCurrentSweepAngle;
            if (sweepAngle == 360) {
                this.progressListener.onProgressEnd();
            }
            canvas.drawArc(fBounds, startAngle, sweepAngle, false, mPaint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    /**
     * Sets the current sweep angle, so the fancy loading animation can happen
     *
     * @param currentSweepAngle
     */
    public void setCurrentSweepAngle(float currentSweepAngle) {
        mCurrentSweepAngle = currentSweepAngle;
        invalidateSelf();
    }

    /**
     * Set up all the animations. There are two animation: Global angle animation and sweep animation.
     */
    private void setupAnimations() {
        mValueAnimatorSweep = ValueAnimator.ofFloat(mCurrentSweepAngle, 360f);
        mValueAnimatorSweep.setInterpolator(SWEEP_INTERPOLATOR);
        mValueAnimatorSweep.setDuration(SWEEP_ANIMATOR_DURATION);

        mValueAnimatorSweep.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setCurrentSweepAngle((float)animation.getAnimatedValue());
                mAnimatedView.invalidate();
            }
        });
    }
}