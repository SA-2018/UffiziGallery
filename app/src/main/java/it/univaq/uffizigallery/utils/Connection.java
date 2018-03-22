package it.univaq.uffizigallery.utils;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Riccardo on 20/03/2018.
 */

public class Connection {

    public Object connectionToServer(){
        String address = "http://uffizi.easyline.univaq.it/UFFIZI/api";
        String result = "";
            try {
                URL url = new URL(address);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                try {
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(30000); //30 secondi
                    connection.setReadTimeout(30000); //30 secondi

                    ServerAPI server = new ServerAPI();
                    result =  server.getCheckpointActive();
                    System.out.println(result);

                } catch(JSONException e2){

                } finally {
                    connection.disconnect();
                }

            } catch(IOException e1){

            }

            return result;

    }

}
