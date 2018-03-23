package it.univaq.uffizigallery;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.univaq.uffizigallery.model.Checkpoint;
import it.univaq.uffizigallery.utils.CheckpointService;
import it.univaq.uffizigallery.utils.ServerAPI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("yolo");
        progress.setMessage("Canchero ");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.setIndeterminate(true);
        progress.show();
/*
        List<String> a = new ArrayList<String>();
        a.add("yolo1");
        a.add("yolo2");
        a.add("yolo2");
        a.add("yolo2");
        a.add("yolo2");
        a.add("yolo2");
        a.add("yolo2");
        a.add("yolo2");
        a.add("yolo2");

        CustomAdapter adapter = new CustomAdapter(this, R.layout.rowcustom, list);
        listView.setAdapter(adapter);

        Adapter adapter = new Adapter(getApplicationContext(), data);
        ListView myList = findViewById(R.id.checkpoint_list);
        myList.setAdapter(adapter);
*/
    }
}
