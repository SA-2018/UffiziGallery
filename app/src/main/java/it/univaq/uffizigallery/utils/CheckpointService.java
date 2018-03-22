package it.univaq.uffizigallery.utils;


import java.util.concurrent.ExecutionException;

import it.univaq.uffizigallery.model.Checkpoint;

/**
 * Created by Riccardo on 21/03/2018.
 */

public class CheckpointService {

    public Object findActive(){

        ConnectionFromServer fromServer = new ConnectionFromServer();
        fromServer.execute();

        try {
            String result = fromServer.get();

            //necessita conversione JSON to List<Checkpoint>

            System.out.println(result);

        } catch(ExecutionException |InterruptedException e){
            e.printStackTrace();
        }

        return null;

    }

    public Checkpoint find(long id_checkpoint){
        //manca implementazione
        return null;
    }

}
