package it.univaq.uffizigallery.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import it.univaq.uffizigallery.model.Checkpoint;
import it.univaq.uffizigallery.utils.CheckpointService;
import it.univaq.uffizigallery.utils.ServerAPI;

import it.univaq.uffizigallery.MainActivity;

/**
 * Created by Riccardo on 25/03/2018.
 */

public class Services extends IntentService {

    public static final String ACTION_DOWNLOAD =  "action_download";

    private static final String NAME = Services.class.getSimpleName();

    public Services() {
        super(NAME);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null && intent.getAction() != null) {

            String action = intent.getAction();
            switch (action) {
                case ACTION_DOWNLOAD:
                    download();
                    break;
            }
        }
    }

    private void download(){

        Intent intent = new Intent(MainActivity.ACTION_SERVICE_COMPLETED);

            try {

                ServerAPI fromServer = new ServerAPI();
                List<Checkpoint> checkpoints = CheckpointService.JSONtoCheckpointList(fromServer.getCheckpointActive());

                final ObjectMapper objectMapper = new ObjectMapper();
                final StringWriter stringWriter = new StringWriter();
                objectMapper.writeValue((Writer) stringWriter, (Object) checkpoints);
                String response = stringWriter.toString();


                if(response != null) {

                JSONArray array = new JSONArray(response);
                intent.putExtra("data", array.toString());

                intent.putExtra("success", true);

                } else {
                    intent.putExtra("success", false);
                }

            } catch (IOException|JSONException e) {
                e.printStackTrace();
                intent.putExtra("success", false);
            }

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

    }
}
