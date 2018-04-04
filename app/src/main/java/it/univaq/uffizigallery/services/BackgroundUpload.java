package it.univaq.uffizigallery.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;

import java.util.concurrent.ExecutionException;

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

        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;

        try {
            activeNetwork = cm.getActiveNetworkInfo();
        } catch (NullPointerException e1){
            e1.printStackTrace();
        }

        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if(isConnected){

            ConnectionToServer toServer = new ConnectionToServer();

            try {
                toServer.execute(getApplicationContext());
                toServer.get();

            }catch(InterruptedException|ExecutionException e){
                e.printStackTrace();
            }

        }

    }

}
