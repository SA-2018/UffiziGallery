package it.univaq.uffizigallery.utils;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Riccardo on 20/03/2018.
 */

public class ConnectionFromServer extends AsyncTask<String, Integer, String> {

    @Override
    protected String doInBackground(String[] objects) {
        String result = (String)connectionFromServer();

        return result;
    }


    public Object connectionFromServer(){
        String address = "http://uffizi.easyline.univaq.it/UFFIZI/api/checkpoint/get";
        InputStream in = null;
        StringBuilder stringBuilder = null;

            try {
                URL url = new URL(address);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                try {
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(30000); //30 secondi
                    connection.setReadTimeout(30000); //30 secondi

                    int responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        in = connection.getInputStream();
                    } else {
                        in = connection.getErrorStream();
                    }

                    stringBuilder = new StringBuilder();
                    BufferedInputStream bis = new BufferedInputStream(in);
                    byte[] buffer = new byte[1024];
                    int length = 0;

                    while ((length = bis.read(buffer)) > 0) {
                        String stringBuffer = new String(buffer, 0, length);
                        stringBuilder.append(stringBuffer);
                    }

                } finally {
                    connection.disconnect();
                }

            } catch(IOException e1){
                e1.printStackTrace();
            }

            if(stringBuilder == null)
                return null;
            else
                return stringBuilder.toString();

    }

}
