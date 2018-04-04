package it.univaq.uffizigallery.utils;

import android.content.Context;
import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import it.univaq.uffizigallery.database.DBHelper;
import it.univaq.uffizigallery.model.Ticket;

/**
 * Created by Riccardo on 03/04/2018.
 */

public class ConnectionToServer extends AsyncTask<Object, Integer, Integer> {

    @Override
    protected Integer doInBackground(Object objects[]) {

        Context context = (Context) objects[0];

        if(DBHelper.get(context).ticketCounter() == 0){

            return 0;

        } else {

            List<Ticket> todolist = DBHelper.get(context).getAll();
            int count = 0;

            while(!todolist.isEmpty()){
                Ticket ticket = todolist.get(count);

                boolean success = connectionToServer(ticket);

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
    private boolean connectionToServer(final Ticket ticket) {

        String address = "http://uffizi.easyline.univaq.it/UFFIZI/api/ticket/add";
        boolean success = false;

        String json_string = "{\"in_out\":\"" + ticket.getIn_out() +
                "\",\"barcode\":\"" + ticket.getBarcode() +
                "\",\"device_imei\":\"" + ticket.getDev_imei() +
                "\",\"device_name\":\"" + ticket.getDev_name() +
                "\",\"id_checkpoint\":\"" + ticket.getId_checkpoint() +
                "\",\"time\":\"" + ticket.getTime() +
                "\",\"type\":\"" + ticket.getTipo() +
                "\",\"lat\":\"" + ticket.getLatitude() +
                "\",\"lng\":\"" +  ticket.getLongitude() +
                "\",\"accuracy\":\"" + ticket.getAccuracy() +
                "\"}";

        //Map<String, Object> obj = new HashMap<String, Object>();

        //creazione jsonstring
        /*
        try {

            obj.put("in_out", ticket.getIn_out());
            obj.put("barcode", ticket.getBarcode());
            obj.put("device_imei", ticket.getDev_imei());
            obj.put("device_name", ticket.getDev_name());
            obj.put("id_checkpoint", new Long(ticket.getId_checkpoint()).toString() );
            //obj.put("checkpoint", ticket.getCheckpoint().toMap());
            obj.put("time", ticket.getTime());
            obj.put("type", ticket.getTipo());
            obj.put("lat", new Double(ticket.getLatitude()).toString());
            obj.put("lng", new Double(ticket.getLongitude()).toString());
            obj.put("accuracy", new Double(ticket.getAccuracy()).toString());

            final ObjectMapper objectMapper = new ObjectMapper();
            final StringWriter stringWriter = new StringWriter();
            objectMapper.writeValue((Writer) stringWriter, (Object) obj);

            json_string = stringWriter.toString();

        }catch(IOException e){
            e.printStackTrace();
        }*/


        //send to server
        HttpURLConnection con = null;

        try {

            URL url = new URL(address);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");

            String urlParameters = "json=" + json_string;

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                success = true;
            } else {
                success = false;
            }

            /*
            System.out.println("\nSending 'POST' request to URL : " + address);
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();


            System.out.println(response.toString());
            */


        }catch(Exception e){
            e.printStackTrace();
        } finally {
            con.disconnect();
        }

        return success;

    }

}
