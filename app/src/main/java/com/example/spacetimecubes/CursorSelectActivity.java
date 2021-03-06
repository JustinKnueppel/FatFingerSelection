package com.example.spacetimecubes;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class CursorSelectActivity extends AppCompatActivity {
    private ImageView _cursor;
    private ImageView _matrixView;
    private Button _submitButton;
    private String[] images;
    private int imageCounter;
    private DotMatrix currentImage;
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

        Intent i = getIntent();
        Bundle extras = i.getExtras();

        if (extras != null) {
            images = i.getStringArrayExtra("images");

            List<String> imageList = Arrays.asList(images);
            Collections.shuffle(imageList);
            imageList.toArray(images);
        }
        imageCounter = 0;

        setContentView(R.layout.activity_cursor_select);

        mVisible = true;
        mContentView = findViewById(R.id.dot_matrix);

        _cursor = findViewById(R.id.mousePointer);
        _matrixView = findViewById(R.id.dot_matrix);
        _submitButton = findViewById(R.id.two_d_submit);

        String firstImage = images[0];
        this.currentImage = new DotMatrix(firstImage);

        loadImage(this.currentImage);
        this.data = initData(this.currentImage);

        startTime = System.nanoTime();

        _submitButton.setOnClickListener(submitButtonHandler);

        View trackPad = findViewById(R.id.track_pad);
        trackPad.setOnTouchListener(trackPadTouchListener);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.two_d_submit).setOnTouchListener(mDelayHideTouchListener);
    }

    private Data initData(DotMatrix dotMatrix) {
        Data data = new Data("cursor", dotMatrix);
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

    /*
     * Get top left corner of cursor svg
     */
    private Coordinates<Float> getCursorTopLeft() {
        float x = _cursor.getX();
        float y = _cursor.getY();
        return new Coordinates<>(x, y);
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

    private double distance(Coordinates<Float> coordinateOne, Coordinates<Float> coordinateTwo) {
        double xdiff = coordinateOne.getX() - coordinateTwo.getX();
        double ydiff = coordinateOne.getY() - coordinateTwo.getY();
        return Math.sqrt(Math.pow(xdiff, 2) + Math.pow(ydiff, 2));
    }

    private void loadImage(DotMatrix dotMatrix) {
        int id = getResources().getIdentifier(dotMatrix.getName(), "drawable", getPackageName());
        _matrixView.setImageResource(id);

    }

    /* Temporary */
    private void setCursorCoordinates(Coordinates<Float> newCoords) {
        _cursor.setX(newCoords.getX());
        _cursor.setY(newCoords.getY());
    }
    private final View.OnClickListener submitButtonHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            endTime = System.nanoTime();
            data.setTimeTaken(startTime, endTime);

            Coordinates<Float> viewDotCoords = currentImage.getViewCoordinates();
            Coordinates<Float> mouseCoords = getCursorTopLeft();

            double pixelDistance = distance(viewDotCoords, mouseCoords);
            double pixelDistanceFromCircle = pixelDistance < currentImage.getCircleWidth() ?
                    0 :
                    pixelDistance - (float)currentImage.getCircleWidth()/2;
            double normalizedDistanceFromCircle = pixelDistanceFromCircle/currentImage.getCircleWidth();

            data.setPixelDistance(pixelDistance);

            /* Send data to database */
            data.post();

            /* Log normalized distance */
            Data.processCircleDistance(normalizedDistanceFromCircle);

            imageCounter++;
            if (imageCounter >= images.length) {
                finish();
                return;
            }
            String nextImage = images[imageCounter];
            currentImage = new DotMatrix(nextImage);

            loadImage(currentImage);

            data = initData(currentImage);
            startTime = System.nanoTime();
        }
    };

    float oldMouseX, oldMouseY, dX, dY;
    private final View.OnTouchListener trackPadTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    oldMouseX = event.getRawX();
                    oldMouseY = event.getRawY();

                    break;

                case MotionEvent.ACTION_MOVE:
                    dX = event.getRawX() - oldMouseX;
                    dY = event.getRawY() - oldMouseY;
                    oldMouseX = event.getRawX();
                    oldMouseY = event.getRawY();

                    /* Make sure cursor within constraints */
                    float cursorHeight = _cursor.getHeight();
                    /* Don't let the cursor get too high */
                    float topConstraint = _matrixView.getTop() + (float)0.8 * cursorHeight;
                    float bottomConstraint = _matrixView.getBottom();
                    float leftConstraint = _matrixView.getLeft();
                    float  rightConstraint = _matrixView.getRight();

                    float newCursorX = _cursor.getX() + dX;
                    float newCursorY = _cursor.getY() + dY;

                    boolean movementAllowed = newCursorX >= leftConstraint && newCursorX <= rightConstraint &&
                            newCursorY >= topConstraint && newCursorY <= bottomConstraint;

                    if (true || movementAllowed) {
                        _cursor.animate()
                                .x(newCursorX)
                                .y(newCursorY)
                                .setDuration(0)
                                .start();
                    }
                    break;
                default:
                    return false;
            }
            return true;
        }
    };
}
