package com.pp.keemaster;

import static android.os.Build.VERSION.SDK_INT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.ensurePermissions();

        setContentView(R.layout.activity_main);

        Button loadDbButton = findViewById(R.id.btnLoadDb);
        loadDbButton.setOnClickListener(v -> new LoadDbTask(MainActivity.this).execute());
    }

    private class LoadDbTask {
        private final Activity activity;
        private KpDbReader kpDbReader;
        public final ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);

        public LoadDbTask(Activity activity) {
            this.activity = activity;
        }

        private void startBackground() {
            this.pdLoading.setMessage("\tLoading...");
            this.pdLoading.show();

            new Thread(() -> {
                doInBackground();
                activity.runOnUiThread(this::onPostExecute);
            }).start();
        }

        public void execute() {
            startBackground();
        }

        public void doInBackground() {
            File baseDir = new File(Environment.getExternalStorageDirectory(), "KeeMaster");
            File keePassDb = new File(baseDir, "db.kdbx");
            EditText dbPasswordInput = findViewById(R.id.dbPassword);
            String dbPassword = dbPasswordInput.getText().toString();
            try {
                this.kpDbReader = new KpDbReader(new FileInputStream(keePassDb), dbPassword);
            } catch (Exception e) {
                this.kpDbReader = null;
            }
        }

        public void onPostExecute() {
            this.pdLoading.dismiss();
            TextView textView = findViewById(R.id.homeErrorMsg);
            if (this.kpDbReader == null) {
                textView.setVisibility(View.VISIBLE);
            } else {
                ((EditText)findViewById(R.id.dbPassword)).setText("");
                textView.setVisibility(View.INVISIBLE);
                Intent sectionsIntent = new Intent(MainActivity.this, SectionsActivity.class);
                sectionsIntent.putStringArrayListExtra("sectionNames", this.kpDbReader.getSectionNames());
                sectionsIntent.putExtra("sectionsJson", this.kpDbReader.getSectionsDataJson());
                startActivity(sectionsIntent);
            }
        }
    }

    private void ensurePermissions() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            this.ensurePermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE);
        } else {
            this.ensurePermissions(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            Uri uri = Uri.fromParts("package", this.getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
    }

    private void ensurePermissions(String... permissions) {
        for (int i = 0; i < permissions.length; i++) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{permissions[i]}, i);
            }
        }
    }
}