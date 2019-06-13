package com.example.custom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.custom.views.CustomView;

public class MainActivity extends AppCompatActivity {

    CustomView customView;
    Button btnPlus, btnMinus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customView = findViewById(R.id.valueBar);
        btnPlus = findViewById(R.id.plus);
        btnMinus = findViewById(R.id.minus);
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customView.setValue(customView.getCurrentValue()-10);
            }
        });
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customView.setValue(customView.getCurrentValue()+10);
            }
        });



    }
}
