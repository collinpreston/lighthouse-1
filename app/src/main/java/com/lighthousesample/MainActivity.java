package com.lighthousesample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lighthouse.LIDAR;
import com.lighthouse.LidarDisplay;
import com.unstoppable.submitbuttonview.SubmitButton;

import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    LIDAR myLidar;
    LidarDisplay lidarDisplay;
    boolean isConnectedToLighthouse = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.buy:
                Intent buyBrowserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://curiolighthouse.wixsite.com/lighthouse/products"));
                startActivity(buyBrowserIntent);
                return true;
            case R.id.help:
                Intent helpBrowserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.curiolighthouse.com/support/help"));
                startActivity(helpBrowserIntent);
                return true;
            case R.id.integration_instructions:
                Intent integrationBrowserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.curiolighthouse.com/developers/android-integration"));
                startActivity(integrationBrowserIntent);
                return true;
            case R.id.about:
                Intent aboutBrowserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.curiolighthouse.com/support/faq"));
                startActivity(aboutBrowserIntent);
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // This is the graphical plotting of the LIDAR data.
        lidarDisplay = new LidarDisplay(this);
        ConstraintLayout linearLayout = findViewById(R.id.constraint_layout);
        linearLayout.addView(lidarDisplay);
        lidarDisplay.setLidarViewScaleRate(10);

        myLidar = new LIDAR(this, lidarDisplay);
        myLidar.setBluetoothBytePacketSize(2520);
        myLidar.setIntensityThresholdFilter(20);
        myLidar.setRpmThreshold(2300);

        // This is the button to start the LIDAR unit.
        final ImageView lighthouseTitleImgView = findViewById(R.id.lighthouse_title_imgView);
        final ImageView mainLogoImgView = findViewById(R.id.main_logo_imgView);
        final SubmitButton connectAndScanBtn = findViewById(R.id.connect_and_scan_btn);
        final ImageView directionArrowImgView = findViewById(R.id.directionArrowImgView);


        connectAndScanBtn.setOnClickListener(v -> {
            if (isConnectedToLighthouse) {
                myLidar.startLIDAR();
                connectAndScanBtn.doResult(true);
            } else {
                if (myLidar.connectToLIDAR()) {
                    if (myLidar.getOutStream() == null) {
                        connectAndScanBtn.doResult(false);
                    } else {
                        isConnectedToLighthouse = true;
                        myLidar.startLIDAR();
                        connectAndScanBtn.doResult(true);
                        lidarDisplay.setVisibility(View.VISIBLE);
                        lighthouseTitleImgView.setVisibility(View.INVISIBLE);
                        mainLogoImgView.setVisibility(View.INVISIBLE);
                        directionArrowImgView.setVisibility(View.VISIBLE);
                    }
                } else {
                    connectAndScanBtn.reset();
                }
            }
        });


    }

}
