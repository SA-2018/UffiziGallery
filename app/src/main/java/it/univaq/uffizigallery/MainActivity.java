package it.univaq.uffizigallery;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import it.univaq.uffizigallery.services.Services;

public class MainActivity extends AppCompatActivity {

    public static final String ACTION_SERVICE_COMPLETED = "action_service_completed";

    private RecyclerView recyclerView;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent == null || intent.getAction() == null) return;

            switch (intent.getAction()){
                case ACTION_SERVICE_COMPLETED:

                    if(intent.getBooleanExtra("success", true)){
                        String data = intent.getStringExtra("data");

                        if(data != null) {
                            try {
                                JSONArray array = new JSONArray(data);
                                Adapter adapter = new Adapter(array);
                                recyclerView.setAdapter(adapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.main_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(new Adapter(new JSONArray()));

    }


    @Override
    protected void onResume() {
        super.onResume();
/*
        ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("yolo");
        progress.setMessage("Canchero ");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.setIndeterminate(true);
        progress.show();*/

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_SERVICE_COMPLETED);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver, filter);

        // Download

        Intent intent = new Intent(getApplicationContext(), Services.class);
        intent.setAction(Services.ACTION_DOWNLOAD);
        startService(intent);

        //progress.dismiss();




    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(receiver);
    }

}
