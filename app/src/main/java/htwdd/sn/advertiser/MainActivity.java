package htwdd.sn.advertiser;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if( !BluetoothAdapter.getDefaultAdapter().isMultipleAdvertisementSupported() ) {
            Log.e( "BLE", "Multiple advertisement not supported");
        }

        this.advertise();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void advertise() {
        BluetoothLeAdvertiser advertiser = BluetoothAdapter.getDefaultAdapter().getBluetoothLeAdvertiser();

        AdvertiseSettings settings = (new AdvertiseSettings.Builder())
                .setConnectable(false)
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
                .build();

        ParcelUuid uuid = new ParcelUuid(UUID.fromString(getString(R.string.ble_uuid)));

        AdvertiseData data = (new AdvertiseData.Builder())
                .setIncludeDeviceName(true)
                .addServiceUuid(uuid)
                .addServiceData(uuid, "Moin".getBytes(StandardCharsets.UTF_8))
                .build();

        AdvertiseCallback callback = new AdvertiseCallback() {
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                super.onStartSuccess(settingsInEffect);
            }

            @Override
            public void onStartFailure(int errorCode) {
                Log.e( "BLE", "Advertising onStartFailure: " + errorCode );
                super.onStartFailure(errorCode);
            }
        };

        advertiser.startAdvertising(settings, data, callback);
    }
}