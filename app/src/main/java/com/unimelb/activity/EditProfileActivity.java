package com.unimelb.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.unimelb.instagramlite.R;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;

/*
 * An activity for user edit his own profile page content.
 */
public class EditProfileActivity extends AppCompatActivity {

    private static final int REQUEST_LIST_CODE = 0;
    private static final int REQUEST_CAMERA_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initView();
        initData();
    }

    private void initView() {
        TextView changeAvatarTv = findViewById(R.id.edit_profile_change_avatar_btn);
        changeAvatarTv.setOnClickListener((view) -> {
            ISNav.getInstance().init((ImageLoader) (context, path, imageView) -> Glide.with(context).load(path).into(imageView));
        });
    }

    private void initData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CAMERA_CODE && resultCode == RESULT_OK && data != null) {
            String path = data.getStringExtra("result"); // 图片地址
            System.out.println(path);
        }
    }
}
