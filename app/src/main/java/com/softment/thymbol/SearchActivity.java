package com.softment.thymbol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.softment.thymbol.Adapter.SearchResultAdapter;
import com.softment.thymbol.Model.StoreModel;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private TextView no_stores_available;
    private SearchResultAdapter searchResultAdapter;
    private ArrayList<StoreModel> storeModels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        storeModels = (ArrayList<StoreModel>) getIntent().getSerializableExtra("stores");
        if (storeModels == null) {
            finish();
        }

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        no_stores_available = findViewById(R.id.no_stores_available);

        if (storeModels.size() > 0) {
            no_stores_available.setVisibility(View.GONE);
        }
        else {
            no_stores_available.setVisibility(View.VISIBLE);
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchResultAdapter = new SearchResultAdapter(this, storeModels);
        recyclerView.setAdapter(searchResultAdapter);



    }
}
