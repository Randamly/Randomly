package com.example.randomly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MsgAct extends AppCompatActivity {

    private TextView totalText;
    private SharedPreferences prefs;
    private static final String PREFS_NAME = "prefs_name";
    private static final String PRODUCT_DATA_KEY = "product_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_msg);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        totalText = findViewById(R.id.totalText);

        Button confirmButton = findViewById(R.id.confirmButton);


        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);


        int total = getIntent().getIntExtra("cart_total", 0);
        totalText.setText("Total: $" + total);


        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v -> {
            Intent intent = new Intent(MsgAct.this, BagAct.class);
            startActivity(intent);
            finish();
        });



        confirmButton.setOnClickListener(v -> {
            Toast.makeText(MsgAct.this, "Order confirmed!", Toast.LENGTH_SHORT).show();


            clearCart();
            totalText.setText("Total: $0");
        });
    }

    private int getCartTotal() {
        String jsonData = prefs.getString(PRODUCT_DATA_KEY, "");
        int total = 0;

        if (!jsonData.isEmpty()) {
            try {
                JSONArray productsArray = new JSONArray(jsonData);
                for (int i = 0; i < productsArray.length(); i++) {
                    JSONObject product = productsArray.getJSONObject(i);
                    String name = product.getString("name");
                    int quantity = product.getInt("quantity");
                    int bought = getBoughtAmount(name, quantity);
                    if (bought > 0) {
                        int price = getPrice(name);
                        total += price * bought;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return total;
    }

    private int getBoughtAmount(String name, int savedQty) {
        try {
            String jsonData = prefs.getString(PRODUCT_DATA_KEY, "");
            JSONArray productsArray = new JSONArray(jsonData);
            for (int i = 0; i < productsArray.length(); i++) {
                JSONObject product = productsArray.getJSONObject(i);
                if (product.getString("name").equals(name)) {
                    return product.getInt("bought");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getPrice(String name) {
        switch (name) {
            case "T-Shirt":
                return 30;
            case "Jeans":
                return 50;
            case "Shoe":
                return 40;
            default:
                return 20;
        }
    }


    private void clearCart() {
        try {
            String jsonData = prefs.getString(PRODUCT_DATA_KEY, "");
            JSONArray productsArray = new JSONArray(jsonData);
            for (int i = 0; i < productsArray.length(); i++) {
                JSONObject product = productsArray.getJSONObject(i);
                product.put("bought", 0);
                product.put("quantity", 1);
            }
            prefs.edit().putString(PRODUCT_DATA_KEY, productsArray.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
