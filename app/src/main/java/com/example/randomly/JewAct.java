package com.example.randomly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JewAct extends AppCompatActivity {

    private ListView listView;
    private SearchView searchView;
    private Spinner categorySpinner;
    private ConstraintLayout mainLayout;
    private View productsContainer;
    LinearLayout productLayout1, productLayout2, productLayout3;
    Button addToFavoritesBtn0, addToBagBtn0;
    Button addToFavoritesBtn1, addToBagBtn1;
    Button addToFavoritesBtn2, addToBagBtn2;
    private ArrayAdapter<String> adapter;
    private final String[] categories = {"Crafts", "Furniture", "Clothes", "Random"};
    private TextView quantityText2, quantityText3, quantityText4;
    private SharedPreferences prefs;
    private static final String PREFS_NAME = "prefs_name";
    private static final String FAVORITES_KEY = "favorites_json";
    private static final String PRODUCT_DATA_KEY = "product_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_jew);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Init views
        listView = findViewById(R.id.listView);
        searchView = findViewById(R.id.searchView);
        categorySpinner = findViewById(R.id.categorySpinner);
        mainLayout = findViewById(R.id.main);
        quantityText2 = findViewById(R.id.quantityText2);


        productLayout1 = findViewById(R.id.productLayout1);


        addToFavoritesBtn0 = findViewById(R.id.addToFavoritesBtn0);


        addToBagBtn0 = findViewById(R.id.addToBagBtn0);



        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Nav buttons
        findViewById(R.id.bagIcon).setOnClickListener(v -> startActivity(new Intent(this, BagAct.class)));
        findViewById(R.id.settingsIcon).setOnClickListener(v -> startActivity(new Intent(this, SettAct.class)));
        findViewById(R.id.profileIcon).setOnClickListener(v -> startActivity(new Intent(this, LikeAct.class)));
        findViewById(R.id.im1).setOnClickListener(v -> startActivity(new Intent(this, MailAct.class)));
        findViewById(R.id.home).setOnClickListener(v -> startActivity(new Intent(this, Activity3.class)));

        // Load and set up
        loadProductData();
        setupSpinner();
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = (String) parent.getItemAtPosition(position);
            Intent intent = null;

            switch (selectedItem) {
                case "Clothes":
                    intent = new Intent(JewAct.this, CloAct.class);
                    break;
                case "Furniture":
                    intent = new Intent(JewAct.this, FurAct.class);
                    break;
                case "Crafts":
                    intent = new Intent(JewAct.this, CraAct.class);
                    break;
                case "Random":
                    intent = new Intent(JewAct.this, RandAct.class);
                    break;
            }

            if (intent != null) {
                startActivity(intent);
                hideListView();
            } else {
                showToast("No matching activity found");
            }
        });
        // Bag click
        addToBagBtn0.setOnClickListener(v -> handleAddToCart("titanic"));



        // Favorites
        addToFavoritesBtn0.setOnClickListener(v -> toggleFavorite("titanic"));



        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLACK);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                textView.setMinHeight(120);
                return view;
            }
        };
        listView.setAdapter(adapter);
        listView.setVisibility(View.GONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String query) { return false; }

            @Override public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) hideListView();
                else showListView(newText);
                return true;
            }
        });

        mainLayout.setOnTouchListener((v, event) -> {
            if (listView.getVisibility() == View.VISIBLE && event.getAction() == MotionEvent.ACTION_DOWN) {
                Rect listViewRect = new Rect();
                listView.getGlobalVisibleRect(listViewRect);
                if (!listViewRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    hideListView();
                }
            }
            return false;
        });
    }

    private void handleAddToCart(String productName) {
        if (canAddToCart(productName)) {
            updateQuantity(productName);
            incrementBoughtQuantity(productName);
            showToast(productName + " added to cart!");
        } else {
            showToast(productName + " is out of stock!");
        }
    }

    private void incrementBoughtQuantity(String productName) {
        try {
            Gson gson = new Gson();
            String jsonData = prefs.getString(PRODUCT_DATA_KEY, "");
            Type type = new TypeToken<List<Product>>() {}.getType();
            List<Product> products = gson.fromJson(jsonData, type);
            for (Product product : products) {
                if (product.getName().equals(productName)) {
                    product.setBought(product.getBought() + 1);
                    break;
                }
            }
            prefs.edit().putString(PRODUCT_DATA_KEY, gson.toJson(products)).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void toggleFavorite(String productName) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<String>>() {}.getType();
        String favoritesJson = prefs.getString(FAVORITES_KEY, "[]");
        List<String> favorites = gson.fromJson(favoritesJson, listType);
        if (favorites == null) favorites = new ArrayList<>();

        boolean isFavorite = favorites.contains(productName);
        if (isFavorite) {
            favorites.remove(productName);
        } else {
            favorites.add(productName);
        }

        prefs.edit().putString(FAVORITES_KEY, gson.toJson(favorites)).apply();
        showToast(productName + (isFavorite ? " removed from" : " added to") + " favorites");
    }

    private void loadProductData() {
        Gson gson = new Gson();
        String jsonData = prefs.getString(PRODUCT_DATA_KEY, null);
        if (jsonData == null) {
            List<Product> defaultProducts = new ArrayList<>();
            defaultProducts.add(new Product("titanic", 10, 0));


            prefs.edit().putString(PRODUCT_DATA_KEY, gson.toJson(defaultProducts)).apply();
            jsonData = prefs.getString(PRODUCT_DATA_KEY, null);
        }

        Type type = new TypeToken<List<Product>>() {}.getType();
        List<Product> products = gson.fromJson(jsonData, type);
        for (Product product : products) {
            switch (product.getName()) {
                case "titanic": quantityText2.setText(String.valueOf(product.getQuantity())); break;


            }
        }
    }

    private void saveProductData() {
        Gson gson = new Gson();
        List<Product> products = new ArrayList<>();
        products.add(new Product("titanic", Integer.parseInt(quantityText2.getText().toString()), 0));


        prefs.edit().putString(PRODUCT_DATA_KEY, gson.toJson(products)).apply();
    }

    private void setupSpinner() {
        String[] categories = {"All", "Ring", "Necklace", "Earrings", "Bracelet"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(spinnerAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterProducts(parent.getItemAtPosition(position).toString());
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {
                filterProducts("All");
            }
        });
    }

    private void filterProducts(String category) {
        productLayout1.setVisibility(category.equals("All") || category.equals("Necklace") ? View.VISIBLE : View.GONE);


    }

    private boolean canAddToCart(String productName) {
        switch (productName) {
            case "titanic": return Integer.parseInt(quantityText2.getText().toString()) > 0;


            default: return false;
        }
    }

    private void updateQuantity(String productName) {
        try {
            TextView targetView = null;
            switch (productName) {
                case "titanic": targetView = quantityText2; break;


            }
            if (targetView != null) {
                int current = Integer.parseInt(targetView.getText().toString());
                if (current > 0) {
                    targetView.setText(String.valueOf(current - 1));
                    saveProductData();
                }
            }
        } catch (Exception e) {
            showToast("Error updating quantity");
            e.printStackTrace();
        }
    }

    private void showListView(String searchText) {
        adapter.getFilter().filter(searchText);
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View item = adapter.getView(i, null, listView);
            item.measure(0, 0);
            totalHeight += item.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.setVisibility(View.VISIBLE);
        if (productsContainer != null) productsContainer.setVisibility(View.GONE);
    }

    private void hideListView() {
        listView.setVisibility(View.GONE);
        if (productsContainer != null) productsContainer.setVisibility(View.VISIBLE);
        searchView.clearFocus();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
