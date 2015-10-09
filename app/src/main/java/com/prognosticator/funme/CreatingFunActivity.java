package com.prognosticator.funme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreatingFunActivity extends AppCompatActivity {

    private Camera cameraObject;
    private CameraView cameraView;
    private ImageView pic;
    private Handler handler;
    FrameLayout preview;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_fun);

        pic = (ImageView) findViewById(R.id.img_view);
        handler = new Handler();
    }

    private Camera getCamera() {

        int cameraId = -1;
        Camera object = null;

        // Search for front or back camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {

            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == CameraInfo.CAMERA_FACING_FRONT || info.facing == CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                break;
            }

        }

        if (cameraId >= 0) object = Camera.open(cameraId);
        return object;

    }


    // Callback interface used to supply image data from a photo capture.
    private Camera.PictureCallback PCallback = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

            if (bitmap == null) {
                Toast.makeText(getApplicationContext(), "Photo not taken", Toast.LENGTH_SHORT).show();
            } else {

                // Show image
                pic.setImageBitmap(bitmap);

                // Save to file
                FileOutputStream outStream = null;

                try {
                    outStream = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".jpg");
                    outStream.write(data);
                    outStream.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            // Release camera
            releaseCamera();

            // Reset camera after a short time period
            handler.postDelayed(new Runnable() {
                public void run() {
                    // reset camera and preview
                    initCamera();
                }
            }, 300);
        }
    };

    // Get camera object and prepare preview
    public void initCamera() {

        cameraObject = getCamera();
        cameraView = new CameraView(this, cameraObject);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(cameraView);

    }

    public void takePhoto(View view) {
        cameraObject.takePicture(null, null, PCallback);
    }

    protected void onResume() {
        super.onResume();
        initCamera();
    }

    public void releaseCamera() {

        if (cameraObject != null) {

            // Release camera and remove surface view from the preview
            cameraObject.release();
            cameraObject = null;
            preview.removeAllViews();
        }

    }

    // Camera preview
    private class CameraView extends SurfaceView implements SurfaceHolder.Callback {

        private SurfaceHolder surfaceHolder;
        private Camera camera;

        public CameraView(Context context, Camera camera) {
            super(context);
            this.camera = camera;
            surfaceHolder = getHolder();
            surfaceHolder.addCallback(this);
        }

        @Override
        public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                if (Build.VERSION.SDK_INT >= 8) {
                    camera.setDisplayOrientation(90);
                }

                camera.setPreviewDisplay(holder);
                camera.startPreview();
            } catch (IOException e) {

            }

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder arg0) {

        }

    }

    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

}
