package com.example.emotiondetector;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.emotiondetector.SurahActivity.SurahMulk;
import com.example.emotiondetector.SurahActivity.SurahYaseen;

public class QuranActivity extends AppCompatActivity {

    ListView listView;
    String [] Suarh = {"Surah Yaseen" ,"Surah Mulk", "Surah Fatiha", "Surah Waqiah" , "Surah Kafiroon", "Surah Ikhlas",
                            "Surah Falak", "Surah Nas"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quran);

        listView = findViewById(R.id.list);
        ArrayAdapter adapter = new ArrayAdapter<>(QuranActivity.this, android.R.layout.simple_list_item_1, Suarh);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView , View view, int i, long l) {
                if (i == 0) {
                    Intent intent = new Intent(QuranActivity.this, SurahYaseen.class);
                    startActivity(intent);
                }else if (i == 1) {
                    Intent intent = new Intent(QuranActivity.this, SurahMulk.class);
                    startActivity(intent);
                }else if (i == 2) {
                    Intent intent = new Intent(QuranActivity.this, SurahFatiha.class);
                    startActivity(intent);
                }else if (i == 3) {
                    Intent intent = new Intent(QuranActivity.this, SurahWaqiah.class);
                    startActivity(intent);
                }else if (i == 4) {
                    Intent intent = new Intent(QuranActivity.this, SurahKafiroon.class);
                    startActivity(intent);
                }else if (i == 5) {
                    Intent intent = new Intent(QuranActivity.this, SurahIkhlas.class);
                    startActivity(intent);
                }else if (i == 6) {
                    Intent intent = new Intent(QuranActivity.this, SurahFalak.class);
                    startActivity(intent);
                }else if (i == 7) {
                    Intent intent = new Intent(QuranActivity.this, SurahNas.class);
                    startActivity(intent);
                }
            }
        });
    }

}