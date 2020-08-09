package com.rohitmaurya.trackorderprogressanimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rohitmaurya.trackorderprogressanimation.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArrayList<Integer> arrayList = new ArrayList<>();
    private int progressBarHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setUpView();
    }

    private void setUpView() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("05 Jan 1995");
        arrayList.add("05 Jan 1995");
        arrayList.add("05 Jan 1995");

        ArrayList<String> aL = new ArrayList<>();
        aL.add("How to: Android Vertical Progress Bar Theming in Android Studio - Crack The Code");
        aL.add("2,883 views•Jan 28, 2017");
        aL.add("28 subscribers");

        for (int i = 0; i < aL.size(); i++) {
            View view = getLayoutInflater().inflate(R.layout.item_layout, null, false);
            view.findViewById(R.id.imageButton).setTag(i);
            ((TextView) view.findViewById(R.id.textView)).setText(arrayList.get(i));
            ((TextView) view.findViewById(R.id.tV)).setText(aL.get(i));

            binding.linearLayout.addView(view);
        }

        startDynamicPart();
    }

    private void startDynamicPart() {
        binding.linearLayout.post(new Runnable() {
            @Override
            public void run() {
                Log.e("sizeIs", binding.linearLayout.getHeight() + "");
                FrameLayout.LayoutParams layoutParams =
                        new FrameLayout.LayoutParams((int) convertDpToPixel(3, MainActivity.this),
                                ViewGroup.LayoutParams.WRAP_CONTENT);

                layoutParams.topMargin = (int) convertDpToPixel(40, MainActivity.this);
                layoutParams.gravity = Gravity.CENTER_HORIZONTAL;

                getChildHeights();

                if (arrayList.size() > 0) {
                    progressBarHeight = binding.linearLayout.getHeight() - (arrayList.get(arrayList.size() - 1) / 3);
                    layoutParams.height = progressBarHeight;
                    binding.progressBar.setMax(progressBarHeight);
                }

                binding.progressBar.setLayoutParams(layoutParams);

                setRunningProgress();
            }
        });
    }

    private int progress;

    private void setRunningProgress() {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {

                binding.progressBar.setProgress(progress);
                if (progress++ <= arrayList.get(0) + (arrayList.get(1) / 1.5))
                    handler.postDelayed(this, 1);
                else
                    changeIconColor(binding.linearLayout, 1);

                if (progress == arrayList.get(0))
                    changeIconColor(binding.linearLayout, 0);
            }
        };
        handler.post(runnable);
    }

    private void changeIconColor(ViewGroup viewGroup, int i) {
        for (int j = 0; j < viewGroup.getChildCount(); j++) {
            View view = viewGroup.getChildAt(j);
            if (view instanceof ViewGroup)
                changeIconColor((ViewGroup) view, i);
            else if (view instanceof ImageButton && view.getTag().equals(i))
                ((ImageButton) view).setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        }
    }

    private void getChildHeights() {
        for (int i = 0; i < binding.linearLayout.getChildCount(); i++) {
            View view = binding.linearLayout.getChildAt(i);
            arrayList.add(view.getHeight());
        }
        Log.e("heightListIs", arrayList.toString());
    }

    public static float convertDpToPixel(float dp, Context context) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float convertPixelsToDp(float px, Context context) {
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}