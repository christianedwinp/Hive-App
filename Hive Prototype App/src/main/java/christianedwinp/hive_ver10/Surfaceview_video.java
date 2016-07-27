package christianedwinp.hive_ver10;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by ChristianEdwin on 16-Jun-16.
 */
public class Surfaceview_video extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;

    public Surfaceview_video(Context context, Camera camera){
        super(context);

        mCamera = camera;
        mCamera.setDisplayOrientation(90);
        //get the holder and set this class as the callback, so we can get camera data here
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    //    showing camera data
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        }
        catch (IOException e){
            Log.d("ERROR","Camera error on surfaceCreated"+e.getMessage());
        }
    }

    //    In case device changing rotation while showing camera
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //check if surface ready to get camera data
        if(mHolder.getSurface()==null) return;

        //stop momentarily camera data
        try { mCamera.stopPreview(); }
        catch (Exception e){}

        //recreate the camera view
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        }
        catch(IOException e){
            Log.d("ERROR","Camera error on surfaceCreated"+e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mCamera.release();
    }
}
