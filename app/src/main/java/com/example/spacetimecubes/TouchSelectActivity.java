package com.example.spacetimecubes;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class TouchSelectActivity extends AppCompatActivity {
    private Coordinates<Integer> _dotCoordinates;
    private ImageView _dotMatrix;
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_touch_select);

        _dotCoordinates = new Coordinates<>(1, 1);
        _dotMatrix = findViewById(R.id.dot_matrix_touch);

        _dotMatrix.setOnTouchListener(dotMatrixOnTouchListener);

        mVisible = true;
        mContentView = findViewById(R.id.dot_matrix_touch);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
    private Coordinates<Integer> getCoordinatesFromFilename(String filename) {
        int x = Integer.parseInt(filename.substring(0, 2));
        int y = Integer.parseInt(filename.substring(2, 4));
        return new Coordinates<>(x, y);
    }

    private final float PIXELS_PER_DP = 3;
    private final float MARGIN = 5 * PIXELS_PER_DP;
    private final float CIRCLE_WIDTH = 10 * PIXELS_PER_DP;
    private final float SPACING = 10 * PIXELS_PER_DP;
    private Coordinates<Float> getViewDotCoordinates(Coordinates<Integer> dotCoordinates) {
        float x = MARGIN + (dotCoordinates.getX() - 1) * (CIRCLE_WIDTH + SPACING) + CIRCLE_WIDTH / 2;
        float y = MARGIN + (dotCoordinates.getY() - 1) * (CIRCLE_WIDTH + SPACING) + CIRCLE_WIDTH / 2;
        return new Coordinates<>(x, y);
    }

    private double distance(Coordinates<Float> coordinateOne, Coordinates<Float> coordinateTwo) {
        double xdiff = coordinateOne.getX() - coordinateTwo.getX();
        double ydiff = coordinateOne.getY() - coordinateTwo.getY();
        return Math.sqrt(Math.pow(xdiff, 2) + Math.pow(ydiff, 2));
    }

    private View.OnTouchListener dotMatrixOnTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float touchX = event.getRawX();
            float touchY = event.getRawY();

            Coordinates<Float> touchCoordinates = new Coordinates<>(touchX, touchY);
            Coordinates<Float> dotViewCoordinates = getViewDotCoordinates(_dotCoordinates);

            double pixelDistance = distance(touchCoordinates, dotViewCoordinates);
            double pixelDistanceFromCircle = pixelDistance < CIRCLE_WIDTH ?
                    0 :
                    pixelDistance - CIRCLE_WIDTH/2;
            double normalizedDistanceFromCircle = pixelDistanceFromCircle/CIRCLE_WIDTH;

            Log.d("Missed By", String.format("%f pixels away \n", pixelDistanceFromCircle));
            Log.d("Missed By", String.format("%f circles away \n", normalizedDistanceFromCircle));

            return true;
        }
    };
}
