package com.example.spacetimecubes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    String[] images = {"dots_0101", "dots_0106", "dots_0116", "dots_0302", "dots_0313", "dots_0706", "dots_0711", "dots_0901", "dots_0909", "dots_0916", "dots_1207", "dots_1315", "dots_1403", "dots_1601", "dots_1611", "dots_1616"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
