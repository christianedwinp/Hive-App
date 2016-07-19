package christianedwinp.hive_ver10;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by ChristianEdwin on 15-Jun-16.
 */
public class shot_video_drone extends Fragment {

    private Camera mVideo = null;
    private Surfaceview_video mVideoView = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.shot_video_drone, container, false);
        videoSettings(view);
        return view;
    }

    public void videoSettings(View view){
        try {
            mVideo = Camera.open();
        }
        catch (Exception e){
            Log.d("ERROR", "Failed to get camera: " + e.getMessage());
        }

        if(mVideo != null){
            mVideoView = new Surfaceview_video(getActivity(),mVideo);
            FrameLayout video_view = (FrameLayout)view.findViewById(R.id.video_view);
            video_view.addView(mVideoView);
        }
    }
}
