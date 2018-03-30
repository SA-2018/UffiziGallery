package it.univaq.uffizigallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import it.univaq.uffizigallery.model.Checkpoint;
import it.univaq.uffizigallery.services.CheckpointService;

/**
 * Created by Riccardo on 26/03/2018.
 */

public class CheckpointHubActivity extends AppCompatActivity {

    private Button button1;
    private Button button2;
    private TextView textview1, textview2, textview3, textview4, textview5, textview6, textview7, textview8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub_checkpoint);
        button1 = findViewById(R.id.button4);
        button2 = findViewById(R.id.button5);

        textview1 = findViewById(R.id.textview1);
        textview2 = findViewById(R.id.textview2);
        textview3 = findViewById(R.id.textview3);
        textview4 = findViewById(R.id.textview4);
        textview5 = findViewById(R.id.textview5);
        textview6 = findViewById(R.id.textview6);
        textview7 = findViewById(R.id.textview7);
        textview8 = findViewById(R.id.textview8);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CameraTestActivity.class);
                intent.putExtra("checkpoint", getIntent().getStringExtra("checkpoint"));
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        Checkpoint checkpoint = CheckpointService.JSONtoCheckpoint(getIntent().getStringExtra("checkpoint"));
        textview1.setText("NOME");
        textview1.setGravity(Gravity.CENTER);
        textview2.setText( checkpoint.getNome()!=null?checkpoint.getNome():"no data" );
        textview2.setGravity(Gravity.CENTER);

        textview3.setText("TIPO");
        textview3.setGravity(Gravity.CENTER);
        textview4.setText( checkpoint.getTipo()!=null?checkpoint.getTipo():"no data" );
        textview4.setGravity(Gravity.CENTER);

        textview5.setText("IN_OUT");
        textview5.setGravity(Gravity.CENTER);
        textview6.setText( checkpoint.getIn_out()!=null?checkpoint.getIn_out():"no data" );
        textview6.setGravity(Gravity.CENTER);

        textview7.setText("CHILDSIZE");
        textview7.setGravity(Gravity.CENTER);
        long childsize_value = checkpoint.getChildsize();

        textview8.setText(new Integer((int)childsize_value).toString());
        textview8.setGravity(Gravity.CENTER);

    }
}
