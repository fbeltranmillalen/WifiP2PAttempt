package com.example.admin.wifip2pattempt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnOnOff, btnDiscover, btnSend;
    ListView listView;
    TextView read_msg_box, connectionStatus;
    EditText writeMsg;

    WifiManager wifiManager;

    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;

    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialWork();
        exqListener();
    }

    public void desactivandoWifiToast(){
        Toast.makeText(this, "Desactivando Wifi", Toast.LENGTH_SHORT).show();
        btnOnOff.setText("ON");
    }

    public void activandoWifiToast(){
        Toast.makeText(this, "Activando Wifi", Toast.LENGTH_SHORT).show();
        btnOnOff.setText("OFF");

    }

    private void exqListener() {

        btnOnOff.setOnClickListener(new View.OnClickListener(){//unir boton con listener
            @Override
            public void onClick(View v) {
                if(wifiManager.isWifiEnabled()){
                    wifiManager.setWifiEnabled(false);
                    desactivandoWifiToast();
                }
                else{
                    wifiManager.setWifiEnabled(true);
                    activandoWifiToast();
                }
            }
        });
    }

    private void initialWork(){
        btnOnOff = findViewById(R.id.onOff);
        btnDiscover = findViewById(R.id.discover);
        btnSend = findViewById(R.id.sendButton);
        listView = findViewById(R.id.peerListView);
        read_msg_box = findViewById(R.id.readMsg);
        connectionStatus = findViewById(R.id.connectionStatus);
        writeMsg = findViewById(R.id.writeMsg);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);

        mReceiver = new WifiDirectBroadcastReceiver(mManager, mChannel, this);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

}
