package it.univaq.uffizigallery;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import it.univaq.uffizigallery.services.BackgroundUpload;
import it.univaq.uffizigallery.services.Services;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String ACTION_SERVICE_COMPLETED = "action_service_completed";
    public static final String ACTION_UPLOAD_COMPLETED = "action_upload_completed";

    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressDialog progress;

    /**
     * Permissions
     */
    private static final int PERMISSION_MULTIPLE_REQUEST = 1;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent == null || intent.getAction() == null) return;

            switch (intent.getAction()){
                case ACTION_SERVICE_COMPLETED:

                    //download completed, dismissing progress dialog
                    progress.dismiss();
                    mSwipeRefreshLayout.setRefreshing(false);

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

                case ACTION_UPLOAD_COMPLETED:
                    // upload action
                    Intent intent_upload = new Intent(getApplicationContext(), BackgroundUpload.class);
                    intent_upload.setAction(BackgroundUpload.ACTION_UPLOAD);
                    intent_upload.putExtra("source", "MainActivity");
                    startService(intent_upload);
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
        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);


        // TODO : add location permission
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) +
                ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) +
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) +
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.INTERNET, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE},
                        PERMISSION_MULTIPLE_REQUEST);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case PERMISSION_MULTIPLE_REQUEST:
                if(grantResults.length > 0){
                    boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean internet = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean gps = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean phone_state = grantResults[3] == PackageManager.PERMISSION_GRANTED;

                    if(camera && internet && gps && phone_state){

                    } else {
                        this.finish();
                        System.exit(1);
                    }

                }
                break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = null;
        try {
            activeNetwork = cm.getActiveNetworkInfo();
        } catch (NullPointerException e1){
            e1.printStackTrace();
        }

        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        //progress dialog
        progress = new ProgressDialog(this);

        if(!isConnected){
            //snackbar creation
            Snackbar snackbar = Snackbar.make(findViewById(R.id.main_constraintlayout), "No Internet connection", Snackbar.LENGTH_LONG);
            snackbar.show();

            return;
        }

        //progress setting
        progress.setMessage("Download checkpoint...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.setIndeterminate(true);
        progress.show();

        //preparing to download
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_SERVICE_COMPLETED);
        filter.addAction(ACTION_UPLOAD_COMPLETED);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver, filter);

        // download action

        final Intent intent = new Intent(getApplicationContext(), Services.class);
        intent.setAction(Services.ACTION_DOWNLOAD);
        startService(intent);

        // upload action
        Intent intent_upload = new Intent(getApplicationContext(), BackgroundUpload.class);
        intent_upload.setAction(BackgroundUpload.ACTION_UPLOAD);
        intent_upload.putExtra("source", "MainActivity");
        startService(intent_upload);

    }

    @Override
    public void onRefresh() {

        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = null;
        try {
            activeNetwork = cm.getActiveNetworkInfo();
        } catch (NullPointerException e1){
            e1.printStackTrace();
        }

        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(!isConnected){
            //snackbar creation
            Snackbar snackbar = Snackbar.make(findViewById(R.id.main_constraintlayout), "No Internet connection", Snackbar.LENGTH_LONG);
            snackbar.show();

            mSwipeRefreshLayout.setRefreshing(false);

            return;
        }

        //preparing to download

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_SERVICE_COMPLETED);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver, filter);

        // download action

        final Intent intent = new Intent(getApplicationContext(), Services.class);
        intent.setAction(Services.ACTION_DOWNLOAD);
        startService(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(receiver);
    }

}
