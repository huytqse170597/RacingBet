package com.prm392.racing;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class BetActivity extends AppCompatActivity {
    private CheckBox cbHorse1, cbHorse2, cbHorse3;
    private EditText etBet1, etBet2, etBet3;
    private Button btnConfirmBet, btnCancel;
    private TextView tvTotalCoins, tvTotalBet, tvTotalRemain;
    private int totalCoins;
    private int totalBet = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bet);

        cbHorse1 = findViewById(R.id.cbHorse1);
        cbHorse2 = findViewById(R.id.cbHorse2);
        cbHorse3 = findViewById(R.id.cbHorse3);

        etBet1 = findViewById(R.id.etBet1);
        etBet2 = findViewById(R.id.etBet2);
        etBet3 = findViewById(R.id.etBet3);

        btnConfirmBet = findViewById(R.id.btnConfirmBet);
        btnCancel = findViewById(R.id.btnCancel);
        tvTotalCoins = findViewById(R.id.tvTotalCoins);
        tvTotalBet = findViewById(R.id.tvTotalBet);
        tvTotalRemain = findViewById(R.id.tvTotalRemain);

        totalCoins = getIntent().getIntExtra("TOTAL_COINS", 0);
        totalBet = getIntent().getIntExtra("TOTAL_BET", 0);

        tvTotalCoins.setText(String.valueOf(totalCoins));
        tvTotalBet.setText(String.valueOf(totalBet));
        tvTotalRemain.setText(totalCoins - totalBet + "");

        // Check trạng thái cược đã đc hoàn thành hay chưa
        Map<Integer, Integer> currentBets = (Map<Integer, Integer>) getIntent().getSerializableExtra("BET_MAP");
        if (currentBets != null) {
            if (currentBets.containsKey(1)) {
                cbHorse1.setChecked(true);
                etBet1.setText(String.valueOf(currentBets.get(1)));
            }
            if (currentBets.containsKey(2)) {
                cbHorse2.setChecked(true);
                etBet2.setText(String.valueOf(currentBets.get(2)));
            }
            if (currentBets.containsKey(3)) {
                cbHorse3.setChecked(true);
                etBet3.setText(String.valueOf(currentBets.get(3)));
            }
        }

        // Gắn sự kiện update coins khi thay đổi input hoặc checkbox
        setupListeners();

        // Xử lý đặt cược
        btnConfirmBet.setOnClickListener(v -> {
            Map<Integer, Integer> newBets = new HashMap<>();

            if (cbHorse1.isChecked()) {
                String betValue = etBet1.getText().toString();
                if (betValue.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập số xu cho Ngựa 1!", Toast.LENGTH_SHORT).show();
                    return;
                }
                newBets.put(1, Integer.parseInt(betValue));
            }

            if (cbHorse2.isChecked()) {
                String betValue = etBet2.getText().toString();
                if (betValue.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập số xu cho Ngựa 2!", Toast.LENGTH_SHORT).show();
                    return;
                }
                newBets.put(2, Integer.parseInt(betValue));
            }

            if (cbHorse3.isChecked()) {
                String betValue = etBet3.getText().toString();
                if (betValue.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập số xu cho Ngựa 3!", Toast.LENGTH_SHORT).show();
                    return;
                }
                newBets.put(3, Integer.parseInt(betValue));
            }

            totalBet = 0;
            for (int value : newBets.values()) {
                totalBet += value;
            }

            if (newBets.isEmpty()) {
                Toast.makeText(this, "Bạn chưa chọn ngựa nào để cược!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (totalBet > totalCoins) {
                Toast.makeText(this, "Bạn không đủ xu!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Trả kết quả về
            Intent resultIntent = new Intent();
            resultIntent.putExtra("BET_MAP", new HashMap<>(newBets));
            resultIntent.putExtra("TOTAL_BET", totalBet);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        //handle Cancel
        btnCancel.setOnClickListener(v -> finish());

    }

    private void setupListeners() {
        // Checkbox change listener
        cbHorse1.setOnCheckedChangeListener((buttonView, isChecked) -> updateTotalBet());
        cbHorse2.setOnCheckedChangeListener((buttonView, isChecked) -> updateTotalBet());
        cbHorse3.setOnCheckedChangeListener((buttonView, isChecked) -> updateTotalBet());

        etBet1.addTextChangedListener(textWatcher);
        etBet2.addTextChangedListener(textWatcher);
        etBet3.addTextChangedListener(textWatcher);
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            updateTotalBet();
        }

        @Override
        public void afterTextChanged(Editable s) { }
    };

    private void updateTotalBet() {
        totalBet = 0;

        if (cbHorse1.isChecked()) {
            String bet = etBet1.getText().toString();
            if (!bet.isEmpty()) totalBet += Integer.parseInt(bet);
        }

        if (cbHorse2.isChecked()) {
            String bet = etBet2.getText().toString();
            if (!bet.isEmpty()) totalBet += Integer.parseInt(bet);
        }

        if (cbHorse3.isChecked()) {
            String bet = etBet3.getText().toString();
            if (!bet.isEmpty()) totalBet += Integer.parseInt(bet);
        }

        tvTotalBet.setText(String.valueOf(totalBet));
        tvTotalRemain.setText(totalCoins - totalBet + "");
    }

}
