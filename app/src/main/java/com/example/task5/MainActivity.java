package com.example.task5;


import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private EditText editTextFilter;
    private List<String> currencyList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        editTextFilter = findViewById(R.id.editTextFilter);

        currencyList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, currencyList);
        listView.setAdapter(adapter);

        // Start data loading
        new DataLoader(this, new DataLoader.DataLoadedListener() {
            @Override
            public void onDataLoaded(List<String> currencies) {
                currencyList.clear();
                currencyList.addAll(currencies);
                adapter.notifyDataSetChanged();
            }
        }).execute("http://www.floatrates.com/daily/usd.xml");

        // Set up filter
        editTextFilter.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }
}