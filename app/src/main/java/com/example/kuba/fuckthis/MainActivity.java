package com.example.kuba.fuckthis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

    TextView mainText;
    WifiManager mainWifi;
    WifiReceiver receiverWifi = new WifiReceiver();
    List<ScanResult> wifiList;
    LocationComp locationComp = new LocationComp();
    ApRatioGuess ratio = new ApRatioGuess();
    FileLogger fileLogger;

    boolean requestStop = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mainText = (TextView) findViewById(R.id.mainText);
        mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "Refresh");

        return super.onCreateOptionsMenu(menu);
    }

    public void handleRecordButtonClick(View view) {
        registerReceiver(receiverWifi, new IntentFilter(
                        WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        );

        EditText editText = (EditText) findViewById(R.id.editText);

        String filename = "wifi_log.txt";
        if (editText.getText().length() > 0) {
            filename = editText.getText().toString();
        }

        fileLogger = new FileLogger(filename);

        requestStop = false;

        mainWifi.startScan();
        mainText.setText("\nStarting Scan...\n");
    }

    public void handleStopButtonClick(View view) throws IOException {
        requestStop = true;
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        mainWifi.startScan();
        mainText.setText("Starting Scan");

        return super.onMenuItemSelected(featureId, item);
    }

    protected void onPause() {
        unregisterReceiver(receiverWifi);

        super.onPause();
    }

    class WifiReceiver extends BroadcastReceiver {

        ScanResult extractMaxSignal(List<ScanResult> scanResultList) {
            int removeIndex = 0;

            double max = Double.POSITIVE_INFINITY;
            for (int i = 0; i < scanResultList.size(); i++) {
                if (max < scanResultList.get(i).level) {
                    max = scanResultList.get(i).level;
                    removeIndex = i;
                }
            }

            return scanResultList.remove(removeIndex);
        }

        public void onReceive(Context c, Intent intent) {
            ArrayList<Scan> scanArrayList = new ArrayList<>();

            wifiList = mainWifi.getScanResults();

            String labelText = "";
            for (int i = 0; i < wifiList.size(); i++) {
                if (wifiList.size() == 0) break;
                if (scanArrayList.size() >= 2) break;
                if (wifiList.get(i).frequency < 5000) continue;

                ScanResult result = extractMaxSignal(wifiList);

                Scan scan = new Scan(result.BSSID, result.level, result.frequency);
                scanArrayList.add(scan);

                labelText += scan.getBssid() + " - " + scan.getSignalLoss() + "db - " + (double) Math.round(locationComp.getDistance(scan) * 100) / 100 + "m\n";

                fileLogger.log(labelText);
            }

            if (!scanArrayList.isEmpty()) {
                System.out.println(locationComp.getLocation(scanArrayList).toString());
                ratio.addAll(scanArrayList);
            }

            mainText.setText(labelText);

            if (!MainActivity.this.requestStop) {
                mainWifi.startScan();
            } else {
                try {
                    MainActivity.this.fileLogger.flush();
                    MainActivity.this.mainText.setText("Scan complete!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}