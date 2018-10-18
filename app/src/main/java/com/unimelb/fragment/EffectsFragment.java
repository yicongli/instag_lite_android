package com.unimelb.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.unimelb.adapter.FiltersAdapter;
import com.unimelb.component.ThumbnailItem;
import com.unimelb.instagramlite.R;
import com.unimelb.constants.FilePaths;
import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Effect Fragment for editing the image
 */
public class EffectsFragment extends Fragment {

    private final static String TAG = "EffectsFragment";

    static {
        System.loadLibrary("NativeImageProcessor");
    }

    private PhotoView mImageView;           // the photo view
    private FiltersAdapter filtersAdapter;  // effects list adapter
    private ArrayList<ThumbnailItem> images = new ArrayList<>(); // images for effects list

    // control panels
    private RelativeLayout filterPanel;
    private RelativeLayout contrastPanel;
    private RelativeLayout brightnessPanel;

    // control buttons
    private ImageView mBrightness;
    private ImageView mContrast;
    private ImageView mFilter;

    // Labels to show the figure
    private TextView mBrightnessLabel;
    private TextView mContrastLabel;

    private ShareFragmentsListener mListener;   // parrent activity

    public static EffectsFragment newInstance() {
        return new EffectsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_effects, container, false);

        // initiate image view to show the effect of modification
        mImageView = view.findViewById(R.id.image_view);
        RecyclerView mRecyclerView = view.findViewById(R.id.recyclerView);

        // close the activity when touch the back button
        ImageView backButton = view.findViewById(R.id.effectBack);
        backButton.setOnClickListener(View -> {
                Log.d(TAG, "back to previous view");
                mListener.backToPreviousView();
        });

        // go to filter view after touch next
        TextView nextView = view.findViewById(R.id.effectNext);
        nextView.setOnClickListener(View -> {
            Log.d(TAG, "go to Post view");
            // TODO: show the post fragment
        });

        // initiate control button and control panel
        initControlButtons(view);

        filtersAdapter = new FiltersAdapter(this,images);

        // layout of recyclerview
        LinearLayoutManager llFilters = new LinearLayoutManager(view.getContext());
        llFilters.setOrientation(LinearLayoutManager.HORIZONTAL);

        mRecyclerView.setLayoutManager(llFilters);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(filtersAdapter);

        return view;
    }

    /**
     * initiate control button and control panel
     * @param view based view
     */
    private void initControlButtons (View view) {
        filterPanel = view.findViewById(R.id.filter_panel);
        contrastPanel = view.findViewById(R.id.contrast_panel);
        brightnessPanel = view.findViewById(R.id.bright_panel);

        mBrightness = view.findViewById(R.id.effect_bright);
        mBrightness.setOnClickListener(View -> {
            brightnessPanel.setVisibility(android.view.View.VISIBLE);
            filterPanel.setVisibility(android.view.View.GONE);
            contrastPanel.setVisibility(android.view.View.GONE);
            mBrightness.setBackgroundColor(0xFFBFBFBF);
            mContrast.setBackgroundColor(0);
            mFilter.setBackgroundColor(0);
        });

        mContrast = view.findViewById(R.id.effect_contrast);
        mContrast.setOnClickListener(View -> {
            brightnessPanel.setVisibility(android.view.View.GONE);
            filterPanel.setVisibility(android.view.View.GONE);
            contrastPanel.setVisibility(android.view.View.VISIBLE);
            mContrast.setBackgroundColor(0xFFBFBFBF);
            mBrightness.setBackgroundColor(0);
            mFilter.setBackgroundColor(0);
        });

        mFilter = view.findViewById(R.id.effect_filter);
        mFilter.setOnClickListener(View -> {
            brightnessPanel.setVisibility(android.view.View.GONE);
            filterPanel.setVisibility(android.view.View.VISIBLE);
            contrastPanel.setVisibility(android.view.View.GONE);
            mFilter.setBackgroundColor(0xFFBFBFBF);
            mBrightness.setBackgroundColor(0);
            mContrast.setBackgroundColor(0);
        });

        mFilter.setBackgroundColor(0xFFBFBFBF);
        brightnessPanel.setVisibility(android.view.View.GONE);
        contrastPanel.setVisibility(android.view.View.GONE);

        // init brightness seek panel
        mBrightnessLabel = view.findViewById(R.id.textView_bright);
        SeekBar mSeekBarBrightness = view.findViewById(R.id.seekBar_bright);
        mSeekBarBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                String text = "Brightness: " + (progress - 30);
                mBrightnessLabel.setText(text);
                Log.d(TAG, "Changing Brightness seekbar's progress");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "Started Brightness tracking seekbar");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "Stopped Brightness tracking seekbar");

                Filter myFilter = new Filter();
                // TODO: Adjust param
                myFilter.addSubFilter(new BrightnessSubFilter(seekBar.getProgress() - 30));

                Bitmap ouputImage = myFilter.processFilter(((BitmapDrawable)mImageView.getDrawable()).getBitmap());

                setImage(ouputImage);
                bindDataToAdapter(ouputImage);
            }
        });

        // init contrast seek panel
        mContrastLabel = view.findViewById(R.id.textView_contrast);
        SeekBar mSeekBarConstrast = view.findViewById(R.id.seekBar_contrast);
        mSeekBarConstrast.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                String text = "Brightness: " + progress / 10.0;
                mContrastLabel.setText(text);
                Log.d(TAG, "Changing Constrast seekbar's progress");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "Started Constrast tracking seekbar");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "Stopped Constrast tracking seekbar");

                Filter myFilter = new Filter();
                // TODO: Adjust param
                myFilter.addSubFilter(new ContrastSubFilter((float)(seekBar.getProgress() / 10.0)));

                Bitmap ouputImage = myFilter.processFilter(((BitmapDrawable)mImageView.getDrawable()).getBitmap());

                setImage(ouputImage);
                bindDataToAdapter(ouputImage);
            }
        });
    }

    /**
     * bind the data to the adatpter
     * @param bmp scource image
     */
    private void bindDataToAdapter(final Bitmap bmp) {
        Handler handler = new Handler();
        Runnable r = () -> {
            ThumbnailItem t1 = new ThumbnailItem("Original",bmp,null);
            ThumbnailItem t2 = new ThumbnailItem("Star Lit", bmp,SampleFilters.getStarLitFilter());
            ThumbnailItem t3 = new ThumbnailItem("Blue Mess", bmp,SampleFilters.getBlueMessFilter());
            ThumbnailItem t4 = new ThumbnailItem("Awe Struck Vibe", bmp,SampleFilters.getAweStruckVibeFilter());
            ThumbnailItem t5 = new ThumbnailItem("Lime Stutter", bmp, SampleFilters.getLimeStutterFilter());
            ThumbnailItem t6 = new ThumbnailItem("Night Wisper", bmp,SampleFilters.getNightWhisperFilter());

            images.clear();
            images.add(t1); // Original Image
            images.add(t2);
            images.add(t3);
            images.add(t4);
            images.add(t5);
            images.add(t6);

            filtersAdapter.notifyDataSetChanged();
        };

        handler.post(r);
    }

    /**
     * set image showing in the imageView
     * @param bmp scource image
     */
    public void setImage(Bitmap bmp) {
        mImageView.setImageBitmap(bmp);
    }

    /**
     * save modified image to local
     * @return image absolute path
     */
    public String saveImage()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date now = new Date();
        String fileName = "IMG_" + formatter.format(now) + ".png";

        final File file = new File(FilePaths.PICTURE_PATH, fileName);
        String path = mListener.getSelectedImagePath();
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inMutable = true;
        Bitmap bmp = BitmapFactory.decodeFile(Uri.parse(path).getPath(),opts);
        Filter fSel = filtersAdapter.getFilterSelected();
        if(fSel != null)
        {
            bmp = fSel.processFilter(bmp);
        }
        saveBmpToFile(file,bmp);

        return file.getAbsolutePath();
    }

    /**
     * save Bmp file to the local as png
     * @param file target path
     * @param bmp  image source
     */
    private void saveBmpToFile(File file, Bitmap bmp)
    {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
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

    // initiate the presenting page after everytime select different page
    public void initUI() {
        String path = mListener.getSelectedImagePath();
        path = Uri.parse(path).getPath();
        if(path != null) {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inMutable = true;
            opts.inJustDecodeBounds = false;

            // fit image to the imageview
            Bitmap originaBitmap = BitmapFactory.decodeFile(path,opts);
            opts.inSampleSize = calculateInSampleSize(opts,300,300);


            try {
                ExifInterface exif = new ExifInterface(path);
                int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                int rotationInDegrees = exifToDegrees(rotation);
                Matrix matrix = new Matrix();
                if (rotation != 0f) {
                    matrix.preRotate(rotationInDegrees);
                    originaBitmap = Bitmap.createBitmap(originaBitmap,0,0,opts.outWidth,opts.outHeight,matrix,true);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            setImage(originaBitmap);
            bindDataToAdapter(originaBitmap);
        }

    }

    /**
     * calculate the Size of sample view
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * adjust orientation
     * @param exifOrientation orientation
     * @return the degree need to rotate
     */
    private int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ShareFragmentsListener) {
            mListener = (ShareFragmentsListener) context; // get context
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