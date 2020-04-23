package com.example.spacetimecubes;

import android.content.Context;
import android.util.DisplayMetrics;

public class DotMatrix {
    private enum MatrixType {SPREAD, BIG, DENSE};

    private static final float PIXELS_PER_DP = getPixelsPerDp(SpaceTimeCubes.getAppContext());

    private String name;
    private MatrixType type;
    private Coordinates<Integer> target;
    private float margin;
    private float circleWidth;
    private float spaceBetween;

    public DotMatrix(String name) {
        this.name = name;
        this.type = getType(name);
        this.target = getTargetCoordinates(name);
        this.margin = getMargins(this.type);
        this.circleWidth = getCircleRadius(this.type);
        this.spaceBetween = getSpaceBetween(this.type);
    }

    public Coordinates<Float> getViewCoordinates() {
        float x = this.margin + (this.target.getX() - 1) * (this.circleWidth + this.spaceBetween) + this.circleWidth / 2;
        float y = this.margin + (this.target.getY() - 1) * (this.circleWidth + this.spaceBetween) + this.circleWidth / 2;


        /* Scale for pixel density */
        float viewX = x * DotMatrix.PIXELS_PER_DP;
        float viewY = y * DotMatrix.PIXELS_PER_DP;

        return new Coordinates<>(viewX, viewY);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type.toString().toLowerCase();
    }

    public float getCircleWidth() {
        return circleWidth * PIXELS_PER_DP;
    }

    public float getSpaceBetween() {
        return spaceBetween * PIXELS_PER_DP;
    }

    private static MatrixType getType(String name) {
        if (name.contains("spread")) {
            return MatrixType.SPREAD;
        } else if (name.contains("big")) {
            return MatrixType.BIG;
        } else {
            return MatrixType.DENSE;
        }
    }

    private static float getPixelsPerDp(Context context){
        return (float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT;
    }

    private static Coordinates<Integer> getTargetCoordinates(String name) {
        int numberIndex = 0;
        while (!Character.isDigit(name.charAt(numberIndex)) && numberIndex < name.length()) {
            numberIndex++;
        }

        int x = Integer.parseInt(name.substring(numberIndex, numberIndex + 2));
        int y = Integer.parseInt(name.substring(numberIndex + 2, numberIndex + 4));

        return new Coordinates<>(x, y);
    }

    private static int getMargins(MatrixType matrixType) {
        switch (matrixType) {
            case SPREAD:
                return 15;
            case BIG:
                return 10;
            case DENSE:
                return 5;
            default:
                return 0;
        }
    }

    private static int getCircleRadius(MatrixType matrixType) {
        switch(matrixType) {
            case BIG:
                return 20;
            default:
                return 10;
        }
    }

    private static int getSpaceBetween(MatrixType matrixType) {
        switch (matrixType) {
            case SPREAD:
                return 30;
            case BIG:
                return 20;
            case DENSE:
                return 10;
            default:
                return 0;
        }
    }
}
