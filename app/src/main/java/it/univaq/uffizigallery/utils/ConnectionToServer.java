package it.univaq.uffizigallery.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.commons.lang3.SerializationUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.univaq.uffizigallery.database.DBHelper;
import it.univaq.uffizigallery.model.Ticket;

/**
 * Created by Riccardo on 03/04/2018.
 */

public class ConnectionToServer extends AsyncTask<Object, Integer, Integer> {

    @Override
    protected Integer doInBackground(Object objects[]) {
       // String result = (String)connectionFromServer();
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

        }

    }

    //Todo : riaggiustare funzione connectionToServer()
    private boolean connectionToServer(final Ticket ticket, Context context) {

        String address = "http://uffizi.easyline.univaq.it/UFFIZI/api/ticket/add";

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(
                StringRequest.Method.POST,
                address,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Response: " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("ticket", ticket.toJSON().toString());

                return params;
            }
        };
        queue.add(stringRequest);


        return true;

        //System.out.println(stringBuilder.toString()==null?"null":stringBuilder.toString());

    }


}
