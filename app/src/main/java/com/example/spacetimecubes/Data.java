package com.example.spacetimecubes;

import android.util.Log;

public class Data {
    public static void processPixelDistance(double pixelDistance) {
        Log.d("Missed By", String.format("%f pixels away \n", pixelDistance));

    }
    public static void processCircleDistance(double circleDistance) {
        Log.d("Missed By", String.format("%f circles away \n", circleDistance));
    }
}
