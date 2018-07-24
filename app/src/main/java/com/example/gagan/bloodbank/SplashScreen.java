package com.example.gagan.bloodbank;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        imageView=(ImageView)findViewById(R.id.splashimage);
        Animation myanimation= AnimationUtils.loadAnimation(SplashScreen.this,R.anim.anim);
        imageView.setAnimation(myanimation);
        imageView.startAnimation(myanimation);

        myanimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(SplashScreen.this,PhoneVerify.class));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
