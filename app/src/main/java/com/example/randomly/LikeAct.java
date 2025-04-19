package com.example.randomly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LikeAct extends AppCompatActivity {

    private static final String PREFS_NAME = "prefs_name";
    private static final String PRODUCT_DATA_KEY = "product_data";
    private static final String FAVORITES_KEY = "favorites_json";
    private static final String CART_ITEMS_KEY = "cart_items";

    private LinearLayout favoritesContainer;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_like);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        favoritesContainer = findViewById(R.id.favoritesContainer);

        loadFavorites();

        findViewById(R.id.bagIcon).setOnClickListener(v -> startActivity(new Intent(LikeAct.this, BagAct.class)));
        findViewById(R.id.settingsIcon).setOnClickListener(v -> startActivity(new Intent(LikeAct.this, SettAct.class)));
        findViewById(R.id.im1).setOnClickListener(v -> startActivity(new Intent(LikeAct.this, MailAct.class)));
        findViewById(R.id.home).setOnClickListener(v -> startActivity(new Intent(LikeAct.this, Activity3.class)));
    }

    private void loadFavorites() {
        favoritesContainer.removeAllViews();
        Gson gson = new Gson();

        // Load favorites list
        String favoritesJson = prefs.getString(FAVORITES_KEY, "[]");
        Type setType = new TypeToken<Set<String>>(){}.getType();
        Set<String> favorites = gson.fromJson(favoritesJson, setType);

        if (favorites == null || favorites.isEmpty()) {
            showToast("You don't have any favorites yet.");
            return;
        }


        for (String favoriteName : favorites) {
            addFavoriteProductView(favoriteName);
        }
    }

    private void addFavoriteProductView(String productName) {
        View cardView = getLayoutInflater().inflate(R.layout.favorite_item_layout, null);

        TextView nameText = cardView.findViewById(R.id.itemName);
        nameText.setText(productName);

        TextView priceText = cardView.findViewById(R.id.itemPrice);
        priceText.setText("Price: $" + getPrice(productName)); // Use a default price method

        Button removeButton = cardView.findViewById(R.id.removeBtn);
        removeButton.setOnClickListener(v -> {
            removeFromFavorites(productName);
            favoritesContainer.removeView(cardView);
        });

        Button addToCartButton = cardView.findViewById(R.id.addToCartBtn);
        addToCartButton.setOnClickListener(v -> {
            showToast(productName + " - Check stock in the product's category");
        });

        favoritesContainer.addView(cardView);
    }
    private void addFavoriteProductView(Product product) {
        View cardView = getLayoutInflater().inflate(R.layout.favorite_item_layout, null);

        TextView nameText = cardView.findViewById(R.id.itemName);
        nameText.setText(product.getName());

        TextView priceText = cardView.findViewById(R.id.itemPrice);
        priceText.setText("Price: $" + getPrice(product.getName()));

        Button removeButton = cardView.findViewById(R.id.removeBtn);
        removeButton.setOnClickListener(v -> {
            removeFromFavorites(product.getName());
            favoritesContainer.removeView(cardView);
        });

        Button addToCartButton = cardView.findViewById(R.id.addToCartBtn);
        addToCartButton.setOnClickListener(v -> {
            if (addToCart(product.getName())) {
                showToast(product.getName() + " added to cart");
            } else {
                showToast(product.getName() + " is out of stock");
            }
        });

        favoritesContainer.addView(cardView);
    }

    private boolean addToCart(String productName) {
        Gson gson = new Gson();
        String productsJson = prefs.getString(PRODUCT_DATA_KEY, "[]");
        Type productListType = new TypeToken<List<Product>>(){}.getType();
        List<Product> allProducts = gson.fromJson(productsJson, productListType);

        for (Product product : allProducts) {
            if (product.getName().equals(productName)) {
                if (product.getQuantity() > 0) {
                    product.setQuantity(product.getQuantity() - 1);
                    product.setBought(product.getBought() + 1);

                    // Save updated products
                    String updatedProductsJson = gson.toJson(allProducts);
                    prefs.edit().putString(PRODUCT_DATA_KEY, updatedProductsJson).apply();
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    private void removeFromFavorites(String productName) {
        Gson gson = new Gson();
        String favoritesJson = prefs.getString(FAVORITES_KEY, "[]");
        Type setType = new TypeToken<Set<String>>(){}.getType();
        Set<String> favorites = gson.fromJson(favoritesJson, setType);

        if (favorites != null) {
            favorites.remove(productName);
            String updatedFavoritesJson = gson.toJson(favorites);
            prefs.edit().putString(FAVORITES_KEY, updatedFavoritesJson).apply();
            showToast(productName + " removed from favorites");
        }
    }

    private int getPrice(String name) {
        switch (name) {
            case "T-Shirt": return 30;
            case "Jeans": return 50;
            case "Shoe": return 56;
            case "drawing": return 70;
            case "candle": return 16;
            case "omar": return 9000000;
            case "coin": return 100;
            case "titanic": return 300000;
            default: return 20;
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}