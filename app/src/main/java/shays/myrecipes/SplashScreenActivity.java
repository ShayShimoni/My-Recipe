package shays.myrecipes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import shays.myrecipes.databinding.ActivitySplashScreenBinding;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shays.myrecipes.databinding.ActivitySplashScreenBinding binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Animation imageAnimation = AnimationUtils.loadAnimation(this, R.anim.image_animation);
        Animation textAnimation = AnimationUtils.loadAnimation(this, R.anim.text_animation);
        binding.ivSplashScreen.setAnimation(imageAnimation);
        binding.tvSplashScreen.setAnimation(textAnimation);

        new Handler().postDelayed(() -> {
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        },3000);
    }
}