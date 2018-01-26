package br.com.netodevel.circle_video_record;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wonderkiln.camerakit.CameraKitEventCallback;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

/**
 * @author NetoDevel
 */
public class CircleVideoRecord extends RelativeLayout {

    private static int DEFAULT_ICON_SIZE = 40;
    private static int ID_PROGRESS_FRAME_LAYOUT = 10;

    private Context mContext;
    private RelativeLayout mLayoutVoice;
    private ImageView mImageView;

    private int circleWidth = 0;
    private int circleHeight = 0;

    private boolean recording = false;
    private int mStatus;
    private Camera mCamera;

    private boolean longClicked = false;
    private OnTouchListener mTouchListener;
    private ProgressFrameLayout progressFrameLayout;
    private CameraView mCameraView;
    private ImageButton mCloseButton;
    private ImageButton mPlayButton;
    private boolean cameraStarted = false;
    private VideoListener videoListener;

    private Drawable drawablePlayButton;
    private Drawable drawableCloseButton;
    private String maxTimeMessage = "";
    private RelativeLayout mRootLayout;

    public CircleVideoRecord(Context context) {
        super(context);
        init(context, null, -1, -1);
    }

    public CircleVideoRecord(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs,
                -1, -1);
    }

    public CircleVideoRecord(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, -1);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleVideoRecord(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setup(RelativeLayout view, CameraView cameraView) {
        if (progressFrameLayout == null) {
            progressFrameLayout = new ProgressFrameLayout(mContext);
            progressFrameLayout.setId(ID_PROGRESS_FRAME_LAYOUT);
            progressFrameLayout.setVisibility(INVISIBLE);
            progressFrameLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_circle));

            LayoutParams layoutParams = new LayoutParams(circleWidth != 0 ? circleWidth : 290, circleHeight != 0 ? circleHeight : 290);
            layoutParams.addRule(CENTER_IN_PARENT, TRUE);

            /**
             * Camera
             */
            mCameraView = cameraView;
            //addCameraView();

            /**
             * Add Progress Frame Layout to root
             */
            view.addView(progressFrameLayout, layoutParams);

            /**
             * Close Button
             */
            this.mCloseButton = new ImageButton(mContext);
            this.mCloseButton.setVisibility(INVISIBLE);
            //this.mCloseButton.setAlpha(0.5f);
            this.mCloseButton.setImageDrawable(drawableCloseButton != null ? drawableCloseButton : ContextCompat.getDrawable(mContext, R.drawable.ic_close));
            //this.mCloseButton.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_circle));
            //this.mCloseButton.setColorFilter(Color.WHITE);

            this.mCloseButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPlayButton.setVisibility(VISIBLE);
                    mCloseButton.setVisibility(INVISIBLE);
                    closeVideoRecord();
                }
            });

            LayoutParams closeButtonParams = new LayoutParams(
                    70,
                    70
            );

            closeButtonParams.addRule(BELOW, ID_PROGRESS_FRAME_LAYOUT);
            closeButtonParams.addRule(CENTER_IN_PARENT, TRUE);
            view.addView(mCloseButton, closeButtonParams);

            this.mPlayButton = new ImageButton(mContext);
            this.mPlayButton.setVisibility(INVISIBLE);
            //this.mPlayButton.setAlpha(0.5f);
            this.mPlayButton.setImageDrawable(drawablePlayButton != null ? drawablePlayButton : ContextCompat.getDrawable(mContext, R.drawable.ic_play));
            //this.mPlayButton.setColorFilter(Color.WHITE);

            this.mPlayButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCloseButton.setVisibility(VISIBLE);
                    mPlayButton.setVisibility(INVISIBLE);

                    mCameraView.captureVideo(new CameraKitEventCallback<CameraKitVideo>() {
                        @Override
                        public void callback(CameraKitVideo cameraKitVideo) {
                            if (videoListener != null) {
                                videoListener.onVideoTaken(cameraKitVideo.getVideoFile());
                            }
                        }
                    });

                    recording = true;
                    progressFrameLayout.start();
                    progressFrameLayout.requestLayout();
                }
            });

            LayoutParams playButtonParams = new LayoutParams(
                    70,
                    70
            );

            playButtonParams.addRule(BELOW, ID_PROGRESS_FRAME_LAYOUT);
            playButtonParams.addRule(CENTER_IN_PARENT, TRUE);
            view.addView(mPlayButton, playButtonParams);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                addCenterCamera();
            }

            this.progressFrameLayout.setVisibility(INVISIBLE);
            this.mPlayButton.setVisibility(INVISIBLE);
            this.mCloseButton.setVisibility(INVISIBLE);
        }
    }

    private void addCameraView() {
        LayoutParams cameraParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        cameraParams.addRule(CENTER_IN_PARENT, TRUE);

        progressFrameLayout.addView(mCameraView, cameraParams);
    }

    public void show() {
        Animation animFadeIn = AnimationUtils.loadAnimation(this.mContext, R.anim.to_up);
        animFadeIn.reset();
        this.progressFrameLayout.clearAnimation();
        this.progressFrameLayout.startAnimation(animFadeIn);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cropViews();
        }
        addCameraView();

        this.progressFrameLayout.setVisibility(VISIBLE);
        this.mPlayButton.setVisibility(VISIBLE);
        this.mCloseButton.setVisibility(INVISIBLE);
    }

    public void hide() {
        this.progressFrameLayout.removeView(mCameraView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.mCameraView.setOutlineProvider(null);
        }

        this.progressFrameLayout.setVisibility(INVISIBLE);
        this.mPlayButton.setVisibility(INVISIBLE);
        this.mCloseButton.setVisibility(INVISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void cropViews() {
        ViewOutlineProvider vop = new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setOval(5, 5, view.getWidth() - 5, view.getHeight() - 5);
            }
        };

        this.progressFrameLayout.setOutlineProvider(vop);
        this.progressFrameLayout.setClipToOutline(true);

        ViewOutlineProvider vop2 = new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setOval(10, 10, progressFrameLayout.getWidth() - 10, progressFrameLayout.getHeight() - 10);
            }
        };

        mCameraView.setOutlineProvider(vop2);
        mCameraView.setClipToOutline(true);
    }

    public void setVideoListener(VideoListener videoListener) {
        this.videoListener = videoListener;
    }

    public void closeVideoRecord() {
        this.progressFrameLayout.restartAnimation();
        hide();

        if (recording) {
            mCameraView.stopVideo();
            recording = false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void addCenterCamera() {
        ViewOutlineProvider vop = new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setOval(5, 5, view.getWidth() - 5, view.getHeight() - 5);
            }
        };

        this.progressFrameLayout.setOutlineProvider(vop);
        this.progressFrameLayout.setClipToOutline(true);

        ViewOutlineProvider vop2 = new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setOval(10, 10, progressFrameLayout.getWidth() - 10, progressFrameLayout.getHeight() - 10);
            }
        };

        mCameraView.setOutlineProvider(vop2);
        mCameraView.setClipToOutline(true);

        this.progressFrameLayout.setOnFinishListener(new FinishVideoListener() {
            @Override
            public void maxTime() {
                Toast.makeText(mContext, maxTimeMessage != null ? maxTimeMessage : "Time limit exceeded", Toast.LENGTH_SHORT).show();
                closeVideoRecord();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void init(final Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        mContext = context;
        mStatus = MenuState.IDLE;

        if (attrs != null && defStyleAttr == -1 && defStyleRes == -1) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleVideoRecord, defStyleAttr, defStyleRes);

            drawablePlayButton = typedArray.getDrawable(R.styleable.CircleVideoRecord_play_button_image);
            drawableCloseButton = typedArray.getDrawable(R.styleable.CircleVideoRecord_close_button_image);

            circleWidth = (int) typedArray.getDimension(R.styleable.CircleVideoRecord_circle_width, ViewGroup.LayoutParams.WRAP_CONTENT);
            circleHeight = (int) typedArray.getDimension(R.styleable.CircleVideoRecord_circle_height, ViewGroup.LayoutParams.WRAP_CONTENT);

            maxTimeMessage = (String) typedArray.getString(R.styleable.CircleVideoRecord_max_time_message);
        }
    }

    public CameraView getCamera() {
        return this.mCameraView;
    }

}