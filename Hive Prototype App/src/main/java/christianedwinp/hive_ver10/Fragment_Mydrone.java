package christianedwinp.hive_ver10;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by ChristianEdwin on 14-Jun-16.
 */
public class Fragment_Mydrone extends Fragment{
    String drone_name;
    ImageView dronecollection;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drone_layout, container, false);
        Bundle bundle = getArguments();
        drone_name = bundle.getString("data");
        getIDs(view);
        setEvents();
        return view;
    }

    private void getIDs(View view) {
        dronecollection = (ImageView) view.findViewById(R.id.drone_image);
        if (drone_name.equalsIgnoreCase("HIVE")){
            dronecollection.setImageResource(R.drawable.drone1_sample);
        }
        else if (drone_name.equalsIgnoreCase("CUAUV")) {
            dronecollection.setImageResource(R.drawable.drone2_sample);
        }
        else if (drone_name.equalsIgnoreCase("SONIA")) {
            dronecollection.setImageResource(R.drawable.drone3_sample);
        }
        else {
            dronecollection.setImageResource(R.drawable.add_drone);
        }
    }

    private void setEvents() {

    }
}
