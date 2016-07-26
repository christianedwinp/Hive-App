package christianedwinp.hive_ver10;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import im.ene.lab.toro.Toro;

/**
 * Created by ChristianEdwin on 12-Jun-16.
 */
public class Activity_gallery_ver2 extends AppCompatActivity {
    private WifiManager mainWifi;
    private WifiReceiver receiverWifi;
    private int droneRegNumber = getIntent().getIntExtra("DRONE_REG_NUMBER", 0);

    //Router information
    String routerName = "FrisbeeAP";
    //FrisbeeAP BSSID : 7C:DD:90:86:98:18
    String desiredMacAddress;
    String routerPass = "hkust1019";
    String routerSecurity = "WPA2";
    String routerSecurityDetails = "NETWORK_ADDITIONAL_SECURITY_AES";
    //To get the the correct BSSID number
    public String getBSSID(int i){
        SharedPreferences prefs = getSharedPreferences("DroneData", Context.MODE_PRIVATE);
        Set<String> bssidSet = prefs.getStringSet("droneBSSID",new HashSet<String>());
        ArrayList<String> desiredBSSID = new ArrayList<>();
        for(String str : bssidSet) {
            desiredBSSID.add(str);
        }
        return desiredBSSID.get(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.gallery_activity);
        Toro.attach(this);
        desiredMacAddress = getBSSID(droneRegNumber);

        mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        receiverWifi = new WifiReceiver();
        AlertDialog.Builder WiFiDialog = new AlertDialog.Builder(this);
        //scan available wifi connection
        registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        if(!mainWifi.isWifiEnabled()){
            //Ask permission to turn on wifi and connect to drone automatically
            WiFiDialog.setTitle(R.string.wifi_dialogTitle);
            WiFiDialog.setMessage(R.string.wifi_dialogMessage);
            WiFiDialog.setPositiveButton("AGREE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    addWifiConfig(mainWifi,routerName,desiredMacAddress,routerPass,routerSecurity,routerSecurityDetails);
                }
            });
            WiFiDialog.setNegativeButton("DISAGREE",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(Activity_gallery_ver2.this,"Can't sync data from drone",Toast.LENGTH_LONG).show();
                }
            });
            AlertDialog alertDialog = WiFiDialog.create();
            alertDialog.show();
        }
        else{
            //Check Wifi is connected to drone router
            if(checkConnectedToDesiredWifi(mainWifi)){
                Toast toast = Toast.makeText(getApplicationContext(),"Syncing with drone", Toast.LENGTH_LONG);
                toast.show();
            }
            else {
                //Wifi is on but connected to other network, asking permission to change network
                WiFiDialog.setTitle(R.string.wifi_dialogTitle);
                WiFiDialog.setMessage(R.string.wifi_dialogMessage);
                WiFiDialog.setPositiveButton("AGREE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //disconnect current connection
                        mainWifi.disconnect();
                        //make connection to router
                        addWifiConfig(mainWifi,routerName,desiredMacAddress,routerPass,routerSecurity,routerSecurityDetails);
                    }
                });
                WiFiDialog.setNegativeButton("DISAGREE",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(Activity_gallery_ver2.this,"Can't sync data from drone",Toast.LENGTH_LONG).show();
                    }
                });
                AlertDialog alertDialog = WiFiDialog.create();
                alertDialog.show();
            }
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.gallery_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("Gallery");
            ab.setDisplayHomeAsUpEnabled(true);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.gallery_pager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.gallery_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        //setting the tab style
        tabLayout.setTabTextColors(ContextCompat.getColorStateList(this, R.drawable.tab_selector));
        //setting tab indicator
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.tab_indicator_white));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gallery_toolbar_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings1) {
            return true;
        }
        if(id == R.id.action_search){
            Toast.makeText(Activity_gallery_ver2.this, "Search", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onDestroy() {
        Toro.detach(this);
        super.onDestroy();
    }
    @Override
    protected void onPause() {
        unregisterReceiver(receiverWifi);
        super.onPause();
    }
    @Override
    protected void onResume() {
        registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    class WifiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new gallery_image(), "Photos");
        adapter.addFragment(new gallery_video(), "Video");
        viewPager.setAdapter(adapter);
    }

    //Sliding Tab adapter
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    //Checking connection to desired Wifi
    private boolean checkConnectedToDesiredWifi(WifiManager wifiManager) {
        boolean connected = false;
        WifiInfo wifi = wifiManager.getConnectionInfo();
        if (wifi != null) {
            // get current router Mac address
            String bssid = wifi.getBSSID();
            connected = desiredMacAddress.equalsIgnoreCase(bssid);
        }
        return connected;
    }

    //Wifi Config
    public void addWifiConfig(WifiManager mainWifi, String ssid,String bssid, String password,String securityParam,String securityDetailParam) {
        if (ssid == null || bssid == null) {
            throw new IllegalArgumentException("Required parameters can not be NULL #");
        }
        WifiConfiguration conf = new WifiConfiguration();
        // On devices with version Kitkat and below, We need to send SSID name
        // with double quotes. On devices with version Lollipop, We need to send
        // SSID name without double quotes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            conf.SSID = ssid;
            conf.BSSID = bssid;
        } else {
            conf.SSID = "\"" + ssid + "\"";
            conf.BSSID= "\"" + bssid + "\"";
        }

        if (securityParam.equalsIgnoreCase("WEP")) {
            conf.wepKeys[0] = password;
            conf.wepTxKeyIndex = 0;
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        } else if (securityParam.equalsIgnoreCase("NONE")) {
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        } else if ("WPA".equalsIgnoreCase(securityParam)|| "WPA2".equalsIgnoreCase(securityParam)|| "WPA/WPA2 PSK".equalsIgnoreCase(securityParam)) {
            // appropriate ciper is need to set according to security type used,
            // ifcase of not added it will not be able to connect
            conf.preSharedKey = "\""+ password +"\"";
            conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            conf.status = WifiConfiguration.Status.ENABLED;
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        }

        if (securityDetailParam.equalsIgnoreCase("NETWORK_ADDITIONAL_SECURITY_TKIP")){
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        } else if (securityDetailParam.equalsIgnoreCase("NETWORK_ADDITIONAL_SECURITY_AES")){
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        } else if (securityDetailParam.equalsIgnoreCase("NETWORK_ADDITIONAL_SECURITY_WEP")){
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        } else if (securityDetailParam.equalsIgnoreCase("NETWORK_ADDITIONAL_SECURITY_NONE")){
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.NONE);
        }
        int newNetworkId = mainWifi.addNetwork(conf);
        mainWifi.enableNetwork(newNetworkId, true);
        mainWifi.saveConfiguration();
        mainWifi.setWifiEnabled(true);
        mainWifi.reconnect();
    }

}
