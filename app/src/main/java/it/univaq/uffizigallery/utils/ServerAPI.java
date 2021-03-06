package it.univaq.uffizigallery.utils;

/**
 * Created by Riccardo on 20/03/2018.
 */

import android.content.Context;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.univaq.uffizigallery.model.Checkpoint;
import it.univaq.uffizigallery.model.Ticket;
import it.univaq.uffizigallery.services.CheckpointService;
import it.univaq.uffizigallery.services.TicketService;

public class ServerAPI {

    private CheckpointService checkpointservice;
    private TicketService ticketservice;
    private Context context;

    public ServerAPI(Context context){
        this.context = context;
        this.checkpointservice = new CheckpointService();
        this.ticketservice = new TicketService(this.context);
    }

    public ServerAPI(Checkpoint checkpoint, Context context){
        this.context = context;
        this.checkpointservice = new CheckpointService(checkpoint);
        this.ticketservice = new TicketService(this.context);
    }

    @RequestMapping(value = { "/checkpoint/get" }, method = { RequestMethod.GET, RequestMethod.POST }, produces = {"application/json" })
    @ResponseBody
    public String getCheckpointActive() throws IOException, JSONException {
        final Map<String, Object> map = new HashMap<String, Object>();
        final List<Checkpoint> checkpoints = (List<Checkpoint>) this.checkpointservice.findActive();
        map.put("checkpoints", checkpoints);
        map.put("error", 0);
        final ObjectMapper objectMapper = new ObjectMapper();
        final StringWriter stringWriter = new StringWriter();
        objectMapper.writeValue((Writer) stringWriter, (Object) map);
        return stringWriter.toString();
    }

    @RequestMapping(value = { "/ticket/add" }, method = { RequestMethod.POST })
    @ResponseBody
    public String ticketAdd(@RequestParam final String json) throws IOException, JSONException {
        final JSONObject obj = new JSONObject(json);
        final String in_out = obj.getString("in_out");
        final String barcode = obj.getString("barcode");
        final String dev_name = obj.getString("device_name");
        final String dev_imei = obj.getString("device_imei");
        final String time = obj.getString("time");
        String type = obj.getString("tipo");
        final int id_checkpoint = obj.getInt("id_checkpoint");
        double latitude, longitude, accuracy;
        final Checkpoint checkpoint = this.checkpointservice.find((long) id_checkpoint);
        final Ticket ticket = new Ticket(in_out, barcode, dev_name, dev_imei);
        ticket.setTipo(type);
        ticket.setTime(time);
        ticket.setCheckpoint(checkpoint);
        try {
            latitude = obj.getDouble("latitude");
            longitude = obj.getDouble("longitude");
            accuracy = obj.getDouble("accuracy");
        } catch(JSONException e) {
            latitude = 0;
            longitude = 0;
            accuracy = 0;
        }
        ticket.setLatitude(latitude);
        ticket.setLongitude(longitude);
        ticket.setAccuracy(accuracy);
        this.ticketservice.insert(ticket);
        final long childize = checkpoint.getChildsize() + 1;
        return "{childsize:" + childize + ", error:0}";
    }
}
