package it.univaq.uffizigallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Riccardo on 26/03/2018.
 */

public class CheckpointHubActivity extends AppCompatActivity {

    private Button button1;
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub_checkpoint);
        button1 = findViewById(R.id.button4);
        button2 = findViewById(R.id.button5);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CameraTestActivity.class);
                view.getContext().startActivity(intent);
            }
        });

    }

}
