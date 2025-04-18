package com.example.randomly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Activity2 extends AppCompatActivity {

    private static final String NAME_KEY = "Name";
    private static final String ADDRESS_KEY = "Address";
    private static final String PHONE_KEY = "Phone";
    private static final String FLAG_KEY = "flag";

    private boolean flag = false;

    private EditText user;
    private EditText address;
    private EditText phone;
    private CheckBox rem;
    private Button loginButton;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_2);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupViews();
        setupSharedPrefs();
        checkPrefs();

        // Set  for login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLoginOnClick(v);
            }
        });
    }

    private void setupViews() {
        user = findViewById(R.id.user);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.phone);
        rem = findViewById(R.id.rem);
        loginButton = findViewById(R.id.loginb);
    }

    private void setupSharedPrefs() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
    }

    private void checkPrefs() {
        flag = sharedPreferences.getBoolean(FLAG_KEY, false);
        if (flag) {
            user.setText(sharedPreferences.getString(NAME_KEY, ""));
            address.setText(sharedPreferences.getString(ADDRESS_KEY, ""));
            phone.setText(sharedPreferences.getString(PHONE_KEY, ""));
            rem.setChecked(true);
        }
    }

    public void btnLoginOnClick(View view) {
        String na = user.getText().toString();
        String ad = address.getText().toString();
        String ph = phone.getText().toString();

        if (rem.isChecked()) {
            editor.putString(NAME_KEY, na);
            editor.putString(ADDRESS_KEY, ad);
            editor.putString(PHONE_KEY, ph);
            editor.putBoolean(FLAG_KEY, true);
            editor.commit();
        } else {
            editor.clear();
            editor.commit();
        }


        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        prefs.edit().putBoolean("isLoggedIn", true).apply();

        Toast.makeText(Activity2.this, "Logged in successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(Activity2.this, Activity3.class);
        startActivity(intent);
        finish();
    }
}
