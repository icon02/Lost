package com.lost.lost;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lost.lost.javaRes.bluetooth.ChatController;


import java.util.ArrayList;
import java.util.Set;

public class EmergencyActivity extends AppCompatActivity {

    public static final String EMERGENCY_SEQ = "Lost.App.Help:";

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_OBJECT = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final String DEVICE_OBJECT = "device_name";

    private static final int REQUEST_ENABLE_BLUETOOTH = 1;

    private TextView status;
    private Button btnConnect;
    private ListView listView;
    private Dialog dialog;
    private TextInputLayout inputLayout;
    private ArrayAdapter<String> chatAdapter;
    private ArrayList<String> chatMessages;
    private BluetoothAdapter bluetoothAdapter;

    private ChatController chatController;
    private BluetoothDevice connectingDevice;
    private ArrayAdapter<String> discoveredDevicesAdapter;

    private boolean wifiEnabledAtStart = false;

    private String uid;
    private LatLng myPos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        Bundle bundle = getIntent().getExtras();
        uid = (String) bundle.get(MainActivity.KEY_UID);
        myPos = (LatLng) bundle.get(MainActivity.KEY_POS);

        status = (TextView) findViewById(R.id.status);
        btnConnect = (Button) findViewById(R.id.btn_connect);
        listView = (ListView) findViewById(R.id.list);
        inputLayout = (TextInputLayout) findViewById(R.id.input_layout);
        View btnSend = findViewById(R.id.btn_send);

        btnSend.setOnClickListener((v) -> {
            if (inputLayout.getEditText().getText().toString().equals("")) {
                Toast.makeText(EmergencyActivity.this, "Please write something", Toast.LENGTH_SHORT).show();
            } else {
                //TODO: here
                sendMessage(inputLayout.getEditText().getText().toString());
                inputLayout.getEditText().setText("");
            }
        });

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth isn't available on your device", Toast.LENGTH_LONG).show();
            finish();
        }

        btnConnect.setOnClickListener((v) -> {
            //TODO makeBTDeviceVisible();
            showPrinterPickDialog();
        });

        setTitle("Emergency");

        chatMessages = new ArrayList<>();
        chatAdapter = new ArrayAdapter<>(this, R.layout.simple_list_item_1, chatMessages);
        listView.setAdapter(chatAdapter);

        //TODO wifi should be able to run while connecting via bluetooth
        /*
        WifiManager m = (WifiManager) this.getSystemService(Context.WIFI_SERVICE); //TODO check permissions
        if (m.isWifiEnabled()) {
            wifiEnabledAtStart = true;
            m.setWifiEnabled(false);
        }
        */
    }

    @SuppressWarnings("unused")
    private void makeToastLong(String text) {
        Looper.prepare();
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case ChatController.STATE_CONNECTED:
                            setStatus("Connected to: " + connectingDevice.getName());
                            btnConnect.setEnabled(false);
                            break;
                        case ChatController.STATE_CONNECTING:
                            setStatus("Connecting...");
                            btnConnect.setEnabled(false);
                            break;
                        case ChatController.STATE_LISTEN:
                        case ChatController.STATE_NONE:
                            setStatus("Not connected");
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;

                    String writeMessage = new String(writeBuf);
                    chatMessages.add("Me: " + writeMessage);
                    chatAdapter.notifyDataSetChanged();
                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;



                    String readMessage = new String(readBuf, 0, msg.arg1);
                   /* if(readMessage.substring(0, EMERGENCY_SEQ.length()).equals(EMERGENCY_SEQ)) {
                        //TODO wirte to database
                        String eMsg = readMessage.substring(EMERGENCY_SEQ.length());
                        String uid = getID(eMsg);

                        LatLng location = getLatLngFromMsg(eMsg);


                        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Location");
                        db.setValue(location);
                        Log.i("EmergencyActivity", "recieved Emergency Message and put to server.");
                    } else {*/
                        chatMessages.add(connectingDevice.getName() + ":  " + readMessage);
                        chatAdapter.notifyDataSetChanged();
                    //}
                    break;
                case MESSAGE_DEVICE_OBJECT:
                    connectingDevice = msg.getData().getParcelable(DEVICE_OBJECT);
                    Toast.makeText(getApplicationContext(), "Connected to " + connectingDevice.getName(),
                            Toast.LENGTH_SHORT).show();
                    sendMessage(createEmergencyMessage(uid, myPos));
                    Log.i("EmergencyActivity", "Sent Emergency Message");

                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString("toast"),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });

    private String getID(String msg) {
        int startIndex = 0;
        while(msg.charAt(startIndex) != '%') startIndex++;
        StringBuffer output = new StringBuffer();
        ++startIndex;
        while(msg.charAt(startIndex) != '%') output.append(msg.charAt(startIndex++));
        return output.toString();
    }
    private LatLng getLatLngFromMsg(String msg) {
        int startIndex = 0;
        while(msg.charAt(startIndex) != '$') startIndex++;
        StringBuffer latS = new StringBuffer();
        ++startIndex;
        while(msg.charAt(startIndex) != '$') latS.append(msg.charAt(startIndex++));

        while(msg.charAt(startIndex) != '(') startIndex++;
        StringBuffer longS = new StringBuffer();
        ++startIndex;
        while(msg.charAt(startIndex) != '(') longS.append(msg.charAt(startIndex++));
        LatLng pos = new LatLng(Double.parseDouble(latS.toString()), Double.parseDouble(longS.toString()));


        return pos;
    }

    private String createEmergencyMessage(String uid, LatLng pos) {
        return EMERGENCY_SEQ + "%" + uid + "%$" + pos.latitude + "$(" + pos.longitude + "(";
    }

    private void showPrinterPickDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_bluetooth);
        dialog.setTitle("Bluetooth Devices");

        if (bluetoothAdapter.isDiscovering()) bluetoothAdapter.cancelDiscovery();
        bluetoothAdapter.startDiscovery();

        ArrayAdapter<String> pairedDevicesAdapter = new ArrayAdapter<>(this, R.layout.simple_list_item_1);
        discoveredDevicesAdapter = new ArrayAdapter<>(this, R.layout.simple_list_item_1);

        ListView listView = dialog.findViewById(R.id.pairedDeviceList);
        ListView listView2 = dialog.findViewById(R.id.discoveredDeviceList);
        listView.setAdapter(pairedDevicesAdapter);
        listView2.setAdapter(discoveredDevicesAdapter);


        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(discoveryFinishReceiver, filter);

        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(discoveryFinishReceiver, filter);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                pairedDevicesAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            pairedDevicesAdapter.add("No devices are paired.");
        }

        listView.setOnItemClickListener((parent, view, position, id) -> {
            bluetoothAdapter.cancelDiscovery();
            String info = ((TextView) view).getText().toString();
            String address = info.substring(info.length() - 17);
            connectToDevice(address);
            dialog.dismiss();
        });

        listView2.setOnItemClickListener((adapterView, view, i, l) -> {
            bluetoothAdapter.cancelDiscovery();
            String info = ((TextView) view).getText().toString();
            String address = info.substring(info.length() - 17);

            connectToDevice(address);
            dialog.dismiss();
        });

        dialog.findViewById(R.id.cancelButton).setOnClickListener((v) -> {
            dialog.dismiss();
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    private void setStatus(String s) {
        status.setText(s);
    }

    private void connectToDevice(String deviceAddress) {
        bluetoothAdapter.cancelDiscovery();
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
        chatController.connect(device);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BLUETOOTH:
                if (resultCode == Activity.RESULT_OK) {
                    chatController = new ChatController(this, handler);
                } else {
                    Toast.makeText(this, "Bluetooth still disabled!", Toast.LENGTH_LONG).show();
                }
        }
    }

    private void sendMessage(String message) {
        if (chatController.getState() != ChatController.STATE_CONNECTED) {
            Toast.makeText(this, "Connection was lost!", Toast.LENGTH_LONG).show();
            //TODO what happens if bt connection was lost
            return;
        }

        if (message.length() > 0) {
            byte[] send = message.getBytes();
            chatController.write(send);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);
        } else {
            chatController = new ChatController(this, handler);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (chatController != null) {
            if (chatController.getState() == ChatController.STATE_NONE) {
                chatController.start();
            }
        }
    }

    @Override
    public void onDestroy() {
        /*
        if(wifiEnabledAtStart) {
            WifiManager m = (WifiManager) this.getSystemService(Context.WIFI_SERVICE); //TODO check permissions
            if (!m.isWifiEnabled()) {
                m.setWifiEnabled(true);
            }
        }
        */
        super.onDestroy();
        if (chatController != null)
            chatController.stop();
    }

    private final BroadcastReceiver discoveryFinishReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    discoveredDevicesAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (discoveredDevicesAdapter.getCount() == 0) {
                    discoveredDevicesAdapter.add(getString(R.string.none_found));
                }
            }
        }
    };


}
