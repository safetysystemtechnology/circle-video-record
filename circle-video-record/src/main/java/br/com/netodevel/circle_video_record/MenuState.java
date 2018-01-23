package br.com.netodevel.circle_video_record;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
        MenuState.EXPANDED,
        MenuState.COLLAPSED,
        MenuState.IDLE
})
@interface MenuState {

    int IDLE = 1;
    int EXPANDED = 2;
    int COLLAPSED = 3;

}