package it.univaq.uffizigallery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import it.univaq.uffizigallery.model.Checkpoint;
import it.univaq.uffizigallery.services.BackgroundUpload;
import it.univaq.uffizigallery.services.CheckpointService;
import it.univaq.uffizigallery.services.LocationService;

/**
 * Created by Riccardo on 26/03/2018.
 */

public class CheckpointHubActivity extends AppCompatActivity {

    private Button button1;
    private Button button2;
    private TextView textview1, textview2, textview3, textview4, textview5, textview6, textview7, textview8;
    private Intent intent;

    public static final String ACTION_UPLOAD_COMPLETED = "action_upload_completed";

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent == null || intent.getAction() == null) return;

            switch (intent.getAction()){
                case ACTION_UPLOAD_COMPLETED:
                    // upload action
                    Intent intent_upload = new Intent(getApplicationContext(), BackgroundUpload.class);
                    intent_upload.setAction(BackgroundUpload.ACTION_UPLOAD);
                    intent_upload.putExtra("source", "CheckpointHubActivity");
                    startService(intent_upload);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub_checkpoint);
        button1 = findViewById(R.id.button4);
        button2 = findViewById(R.id.button5);

        textview1 = findViewById(R.id.textview1);
        textview2 = findViewById(R.id.textview2);
        textview3 = findViewById(R.id.textview3);
        textview4 = findViewById(R.id.textview4);
        textview5 = findViewById(R.id.textview5);
        textview6 = findViewById(R.id.textview6);
        textview7 = findViewById(R.id.textview7);
        textview8 = findViewById(R.id.textview8);

        intent = new Intent(getApplicationContext(), CameraTestActivity.class);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CameraTestActivity.class);
                intent.putExtra("checkpoint", getIntent().getStringExtra("checkpoint"));
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        //uploading
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_UPLOAD_COMPLETED);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver, filter);

        // upload action
        Intent intent_upload = new Intent(getApplicationContext(), BackgroundUpload.class);
        intent_upload.setAction(BackgroundUpload.ACTION_UPLOAD);
        intent_upload.putExtra("source", "CheckpointHubActivity");
        startService(intent_upload);

        // textview e GPS creation
        Checkpoint checkpoint = CheckpointService.JSONtoCheckpoint(getIntent().getStringExtra("checkpoint"));
        textview1.setText("NOME");
        textview1.setGravity(Gravity.CENTER);
        textview2.setText( checkpoint.getNome()!=null?checkpoint.getNome():"no data" );
        textview2.setGravity(Gravity.CENTER);

        textview3.setText("TIPO");
        textview3.setGravity(Gravity.CENTER);
        textview4.setText( checkpoint.getTipo()!=null?checkpoint.getTipo():"no data" );
        textview4.setGravity(Gravity.CENTER);

        textview5.setText("IN_OUT");
        textview5.setGravity(Gravity.CENTER);
        textview6.setText( checkpoint.getIn_out()!=null?checkpoint.getIn_out():"no data" );
        textview6.setGravity(Gravity.CENTER);

        textview7.setText("CHILDSIZE");
        textview7.setGravity(Gravity.CENTER);
        long childsize_value = checkpoint.getChildsize();

        textview8.setText(new Integer((int)childsize_value).toString());
        textview8.setGravity(Gravity.CENTER);

        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //button1.setEnabled(true);

                textview8.setText(new Double(location.getLatitude()).toString());

                intent.putExtra("latitude", location.getLatitude());
                intent.putExtra("longitude", location.getLongitude());
                intent.putExtra("accuracy", location.getAccuracy());

                Toast.makeText(getApplicationContext(), "GPS location retrieved ", Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override
            public void onProviderEnabled(String provider) {
                button1.setEnabled(true);
            }
            @Override
            public void onProviderDisabled(String provider) {
                button1.setEnabled(false);
            }
        };

        /* GPS */
        try {

            LocationManager manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            if(manager != null)
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 5, listener);

            LocationService ls = new LocationService(getApplicationContext());
            if(!ls.isGPSEnabled()){
                textview8.setText("yolo");

                Toast.makeText(getApplicationContext(), "Enable your GPS location to continue" , Toast.LENGTH_LONG).show();
                button1.setEnabled(false);
                return;
            }

        } catch(SecurityException e){
            e.printStackTrace();
        }



    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(receiver);
    }

}
