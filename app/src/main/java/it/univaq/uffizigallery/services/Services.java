package it.univaq.uffizigallery.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import it.univaq.uffizigallery.MainActivity;
import it.univaq.uffizigallery.database.DBHelper;
import it.univaq.uffizigallery.model.Checkpoint;
import it.univaq.uffizigallery.model.Ticket;
import it.univaq.uffizigallery.utils.ServerAPI;

/**
 * Created by Riccardo on 25/03/2018.
 */

public class Services extends IntentService {

    public static final String ACTION_DOWNLOAD =  "action_download";
    public static final String ACTION_READ_BARCODE_COMPLETED = "action_read_barcode_completed";

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

                case ACTION_READ_BARCODE_COMPLETED:
                    read_barcode_completed(intent);
                    break;
            }
        }
    }

    private void download(){

        Intent intent = new Intent(MainActivity.ACTION_SERVICE_COMPLETED);

            try {
                ServerAPI fromServer = new ServerAPI();
                List<Checkpoint> checkpoints = CheckpointService.JSONtoCheckpointList(fromServer.getCheckpointActive());

                //from checkpoint list to JSON checkpoint list

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

    // Todo : callback function on read barcode completed
    private void read_barcode_completed(Intent intent){

        String barcode_data = intent.getStringExtra("barcode");
        Checkpoint checkpoint = CheckpointService.JSONtoCheckpoint(intent.getStringExtra("checkpoint"));





    }
}
