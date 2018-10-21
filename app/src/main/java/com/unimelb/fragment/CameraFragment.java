package com.unimelb.fragment;

import android.content.Context;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.squareup.picasso.Picasso;
import com.unimelb.instagramlite.R;
import com.flurgle.camerakit.CameraKit;
import com.flurgle.camerakit.CameraListener;
import com.flurgle.camerakit.CameraView;
import com.unimelb.constants.FilePaths;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShareFragmentsListener} interface
 * to handle interaction events.
 * Use the {@link CameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraFragment extends Fragment {
    /* Tag to identify CameraGragment */
    private final static String TAG = "CameraFragment";
    /* CameraView */
    private CameraView mCameraView;
    /* button to take a picture */
    private ImageButton mTakePhoto;
    /* swith between back and front Camera button */
    private ImageButton mSwitchCamera;
    /* Toggle Flash button */
    private ImageButton mToggleFlash;
    /* Image View to show the photo */
    private PhotoView mImageView;
    /* Next View to show some text */
    private TextView mNextView;
    /* ImageView to show a 3*3 grid cover the photo view */
    private ImageView mGridView;
    /* is photo have be taken */
    private boolean photoTaken = false;
    /* String of path which Image user selected */
    private String mSelectedImage;
    /* ShareFragmentsListener */
    private ShareFragmentsListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment CameraFragment.
     */
    public static CameraFragment newInstance() {
        return new CameraFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        // when touch back button, closing share acticity
        ImageView backButton = view.findViewById(R.id.photoBack);
        backButton.setOnClickListener(View -> {
                Log.d(TAG, "closing share activity");
                getActivity().finish();
        });

        // bind UI with properties
        mCameraView   = view.findViewById(R.id.camera);
        mGridView     = view.findViewById(R.id.grid_camera_view);
        mTakePhoto    = view.findViewById(R.id.btn_take_photo);
        mSwitchCamera = view.findViewById(R.id.btn_switch_camera);
        mToggleFlash  = view.findViewById(R.id.btn_toggle_flash);

        mImageView = view.findViewById(R.id.image_view);
        mImageView.enable();

        // show effect fragment after touch the next button
        mNextView = view.findViewById(R.id.photoNext);
        mNextView.setVisibility(View.GONE);
        mNextView.setOnClickListener(View -> {
            Log.d(TAG, "go to modify activity");
            mListener.showEffectsFragment(mSelectedImage);
        });

        initCamera();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCameraView.start(); // start showing dynamic content in camera view
    }

    @Override
    public void onPause() {
        mCameraView.stop();
        super.onPause();   // stop showing dynamic content in camera view
    }


    /**
     * initiate camera module
     */
    public void initCamera()
    {
        // the listener which will be response after captureImage() is invoked.
        mCameraView.setCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(byte[] picture) {
                super.onPictureTaken(picture);
                // identify currently has taken a photo
                photoTaken = true;
                // generate file name
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                Date now = new Date();
                String fileName = "IMG_" + formatter.format(now) + ".png";
                final File file = new File(FilePaths.CAMERA_PATH, fileName);

                // get the result and rotate 90 degrees before save to local
                // (the original one is not in correct direction)
                Bitmap result = BitmapFactory.decodeByteArray(picture, 0, picture.length);
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap rotatedBitmap = Bitmap.createBitmap(result, 0, 0, result.getWidth(),
                        result.getHeight(), matrix, true);

                // save to local
                saveBitmapToPng(rotatedBitmap,file);

                // show on the ImageView
                mSelectedImage = file.getAbsolutePath();
                Picasso.with(getActivity()).load(file).fit().centerCrop().into(mImageView);
            }
        });

        // the listener which will be response after taking photo button is invoked.
        mTakePhoto.setOnClickListener(view -> {
            if(!photoTaken) {
                mCameraView.captureImage();

                // Hide all buttons related to taking photo and show "next" button
                mCameraView.setVisibility(View.INVISIBLE);
                mGridView.setVisibility(View.INVISIBLE);
                mSwitchCamera.setVisibility(View.GONE);
                mToggleFlash.setVisibility(View.GONE);
                mNextView.setVisibility(View.VISIBLE);
                mImageView.setVisibility(View.VISIBLE);
                mTakePhoto.setBackgroundResource(R.drawable.ic_refresh_black_24dp);
            }
            else{

                // Show all buttons related to taking photo and hide "next" button
                mTakePhoto.setBackgroundResource(R.drawable.ic_photo_camera_black_24dp);
                photoTaken = false;
                mCameraView.setVisibility(View.VISIBLE);
                mGridView.setVisibility(View.VISIBLE);
                mSwitchCamera.setVisibility(View.VISIBLE);
                mImageView.setVisibility(View.GONE);
                mToggleFlash.setVisibility(View.VISIBLE);
                mNextView.setVisibility(View.GONE);

            }
        });

        // change flash mode after toggle flash button
        mToggleFlash.setOnClickListener(View -> toggleFlash());

        // change camera mode
        mSwitchCamera.setOnClickListener(View -> switchCamera());

        // set facing back and flash off as the default mode
        mCameraView.setFlash(CameraKit.Constants.FACING_BACK);
        setFlashIcon(CameraKit.Constants.FACING_BACK);

        mCameraView.setFacing(CameraKit.Constants.FLASH_OFF);
        setCameraModeIcon(CameraKit.Constants.FLASH_OFF);
    }

    /**
     * Save the Bitmap to local as png file
     * @param bmp   source file
     * @param file  target file
     */
    private void saveBitmapToPng(Bitmap bmp, File file)
    {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * switch camera mode
     */
    private void switchCamera() {
        if(mCameraView != null)
        {
            int f = mCameraView.toggleFacing();
            setCameraModeIcon(f);
        }
    }

    /**
     * switch flash mode
     */
    private void toggleFlash()
    {
        if(mCameraView != null)
        {
            int f = mCameraView.toggleFlash();
            setFlashIcon(f);

        }
    }

    /**
     * set camera icon
     * @param mode identity of mode
     */
    private void setCameraModeIcon(int mode)
    {
        if(mode == CameraKit.Constants.FACING_BACK)
        {
            mSwitchCamera.setImageDrawable(getResources().getDrawable(R.drawable.ic_camera_rear));
        }else{
            mSwitchCamera.setImageDrawable(getResources().getDrawable(R.drawable.ic_camera_front));
        }
    }

    /**
     * set flash icon
     * @param mode identity of mode
     */
    private void setFlashIcon(int mode)
    {
        if(mode == CameraKit.Constants.FLASH_OFF)
        {
            mToggleFlash.setImageDrawable(getResources().getDrawable(R.drawable.ic_flash_off));
        }else if(mode == CameraKit.Constants.FLASH_ON)
        {
            mToggleFlash.setImageDrawable(getResources().getDrawable(R.drawable.ic_flash_on));
        }else if(mode == CameraKit.Constants.FLASH_AUTO)
        {
            mToggleFlash.setImageDrawable(getResources().getDrawable(R.drawable.ic_flash_auto));
        }else{
            mToggleFlash.setImageDrawable(getResources().getDrawable(R.drawable.ic_flash_on));
        }
    }

    /**
     * socket for start camera for external invoking
     */
    public void startCamera()
    {
        if(mCameraView != null)
        {
            mCameraView.start();
        }
    }

    /**
     * socket for stop camera for external invoking
     */
    public void stopCamera()
    {
        if(mCameraView != null)
        {
            mCameraView.stop();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ShareFragmentsListener) {
            // get context (parent activity)
            mListener = (ShareFragmentsListener) context;
        } else {
            throw new RuntimeException(context.toString() +
                    " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // release parent activity
        mListener = null;
    }
}
