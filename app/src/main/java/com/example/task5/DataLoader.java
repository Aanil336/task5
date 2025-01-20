package com.example.task5;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {

    private Context context;
    private DataLoadedListener listener;

    public interface DataLoadedListener {
        void onDataLoaded(List<String> currencies);
    }

    public DataLoader(Context context, DataLoadedListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void execute(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<String> currencies = loadCurrencies(url);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null) {
                            listener.onDataLoaded(currencies);
                        }
                    }
                });
            }
        }).start();
    }

    private List<String> loadCurrencies(String urlString) {
        List<String> currencies = new ArrayList<>();
        try {
            URL url = new URL(urlString);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder xmlData = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                xmlData.append(line);
            }
            reader.close();

            // Parse XML data
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            Parser handler = new Parser();
            parser.parse(xmlData.toString(), handler);

            currencies = handler.getCurrencies();

        } catch (Exception e) {
            Log.e("DataLoader", "Error loading data", e);
        }
        return currencies;
    }
}