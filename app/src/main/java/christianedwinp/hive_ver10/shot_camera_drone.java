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
public class shot_camera_drone extends Fragment {

    private Camera mCamera = null;
    private Surfaceview_camera mCameraView = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.shot_camera_drone, container, false);
        cameraSettings(view);
        return view;
    }

    public void cameraSettings(View view){
        try {
            mCamera = Camera.open();
        }
        catch (Exception e){
            Log.d("ERROR", "Failed to get camera: " + e.getMessage());
        }

        if(mCamera != null){
            mCameraView = new Surfaceview_camera(getActivity(), mCamera);
            FrameLayout camera_view = (FrameLayout)view.findViewById(R.id.camera_view);
            camera_view.addView(mCameraView);
        }
    }

}
