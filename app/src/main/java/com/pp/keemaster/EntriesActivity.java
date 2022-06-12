package com.pp.keemaster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

public class EntriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entries);

        String sectionName = getIntent().getStringExtra("sectionName");
        Objects.requireNonNull(getSupportActionBar()).setTitle(sectionName);

        this.displayEntries();
    }

    private void displayEntries() {
        String sectionDataJson = getIntent().getStringExtra("sectionData");
        SectionData sectionData = new Gson().fromJson(sectionDataJson, SectionData.class);

        ArrayList<String> keys = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();
        for (String[] entry : sectionData.entries) {
            keys.add(entry[0]);
            values.add(entry[1]);
        }

        ArrayAdapter<String> keysAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, keys);
        ArrayAdapter<String> valuesAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, values);

        ListView keysLv = findViewById(R.id.entryKeysListView);
        keysLv.setAdapter(keysAdapter);
        ListView valuesLv = findViewById(R.id.entryValuesListView);
        valuesLv.setAdapter(valuesAdapter);
    }
}