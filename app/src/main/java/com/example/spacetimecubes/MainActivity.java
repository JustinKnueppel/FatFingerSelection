package com.example.spacetimecubes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int currentImage = 0;
    int[] images = {R.drawable.dots_0101, R.drawable.dots_0106, R.drawable.dots_0116, R.drawable.dots_0302, R.drawable.dots_0313, R.drawable.dots_0706, R.drawable.dots_0711, R.drawable.dots_0901, R.drawable.dots_0909, R.drawable.dots_0916, R.drawable.dots_1207, R.drawable.dots_1315, R.drawable.dots_1403, R.drawable.dots_1601, R.drawable.dots_1611, R.drawable.dots_1616};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hello World Button
        Button helloWorld = (Button) findViewById(R.id.pushMe);

        // Implement click function
        helloWorld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Hello World!", Toast.LENGTH_SHORT)
                        .show();
            }
        });

        /* Start 2D Activity */
        Button start2D = (Button) findViewById(R.id.start_2d);

        start2D.setOnClickListener((v) -> {
            Intent intent = new Intent(this, TwoDSelectActivity.class);
            startActivity(intent);
        });

        Button startTouchSelect = (Button) findViewById(R.id.start_touch_select);

        startTouchSelect.setOnClickListener(v -> {
            Intent intent = new Intent(this, TouchSelectActivity.class);
            startActivity(intent);
        });
    }

}
