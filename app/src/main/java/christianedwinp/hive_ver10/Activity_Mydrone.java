package christianedwinp.hive_ver10;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ChristianEdwin on 13-Jun-16.
 */

public class Activity_Mydrone extends Fragment{
    public ViewPager viewPager;
    private Adapter_Mydrone adapter;
    Button buttonAddDrone, connectdrone;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.drone_activity, container, false);
        buttonAddDrone = (Button)view.findViewById(R.id.buttonAddPage);
        connectdrone = (Button)view.findViewById(R.id.connectdronebutton);

        viewPager = (ViewPager) view.findViewById(R.id.drone_pager);
        viewPager.setClipToPadding(false);
        viewPager.setPageMargin(12);
        adapter = new Adapter_Mydrone(getFragmentManager(), getActivity(), viewPager);
        viewPager.setAdapter(adapter);

        buttonClick();
        loadDroneData();

        return view;
    }

    private void buttonClick() {
        buttonAddDrone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),Activity_QRread.class);
                startActivity(intent);
            }
        });

        connectdrone.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity(),Activity_Shot_cam.class);
                startActivity(intent);
            }
        });
    }

    public void loadDroneData(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("DroneData", Context.MODE_PRIVATE);
        Set<String> droneRegName = sharedPreferences.getStringSet("droneNames",new HashSet<String>());

        for(String str : droneRegName) {
            Bundle bundle = new Bundle();
            bundle.putString("data", str);
            Fragment_Mydrone fragmentChild = new Fragment_Mydrone();
            fragmentChild.setArguments(bundle);
            adapter.addFrag(fragmentChild,str);
            adapter.notifyDataSetChanged();
        }

    }

}
