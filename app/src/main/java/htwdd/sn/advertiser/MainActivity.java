package htwdd.sn.advertiser;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if( !BluetoothAdapter.getDefaultAdapter().isMultipleAdvertisementSupported() ) {
            Log.e( "BLE", "Multiple advertisement not supported");
            return;
        }

        this.textView = findViewById(R.id.textView);

        this.advertise();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void advertise() {
        BluetoothLeAdvertiser advertiser = BluetoothAdapter.getDefaultAdapter().getBluetoothLeAdvertiser();

        AdvertiseSettings settings = (new AdvertiseSettings.Builder())
                .setConnectable(false)
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
                .build();

        ParcelUuid uuid = new ParcelUuid(UUID.fromString(getString(R.string.ble_uuid)));

        AdvertiseData data = (new AdvertiseData.Builder())
                .addServiceUuid(uuid)
                .build();

        AdvertiseCallback callback = new AdvertiseCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                textView.setText(R.string.advertsing);
                super.onStartSuccess(settingsInEffect);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onStartFailure(int errorCode) {
                textView.setText(getString(R.string.start_failed) + errorCode);
                super.onStartFailure(errorCode);
            }
        };

        advertiser.startAdvertising(settings, data, callback);
    }
}