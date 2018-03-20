package it.univaq.uffizigallery.utils;

/**
 * Created by Riccardo on 20/03/2018.
 */

public class Server_API_Connection {
/*
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
        final String inout = obj.getString("in_out");
        final String barcode = obj.getString("barcode");
        final String dev_name = obj.getString("device_name");
        final String dev_id = obj.getString("device_imei");
        final String time = obj.getString("time");
        String type = obj.getString("type");
        final int id_checkpoint = obj.getInt("id_checkpoint");
        double latitude, longitude, accuracy;
        final Checkpoint checkpoint = this.checkpointservice.find((long) id_checkpoint);
        final Ticket ticket = new Ticket(inout, barcode, dev_name, dev_id);
        ticket.setType(type);
        ticket.settime(time);
        ticket.setCheckpoint(checkpoint);
        try {
            latitude = obj.getDouble("lat");
            longitude = obj.getDouble("lng");
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
        final int childize = checkpoint.getChildsize() + 1;
        return "{childsize:" + childize + ", error:0}";
    }*/
}
