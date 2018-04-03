package it.univaq.uffizigallery;

/**
 * Created by valen on 27/03/2018.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import it.univaq.uffizigallery.services.BackgroundUpload;
import it.univaq.uffizigallery.services.Services;
import it.univaq.uffizigallery.utils.ZBarAPI;

/* Import ZBar Class files */

public class CameraTestActivity extends AppCompatActivity
{
    private Camera mCamera;
    private ZBarAPI mPreview;
    private Handler autoFocusHandler;

    //TextView scanText;
    Button scanButton;

    ImageScanner scanner;

    private boolean barcodeScanned = false;
    private boolean previewing = true;

    static {
        System.loadLibrary("iconv");
    }

    public static final String ACTION_UPLOAD_COMPLETED = "action_upload_completed";
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent == null || intent.getAction() == null) return;

            switch (intent.getAction()){
                case ACTION_UPLOAD_COMPLETED:
                    // upload action
                    Intent intent_upload = new Intent(getApplicationContext(), BackgroundUpload.class);
                    intent_upload.setAction(BackgroundUpload.ACTION_UPLOAD);
                    intent_upload.putExtra("source", "Camera");
                    startService(intent_upload);
                    break;
            }
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_zbar_scan);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        autoFocusHandler = new Handler();
        mCamera = getCameraInstance();

        /* Instance barcode scanner */
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        mPreview = new ZBarAPI(this, mCamera, previewCb, autoFocusCB);
        FrameLayout preview = (FrameLayout)findViewById(R.id.cameraPreview);
        preview.addView(mPreview);

        scanButton = (Button)findViewById(R.id.ScanButton);

        scanButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (barcodeScanned) {
                    barcodeScanned = false;
                    //scanText.setText("Scanning...");
                    mCamera.setPreviewCallback(previewCb);
                    mCamera.startPreview();
                    previewing = true;
                    mCamera.autoFocus(autoFocusCB);
                }
            }
        });
    }

    public void onPause() {
        super.onPause();
        releaseCamera();
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e){
        }
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };

    PreviewCallback previewCb = new PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Parameters parameters = camera.getParameters();
            Size size = parameters.getPreviewSize();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            int result = scanner.scanImage(barcode);

            if (result != 0) {
                previewing = false;
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();

                SymbolSet syms = scanner.getResults();
                for (Symbol sym : syms) {
                    //intent for Services
                    Intent intent = new Intent(getApplicationContext(), Services.class);
                    intent.setAction(Services.ACTION_READ_BARCODE_COMPLETED);
                    intent.putExtra("barcode", sym.getData());
                    intent.putExtra("checkpoint", getIntent().getStringExtra("checkpoint"));
                    intent.putExtra("latitude", getIntent().getStringExtra("latitude"));
                    intent.putExtra("longitude", getIntent().getStringExtra("longitude"));
                    intent.putExtra("accuracy", getIntent().getStringExtra("accuracy"));

                    startService(intent);

                    Toast.makeText(getApplicationContext(), "Sending data..." , Toast.LENGTH_SHORT).show();

                    barcodeScanned = true;
                }
            }
        }
    };

    // Mimic continuous auto-focusing
    AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        //uploading
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_UPLOAD_COMPLETED);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver, filter);

        // upload action
        Intent intent_upload = new Intent(getApplicationContext(), BackgroundUpload.class);
        intent_upload.setAction(BackgroundUpload.ACTION_UPLOAD);
        intent_upload.putExtra("source", "Camera");
        startService(intent_upload);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(receiver);
    }

}
