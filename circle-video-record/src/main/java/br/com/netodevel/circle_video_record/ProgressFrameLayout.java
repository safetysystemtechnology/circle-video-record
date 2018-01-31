package br.com.netodevel.circle_video_record;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

/**
 * @author josevieira
 */
public class ProgressFrameLayout extends FrameLayout {

    private CircularAnimatedDrawable mAnimatedDrawable;
    private State mState;
    private FinishVideoListener finishVideoListener;
    private int recordTime;

    public ProgressFrameLayout(@NonNull Context context) {
        super(context);
        //start();
    }

    public ProgressFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
       // start();
    }

    public ProgressFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //start();
    }

    public void setOnFinishListener(FinishVideoListener listener) {
        this.finishVideoListener = listener;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ProgressFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        //start();
    }

    private enum State {
        PROGRESS, STOP;
    }

    public void startWithOutProgress() {
        this.mState = State.STOP;
    }

    public void start() {
        this.mState = State.PROGRESS;
    }

    public void stop() {
        this.mState = State.STOP;
    }

    public void setRecordTime(Integer milliSeconds) {
        this.recordTime = milliSeconds;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mState == State.PROGRESS) {
            if (mAnimatedDrawable == null || !mAnimatedDrawable.isRunning()) {
                mAnimatedDrawable = new CircularAnimatedDrawable(this, 5, Color.WHITE);
                mAnimatedDrawable.setSweepAnimatorDuration(recordTime);

                mAnimatedDrawable.setOnProgressListener(new ProgressListener() {
                    @Override
                    public void onProgressEnd() {
                        stop();
                        if (finishVideoListener != null) {
                            finishVideoListener.maxTime();
                        }
                    }
                });

                int offset = (getWidth() - getHeight()) / 2;

                int left = offset + 5;
                int right = getWidth() - offset - 5;
                int bottom = getHeight() - 5;
                int top = 5;

                mAnimatedDrawable.setBounds(left, top, right, bottom);
                mAnimatedDrawable.setCallback(this);
                mAnimatedDrawable.start();
            } else {
                mAnimatedDrawable.draw(canvas);
            }

            if (mState == State.STOP) {
                if (mAnimatedDrawable != null) {
                    mAnimatedDrawable.stop();
                }
            }
        }
    }

    public void restartAnimation() {
        this.mState = State.STOP;
        this.mAnimatedDrawable.stop();
    }
}
