package it.univaq.uffizigallery.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ListAdapter;

import java.util.List;

import it.univaq.uffizigallery.model.Ticket;

public class DBHelper extends SQLiteOpenHelper {

    private static final String NAME = "UffiziGallery.db";
    private static final int VERSION = 1;

    private static DBHelper instance = null;

    public static DBHelper get(Context context){
        return instance == null ? instance = new DBHelper(context) : instance;
    }

    private DBHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        TableTicket.create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        TableTicket.upgrade(db);
    }

    public void save(Ticket ticket){
        TableTicket.save(getWritableDatabase(), ticket);
    }

    public void update(Ticket ticket){
        TableTicket.update(getWritableDatabase(), ticket);
    }

    public void delete(Ticket ticket){
        TableTicket.delete(getWritableDatabase(), ticket);
    }

    public int ticketCounter(){ return TableTicket.ticketCounter(getReadableDatabase()); }

    public List<Ticket> getAll(){ return TableTicket.getAll(getReadableDatabase()); }

}
