package it.univaq.uffizigallery.utils;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import it.univaq.uffizigallery.model.Checkpoint;
import it.univaq.uffizigallery.model.Ticket;

/**
 * Created by Riccardo on 03/04/2018.
 */

public class ConnectionToServer extends AsyncTask<Object, Integer, Integer> {

    @Override
    protected Integer doInBackground(Object objects[]) {
       // String result = (String)connectionFromServer();

        Ticket ticket = new Ticket("aa", "aa", 0, 0, "aa", "aa", "aa", "aa", 0, 0, 0, new Checkpoint());
        connectionToServer(ticket, (Context) objects[0]);

/*
        Context context = (Context) objects[0];

        if(DBHelper.get(context).ticketCounter() == 0){

            return 0;

        } else {

            List<Ticket> todolist = DBHelper.get(context).getAll();
            int count = 0;

            while(!todolist.isEmpty()){
                Ticket ticket = todolist.get(count);

                boolean success = connectionToServer(ticket, context);

                if(success){
                    todolist.remove(count);
                    DBHelper.get(context).delete(ticket);
                    count++;
                } else {
                    continue;
                }

            }

            return 0;

        }*/

    return 0;

    }

    //Todo : riaggiustare funzione connectionToServer()
    private boolean connectionToServer(final Ticket ticket, Context context) {

        String address = "http://uffizi.easyline.univaq.it/UFFIZI/api/ticket/add";
        String response = null;

        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            try{

                connection.setRequestMethod("POST");
                connection.setConnectTimeout(30);
                connection.setReadTimeout(30);
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setChunkedStreamingMode(0);

                //TODO : aggiustare
                CloseableHttpClient httpClient = HttpClientBuilder.create().build();

                try {
                    HttpPost request = new HttpPost(address);
                    StringEntity params = new StringEntity(ticket.toJSON().toString());
                    request.addHeader("content-type", "application/json");
                    request.setEntity(params);

                    //httpClient.execute(request);

                    HttpResponse httpResponse = httpClient.execute(request);

                    HttpEntity responseEntity = httpResponse.getEntity();
                    if(responseEntity!=null) {
                        response = EntityUtils.toString(responseEntity);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    httpClient.close();
                }

            } finally {
                connection.disconnect();
            }

        }catch(IOException e){
            e.printStackTrace();
        }

        System.out.println("Response: "  + response);
        return true;


    }


}
