package com.prm392.racing;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView tvResult = findViewById(R.id.tvResult);
        TextView tvBet = findViewById(R.id.tvBet);
        TextView tvCoins = findViewById(R.id.tvCoins);
        Button btnContinue = findViewById(R.id.btnContinue);
        Button btnQuit = findViewById(R.id.btnQuit);

        Intent intent = getIntent();
        String result = intent.getStringExtra("RESULT");
        int betChange = intent.getIntExtra("BET_CHANGE", 0);
        int totalCoins = intent.getIntExtra("TOTAL_COINS", 0);
        int totalBet = intent.getIntExtra("TOTAL_BET", 0);

        tvResult.setText(result);

        String betText;
        if (betChange == 0 && totalBet > 0) {
            betText = "-" + totalBet;
        } else {
            betText = "+" + betChange;
        }
        tvBet.setText(betText);
        tvCoins.setText("" + totalCoins);

        if (betChange > 0) {
            tvBet.setTextColor(Color.parseColor("#388E3C")); // xanh lá
        } else {
            tvBet.setTextColor(Color.parseColor("#D32F2F")); // đỏ
        }

        btnContinue.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("TOTAL_COINS", totalCoins);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        btnQuit.setOnClickListener(v -> {
            Intent mainIntent = new Intent(ResultActivity.this, MainActivity.class);
            mainIntent.putExtra("TOTAL_COINS", totalCoins);
            startActivity(mainIntent);
            finish();
        });
    }
}
