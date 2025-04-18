package com.example.randomly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SettAct extends AppCompatActivity {

    private EditText user, address, phone;
    private Button saveBtn, logoutBtn ;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String NAME_KEY = "Name";
    private static final String ADDRESS_KEY = "Address";
    private static final String PHONE_KEY = "Phone";
    private static final String FLAG_KEY = "flag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sett);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settingsMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        user = findViewById(R.id.userEdit);
        address = findViewById(R.id.addressEdit);
        phone = findViewById(R.id.phoneEdit);
        saveBtn = findViewById(R.id.saveButton);
        logoutBtn = findViewById(R.id.logoutButton);
        findViewById(R.id.home).setOnClickListener(v -> startActivity(new Intent(SettAct.this, Activity3.class)));


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();


        if (sharedPreferences.getBoolean(FLAG_KEY, false)) {
            user.setText(sharedPreferences.getString(NAME_KEY, ""));
            address.setText(sharedPreferences.getString(ADDRESS_KEY, ""));
            phone.setText(sharedPreferences.getString(PHONE_KEY, ""));
        }

        // Save button to update user info
        saveBtn.setOnClickListener(v -> {
            editor.putString(NAME_KEY, user.getText().toString());
            editor.putString(ADDRESS_KEY, address.getText().toString());
            editor.putString(PHONE_KEY, phone.getText().toString());
            editor.putBoolean(FLAG_KEY, true);
            editor.commit();

            Toast.makeText(SettAct.this, "Info updated", Toast.LENGTH_SHORT).show();
        });


        logoutBtn.setOnClickListener(v -> {

            editor.clear();
            editor.commit();


            SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            prefs.edit().putBoolean("isLoggedIn", false).apply();


            Toast.makeText(SettAct.this, "Logged out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettAct.this, Activity2.class));
            finish();
        });
    }
}
