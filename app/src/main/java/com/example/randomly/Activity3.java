package com.example.randomly;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;

public class Activity3 extends AppCompatActivity {

    private ListView listView;
    private SearchView searchView;
    private ConstraintLayout circleContainer;
    private ArrayAdapter<String> adapter;
    private String[] categories = {"Clothes", "Jewelry", "Furniture", "Crafts", "Random"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_3);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        listView = findViewById(R.id.listView);
        searchView = findViewById(R.id.searchView);
        circleContainer = findViewById(R.id.circleContainer);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categories) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);


                textView.setTextColor(Color.BLACK);  // Set text color to black
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);  // Set text size
                textView.setMinHeight(120);  // Set minimum item height

                return view;
            }
        };

        listView.setAdapter(adapter);

        // Calculate and set ListView height based on content
        listView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                listView.removeOnLayoutChangeListener(this);

                // Calculate total height of all items
                int totalHeight = 0;
                for (int i = 0; i < adapter.getCount(); i++) {
                    View listItem = adapter.getView(i, null, listView);
                    listItem.measure(0, 0);
                    totalHeight += listItem.getMeasuredHeight();
                }

                // Set ListView height
                ViewGroup.LayoutParams params = listView.getLayoutParams();
                params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
                listView.setLayoutParams(params);
            }
        });

        // Auto-close
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    listView.setVisibility(View.GONE);
                    circleContainer.setVisibility(View.VISIBLE);
                } else {
                    listView.setVisibility(View.VISIBLE);
                    circleContainer.setVisibility(View.GONE);
                    adapter.getFilter().filter(newText);
                }
                return true;
            }
        });

        // to close when i toutch out side
        findViewById(R.id.main).setOnTouchListener((v, event) -> {
            if (listView.getVisibility() == View.VISIBLE && event.getAction() == MotionEvent.ACTION_DOWN) {
                Rect outRect = new Rect();
                listView.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    listView.setVisibility(View.GONE);
                    circleContainer.setVisibility(View.VISIBLE);
                    searchView.clearFocus();
                }
            }
            return false;
        });



            listView.setOnItemClickListener((parent, view, position, id) -> {
                String selected = adapter.getItem(position);


                Intent intent = null;
                switch (selected) {
                    case "Clothes":
                        intent = new Intent(Activity3.this, CloAct.class);
                        break;
                    case "Jewelry":
                        intent = new Intent(Activity3.this, JewAct.class);
                        break;
                    case "Furniture":
                        intent = new Intent(Activity3.this, FurAct.class);
                        break;
                    case "Crafts":
                        intent = new Intent(Activity3.this, CraAct.class);
                        break;
                    case "Random":
                        intent = new Intent(Activity3.this, RandAct.class);
                        break;
                }
                if (intent != null) {
                    startActivity(intent);
                }
            });
            // Icons
            findViewById(R.id.image1).setOnClickListener(v -> startActivity(new Intent(Activity3.this, CloAct.class)));
            findViewById(R.id.image2).setOnClickListener(v -> startActivity(new Intent(Activity3.this, JewAct.class)));
            findViewById(R.id.image3).setOnClickListener(v -> startActivity(new Intent(Activity3.this, FurAct.class)));
            findViewById(R.id.image4).setOnClickListener(v -> startActivity(new Intent(Activity3.this, CraAct.class)));
            findViewById(R.id.image5).setOnClickListener(v -> startActivity(new Intent(Activity3.this, RandAct.class)));

            findViewById(R.id.bagIcon).setOnClickListener(v -> startActivity(new Intent(Activity3.this, BagAct.class)));
            findViewById(R.id.settingsIcon).setOnClickListener(v -> startActivity(new Intent(Activity3.this, SettAct.class)));
            findViewById(R.id.profileIcon).setOnClickListener(v -> startActivity(new Intent(Activity3.this, LikeAct.class)));
            findViewById(R.id.im1).setOnClickListener(v -> startActivity(new Intent(Activity3.this, MailAct.class)));
        findViewById(R.id.home).setOnClickListener(v -> startActivity(new Intent(Activity3.this, SaleAct.class)));
        }
    }
