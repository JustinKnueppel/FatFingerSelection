package com.example.spacetimecubes;

import android.util.Log;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Data {
    private final String urlAddress = "hillcoat.ddnsfree.com";
    private final int port = 15544;

    private String selection_type;
    private long duration;
    private double distance_from_center;
    private float circle_radius;
    private float space_between;

    public Data(String selection_type, float circle_radius, float space_between) {
        this.selection_type = selection_type;
        this.circle_radius = circle_radius;
        this.space_between = space_between;
    }
    public void setPixelDistance(double pixelDistance) {
        this.distance_from_center = pixelDistance;
        Log.d("Missed By", String.format("%f pixels away \n", pixelDistance));

    }


    public void setTimeTaken(long startTime, long endTime) {
        this.duration = endTime - startTime;
        Log.d("Time taken", Long.toString((endTime - startTime)/1000000) + " milliseconds");
    }

    public static void processCircleDistance(double circleDistance) {
        Log.d("Missed By", String.format("%f circles away \n", circleDistance));
    }

    public void post() {
        assert this.selection_type != null: "Selection type can not be null";
        assert this.duration != 0: "Duration can not be null";
        assert this.distance_from_center != 0: "Distance from center type can not be null";
        assert this.circle_radius != 0: "Circle radius can not be null";
        assert this.space_between != 0: "Space between can not be null";

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https", urlAddress, port, "");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("selection_type", selection_type);
                    jsonParam.put("duration", duration);
                    jsonParam.put("distance_from_center", distance_from_center);
                    jsonParam.put("circle_radius", circle_radius);
                    jsonParam.put("space_between", space_between);

                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
}
