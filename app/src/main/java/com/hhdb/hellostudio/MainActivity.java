package com.hhdb.hellostudio;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;

import com.hhdb.hellostudio.activity.BaseActivity;
import com.hhdb.hellostudio.utils.CameraUtil;
import com.hhdb.hellostudio.utils.ToastUtil;

import java.io.File;
import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.image_iv) ImageView imageIv;

    private Uri imageUri = null;

    private static final int REQUEST_TAKE_PHOTO = 0;
    private static final int REQUEST_TAKE_GALLERY = 1;
    private static final int REQUEST_CROP_PHOTO = 2;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initDefault();
    }

    @OnClick(R.id.take_photo_btn) void takePhoto() {
        Intent intent = CameraUtil.getTakePhotoIntent(imageUri);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    @OnClick(R.id.take_gallery_btn) void takeGallery() {
        Intent galleryIntent = CameraUtil.getTakeGalleryIntent(imageUri);
        startActivityForResult(galleryIntent, REQUEST_TAKE_GALLERY);
    }

    private void initDefault() {
        File outputImage = new File(Environment.getExternalStorageDirectory(), "output_image.jpg");
        imageUri = Uri.fromFile(outputImage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Intent cropIntent = CameraUtil.getCropIntent(imageUri, imageUri);
                    startActivityForResult(cropIntent, REQUEST_CROP_PHOTO);
                }
                break;
            case REQUEST_TAKE_GALLERY:
                if (resultCode == RESULT_OK) {
                    String imagePath = null;
                    if (Build.VERSION.SDK_INT >= 19) {
                        imagePath = CameraUtil.handleImageOnKitKat(this, data);
                    } else {
                        imagePath = CameraUtil.handleImageBeforeKitKat(this, data);
                    }
                    Intent cropIntent = CameraUtil.getCropIntent(Uri.fromFile(new File(imagePath)), imageUri);
                    startActivityForResult(cropIntent, REQUEST_CROP_PHOTO);
                }
                break;
            case REQUEST_CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    // Glide.with(this).load(imageUri).into(image);
                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    imageIv.setImageBitmap(bitmap);
                }
                break;
        }
    }
}
