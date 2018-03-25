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
import android.support.design.widget.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;

import it.univaq.uffizigallery.services.Services;

public class MainActivity extends AppCompatActivity {

    public static final String ACTION_SERVICE_COMPLETED = "action_service_completed";

    private RecyclerView recyclerView;
    private ProgressDialog progress;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent == null || intent.getAction() == null) return;

            switch (intent.getAction()){
                case ACTION_SERVICE_COMPLETED:

                    //download completed, dismissing progress dialog
                    progress.dismiss();

                    if(intent.getBooleanExtra("success", true)){
                        String data = intent.getStringExtra("data");

                        if(data != null) {
                            try {
                                JSONArray array = new JSONArray(data);
                                Adapter adapter = new Adapter(array);
                                recyclerView.setAdapter(adapter);

                                //snackbar creation
                                Snackbar snackbar = Snackbar.make(findViewById(R.id.main_constraintlayout), "Download checkpoint completato", Snackbar.LENGTH_LONG);
                                snackbar.show();

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

        //progress dialog
        progress = new ProgressDialog(this);
        progress.setTitle("yolo");
        progress.setMessage("Canchero ");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.setIndeterminate(true);
        progress.show();

        //preparing to download

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_SERVICE_COMPLETED);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver, filter);

        // download action

        Intent intent = new Intent(getApplicationContext(), Services.class);
        intent.setAction(Services.ACTION_DOWNLOAD);
        startService(intent);

    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(receiver);
    }

}
