package com.unimelb.fragment;

import android.content.Context;
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
import android.net.Uri;

import com.bm.library.PhotoView;
import com.squareup.picasso.Picasso;
import com.unimelb.instagramlite.R;
import com.flurgle.camerakit.CameraKit;
import com.flurgle.camerakit.CameraListener;
import com.flurgle.camerakit.CameraView;
import com.unimelb.utils.FilePaths;

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
    private final static String TAG = "CameraFragment";

    private CameraView mCameraView;
    private ImageButton mTakePhoto;
    private ImageButton mSwitchCamera;
    private ImageButton mToggleFlash;
    private PhotoView mImageView;

    private static int DEFAULT_FLASH_MODE = 0;
    private static int DEFAULT_CAMERA_MODE = 0;

    private boolean photoTaken = false;

    private Uri pathImage = null;

    private ShareFragmentsListener mListener;

    public CameraFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment CameraFragment.
     */
    public static CameraFragment newInstance() {
        CameraFragment fragment = new CameraFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        ImageView backButton = view.findViewById(R.id.photoBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "closing share activity");
                getActivity().finish();
            }
        });

        mCameraView = view.findViewById(R.id.camera);
        mTakePhoto = view.findViewById(R.id.btn_take_photo);
        mSwitchCamera =  view.findViewById(R.id.btn_switch_camera);
        mToggleFlash =  view.findViewById(R.id.btn_toggle_flash);
        mImageView = view.findViewById(R.id.image_view);
        mImageView.enable();

        initCamera();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCameraView.start();
    }

    @Override
    public void onPause() {
        mCameraView.stop();
        super.onPause();
    }


    public void initCamera()
    {
        mCameraView.setCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(byte[] picture) {
                super.onPictureTaken(picture);
                photoTaken = true;
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                Date now = new Date();
                String fileName = "IMG_" + formatter.format(now) + ".png";

                final File file = new File(
                        FilePaths.CAMERA_PATH,
                        fileName
                );

                Bitmap result = BitmapFactory.decodeByteArray(picture, 0, picture.length);

                saveBitmapToPng(result,file);

                pathImage = Uri.fromFile(file);
                Picasso.with(getActivity()).load(file).fit().centerCrop()
                        .into(mImageView);
                mCameraView.setVisibility(View.INVISIBLE);
                mSwitchCamera.setVisibility(View.GONE);
                mToggleFlash.setVisibility(View.GONE);
                //awCamera.setContinueButtonVisibility(View.VISIBLE); // TODO:
                mImageView.setVisibility(View.VISIBLE);
                mTakePhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_refresh));
            }
        });

        mTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!photoTaken)
                    mCameraView.captureImage();
                else{
                    mTakePhoto.setImageDrawable(null);
                    photoTaken = false;
                    mCameraView.setVisibility(View.VISIBLE);
                    mSwitchCamera.setVisibility(View.VISIBLE);
                    mImageView.setVisibility(View.GONE);
                    mToggleFlash.setVisibility(View.VISIBLE);
                    //awCamera.setContinueButtonVisibility(View.GONE); // TODO:
                }
            }
        });

        mToggleFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFlash();
            }
        });

        mSwitchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchCamera();
            }
        });

        mCameraView.setFlash(CameraKit.Constants.FACING_BACK);
        setFlashIcon(CameraKit.Constants.FACING_BACK);

        mCameraView.setFacing(CameraKit.Constants.FLASH_OFF);
        setCameraModeIcon(CameraKit.Constants.FLASH_OFF);
    }

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


    private void switchCamera() {
        if(mCameraView != null)
        {
            int f = mCameraView.toggleFacing();
            setCameraModeIcon(f);
        }
    }

    private void toggleFlash()
    {
        if(mCameraView != null)
        {
            int f = mCameraView.toggleFlash();
            setFlashIcon(f);

        }
    }

    private void setCameraModeIcon(int mode)
    {
        if(mode == CameraKit.Constants.FACING_BACK)
        {
            mSwitchCamera.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_camera_rear));
        }else{
            mSwitchCamera.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_camera_front));
        }
    }

    private void setFlashIcon(int mode)
    {
        if(mode == CameraKit.Constants.FLASH_OFF)
        {
            mToggleFlash.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_flash_off));
        }else if(mode == CameraKit.Constants.FLASH_ON)
        {
            mToggleFlash.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_flash_on));
        }else if(mode == CameraKit.Constants.FLASH_AUTO)
        {
            mToggleFlash.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_flash_auto));
        }else{
            mToggleFlash.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_flash_on));
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
        }
        else {
        }
    }

    public void setCameraViewVisibility(int visible) {
        if(mCameraView != null)
            mCameraView.setVisibility(visible);
    }


    public Uri getPathImage() {
        return pathImage;
    }

    public void startCamera()
    {
        if(mCameraView != null)
        {
            mCameraView.start();
        }
    }

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
