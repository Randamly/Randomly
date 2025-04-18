package com.example.randomly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class BagAct extends AppCompatActivity {

    private ImageView homeButton;
    private LinearLayout cartContainer;
    private TextView totalText;
    private SharedPreferences prefs;
    private static final String PREFS_NAME = "prefs_name";
    private static final String PRODUCT_DATA_KEY = "product_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bag);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cartContainer = findViewById(R.id.cartContainer);
        totalText = findViewById(R.id.totalText);
        homeButton = findViewById(R.id.home);
        Button confirmBuyButton = findViewById(R.id.confirmBuyButton);

        // Home button
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(BagAct.this, Activity3.class);
            startActivity(intent);
            finish();
        });
        findViewById(R.id.settingsIcon).setOnClickListener(v -> startActivity(new Intent(BagAct.this, SettAct.class)));
        findViewById(R.id.profileIcon).setOnClickListener(v -> startActivity(new Intent(BagAct.this, LikeAct.class)));
        findViewById(R.id.im1).setOnClickListener(v -> startActivity(new Intent(BagAct.this, MailAct.class)));

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        loadCartItems();

        confirmBuyButton.setOnClickListener(v -> {
            int total = getCartTotal();
            if (total == 0) {
                showToast("Your cart is empty!");
            } else {
                Intent intent = new Intent(BagAct.this, MsgAct.class);
                intent.putExtra("cart_total", total);
                startActivity(intent);
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private int getCartTotal() {
        String jsonData = prefs.getString(PRODUCT_DATA_KEY, "");
        int total = 0;

        if (!jsonData.isEmpty()) {
            Type listType = new TypeToken<List<Product>>() {}.getType();
            List<Product> products = new Gson().fromJson(jsonData, listType);
            for (Product product : products) {
                if (product.getBought() > 0) {
                    int price = getPrice(product.getName());
                    total += price * product.getBought();
                }
            }
        }

        return total;
    }

    private void loadCartItems() {
        cartContainer.removeAllViews();
        String jsonData = prefs.getString(PRODUCT_DATA_KEY, "");
        int total = 0;

        if (!jsonData.isEmpty()) {
            Type listType = new TypeToken<List<Product>>() {}.getType();
            List<Product> products = new Gson().fromJson(jsonData, listType);
            for (Product product : products) {
                if (product.getBought() > 0) {
                    int price = getPrice(product.getName());
                    total += price * product.getBought();
                    addItemView(product.getName(), product.getBought(), price);
                }
            }
        }

        totalText.setText("Total: $" + total);
    }

    private void addItemView(String name, int bought, int price) {
        View itemView = getLayoutInflater().inflate(R.layout.cart_item_layout, null);

        TextView nameText = itemView.findViewById(R.id.itemName);
        TextView priceText = itemView.findViewById(R.id.itemPrice);
        TextView quantityText = itemView.findViewById(R.id.itemQuantity);
        ImageView removeBtn = itemView.findViewById(R.id.removeBtn);

        nameText.setText(name);
        priceText.setText("Price: $" + price);
        quantityText.setText("Qty: " + bought);

        removeBtn.setOnClickListener(v -> {
            restoreQuantity(name, bought);
            loadCartItems();
        });

        cartContainer.addView(itemView);
    }

    private void restoreQuantity(String name, int bought) {
        String jsonData = prefs.getString(PRODUCT_DATA_KEY, "");
        if (!jsonData.isEmpty()) {
            Type listType = new TypeToken<List<Product>>() {}.getType();
            List<Product> products = new Gson().fromJson(jsonData, listType);

            for (Product product : products) {
                if (product.getName().equals(name)) {
                    product.setQuantity(product.getQuantity() + bought); // back the bought quantity
                    product.setBought(0); // Reset bought quantity
                    break;
                }
            }

            String updatedJson = new Gson().toJson(products);
            prefs.edit().putString(PRODUCT_DATA_KEY, updatedJson).apply();
            showToast(name + " has been removed from cart");
        }
    }
    private int getBoughtAmount(String name) {
        String jsonData = prefs.getString(PRODUCT_DATA_KEY, "");
        if (!jsonData.isEmpty()) {
            Type listType = new TypeToken<List<Product>>() {}.getType();
            List<Product> products = new Gson().fromJson(jsonData, listType);

            for (Product product : products) {
                if (product.getName().equals(name)) {
                    return product.getBought();
                }
            }
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
                return 56;
            case "drawing":
                return 70;
            case "candle":
                return 16;
            case "omar":
                return 9000000;
            case "coin":
                return 100;
            case "titanic":
                return 300000;
            default:
                return 20;
        }
    }

    private int getOriginalStock(String name) {
        return 1;
    }
}
