package com.example.randomly;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MailAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.bagIcon).setOnClickListener(v -> startActivity(new Intent(MailAct.this, BagAct.class)));
        findViewById(R.id.settingsIcon).setOnClickListener(v -> startActivity(new Intent(MailAct.this, SettAct.class)));
        findViewById(R.id.profileIcon).setOnClickListener(v -> startActivity(new Intent(MailAct.this, LikeAct.class)));

        findViewById(R.id.home).setOnClickListener(v -> startActivity(new Intent(MailAct.this, Activity3.class)));

    }
}