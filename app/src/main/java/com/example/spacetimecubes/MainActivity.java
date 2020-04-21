package com.example.spacetimecubes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

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
