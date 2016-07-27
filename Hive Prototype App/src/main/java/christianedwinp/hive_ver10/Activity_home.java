package christianedwinp.hive_ver10;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by ChristianEdwin on 12-Jun-16.
 */
public class Activity_home extends Fragment implements View.OnClickListener {
    private static Button goToGallery_button;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.home_activity,container,false);
        onClick(v);
        return v;
    }

    @Override
    public void onClick(View v) {
        goToGallery_button = (Button)v.findViewById(R.id.goToGallery);
        goToGallery_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),Activity_gallery_ver2.class);
                startActivity(intent);
            }
        });
    }
}
