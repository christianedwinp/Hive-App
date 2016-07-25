package christianedwinp.hive_ver10;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;

import java.util.HashSet;
import java.util.Set;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by ChristianEdwin on 22-Jul-16.
 */
public class Activity_QRread extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    String scanResult;
    public String BSSID;
    Set<String> nameSet = new HashSet<>();
    Set<String> bssidSet = new HashSet<>();
    Set<String> defaultSet = new HashSet<>();

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    protected void onPause(){
        super.onPause();;
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        Log.v("handleResult", result.getText());
        //result format :XX:XX:XX:XX:XX:XX-PRODUCTNAME-Senseas Inc
        if(result.getText().endsWith("Senseas Inc")) {
            scanResult = result.getText();
            //sort the result
            String[] parts = scanResult.split("-");
            BSSID = parts[0]; //xx:xx:xx:xx:xx:xx
            String productName = parts[1]; //product name


            /* //shared preference for single time
            SharedPreferences sharedPreferences = getSharedPreferences("DroneData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("droneName",productName);
            editor.putString("droneBSSID", BSSID);
            editor.apply();*/


            // save the data to preference
            SharedPreferences prefs = getSharedPreferences("DroneData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            if (prefs.getStringSet("droneNames",defaultSet)== defaultSet){
                nameSet.add(productName);
                bssidSet.add(BSSID);
                editor.putStringSet("droneNames", nameSet);
                editor.putStringSet("droneBSSID", bssidSet);
                editor.apply();
            }
            else {
                nameSet = prefs.getStringSet("droneNames",defaultSet);
                bssidSet = prefs.getStringSet("droneBSSID",defaultSet);
                nameSet.add(productName);
                bssidSet.add(BSSID);
                editor.putStringSet("droneNames", nameSet);
                editor.putStringSet("droneBSSID", bssidSet);
                editor.apply();
            }

            //inform user success scan
            Toast.makeText(this, "You successfully add a new drone",  Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(Activity_QRread.this, "Non-compatible QR code", Toast.LENGTH_SHORT).show();
        }
    }

    public String getBSSID() {
        return BSSID;
    }

}
