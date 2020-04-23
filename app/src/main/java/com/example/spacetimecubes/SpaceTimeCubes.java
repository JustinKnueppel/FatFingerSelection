package com.example.spacetimecubes;

import android.app.Application;
import android.content.Context;

public class SpaceTimeCubes extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        SpaceTimeCubes.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return SpaceTimeCubes.context;
    }
}
