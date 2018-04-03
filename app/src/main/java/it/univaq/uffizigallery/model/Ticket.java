package it.univaq.uffizigallery.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Riccardo on 21/03/2018.
 */

public class Ticket implements Serializable {

    private int id;

    private String in_out;
    private String tipo;
    private long id_checkpoint;
    private long childsize;
    private String barcode;
    private String dev_imei;
    private String dev_name;
    private String time;
    private double latitude;
    private double longitude;
    private double accuracy;
    private Checkpoint checkpoint;


    // BUILDERS

    public Ticket(){
        this.in_out = "";
        this.tipo = "";
        this.id_checkpoint = 0;
        this.childsize = 0;
        this.barcode = "";
        this.dev_imei = "";
        this.dev_name = "";
        this.time = "";
        this.latitude = 0;
        this.longitude = 0;
        this.accuracy = 0;
        this.checkpoint = null;
    }

    public Ticket(String in_out, String tipo, long id_checkpoint, long childsize, String barcode, String dev_imei, String dev_name, String time, double latitude, double longitude, double accuracy, Checkpoint checkpoint){
        this.in_out = in_out;
        this.tipo = tipo;
        this.id_checkpoint = id_checkpoint;
        this.childsize = childsize;
        this.barcode = barcode;
        this.dev_imei = dev_imei;
        this.dev_name = dev_name;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
        this.checkpoint = checkpoint;
    }

    public Ticket(String in_out, String barcode, String dev_name, String dev_imei){
        this.in_out = in_out;
        this.barcode = barcode;
        this.dev_name = dev_name;
        this.dev_imei = dev_imei;
    }


    //GET METHODS
    public int getId() { return this.id; }

    public String getIn_out(){
        return this.in_out;
    }

    public String getTipo(){
        return this.tipo;
    }

    public long getId_checkpoint(){
        return this.id_checkpoint;
    }

    public long getChildsize(){
        return this.childsize;
    }

    public String getBarcode(){
        return this.barcode;
    }

    public String getDev_imei(){
        return this.dev_imei;
    }

    public String getDev_name(){
        return this.dev_name;
    }

    public String getTime(){
        return this.time;
    }

    public double getLatitude(){
        return this.latitude;
    }

    public double getLongitude(){
        return this.longitude;
    }

    public double getAccuracy(){ return this.accuracy; }

    public Checkpoint getCheckpoint(){ return this.checkpoint; }


    //SET METHODS
    public void setId(int id) { this.id=id; }

    public void setIn_out(String in_out){
        this.in_out = in_out;
    }

    public void setTipo(String tipo){
        this.tipo = tipo;
    }

    public void setId_checkpoint(long id_checkpoint){
        this.id_checkpoint = id_checkpoint;
    }

    public void setChildsize(long childsize){
        this.childsize = childsize;
    }

    public void setBarcode(String barcode){
        this.barcode = barcode;
    }

    public void setDev_imei(String dev_imei){
        this.dev_imei = dev_imei;
    }

    public void setDev_name(String dev_name){ this.dev_name = dev_name; }

    public void setTime(String time){
        this.time = time;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public void setAccuracy(double accuracy){
        this.accuracy = accuracy;
    }

    public void setCheckpoint(Checkpoint checkpoint){ this.checkpoint = checkpoint; }

    public JSONObject toJSON(){

        try {
            JSONObject json = new JSONObject();
            json.put("in_out", this.in_out);
            json.put("tipo", this.tipo);
            json.put("id_checkpoint", this.id_checkpoint);
            json.put("childsize", this.childsize);
            json.put("barcode", this.barcode);
            json.put("dev_imei", this.dev_imei);
            json.put("dev_name", this.dev_name);
            json.put("time", this.time);
            json.put("latitude", this.latitude);
            json.put("longitude", this.longitude);
            json.put("accuracy", this.accuracy);
            json.put("checkpoint", this.checkpoint.toJSON());
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Ticket parseJSON(JSONObject json){

        try {
            Ticket ticket = new Ticket();
            ticket.setIn_out(json.getString("in_out"));
            ticket.setTipo(json.getString("tipo"));
            ticket.setId_checkpoint(json.getLong("id_checkpoint"));
            ticket.setChildsize(json.getLong("childsize"));
            ticket.setBarcode(json.getString("barcode"));
            ticket.setDev_imei(json.getString("dev_imei"));
            ticket.setDev_name(json.getString("dev_name"));
            ticket.setTime(json.getString("time"));
            ticket.setLatitude(json.getDouble("latitude"));
            ticket.setLongitude(json.getDouble("longitude"));
            ticket.setAccuracy(json.getDouble("accuracy"));
            ticket.setCheckpoint(Checkpoint.parseJSON(json.getJSONObject("checkpoint")));

            return ticket;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Ticket> parseJSONArray(JSONArray array){
        List<Ticket> tickets = new ArrayList<Ticket>();

        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject item = array.getJSONObject(i);
                Ticket ticket = Ticket.parseJSON(item);
                if (ticket != null) tickets.add(ticket);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }

        return tickets;
    }

    public static JSONArray toJSONArray(List<Ticket> tickets) {
        JSONArray array = new JSONArray();

        for(Ticket city : tickets) {
            JSONObject item = city.toJSON();
            if(item != null) array.put(item);
        }

        return array;
    }

}
