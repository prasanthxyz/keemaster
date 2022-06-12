package com.pp.keemaster;

import java.util.ArrayList;
import java.util.List;

public class Sections {
    public List<SectionData> data;

    public Sections() {
        this.data = new ArrayList<>();
    }

    public void addSectionData(SectionData sectionData) {
        this.data.add(sectionData);
    }
}
