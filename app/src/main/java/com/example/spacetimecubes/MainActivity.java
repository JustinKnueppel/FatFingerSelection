package com.example.spacetimecubes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;

import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] denseImages = {"dots_0101", "dots_0106", "dots_0116", "dots_0302", "dots_0313", "dots_0706", "dots_0711", "dots_0901", "dots_0909", "dots_0916", "dots_1207", "dots_1315", "dots_1403", "dots_1601", "dots_1611", "dots_1616"};
        String[] bigImages = {"dots_0101_big", "dots_0108_big", "dots_0302_big", "dots_0307_big", "dots_0405_big", "dots_0603_big", "dots_0606_big", "dots_0801_big", "dots_0808_big"};
        String[] spreadImages = {"dots_0101_spread", "dots_0108_spread", "dots_0302_spread", "dots_0307_spread", "dots_0405_spread", "dots_0603_spread", "dots_0606_spread", "dots_0801_spread", "dots_0808_spread"};

        String[][] imageArrays = new String[][] {denseImages, bigImages, spreadImages};
        String[] images = Stream.of(imageArrays)
                .flatMap(Stream::of)
                .toArray(String[]::new);

        /* Start Cursor Select */
        Button startCursorSelect = (Button) findViewById(R.id.start_cursor);
        startCursorSelect.setOnClickListener((v) -> {
            Intent intent = new Intent(this, CursorSelectActivity.class);
            Bundle bundle = new Bundle();
            intent.putExtra("images", images);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        /* Start Touch Select */
        Button startTouchSelect = (Button) findViewById(R.id.start_touch_select);
        startTouchSelect.setOnClickListener(v -> {
            Intent intent = new Intent(this, TouchSelectActivity.class);
            Bundle bundle = new Bundle();
            intent.putExtra("images", images);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

}
