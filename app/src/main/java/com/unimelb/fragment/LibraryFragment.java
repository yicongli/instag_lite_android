package com.unimelb.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.unimelb.adapter.SquareImageAdapter;
import com.unimelb.instagramlite.R;
import com.unimelb.utils.FIleSearch;
import com.unimelb.utils.FilePaths;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShareFragmentsListener} interface
 * to handle interaction events.
 * Use the {@link LibraryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LibraryFragment extends Fragment {

    private final static String TAG = "LibraryFragment";
    private final static int NUM_COLUMN = 3 ;

    private String mAppend = "file:/";
    private String mSelectedImage;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    private GridView    gridView;
    private ImageView   libraryImageView;
    private ProgressBar libraryProgressBar;

    private ArrayList<String> directories;

    private ShareFragmentsListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment LibraryFragment.
     */
    public static LibraryFragment newInstance() {
        return new LibraryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // initiate the properties of the class
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        gridView = view.findViewById(R.id.libraryGridVIew);
        libraryImageView = view.findViewById(R.id.libraryImageVIew);
        libraryProgressBar = view.findViewById(R.id.libraryProgressBar);
        Spinner directorySpinner = view.findViewById(R.id.librarySpinner);

        libraryProgressBar.setVisibility(View.GONE);
        // set the configuration of image loader
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));

        // close the activity when touch the back button
        ImageView backButton = view.findViewById(R.id.libraryBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "closing share activity");
                getActivity().finish();
            }
        });

        // go to filter view after touch next
        TextView nextView = view.findViewById(R.id.libraryNext);
        nextView.setOnClickListener(View -> {
            Log.d(TAG, "go to modify activity");
            mListener.selectedImage(mSelectedImage);
        });

        // initiate data source
        directories = initDataSource();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, folderName(directories));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        directorySpinner.setAdapter(adapter);

        directorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String path = directories.get(i);
                Log.d(TAG, "spinner clicked" + path);
                initGridView(path);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Log.d(TAG, "create view");
        return view;
    }

    /**
     * initiate paths
     * @return the path contain the images
     */
    private ArrayList<String> initDataSource () {

        Log.d(TAG, "initDataSource");

        ArrayList<String> tmpList = new ArrayList<>();

        ArrayList<String> picturePathList = FIleSearch.getDirectoryPaths(FilePaths.PICTURE_PATH);
        if (picturePathList.size() != 0 ) {
            tmpList = picturePathList;
        }

        tmpList.add(0,FilePaths.CAMERA_PATH);

        return  tmpList;
    }

    /**
     * Generate folder name of each path
     * @param paths
     * @return
     */
    private ArrayList<String> folderName (ArrayList<String> paths) {
        ArrayList<String> folderName = new ArrayList<>();
        for (String path : paths) {
            int indexOfBackSlash = path.lastIndexOf("/");
            String name = path.substring(indexOfBackSlash + 1);
            folderName.add(name);
        }

        return folderName;
    }

    /**
     * initiate grid view
     * @param path the image directory path
     */
    private void initGridView (String path) {
        Log.d(TAG, "initGridView" + path);

        final ArrayList<String> picturePaths = FIleSearch.getFilePaths(path);
        String pathArray[] = new String[picturePaths.size()];
        pathArray = picturePaths.toArray(pathArray);

        // set grid column width
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageViewWidth = gridWidth / NUM_COLUMN;

        gridView.setColumnWidth(imageViewWidth);

        // adapter
        SquareImageAdapter imageAdapter = new SquareImageAdapter(this.getContext(), pathArray);
        gridView.setAdapter(imageAdapter);

        //set the first image to be displayed when the activity fragment view is inflated
        try {
            String imageUrl = picturePaths.size() == 0 ? "" : picturePaths.get(0);
            setImage(imageUrl, libraryImageView, mAppend);
            mSelectedImage = imageUrl;
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.e(TAG, "ArrayIndexOutOfBoundsException: " + e.getMessage());
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: selected an image: " + picturePaths.get(position));

                setImage(picturePaths.get(position), libraryImageView, mAppend);
                mSelectedImage = picturePaths.get(position);
            }
        });

    }

    /**
     * set the image shown in the image view
     * @param imgURL URL of image
     * @param image  imageView
     * @param append the append of image path
     */
    private void setImage(String imgURL, ImageView image, String append){
        Log.d(TAG, "setImage: setting image");

        imageLoader.displayImage(append + imgURL, image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                libraryProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                libraryProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                libraryProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                libraryProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ShareFragmentsListener) {
            mListener = (ShareFragmentsListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
