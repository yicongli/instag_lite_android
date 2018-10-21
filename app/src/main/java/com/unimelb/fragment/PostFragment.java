package com.unimelb.fragment;

import android.app.Dialog;
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
import android.widget.Toast;

import com.unimelb.constants.CommonConstants;
import com.unimelb.instagramlite.R;
import com.unimelb.net.ErrorHandler;
import com.unimelb.net.HttpRequest;
import com.unimelb.net.IResponseHandler;
import com.unimelb.plugin.LoadingDialog;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShareFragmentsListener} interface
 * to handle interaction events.
 * Use the {@link PostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostFragment extends Fragment {
    private PostFragment context;

    private final static String TAG = "PostFragment";

    private ImageView postImageView;
    private EditText postEditText;

    private ShareFragmentsListener mListener;

    private String imagePath;

    private Dialog loadingDialog;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LibraryFragment.
     */
    public static PostFragment newInstance() {
        return new PostFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = this;
        // initiate the properties of the class
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        postImageView = view.findViewById(R.id.post_image_view);
        postEditText = view.findViewById(R.id.post_image_caption);

        postEditText.setHint(R.string.string_post_hint);


        // close the activity when touch the back button
        ImageView backButton = view.findViewById(R.id.post_toolbar_back);
        backButton.setOnClickListener(View -> {
            Log.d(TAG, "go back to effect view");
            mListener.backToPreviousView();
        });

        // go to filter view after touch next
        TextView nextView = view.findViewById(R.id.post_share);
        nextView.setOnClickListener(View -> {
            loadingDialog = LoadingDialog.showWaitDialog(context.getActivity(), "Loading");
            String tags = extractTags(postEditText.getText().toString());

            // post data to the server
            File file = new File(imagePath);
            System.out.println(file);
            Map<String, Object> hashBodyMap = new HashMap<>();
            hashBodyMap.put("image", file);
            hashBodyMap.put("tags", tags);
            hashBodyMap.put("location", CommonConstants.latitude + ", " + CommonConstants.longitude);

            HttpRequest.getInstance().doFilePostRequestAsync(CommonConstants.IP + "/api/v1/media/self", hashBodyMap, new IResponseHandler() {
                @Override
                public void onFailure(int statusCode, String errJson) {
                    new ErrorHandler(context.getActivity()).handle(statusCode, errJson);
                    LoadingDialog.closeDialog(loadingDialog);
                }

                @Override
                public void onSuccess(String json) {
                    System.out.println(json);
                    context.getActivity().runOnUiThread(() -> {
                        LoadingDialog.closeDialog(loadingDialog);
                        Toast.makeText(context.getActivity(), "Share successful", Toast.LENGTH_LONG).show();
                        context.getActivity().finish();
                    });
                }
            });
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

    /**
     * set image of image view with image path
     *
     * @param imagePath
     */
    public void setImageViewWithImagePath(String imagePath) {
        this.imagePath = imagePath;
        postImageView.setImageURI(Uri.fromFile(new File(imagePath)));
    }

    /**
     * Extract the tags in the caption before send the
     *
     * @param captionStr
     */
    public String extractTags(String captionStr) {
        String tags = "";

        // split string by #
        String[] split = captionStr.split("#");

        // add tag to the tag set
        for (int i = 1; i < split.length; i++) {
            String tag = split[i];
            tag = tag.replaceAll("\\s", "");

            if (i > 1) {
                tags += ", ";
            }
            tags += tag;
        }

        return tags;
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