package christianedwinp.hive_ver10;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import im.ene.lab.toro.Toro;
import im.ene.lab.toro.ToroVideoViewHolder;
import im.ene.lab.toro.widget.ToroVideoView;

/**
 * Created by ChristianEdwin on 08-Jul-16.
 */
public class gallery_video extends Fragment{
    private File[] unsorted_listfile;
    private ArrayList<String> FilePathStrings = new ArrayList<>();
    private ArrayList<File> sorted_listFile = new ArrayList<>();
    File file;

    private RecyclerView recyclerView;
    private adapter_gallery adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.gallery_view_activity, container, false);

        // Check for SD Card presence and if the memory is SD card (not phone memory)
        if (!EnvironmentCompat.getStorageState(new File("/storage/extSdCard")).equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(getActivity(), "Error! No SDCARD Found!", Toast.LENGTH_LONG).show();
        } else {
            // Locate the image folder in your SD Card
            file = new File(Environment.getExternalStorageDirectory() + "/The Hive Image");
            // Create a new folder if no folder named The Hive Image exist
            System.out.println("State: " + file.mkdir());
        }

        //Retrieve and sorting image data only
        if (file.isDirectory()) {
            unsorted_listfile = file.listFiles();

            //Retrieve and sorting video data only
            for (int i = 0; i < unsorted_listfile.length; i++) {
                String fileName = unsorted_listfile[i].getName();
                CharSequence mpg = ".mpg"; CharSequence mp4 = ".mp4"; CharSequence avi = ".avi";CharSequence wmv = ".wmv";

                if(fileName.toLowerCase().contains(mpg) || fileName.toLowerCase().contains(mp4) || fileName.toLowerCase().contains(avi)||fileName.toLowerCase().contains(wmv) ){
                    sorted_listFile.add(unsorted_listfile[i]);
                    FilePathStrings.add(unsorted_listfile[i].getAbsolutePath());
                }
            }
        }

        //setting recycleview to grid type
        recyclerView = (RecyclerView) layout.findViewById(R.id.gallery_gridview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),3); //3 indicate number of grid column you want
        recyclerView.setLayoutManager(layoutManager);

        //attach adapter to recyleview
        adapter = new adapter_gallery(getActivity(),sorted_listFile);
        recyclerView.setAdapter(adapter);

        //attach item decoration to grid recycleview
        recyclerView.addItemDecoration(new ItemDecorationSettings(
                getResources().getDimensionPixelSize(R.dimen.photos_list_spacing),
                getResources().getInteger(R.integer.photo_list_preview_columns),
                getResources().getInteger(R.integer.photo_list_top_offset),
                getResources().getInteger(R.integer.photo_list_bottom_offset), 3));//3 indicate number of grid column you want

        //attach on touch event to grid recyleview
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {
                Toast.makeText(getActivity(), "click!", Toast.LENGTH_LONG).show();
            }
        }));

        //Show the layout use : gallery_video_activity.xmlt.xml
        return layout;
    }
    @Override public void onResume() {
        super.onResume();
        Toro.register(recyclerView);
    }
    @Override public void onPause() {
        Toro.unregister(recyclerView);
        super.onPause();
    }

    //Grid RecycleView Adapter
    class adapter_gallery extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList files;
        private LayoutInflater inflater;

        /* THE CONSTRUCTOR */
        public adapter_gallery(Context context, ArrayList files){
            this.files = files;
        }

        /* VIEW HOLDER TYPES */
        //Video data view holder, this use Toro library
        public class VideoHolder extends ToroVideoViewHolder {
            TextView videoTime;
            public VideoHolder(View itemView) {
                super(itemView);
                //caption = (TextView) itemView.findViewById(R.id.text); >> keep in case need text underneath video
                videoTime = (TextView) itemView.findViewById(R.id.videoTime);
            }

            @Override
            public void bind(@Nullable Object object) {
            }

            @Override
            protected ToroVideoView findVideoView(View itemView) {
                return (ToroVideoView) itemView.findViewById(R.id.video);
            }

            @Nullable
            @Override
            public String getVideoId() {
                return "video's id and it's order:" + getAdapterPosition();
            }
        }

        /* NECESSARY PUBLIC FUNCTIONS FOR RECYCLEVIEW */
        //to determine the number of items
        @Override
        public int getItemCount() {
            return files.size();
        }

        //To inflate item layout and create holder
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder;
            Context context = parent.getContext();
            inflater=LayoutInflater.from(context);
            View videoLayoutView = inflater.inflate(R.layout.gallery_grid_video_layout, parent, false);
            viewHolder = new VideoHolder(videoLayoutView);
            return viewHolder;
        }

        //to populate the data into selected view holder
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            //holder.caption.setText(files[position].getName()); >> keep in case need text underneath image
            VideoHolder vh = (VideoHolder) viewHolder;
            vh.bind(this);
        }

        // Toro requires this method to return item's unique Id.
        @Override public long getItemId(int position) {
            return position;
        }
    }

    /* ITEM DECORATION FOR GRID RECYCLEVIEW */
    class ItemDecorationSettings extends RecyclerView.ItemDecoration {

        private int mSizeGridSpacingPx;
        private int mGridSize;
        private int mTopOffset;
        private int mBottomOffset;
        private int mNumColumns;

        private boolean mNeedLeftSpacing = false;

        public ItemDecorationSettings(int gridSpacingPx, int gridSize, int topOffset, int bottomOffset, int numColumn) {
            mSizeGridSpacingPx = gridSpacingPx;
            mGridSize = gridSize;
            mTopOffset = topOffset;
            mBottomOffset = bottomOffset;
            mNumColumns = numColumn;
        }

        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int frameWidth = (int) ((parent.getWidth() - (float) mSizeGridSpacingPx * (mGridSize - 1)) / mGridSize);
            int padding = parent.getWidth() / mGridSize - frameWidth;
            int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
            int childCount = state.getItemCount();
            int lastRowChildCount = getLastRowChildCount(childCount);

            boolean childIsInTopRow = parent.getChildAdapterPosition(view) < mNumColumns;
            boolean childIsInBottomRow = parent.getChildAdapterPosition(view) >= childCount - lastRowChildCount;

            if (childIsInTopRow) {
                outRect.top = mTopOffset;
            }
            else{
                outRect.top = mSizeGridSpacingPx;
            }

            if (itemPosition % mGridSize == 0) {
                outRect.left = 0;
                outRect.right = padding;
                mNeedLeftSpacing = true;
            } else if ((itemPosition + 1) % mGridSize == 0) {
                mNeedLeftSpacing = false;
                outRect.right = 0;
                outRect.left = padding;
            } else if (mNeedLeftSpacing) {
                mNeedLeftSpacing = false;
                outRect.left = mSizeGridSpacingPx - padding;
                if ((itemPosition + 2) % mGridSize == 0) {
                    outRect.right = mSizeGridSpacingPx - padding;
                } else {
                    outRect.right = mSizeGridSpacingPx / 2;
                }
            } else if ((itemPosition + 2) % mGridSize == 0) {
                mNeedLeftSpacing = false;
                outRect.left = mSizeGridSpacingPx / 2;
                outRect.right = mSizeGridSpacingPx - padding;
            } else {
                mNeedLeftSpacing = false;
                outRect.left = mSizeGridSpacingPx / 2;
                outRect.right = mSizeGridSpacingPx / 2;
            }

            if (childIsInBottomRow) {
                outRect.bottom = mBottomOffset;
            }
            else{
                outRect.bottom = 0;
            }
        }

        private int getLastRowChildCount(int itemCount) {
            int lastRowChildCount = itemCount % mNumColumns;
            if (lastRowChildCount == 0) {
                lastRowChildCount = mNumColumns;
            }
            return lastRowChildCount;
        }
    }

    /* ON TOUCH EVENT FOR GRID ITEM */
    static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private OnItemClickListener mListener;

        public interface OnItemClickListener {
            void onItemClick(View view, int position);
        }

        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
