package com.example.randomly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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



public class FurAct extends AppCompatActivity {

    private ListView listView;
    private SearchView searchView;
    private Spinner categorySpinner;
    private ConstraintLayout mainLayout;
    private ArrayAdapter<String> adapter;

    private SharedPreferences prefs;
    private static final String PREFS_NAME = "prefs_name";
    private static final String PRODUCT_DATA_KEY = "product_data";

    private String[] categories = {"All", "Carpet", "Table", "Sofa", "Others"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fur);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        try {
            initializeViews();
            prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

            setupClickListeners();
            setupAdapters();
            setupSearchView();

        } catch (Exception e) {
            Toast.makeText(this, "Error initializing screen", Toast.LENGTH_SHORT).show();
            Log.e("FurAct", "Init error", e);
        }


    }

    private void initializeViews() {
        listView = findViewById(R.id.productListView);
        searchView = findViewById(R.id.searchView);
        categorySpinner = findViewById(R.id.categorySpinner);
        mainLayout = findViewById(R.id.main);
    }

    private void setupClickListeners() {
        findViewById(R.id.bagIcon).setOnClickListener(v ->
                startActivity(new Intent(FurAct.this, BagAct.class)));
        findViewById(R.id.settingsIcon).setOnClickListener(v ->
                startActivity(new Intent(FurAct.this, SettAct.class)));
        findViewById(R.id.profileIcon).setOnClickListener(v ->
                startActivity(new Intent(FurAct.this, LikeAct.class)));
        findViewById(R.id.im1).setOnClickListener(v ->
                startActivity(new Intent(FurAct.this, MailAct.class)));
        findViewById(R.id.home).setOnClickListener(v ->
                startActivity(new Intent(FurAct.this, Activity3.class)));
    }

    private void setupAdapters() {
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.BLACK);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                tv.setMinHeight(120);
                return view;
            }
        };
        listView.setAdapter(adapter);
        listView.setVisibility(View.GONE);
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    listView.setVisibility(View.GONE);
                } else {
                    filterSearchResults(newText);
                }
                return true;
            }
        });

        mainLayout.setOnTouchListener((v, event) -> {
            if (listView.getVisibility() == View.VISIBLE && event.getAction() == MotionEvent.ACTION_DOWN) {
                Rect listViewRect = new Rect();
                listView.getGlobalVisibleRect(listViewRect);
                if (!listViewRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    listView.setVisibility(View.GONE);
                }
            }
            return false;
        });
    }







    private void filterSearchResults(String query) {
        adapter.getFilter().filter(query);
        listView.setVisibility(View.VISIBLE);
    }
}
