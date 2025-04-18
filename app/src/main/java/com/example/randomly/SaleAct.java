package com.example.randomly;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class SaleAct extends AppCompatActivity {

    private static final int IMAGE_PICK_CODE = 1001;

    private ImageView itemImage;
    private EditText titleInput, descriptionInput;
    private Spinner conditionSpinner;
    private Button uploadImageBtn, submitBtn;

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sale);

        itemImage = findViewById(R.id.itemImage);
        titleInput = findViewById(R.id.titleInput);
        descriptionInput = findViewById(R.id.descriptionInput);
        conditionSpinner = findViewById(R.id.conditionSpinner);
        uploadImageBtn = findViewById(R.id.uploadImageBtn);
        submitBtn = findViewById(R.id.submitBtn);

        findViewById(R.id.bagIcon).setOnClickListener(v -> startActivity(new Intent(SaleAct.this, BagAct.class)));
        findViewById(R.id.settingsIcon).setOnClickListener(v -> startActivity(new Intent(SaleAct.this, SettAct.class)));
        findViewById(R.id.profileIcon).setOnClickListener(v -> startActivity(new Intent(SaleAct.this, LikeAct.class)));
        findViewById(R.id.im1).setOnClickListener(v -> startActivity(new Intent(SaleAct.this, MailAct.class)));
        findViewById(R.id.home).setOnClickListener(v -> startActivity(new Intent(SaleAct.this, Activity3.class)));

        // Setup spinner values
        Spinner conditionSpinner = findViewById(R.id.conditionSpinner);

        String[] conditions = {"Excellent", "Good", "Average", "Poor"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, conditions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conditionSpinner.setAdapter(adapter);

        uploadImageBtn.setOnClickListener(v -> openGallery());

        submitBtn.setOnClickListener(v -> {
            String title = titleInput.getText().toString().trim();
            String description = descriptionInput.getText().toString().trim();
            String condition = conditionSpinner.getSelectedItem().toString();

            if (title.isEmpty() || description.isEmpty() || imageUri == null) {
                Toast.makeText(this, "Please fill all fields and upload an image.", Toast.LENGTH_SHORT).show();
                return;
            }


            Toast.makeText(this,
                    "We received your offer. If our company accepts, we'll contact you on your registered phone number.",
                    Toast.LENGTH_LONG).show();
        });

    }
// to load an image
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                itemImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
