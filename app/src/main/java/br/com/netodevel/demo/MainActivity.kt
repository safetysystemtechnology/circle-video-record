package br.com.netodevel.demo

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.Log
import br.com.netodevel.circle_video_record.BuilderCameraView
import com.wonderkiln.camerakit.CameraView
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author NetoDevel
 */
class MainActivity : AppCompatActivity() {

    lateinit var cameraView: CameraView
    var mStatus: Boolean? = false

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * Build CameraView
         */
        cameraView = BuilderCameraView().build(this)

        /**
         * Setup Circle Video Record
         */
        circle_video_record.setup(root_layout, cameraView)

        //circle_video_record.setRecordTime(2000)

        button_record.setOnClickListener {
            mStatus = !mStatus!!;

            if (mStatus == true) {
                circle_video_record.show()
            } else {
                circle_video_record.hide()
            }

            /**
             * Listener callback video file
             */
            circle_video_record.setVideoListener {
                Log.d("video_path", it.absolutePath)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        cameraView.start()
    }

    override fun onPause() {
        cameraView.stop()
        super.onPause()
    }

}