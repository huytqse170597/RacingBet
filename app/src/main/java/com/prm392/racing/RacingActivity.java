package com.prm392.racing;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RacingActivity extends AppCompatActivity {

    private ImageView horse1, horse2, horse3, finishLine;
    private Button btnStart, btnBack, btnBet;
    private TextView tvResult, tvCoins;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Random random = new Random(System.currentTimeMillis());
    private boolean isRaceRunning = false, isBet = false;
    private int totalCoins = 100; // Số xu ban đầu
    private int currentTotalBet = 0;
    private Map<Integer, Integer> betHorsesMap = new HashMap<>(); // số xu cược cua tung ngua
    private List<Float> horsePositions = new ArrayList<>(); // vị trí
    private List<Integer> finishOrder = new ArrayList<>(); // xep hang
    private float finishLineX; // Vị trí X của vạch đích

    private static final int REQUEST_BET = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_racing);

        horse1 = findViewById(R.id.horse1);
        horse2 = findViewById(R.id.horse2);
        horse3 = findViewById(R.id.horse3);
        finishLine = findViewById(R.id.finishline);
        btnStart = findViewById(R.id.btnStart);
        btnBack = findViewById(R.id.btnBack);
        tvResult = findViewById(R.id.tvResult);
        tvCoins = findViewById(R.id.tvCoins);
        btnBet = findViewById(R.id.btnBet);


        updateCoinDisplay();
        if (!isBet) {
            btnStart.setVisibility(View.GONE);
        }


//        betHorsesMap.put(1, 50);
//        betHorsesMap.put(2, 40);
//        betHorsesMap.put(3, 30);

        finishLine.post(() -> {
            finishLineX = finishLine.getX() + finishLine.getWidth();
            Log.d("FinishLine", "Vị trí X của vạch đích: " + finishLineX);
        });

        btnStart.setOnClickListener(v -> {
            if (!isRaceRunning) {
                totalCoins -= currentTotalBet;
                currentTotalBet = 0;
                startRace();
            }
        });

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(RacingActivity.this, MainActivity.class);
            intent.putExtra("TOTAL_COINS", totalCoins);
            betHorsesMap.clear();
            startActivity(intent);
        });

        //gửi số tiền cược sang BetActivity để bet
        btnBet.setOnClickListener(v -> {
            Intent intent = new Intent(this, BetActivity.class);
            intent.putExtra("TOTAL_COINS", totalCoins);
            intent.putExtra("BET_MAP", (Serializable) betHorsesMap);
            intent.putExtra("TOTAL_BET", currentTotalBet);
            startActivityForResult(intent, REQUEST_BET);
        });

    }

    //Nhận data bet trả về
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            currentTotalBet = data.getIntExtra("TOTAL_BET", 0);
            HashMap<Integer, Integer> newBets = (HashMap<Integer, Integer>) data.getSerializableExtra("BET_MAP");
            if (newBets != null) {
                betHorsesMap.clear();
                betHorsesMap.putAll(newBets);
                Toast.makeText(this, "Đã đặt cược thành công!", Toast.LENGTH_SHORT).show();
                btnStart.setVisibility(View.VISIBLE);
                isBet = true;
            }
        }
    }

    private void startRace() {
        isRaceRunning = true;
        btnStart.setEnabled(false);
        tvResult.setText("Đang đua...");
        finishOrder.clear();
        horsePositions.clear();
        horsePositions.add(0f);
        horsePositions.add(0f);
        horsePositions.add(0f);

        horse1.setTranslationX(0);
        horse2.setTranslationX(0);
        horse3.setTranslationX(0);

        float baseSpeed1 = 5.0f + random.nextFloat() * 2.0f;
        float baseSpeed2 = 5.0f + random.nextFloat() * 2.0f;
        float baseSpeed3 = 5.0f + random.nextFloat() * 2.0f;

        float[] horseModifiers = {1.0f, 1.0f, 1.0f}; // yếu tố bên ngoài
        int[] stamina = {100, 100, 100}; // Độ bền đồng đều cho mọi ngựa

        Runnable raceRunnable = new Runnable() {
            @Override
            public void run() {
                boolean raceFinished = false;
                for (int i = 0; i < horsePositions.size(); i++) {
                    if (horsePositions.get(i) >= finishLineX) {
                        if (!finishOrder.contains(i + 1)) {
                            finishOrder.add(i + 1);
                            Log.d("HorseFinish", "Ngựa " + (i + 1) + " đã về đích tại vị trí: " + horsePositions.get(i));
                        }
                        if (finishOrder.size() >= 2) {
                            raceFinished = true;
                            break;
                        }
                    }
                }

                if (!raceFinished) {
                    for (int i = 0; i < horsePositions.size(); i++) {
                        if (!finishOrder.contains(i + 1)) {
                            if (random.nextFloat() < 0.05) { // 5% cơ hội thay đổi trạng thái
                                float eventType = random.nextFloat();
                                if (eventType < 0.4f && stamina[i] > 20) { // 40% mệt mỏi
                                    horseModifiers[i] = 0.7f;
                                    stamina[i] -= 10;
                                } else if (eventType < 0.7f) { // 30% bứt tốc
                                    horseModifiers[i] = 1.5f;
                                } else { // 30% trở lại bình thường
                                    horseModifiers[i] = 1.0f;

                                }
                            }

                            float step;
                            switch (i) {
                                case 0:
                                    step = baseSpeed1 * horseModifiers[i] + (random.nextFloat() * 0.5f - 0.25f);
                                    horsePositions.set(i, horsePositions.get(i) + step);
                                    horse1.setTranslationX(horsePositions.get(i));
                                    break;
                                case 1:
                                    step = baseSpeed2 * horseModifiers[i] + (random.nextFloat() * 0.5f - 0.25f);
                                    horsePositions.set(i, horsePositions.get(i) + step);
                                    horse2.setTranslationX(horsePositions.get(i));
                                    break;
                                case 2:
                                    step = baseSpeed3 * horseModifiers[i] + (random.nextFloat() * 0.5f - 0.25f);
                                    horsePositions.set(i, horsePositions.get(i) + step);
                                    horse3.setTranslationX(horsePositions.get(i));
                                    break;
                            }
                        }
                    }
                    handler.postDelayed(this, 50); // Cập nhật mỗi 50ms
                } else {
                    isRaceRunning = false;
                    btnStart.setEnabled(true);
                    calculateResults();
                    isBet = false;
                    btnStart.setVisibility(View.GONE);
                }
            }
        };

        handler.postDelayed(raceRunnable, 50);
    }

    private void calculateResults() {
        int winner = finishOrder.get(0);
        int runnerUp = finishOrder.get(1);
        tvResult.setText("VỀ NHẤT: Ngựa " + winner + " - VỀ NHÌ: Ngựa " + runnerUp);

        // Duyệt từng ngựa đã cược
        for (Map.Entry<Integer, Integer> entry : betHorsesMap.entrySet()) {
            int horseNumber = entry.getKey();
            int betAmount = entry.getValue();

            if (horseNumber == winner) {
                totalCoins += (int)(betAmount * 1.9);
            } else if (horseNumber == runnerUp) {
                totalCoins += betAmount;
            }

        }

        Log.d("RESULT", "Số xu sau race: " + totalCoins);
        updateCoinDisplay();
        betHorsesMap.clear();
        btnStart.setVisibility(View.GONE);
    }


    private void updateCoinDisplay() {
        tvCoins.setText("XU: " + totalCoins);
    }
}