package com.unimelb.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.unimelb.instagramlite.R;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShareFragmentsListener} interface
 * to handle interaction events.
 * Use the {@link PostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostFragment extends Fragment {

    private final static String TAG = "PostFragment";

    private ImageView   postImageView;
    private EditText    postEditText;

    private ShareFragmentsListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment LibraryFragment.
     */
    public static PostFragment newInstance() {
        return new PostFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // initiate the properties of the class
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        postImageView = view.findViewById(R.id.post_image_view);
        postEditText  = view.findViewById(R.id.post_image_caption);


        // close the activity when touch the back button
        ImageView backButton = view.findViewById(R.id.post_toolbar_back);
        backButton.setOnClickListener(View -> {
                Log.d(TAG, "go back to effect view");
                mListener.backToPreviousView();
        });

        // go to filter view after touch next
        TextView nextView = view.findViewById(R.id.post_share);
        nextView.setOnClickListener(View -> {
            Log.d(TAG, "post data to server");
            // TODO post data to the server
        });

        postEditText.setOnClickListener(View -> {
            String text = postEditText.getText().toString();
            String hint = getResources().getString(R.string.string_post_hint);
            if (text.equals(hint)) {
                postEditText.getText().clear();
            }
        });

        Log.d(TAG, "create view");
        return view;
    }

    public void setImageViewWithImagePath (String imagePath) {
        postImageView.setImageURI(Uri.fromFile(new File(imagePath)));
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
