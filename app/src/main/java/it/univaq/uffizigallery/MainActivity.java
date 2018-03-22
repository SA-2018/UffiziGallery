package it.univaq.uffizigallery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import it.univaq.uffizigallery.utils.Connection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Connection connection = new Connection();
        connection.connectionToServer();
    }
}
