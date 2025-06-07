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

public class TutorialActivity extends AppCompatActivity {

    private Button btnBackHome;
    private MediaPlayer tutorTonePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tutorial);

        tutorTonePlayer = MediaPlayer.create(this, R.raw.nhac_dua_ngua);
        tutorTonePlayer.setLooping(true);
        tutorTonePlayer.start();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tutorial), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnBackHome = findViewById(R.id.button_back);

        btnBackHome.setOnClickListener(v -> {
            Intent intent = new Intent(TutorialActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tutorTonePlayer != null) {
            if (tutorTonePlayer.isPlaying()) {
                tutorTonePlayer.stop();
            }
            tutorTonePlayer.release();
            tutorTonePlayer = null;
        }
    }
}
