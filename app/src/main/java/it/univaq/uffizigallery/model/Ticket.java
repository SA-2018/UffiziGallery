package it.univaq.uffizigallery.model;

/**
 * Created by Riccardo on 21/03/2018.
 */

public class Ticket {

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
    }

    public Ticket(String in_out, String tipo, long id_checkpoint, long childsize, String barcode, String dev_imei, String dev_name, String time, double latitude, double longitude, double accuracy){
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
    }


    //GET METHODS

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


    //SET METHODS

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

}
