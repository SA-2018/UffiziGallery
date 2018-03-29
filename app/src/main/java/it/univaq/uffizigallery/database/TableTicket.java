package it.univaq.uffizigallery.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import it.univaq.uffizigallery.model.Ticket;


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
}
