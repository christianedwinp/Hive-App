package christianedwinp.hive_ver10;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by ChristianEdwin on 13-Jun-16.
 */
public class adapter_drone extends FragmentStatePagerAdapter{

    private final ArrayList<Fragment> mFragmenDroneImageList = new ArrayList<>(); //mFragmentDroneImageList : ARRAY LIST OF FRAGMENT
    private final ArrayList<String> mFragmentDroneNameList = new ArrayList<>(); //mFragmentDroneNameList : ARRAY LIST OF STRING
    Context context;

    public adapter_drone(FragmentManager manager, Context context){
        super(manager);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmenDroneImageList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmenDroneImageList.size();
    }

//    logic to add drone model
    public void addFrag(Fragment droneimage, String dronename) {
        mFragmenDroneImageList.add(droneimage);
        mFragmentDroneNameList.add(dronename);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentDroneNameList.get(position);
    }

}
