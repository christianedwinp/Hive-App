package christianedwinp.hive_ver10;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ChristianEdwin on 13-Jun-16.
 */

public class Activity_Mydrone extends Fragment{
    private ViewPager viewPager;
    private adapter_drone adapter;
    Button buttonAddPage;
    Button connectdrone;
    TextView textView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.mydrone_activity, container, false);
        getIDs(view);
        setEvents();
        return view;
    }

    private void getIDs(View view){
        buttonAddPage = (Button)view.findViewById(R.id.buttonAddPage);
        connectdrone = (Button)view.findViewById(R.id.connectdronebutton);
        textView = (TextView)view.findViewById(R.id.editTextPageName);
        viewPager = (ViewPager) view.findViewById(R.id.drone_pager); //get the view pager and make it into an object
        adapter = new adapter_drone(getFragmentManager(),getActivity()); //make a new adapter
        viewPager.setAdapter(adapter); // set the adapter of the viewpager
    }

    private void setEvents() {
        buttonAddPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!textView.getText().toString().equals("")) {
                    addPage(textView.getText() + "");
                    textView.setText("");
                } else {
                    Toast.makeText(getActivity(), "Page name is empty", Toast.LENGTH_SHORT).show();
                }
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

    public void addPage(String pagename) {
        Bundle bundle = new Bundle();
        bundle.putString("data", pagename);
        FragmentChild_mydrone_collection fragmentChild = new FragmentChild_mydrone_collection();
        fragmentChild.setArguments(bundle);
        adapter.addFrag(fragmentChild, pagename);
        adapter.notifyDataSetChanged();
        viewPager.setCurrentItem(adapter.getCount());
    }
}
