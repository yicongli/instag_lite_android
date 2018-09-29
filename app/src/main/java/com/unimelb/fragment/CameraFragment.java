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

import com.bm.library.PhotoView;
import com.unimelb.instagramlite.R;
import com.wonderkiln.camerakit.CameraView;

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
