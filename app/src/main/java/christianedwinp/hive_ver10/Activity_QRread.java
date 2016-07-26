package christianedwinp.hive_ver10;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;

import java.util.ArrayList;
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
    private static final String TAG = Activity_QRread.class.getSimpleName();

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        resetSet();
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    protected void onPause(){
        super.onPause();;
        resetSet();
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

            // save the data to preference
            SharedPreferences prefs = getSharedPreferences("DroneData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            resetSet();
            if (prefs.getStringSet("droneNames",defaultSet)== defaultSet){
                nameSet.add(productName);
                bssidSet.add(BSSID);
                Log.d(TAG, "NEW NAMESET :"+ nameSet);
                editor.putStringSet("droneNames", nameSet);
                Set<String> value = prefs.getStringSet("droneNames",defaultSet);
                Log.d(TAG, "NEW EDITOR NAMESET :"+ value);
                editor.putStringSet("droneBSSID", bssidSet);
                editor.apply();
            }
            else {
                nameSet = prefs.getStringSet("droneNames",defaultSet);
                bssidSet = prefs.getStringSet("droneBSSID",defaultSet);

                Log.d(TAG, "OLD EDITOR NAMESET :"+ nameSet);

                nameSet.add(productName);
                bssidSet.add(BSSID);
                editor.clear();
                editor.apply();

                Set<String> value = prefs.getStringSet("droneNames",defaultSet);
                Log.d(TAG, "AFTER CLEAR EDITOR NAMESET :"+ value);Log.d(TAG, "AFTER CLEAR NAMESET :"+ nameSet);

                editor.putStringSet("droneNames", nameSet);
                editor.putStringSet("droneBSSID", bssidSet);
                editor.apply();

                Set<String> value2 = prefs.getStringSet("droneNames",defaultSet);
                Log.d(TAG, "REINPUT EDITOR NAMESET :"+ value2);
            }
            //inform user success scan
            Toast.makeText(this, "You successfully add a new drone",  Toast.LENGTH_LONG).show();

            //Go back to Drone collection page and add notif +1
            Intent intent = new Intent(this, Activity_Main.class);
            intent.putExtra("ADD_DRONE", 1);
            startActivity(intent);
        }
        else {
            //show QR dialog if scan unsuccessful
            AlertDialog.Builder QR_dialog = new AlertDialog.Builder(this);
            QR_dialog.setTitle(R.string.QR_dialogTitle);
            QR_dialog.setMessage(R.string.QR_dialogMessage );
            //if yes, rescan QR code
            QR_dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    mScannerView.resumeCameraPreview(Activity_QRread.this);
                }
            });
            //if no, go back to drone collection
            QR_dialog.setNegativeButton("NO",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Activity_QRread.this, Activity_Main.class);
                    startActivity(intent);
                }
            });
            AlertDialog alertDialog = QR_dialog.create();
            alertDialog.show();
        }
    }

    public String getBSSID() {
        return BSSID;
    }

    public void resetSet(){
        nameSet.clear();
        bssidSet.clear();
    }

}
