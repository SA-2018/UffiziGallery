package it.univaq.uffizigallery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import it.univaq.uffizigallery.utils.ConnectionFromServer;
import it.univaq.uffizigallery.utils.ServerAPI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ServerAPI fromServer = new ServerAPI();
        try {
            fromServer.getCheckpointActive();
        } catch(IOException|JSONException e){
            e.printStackTrace();
        }

    }
}
