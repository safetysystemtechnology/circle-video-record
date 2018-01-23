package br.com.netodevel.demo

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author NetoDevel
 */
class MainActivity : AppCompatActivity() {

    var mStatus: Boolean? = false

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        circle_video_record.setup(root_layout)
        button_record.setOnClickListener {
            mStatus = !mStatus!!;

            if (mStatus == true) {
                circle_video_record.show()
            } else {
                circle_video_record.hide()
            }

            circle_video_record.setVideoListener {
                Log.d("video_path", it.absolutePath)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        circle_video_record.camera.start()
    }

    override fun onPause() {
        circle_video_record.camera.stop()
        super.onPause()
    }

}