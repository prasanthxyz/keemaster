package com.pp.keemaster;

import com.google.gson.Gson;

import org.linguafranca.pwdb.kdbx.KdbxCreds;
import org.linguafranca.pwdb.kdbx.simple.SimpleDatabase;
import org.linguafranca.pwdb.kdbx.simple.SimpleEntry;
import org.linguafranca.pwdb.kdbx.simple.SimpleGroup;

import java.io.FileInputStream;
import java.util.ArrayList;

public class KpDbReader {
    private final ArrayList<String> sectionNames;
    private final Sections sections;

    public KpDbReader(FileInputStream inputStream, String password) throws Exception {
        KdbxCreds creds = new KdbxCreds(password.getBytes());
        SimpleDatabase db = SimpleDatabase.load(creds, inputStream);

        this.sectionNames = new ArrayList<>();
        this.sections = new Sections();
        for (SimpleGroup group : db.getRootGroup().getGroups()) {
            this.sectionNames.add(group.getName());

            SectionData sectionData = new SectionData();
            for (SimpleEntry entry : group.getEntries()) {
                sectionData.addEntry(entry);
            }
            this.sections.addSectionData(sectionData);
        }
    }

    public String getSectionsDataJson() {
        return new Gson().toJson(this.sections);
    }

    public ArrayList<String> getSectionNames() {
        return this.sectionNames;
    }
}
