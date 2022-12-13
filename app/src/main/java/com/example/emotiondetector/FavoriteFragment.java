package com.example.emotiondetector;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.emotiondetector.SurahActivity.SurahMulk;
import com.example.emotiondetector.SurahActivity.SurahYaseen;


public class FavoriteFragment extends Fragment {

    ListView listView;
    String [] Suarh = {"Surah Yaseen" ,"Surah Mulk", "Surah Fatiha", "Surah Waqiah" , "Surah Kafiroon", "Surah Ikhlas",
            "Surah Falak", "Surah Nas"};
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_favorite, container, false);

        listView = view.findViewById(R.id.list);
        ArrayAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, Suarh);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView , View view, int i, long l) {
                if (i == 0) {
                    Intent intent = new Intent(getActivity(), SurahYaseen.class);
                    startActivity(intent);
                }else if (i == 1) {
                    Intent intent = new Intent(getActivity(), SurahMulk.class);
                    startActivity(intent);
                }else if (i == 2) {
                    Intent intent = new Intent(getActivity(), SurahFatiha.class);
                    startActivity(intent);
                }else if (i == 3) {
                    Intent intent = new Intent(getActivity(), SurahWaqiah.class);
                    startActivity(intent);
                }else if (i == 4) {
                    Intent intent = new Intent(getActivity(), SurahKafiroon.class);
                    startActivity(intent);
                }else if (i == 5) {
                    Intent intent = new Intent(getActivity(), SurahIkhlas.class);
                    startActivity(intent);
                }else if (i == 6) {
                    Intent intent = new Intent(getActivity(), SurahFalak.class);
                    startActivity(intent);
                }else if (i == 7) {
                    Intent intent = new Intent(getActivity(), SurahNas.class);
                    startActivity(intent);
                }
            }
        });
        return view;

    }
}