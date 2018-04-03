package it.univaq.uffizigallery.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import java.util.concurrent.ExecutionException;

import it.univaq.uffizigallery.CameraTestActivity;
import it.univaq.uffizigallery.CheckpointHubActivity;
import it.univaq.uffizigallery.MainActivity;
import it.univaq.uffizigallery.utils.ConnectionToServer;

/**
 * Created by Riccardo on 03/04/2018.
 */

public class BackgroundUpload extends IntentService {

    public static final String ACTION_UPLOAD =  "action_upload";

    private static final String NAME = Services.class.getSimpleName();

    public BackgroundUpload() {
        super(NAME);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null && intent.getAction() != null) {

            String action = intent.getAction();
            switch (action) {
                case ACTION_UPLOAD:
                    upload(intent);
                    break;
            }
        }
    }

    private void upload(Intent intent){

        String source = intent.getStringExtra("source");
        Intent intent_response = null;

        if(source.equals("MainActivity")){
            intent_response = new Intent(MainActivity.ACTION_UPLOAD_COMPLETED);
        } else if(source.equals("CheckpointHubActivity")){
            intent_response = new Intent(CheckpointHubActivity.ACTION_UPLOAD_COMPLETED);
        } else if(source.equals("Camera")){
            intent_response = new Intent(CameraTestActivity.ACTION_UPLOAD_COMPLETED);
        }


        ConnectionToServer toServer = new ConnectionToServer();

        try {
            toServer.execute(getApplicationContext());
            toServer.get();

        }catch(InterruptedException|ExecutionException e){
            //
        }


        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent_response);
    }


}
