# Circle Video Record
Simple video recorder component for android

## Demo
<p align="center">
  <img src="art/demo.gif" height="500" alt="video record android" />
</p>

## Install
Add the dependecy in `build.gradle(module: app)`

```gradle
repositories {
    mavenCentral()
    maven {
        url  "https://safety.bintray.com/maven"
    }
}

dependencies {
  compile 'br.com.netodevel:circle-video-record:1.0'
}

```

## Usage

### XML

```xml
<br.com.netodevel.circle_video_record.CircleVideoRecord
        android:id="@+id/circle_video_record"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerInParent="true"
        app:circle_width="280dp"
        app:circle_height="280dp"
        app:play_button_image="@drawable/ic_play_circle"
        app:close_button_image="@drawable/ic_stop"
        app:max_time_message="Max time!!">
</br.com.netodevel.circle_video_record.CircleVideoRecord>
```
### Kotlin
```kotlin
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
```

## License
    The MIT License (MIT)

    Copyright (c) Safety System Technology

    Permission is hereby granted, free of charge, to any person obtaining a
    copy of this software and associated documentation files (the "Software"),
    to deal in the Software without restriction, including without limitation
    the rights to use, copy, modify, merge, publish, distribute, sublicense,
    and/or sell copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included
    in all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
    INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
    FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
    ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
