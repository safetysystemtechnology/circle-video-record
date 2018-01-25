package br.com.netodevel.circle_video_record;

import android.content.Context;

import com.wonderkiln.camerakit.CameraView;

/**
 * @author NetoDevel
 */
public class BuilderCameraView {

    private CameraView mCameraView;

    public CameraView build(Context context) {
        this.mCameraView = new CameraView(context);
        return mCameraView;
    }

}
