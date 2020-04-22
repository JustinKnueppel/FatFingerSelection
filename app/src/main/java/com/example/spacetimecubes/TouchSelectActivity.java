package com.example.spacetimecubes;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class TouchSelectActivity extends AppCompatActivity {
    private ImageView _dotMatrix;
    private String[] images;
    private int imageCounter;
    private Coordinates<Integer> dotPosition;
    private long startTime;
    private long endTime;
    private Data data;
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

        _dotMatrix = findViewById(R.id.dot_matrix_touch);

        Intent i = getIntent();
        Bundle extras = i.getExtras();

        if (extras != null) {
            images = i.getStringArrayExtra("images");

            List<String> imageList = Arrays.asList(images);
            Collections.shuffle(imageList);
            imageList.toArray(images);
        }
        this.imageCounter = 0;

        String firstImage = images[0];
        this.dotPosition = getCoordinatesFromFilename(firstImage);
        loadImage(firstImage);

        this.data = initData(firstImage);

        this.startTime = System.nanoTime();

        this._dotMatrix.setOnTouchListener(dotMatrixOnTouchListener);

        mVisible = true;

    }

    private Data initData(String imageName) {
        float circleRadius = 10*PIXELS_PER_DP;
        float spaceBetween = 10*PIXELS_PER_DP;
        Data data = new Data("touch", circleRadius, spaceBetween);
        return data;
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
    }

    @SuppressLint("InlinedApi")
    private void show() {
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
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
        int x = Integer.parseInt(filename.substring(5, 7));
        int y = Integer.parseInt(filename.substring(7, 9));
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

    private void loadImage(String nextImage) {
        int id = getResources().getIdentifier(nextImage, "drawable", getPackageName());
        _dotMatrix.setImageResource(id);
    }
    private View.OnTouchListener dotMatrixOnTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                endTime = System.nanoTime();
                data.setTimeTaken(startTime, endTime);

                float touchX = event.getX();
                float touchY = event.getY();

                Coordinates<Float> touchCoordinates = new Coordinates<>(touchX, touchY);
                Coordinates<Float> dotViewCoordinates = getViewDotCoordinates(dotPosition);

                double pixelDistance = distance(touchCoordinates, dotViewCoordinates);
                double pixelDistanceFromCircle = pixelDistance < CIRCLE_WIDTH ?
                        0 :
                        pixelDistance - CIRCLE_WIDTH / 2;
                double normalizedDistanceFromCircle = pixelDistanceFromCircle / CIRCLE_WIDTH;

                data.setPixelDistance(pixelDistance);

                /* Send data to database */
                data.post();

                /* Log normalized distance */
                Data.processCircleDistance(normalizedDistanceFromCircle);

                imageCounter++;
                if (imageCounter >= images.length) {
                    finish();
                    return true;
                }
                String nextImage = images[imageCounter];
                dotPosition = getCoordinatesFromFilename(nextImage);
                loadImage(nextImage);

                data = initData(nextImage);

                startTime = System.nanoTime();

                return true;
            }
            return false;
        }
    };
}
