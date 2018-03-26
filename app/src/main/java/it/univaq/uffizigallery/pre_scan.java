package it.univaq.uffizigallery;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Button;

import org.json.JSONArray;

/**
 * Created by Riccardo on 26/03/2018.
 */

public class pre_scan extends AppCompatActivity {
    private Button button1, button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pre_scan);
        button1 = findViewById(R.id.button4);
        button2 = findViewById(R.id.button5);
    }

}
