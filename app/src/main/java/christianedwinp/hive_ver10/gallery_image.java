package christianedwinp.hive_ver10;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by ChristianEdwin on 08-Jul-16.
 */
public class gallery_image extends Fragment {
    private ArrayList<File> sorted_listFile;
    File file;

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
            File[] unsorted_listFile = file.listFiles();
            sorted_listFile = new ArrayList<>();

            for (int i = 0; i < unsorted_listFile.length; i++) {
                String fileName = unsorted_listFile[i].getName();
                CharSequence jpg = ".jpg"; CharSequence png = ".png"; CharSequence tif = ".tif";

                if(fileName.toLowerCase().contains(jpg) || fileName.toLowerCase().contains(png) || fileName.toLowerCase().contains(tif) ){
                    sorted_listFile.add(unsorted_listFile[i]);
                }
            }
        }

        //setting recycleview to grid type
        RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.gallery_gridview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),3); //3 indicate number of grid column you want
        recyclerView.setLayoutManager(layoutManager);

        //attach adapter to recyleview
        adapter_gallery adapter = new adapter_gallery(getActivity(), sorted_listFile);
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

        //Show the layout use : gallery_view_activityxml
        return layout;
    }

    //Grid RecycleView Adapter
    class adapter_gallery extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList files;
        private Context mContext;
        private LayoutInflater inflater;

        /* THE CONSTRUCTOR */
        public adapter_gallery(Context context, ArrayList files){
            mContext = context;
            this.files = files;
        }

        /* VIEW HOLDER TYPES */
        // Image data view holder
        public class ImageHolder extends RecyclerView.ViewHolder{
            //TextView caption; >> keep in case need text underneath image
            ImageView image;
            public ImageHolder(View itemView) {
                super(itemView);
                //caption = (TextView) itemView.findViewById(R.id.text); >> keep in case need text underneath image
                image = (ImageView) itemView.findViewById(R.id.image);
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

            View imageLayoutView = inflater.inflate(R.layout.gallery_grid_image_layout, parent, false);
            viewHolder = new ImageHolder(imageLayoutView);
            return viewHolder;
        }

        //to populate the data into selected view holder
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            ImageHolder vh = (ImageHolder) viewHolder;
            //holder.caption.setText(files[position].getName()); >> keep in case need text underneath image
            Picasso.with(mContext)
                    .load((File) files.get(position))
                    .centerCrop()
                    .resize(350,350)
                    .into(vh.image);
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
