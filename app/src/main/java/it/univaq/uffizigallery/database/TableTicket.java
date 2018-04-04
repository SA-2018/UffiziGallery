package it.univaq.uffizigallery.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import it.univaq.uffizigallery.model.Ticket;
import it.univaq.uffizigallery.services.CheckpointService;


public class TableTicket {

    private static final String TABLE_NAME = "tickets";

    private static final String ID = "id";
    private static final String IN_OUT = "in_out";
    private static final String TIPO = "tipo";
    private static final String ID_CHECKPOINT = "id_checkpoint";
    private static final String CHILDSIZE = "childsize";
    private static final String BARCODE = "barcode";
    private static final String DEV_IMEI = "dev_imei";
    private static final String DEV_NAME = "dev_name";
    private static final String TIME = "time";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String ACCURACY = "accuracy";
    private static final String CHECKPOINT = "checkpoint";

    public static void create(SQLiteDatabase db){
        String sql = "CREATE TABLE " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                IN_OUT + " TEXT NOT NULL, " +
                TIPO + " TEXT NOT NULL, " +
                ID_CHECKPOINT + " NUMERIC, " +
                CHILDSIZE + " NUMERIC, " +
                BARCODE + " TEXT NOT NULL, " +
                DEV_IMEI + " TEXT NOT NULL, " +
                DEV_NAME + " TEXT NOT NULL, " +
                TIME + " TEXT NOT NULL, " +
                LATITUDE + " NUMERIC, " +
                LONGITUDE + " NUMERIC, " +
                ACCURACY + " NUMERIC, " +
                CHECKPOINT + " TEXT NOT NULL" +
                ")";
        db.execSQL(sql);
    }

    public static void drop(SQLiteDatabase db){
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
    }

    public static void upgrade(SQLiteDatabase db){
        drop(db);
        create(db);
    }

    public static void save(SQLiteDatabase db, Ticket ticket){

        ContentValues values = new ContentValues();
        values.put(IN_OUT, ticket.getIn_out());
        values.put(TIPO, ticket.getTipo());
        values.put(ID_CHECKPOINT, ticket.getId_checkpoint());
        values.put(CHILDSIZE, ticket.getChildsize());
        values.put(BARCODE, ticket.getBarcode());
        values.put(DEV_IMEI, ticket.getDev_imei());
        values.put(DEV_NAME, ticket.getDev_name());
        values.put(TIME, ticket.getTime());
        values.put(LATITUDE, ticket.getLatitude());
        values.put(LONGITUDE, ticket.getLongitude());
        values.put(ACCURACY, ticket.getAccuracy());
        values.put(CHECKPOINT, ticket.getCheckpoint().toJSON().toString());

        long id = db.insert(TABLE_NAME, null, values);
        if(id != -1) ticket.setId((int) id);
    }

    public static boolean update(SQLiteDatabase db, Ticket ticket){

        ContentValues values = new ContentValues();
        values.put(IN_OUT, ticket.getIn_out());
        values.put(ID_CHECKPOINT, ticket.getId_checkpoint());
        values.put(CHILDSIZE, ticket.getChildsize());
        values.put(BARCODE, ticket.getBarcode());
        values.put(DEV_IMEI, ticket.getDev_imei());
        values.put(DEV_NAME, ticket.getDev_name());
        values.put(TIME, ticket.getTime());
        values.put(LATITUDE, ticket.getLatitude());
        values.put(LONGITUDE, ticket.getLongitude());
        values.put(ACCURACY, ticket.getAccuracy());
        values.put(CHECKPOINT, ticket.getCheckpoint().toJSON().toString());
//        int rows = db.update(TABLE_NAME, values, ID +"= ?", new String[]{ String.valueOf(city.getId())} );
        return db.update(TABLE_NAME, values, ID +"=" + ticket.getId(), null) == 1;
    }

    public static boolean delete(SQLiteDatabase db, Ticket ticket){

        return db.delete(TABLE_NAME, ID +"=" + ticket.getId(), null) == 1;
    }

    public static int ticketCounter(SQLiteDatabase db){

        String sql = "SELECT * FROM " + TABLE_NAME ;
        Cursor cursor = null;
        int i = 0;
        try{
            cursor = db.rawQuery(sql, null);
            while(cursor.moveToNext()) {
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(cursor != null) cursor.close();
        }
        return i;
    }

    public static List<Ticket> getAll(SQLiteDatabase db){

        List<Ticket> tickets = new ArrayList<>();

        String sql = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = null;
        try{
            cursor = db.rawQuery(sql, null);
            while(cursor.moveToNext()) {
                Ticket ticket = new Ticket();
                ticket.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                ticket.setIn_out(cursor.getString(cursor.getColumnIndex(IN_OUT)));
                ticket.setTipo(cursor.getString(cursor.getColumnIndex(TIPO)));
                ticket.setId_checkpoint(cursor.getInt(cursor.getColumnIndex(ID_CHECKPOINT)));
                ticket.setChildsize(cursor.getLong(cursor.getColumnIndex(CHILDSIZE)));
                ticket.setBarcode(cursor.getString(cursor.getColumnIndex(BARCODE)));
                ticket.setDev_imei(cursor.getString(cursor.getColumnIndex(DEV_IMEI)));
                ticket.setDev_name(cursor.getString(cursor.getColumnIndex(DEV_NAME)));
                ticket.setTime(cursor.getString(cursor.getColumnIndex(TIME)));
                ticket.setLatitude(cursor.getDouble(cursor.getColumnIndex(LATITUDE)));
                ticket.setLongitude(cursor.getDouble(cursor.getColumnIndex(LONGITUDE)));
                ticket.setAccuracy(cursor.getDouble(cursor.getColumnIndex(ACCURACY)));
                ticket.setCheckpoint(CheckpointService.JSONtoCheckpoint(cursor.getString(cursor.getColumnIndex(CHECKPOINT))));

                tickets.add(ticket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(cursor != null) cursor.close();
        }

        return tickets;
    }

}
