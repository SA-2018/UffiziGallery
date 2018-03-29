package it.univaq.uffizigallery.services;

import android.content.Context;

import it.univaq.uffizigallery.database.DBHelper;
import it.univaq.uffizigallery.model.Ticket;

/**
 * Created by Riccardo on 21/03/2018.
 */

public class TicketService {

    private Context context;
    public TicketService(){
        this.context = null;
    }
    public TicketService(Context context){
    this.context = context;
    }

    public void insert(Ticket ticket){
        DBHelper.get(this.context).save(ticket);
    }

}
