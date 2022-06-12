package com.pp.keemaster;

import org.linguafranca.pwdb.kdbx.simple.SimpleEntry;

import java.util.ArrayList;
import java.util.List;

public class SectionData {
    public List<String[]> entries;

    public SectionData() {
        this.entries = new ArrayList<>();
    }

    public void addEntry(SimpleEntry entry) {
        String[] entryData = new String[2];
        entryData[0] = entry.getTitle();
        entryData[1] = entry.getPassword();
        this.entries.add(entryData);
    }
}
