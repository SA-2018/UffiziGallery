package it.univaq.uffizigallery.services;


import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import it.univaq.uffizigallery.model.Checkpoint;
import it.univaq.uffizigallery.utils.ConnectionFromServer;

/**
 * Created by Riccardo on 21/03/2018.
 */

public class CheckpointService {

    public Object findActive(){

        ConnectionFromServer fromServer = new ConnectionFromServer();
        fromServer.execute();

        try {

            String jsonresult = fromServer.get();

            return JSONtoCheckpointList(jsonresult);

        } catch(InterruptedException|ExecutionException e){
            e.printStackTrace();
        }


        return null;

    }

    public Checkpoint find(long id_checkpoint){
        //manca implementazione
        return null;
    }

    public static List<Checkpoint> JSONtoCheckpointList(String JSONstring){

        try {

            //conversione JSON to List<Checkpoint>
            ObjectMapper mapper = new ObjectMapper();
            List<Checkpoint> checkpointList = new ArrayList<Checkpoint>();

            TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {};
            Map<String, Object> resultMap = mapper.readValue(JSONstring, typeRef);

            if(resultMap.get("error").equals(0)){

                List<Map<String, Object>> arrayMap = (List<Map<String, Object>>) resultMap.get("checkpoints");

                for (Map<String, Object> item : arrayMap) {

                    checkpointList.add(new Checkpoint(((Integer)item.get("id")).longValue(), (String)item.get("nome"), (String)item.get("in_out"), ((Boolean)item.get("attivo")).booleanValue(), (String)item.get("tipo"), ((Integer)item.get("childsize")).longValue(), (String)item.get("location")));

                }

                return checkpointList;

            } else {
                return null;
            }

        } catch(IOException e){
            e.printStackTrace();
        }

        return null;

    }

}
