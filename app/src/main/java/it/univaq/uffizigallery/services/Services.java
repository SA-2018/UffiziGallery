package it.univaq.uffizigallery.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.univaq.uffizigallery.CheckpointHubActivity;
import it.univaq.uffizigallery.MainActivity;
import it.univaq.uffizigallery.model.Checkpoint;
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
                ServerAPI fromServer = new ServerAPI(getApplicationContext());
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

        Intent response = new Intent(CheckpointHubActivity.ACTION_SCAN_COMPLETED);

        String barcode_data = intent.getStringExtra("barcode");
        Checkpoint checkpoint = CheckpointService.JSONtoCheckpoint(intent.getStringExtra("checkpoint"));
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

        try {

            Map<String, Object> data = new HashMap<String, Object>();
            data.put("in_out", checkpoint.getIn_out());
            data.put("tipo", checkpoint.getTipo());
            data.put("id_checkpoint", checkpoint.getId());
            data.put("barcode", barcode_data);
            data.put("device_imei", telephonyManager.getDeviceId());
            data.put("device_name", getDeviceName());

            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            data.put("time", timeStamp);
            data.put("latitude", intent.getStringExtra("latitude"));
            data.put("longitude", intent.getStringExtra("longitude"));
            data.put("accuracy", intent.getStringExtra("accuracy"));

            final ObjectMapper objectMapper = new ObjectMapper();
            final StringWriter stringWriter = new StringWriter();
            objectMapper.writeValue((Writer) stringWriter, (Object) data);

            String JSONString = stringWriter.toString();
            ServerAPI toServer = new ServerAPI(checkpoint, getApplicationContext());

            String result = toServer.ticketAdd(JSONString);

            int error;

            try {
                JSONObject o = new JSONObject(result);
                error = o.getInt("error");

                if(error == 0){
                    checkpoint.setChildsize(checkpoint.getChildsize() + 1);
                    response.putExtra("checkpoint", checkpoint.toString());
                } else {
                    response.putExtra("checkpoint", checkpoint.toString());
                }

            } catch(JSONException e){
                e.printStackTrace();
            }

        }catch(SecurityException|NullPointerException|IOException|JSONException e){
            e.printStackTrace();
        }


        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(response);

    }

    /**
     * FUNZIONI DI APPOGGIO ALLA CLASSE SERVICES
     */

    //funzione appoggio
    private String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    //funzione appoggio
    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

}
