package com.gitonway.lee.niftynotification;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.gitonway.lee.niftynotification.lib.Effects;
import com.gitonway.lee.niftynotification.lib.NiftyNotificationView;
import com.gitonway.lee.niftynotification.lib.NotifyConfiguration;

public class MainActivity extends Activity {
    private Effects effect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showNotify(View v) {

        String msg = "This is Testing Message";
//        String msg="Today we’d like to share a couple of simple styles and effects for android notifications.";

        switch (v.getId()) {
            case R.id.scale:
                effect = Effects.scale;
                break;
            case R.id.thumbSlider:
                effect = Effects.thumbSlider;
                break;
            case R.id.jelly:
                effect = Effects.jelly;
                break;
            case R.id.slidein:
                effect = Effects.slideIn;
                break;
            case R.id.flip:
                effect = Effects.flip;
                break;
            case R.id.slideOnTop:effect=Effects.slideOnTop;break;
            case R.id.standard:
                effect = Effects.standard;
                break;
        }


        NiftyNotificationView.build(this, msg, effect)
//                .setIcon(R.drawable.lion)         //You must call this method if you use ThumbSlider effect
                .show();


//        You can configure like this
//        The default

        NotifyConfiguration cfg = new NotifyConfiguration.Builder()
                .setAnimDuration(700)
                .setDisplayDuration(2500)
                .setBackgroundColor("#FFBDC3C7")
                .setTextColor("#FF444444")
                .setIconBackgroundColor("#FFFFFFFF")
                .setTextPadding(5)                      //dp
                .setViewHeight(48)                      //dp
                .setViewWidth(120)                      //dp
                .setTextLines(2)                        //You had better use setViewHeight and setTextLines together
                .setTextGravity(Gravity.CENTER)         //only text def  Gravity.CENTER,contain icon Gravity.CENTER_VERTICAL
                .build();

        NiftyNotificationView.build(this, msg, effect, cfg)
                .setIcon(R.drawable.lion)               //remove this line ,only text
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //add your code
                    }
                })
                .show();                               //  show(boolean) allow duplicates   or showSticky() sticky notification,you can call removeSticky() method close it
    }

}
