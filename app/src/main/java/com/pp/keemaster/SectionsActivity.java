package com.pp.keemaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

public class SectionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sections);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Sections");

        this.displaySections();
    }

    private void displaySections() {
        ArrayList<String> sectionNames = getIntent().getStringArrayListExtra("sectionNames");
        String sectionsJson = getIntent().getStringExtra("sectionsJson");
        Sections sections = new Gson().fromJson(sectionsJson, Sections.class);

        ListView lv = findViewById(R.id.sectionsListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, sectionNames);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener((parent, view, position, id) -> {
            Intent entriesIntent = new Intent(this, EntriesActivity.class);
            entriesIntent.putExtra("sectionName", sectionNames.get(position));
            String sectionDataJson = new Gson().toJson(sections.data.get(position));
            entriesIntent.putExtra("sectionData", sectionDataJson);
            startActivity(entriesIntent);
        });
    }
}