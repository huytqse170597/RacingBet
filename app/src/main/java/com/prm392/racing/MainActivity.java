package com.prm392.racing;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button btnStart,btnGuide;
    private MediaPlayer loginTonePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        loginTonePlayer = MediaPlayer.create(this, R.raw.nhac_dua_ngua);
        loginTonePlayer.setLooping(true);
        loginTonePlayer.start();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnStart = findViewById(R.id.btnStart);
        btnGuide = findViewById(R.id.btnGuide);

        btnStart.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RacingActivity.class);
            startActivity(intent);
            finish();
        });

        btnGuide.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TutorialActivity.class);
            startActivity(intent);
            finish();
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loginTonePlayer != null) {
            if (loginTonePlayer.isPlaying()) {
                loginTonePlayer.stop();
            }
            loginTonePlayer.release();
            loginTonePlayer = null;
        }
    }


}