package christianedwinp.hive_ver10;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by ChristianEdwin on 23-Jul-16.
 */
public class Adapter_Mydrone extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> RegisterDroneFragments = new ArrayList<>();
    private final ArrayList<String> RegisterDroneNames = new ArrayList<>();
    Context context;
    ViewPager viewPager;

    public Adapter_Mydrone(FragmentManager manager, Context context, ViewPager viewPager) {
        super(manager);
        this.context = context;
        this.viewPager = viewPager;
    }

    @Override
    public Fragment getItem(int position) {
        return RegisterDroneFragments.get(position);
    }

    @Override
    public int getCount() {
        return RegisterDroneFragments.size();
    }


    public void addFrag(Fragment fragment, String name) {
        RegisterDroneFragments.add(fragment);
        RegisterDroneNames.add(name);
    }
    public void removeFrag(int position) {
        Fragment fragment = RegisterDroneFragments.get(position);
        RegisterDroneFragments.remove(fragment);
        RegisterDroneNames.remove(position);
        destroyFragmentView(viewPager, position, fragment);
        notifyDataSetChanged();
    }
    public void destroyFragmentView(ViewGroup container, int position, Object object) {
        FragmentManager manager = ((Fragment) object).getFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove((Fragment) object);
        trans.commit();
    }
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return RegisterDroneNames.get(position);
    }

}