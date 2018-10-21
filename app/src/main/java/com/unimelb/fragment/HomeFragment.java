package com.unimelb.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.unimelb.activity.DeviceListActivity;
import com.unimelb.adapter.PostImageAdapter;
import com.unimelb.constants.BluetoothConstants;
import com.unimelb.constants.CommonConstants;
import com.unimelb.entity.Post;
import com.unimelb.instagramlite.R;
import com.unimelb.net.BluetoothPictureServices;
import com.unimelb.net.ErrorHandler;
import com.unimelb.net.HttpRequest;
import com.unimelb.net.IResponseHandler;
import com.unimelb.net.ResponseModel;
import com.unimelb.net.model.Medium;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class HomeFragment extends Fragment {
    /* context */
    private HomeFragment homeFragment;

    private RecyclerView postListView;

    private boolean sortDateOrder = true;

    private TextView emptyTipTv;

    private PostImageAdapter postListAdapter;

    /**
     * List of posts
     */
    private List<Post> postList = new ArrayList<>();

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    public static final int CODE_SELECT_IMAGE = 6;

    /**
     * Local Bluetooth adapter
     */
    private BluetoothAdapter mBluetoothAdapter = null;

    public static final String TAG = "HomeFragment";

    /**
     * Member object for the picture services
     */
    private BluetoothPictureServices mPictureService = null;

    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        homeFragment = this;
        initView(view);

        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        return view;
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // If BT is not on, request that it be enabled.
//        // setupPicture() will then be called during onActivityResult
//        if (!mBluetoothAdapter.isEnabled()) {
//            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
//            // Otherwise, setup the picture session
//        } else if (mPictureService == null) {
//            setupPicture();
//        }
//    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (mPictureService != null) {
//            mPictureService.stop();
//        }
//    }

//    @Override
//    public void onResume() {
//        super.onResume();
//
//        // Performing this check in onResume() covers the case in which BT was
//        // not enabled during onStart(), so we were paused to enable it...
//        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
//        if (mPictureService != null) {
//            // Only if the state is STATE_NONE, do we know that we haven't started already
//            if (mPictureService.getState() == BluetoothPictureServices.STATE_NONE) {
//                // Start the Bluetooth picture services
//                mPictureService.start();
//            }
//        }
//    }

    /**
     * Initialise views
     *
     * @param view
     */
    public void initView(View view) {
        RefreshLayout refreshLayout = view.findViewById(R.id.refresh_layout);
        refreshLayout.setRefreshHeader(new WaterDropHeader(this.getContext()));
        refreshLayout.setOnRefreshListener(layout -> {
            initData(layout);
        });
//        refreshLayout.setOnLoadMoreListener(layout -> {
//            layout.finishLoadMore(2000/*,false*/);// false means false
//        });

        refreshLayout.autoRefresh();

        postListView = view.findViewById(R.id.post_list);
        postListView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        postListAdapter = new PostImageAdapter(this.getActivity(), postList);
        postListView.setAdapter(postListAdapter);
        emptyTipTv = view.findViewById(R.id.post_empty_label);

        // Sort button onClick listener
        view.findViewById(R.id.home_sort_btn).setOnClickListener((btnView) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
            builder.setMessage("Select sort method?")
                    .setNegativeButton("by Date", (dialog, which) -> {
                        sortByDate();
                    })
                    .setPositiveButton("by Location", (dialog, which) -> {
                        sortByLocation();
                    })
                    .setNeutralButton("Cancel", null)
                    .create()
                    .show();
        });

        // Bluetooth button onClick listener
        view.findViewById(R.id.home_bluetooth_btn).setOnClickListener((bluetoothView) -> {
            Intent intent1 = new Intent(getContext(), DeviceListActivity.class);
            startActivityForResult(intent1, REQUEST_CONNECT_DEVICE_SECURE);

        });
    }

    /**
     * Initialize post data
     */
    private void initData(RefreshLayout layout) {
        HttpRequest.getInstance().doGetRequestAsync(CommonConstants.IP + "/api/v1/media/recent", null, new IResponseHandler() {
            @Override
            public void onFailure(int statusCode, String errJson) {
                new ErrorHandler(homeFragment.getActivity()).handle(statusCode, errJson);
            }

            @Override
            public void onSuccess(String json) {
                System.out.println(json);
                ResponseModel rm = new ResponseModel(json);
                JSONObject data = rm.getData();
                JSONArray media = (JSONArray) data.get("media");
                List<Medium> mediumList = new ArrayList<>();

                for (Object mediumObj : media) {
                    Medium medium = new Medium(mediumObj.toString());
                    mediumList.add(medium);
                }

                Activity context = homeFragment.getActivity();
                context.runOnUiThread(() -> {
                    for (Medium medium : mediumList) {
                        Post post = new Post(medium.getMediumId(), medium.getUser().getAvatarUrl(), medium.getUser().getUsername(), medium.getPhotoUrl(), medium.getLocation(), medium.getPostDateString(), medium.getPostDate(), medium.getLikes().size(), medium.getComments().size(), medium.getLat(), medium.getLng());
                        postList.add(post);
                    }

                    layout.finishRefresh();
                    postListAdapter.notifyDataSetChanged();

                    if (postList.size() == 0) {
                        emptyTipTv.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }


    /**
     * Sort by date
     */
    public void sortByDate() {
        if (sortDateOrder) {
            Collections.sort(postList, (post1, post2) -> post2.getDate().compareTo(post1.getDate()));
        } else {
            Collections.sort(postList, (post1, post2) -> post1.getDate().compareTo(post2.getDate()));
        }
        sortDateOrder = !sortDateOrder;
        postListAdapter.notifyDataSetChanged();
    }

    /**
     * Sort by location
     */
    public void sortByLocation() {
        Collections.sort(postList, (post1, post2) -> (int) (post1.getDistance() - post2.getDistance()));
        postListAdapter.notifyDataSetChanged();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up picture
                    setupPicture();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(getActivity(), R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }

            case CODE_SELECT_IMAGE:
                if (resultCode == Activity.RESULT_OK) {

                    selectPic(data);
                }
                break;
        }
    }

    /**
     * Establish connection with other device
     *
     * @param data   An {@link Intent} with {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

        Intent albumIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(albumIntent, CODE_SELECT_IMAGE);


        Log.d(TAG, "Device connect");
    }

    /**
     * Makes this device discoverable for 300 seconds (5 minutes).
     */
    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    private void selectPic(Intent intent) {

        //uri=content://media/external/images/media/767
        Uri selectImageUri = intent.getData();

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectImageUri);
            sendPicture(bitmap);

            Toast.makeText(getActivity(), "Send", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set up the UI and background operations for picture.
     */
    private void setupPicture() {
        Log.d(TAG, "setupPicture()");

        /**
         * The Handler that gets information back from the BluetoothPictureServices
         */
        @SuppressLint("HandlerLeak") final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                FragmentActivity activity = getActivity();
                switch (msg.what) {
                    case BluetoothConstants.MESSAGE_STATE_CHANGE:
                        switch (msg.arg1) {
                            case BluetoothPictureServices.STATE_CONNECTED:
                                break;
                            case BluetoothPictureServices.STATE_CONNECTING:
                                break;
                            case BluetoothPictureServices.STATE_LISTEN:
                            case BluetoothPictureServices.STATE_NONE:
                                break;
                        }
                        break;
                    case BluetoothConstants.MESSAGE_WRITE:
                        byte[] writeBuf = (byte[]) msg.obj;
                        // construct a string from the buffer
                        String writeMessage = new String(writeBuf);
                        break;
                    case BluetoothConstants.MESSAGE_READ:
//                    byte[] readBuf = (byte[]) msg.obj;
//                    // construct a string from the valid bytes in the buffer
//                    String readMessage = new String(readBuf, 0, msg.arg1);
                        //Jinge 修改bug
                        Bundle data = msg.getData();
                        String readMessage = data.getString("BTdata");
                        //Jinge 组合传入的字符串
                        //receivedImageString += readMessage;
                        Toast.makeText(getActivity(), "接受中/接收完成，点击“BYTE转图片”", Toast.LENGTH_SHORT).show();
//                    Log.i("Jinge","readMessage="+readMessage);
//                    mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
                        break;
                    case BluetoothConstants.MESSAGE_DEVICE_NAME:
                        // save the connected device's name
                        mConnectedDeviceName = msg.getData().getString(BluetoothConstants.DEVICE_NAME);
                        if (null != activity) {
                            Toast.makeText(activity, "Connected to "
                                    + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case BluetoothConstants.MESSAGE_TOAST:
                        if (null != activity) {
                            Toast.makeText(activity, msg.getData().getString(BluetoothConstants.TOAST),
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        };
        // Initialize the BluetoothPictureServices to perform bluetooth connections
        mPictureService = new BluetoothPictureServices(getActivity(), mHandler);

    }

    /**
     * Sends a picture.
     *
     * @param picture A bitmap picture to send.
     */
    private void sendPicture(Bitmap picture) {
        // Check that we're actually connected before trying anything
        if (mPictureService.getState() != BluetoothPictureServices.STATE_CONNECTED) {
            Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (picture != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            picture.compress(Bitmap.CompressFormat.PNG, 100, stream);
            // Get the picture bytes and tell the BluetoothPictureServices to write
            byte[] send = stream.toByteArray();
            mPictureService.write(send);

            Toast.makeText(getActivity(), "Successfully send.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "You have not choose any image.", Toast.LENGTH_LONG).show();
        }

    }


}