package com.unimelb.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.unimelb.instagramlite.R;

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

    private GridView    gridView;
    private ImageView   libraryImageView;
    private ProgressBar libraryProgressBar;
    private Spinner     directorySpinner;

    private ShareFragmentsListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment LibraryFragment.
     */
    public static LibraryFragment newInstance() {
        LibraryFragment fragment = new LibraryFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view            = inflater.inflate(R.layout.fragment_library, container, false);
        gridView            = view.findViewById(R.id.libraryGridVIew);
        libraryImageView    = view.findViewById(R.id.libraryImageVIew);
        libraryProgressBar  = view.findViewById(R.id.libraryProgressBar);
        directorySpinner    = view.findViewById(R.id.librarySpinner);
        directorySpinner.setVisibility(View.GONE);

        ImageView backButton = view.findViewById(R.id.libraryBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "closing share activity");
                getActivity().finish();
            }
        });

        TextView nextView = view.findViewById(R.id.libraryNext);
        nextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "go to modify activity");
                // TODO: the logic of
            }
        });

        Log.d(TAG, "create view");
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //mListener.selectedImage(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ShareFragmentsListener) {
            mListener = (ShareFragmentsListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
