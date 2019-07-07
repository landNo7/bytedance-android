package com.bytedance.camera.demo;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import static com.bytedance.camera.demo.utils.Utils.MEDIA_TYPE_IMAGE;
import static com.bytedance.camera.demo.utils.Utils.MEDIA_TYPE_VIDEO;
import static com.bytedance.camera.demo.utils.Utils.getOutputMediaFile;

public class CustomCameraActivity extends AppCompatActivity {

    private static final String TAG = "CustomCameraActivity";
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private SeekBar mZoomBar;
    private int CAMERA_TYPE = Camera.CameraInfo.CAMERA_FACING_BACK;

    private boolean isRecording = false;
    private int camIdx;
    private int rotationDegree = 0;
    private int mFps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_custom_camera);

        if (mCamera != null)
            releaseCameraAndPreview();

        mCamera = getCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
        camIdx = Camera.CameraInfo.CAMERA_FACING_BACK;
        mSurfaceView = findViewById(R.id.img);
        //todo 给SurfaceHolder添加Callback
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                try {
                    mCamera.setPreviewDisplay(surfaceHolder);
                    mCamera.startPreview();
                } catch (IOException e) {
                    Log.d(TAG, "surfaceCreated: ");
                    e.printStackTrace();
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        });
        findViewById(R.id.btn_picture).setOnClickListener(v -> {
            //todo 拍一张照片
            mCamera.takePicture(null,null,mPicture);
        });

        findViewById(R.id.btn_record).setOnClickListener(v -> {
            //todo 录制，第一次点击是start，第二次点击是stop
            if (isRecording) {
                //todo 停止录制
                mMediaRecorder.stop();
                mMediaRecorder.reset();
                mMediaRecorder.release();
                mMediaRecorder = null;
                mCamera.lock();
                isRecording = false;
                Log.d(TAG, "onCreate: isRecording = true");
            } else {
                //todo 录制
                Log.d(TAG, "onCreate: isRecording = false");
                mMediaRecorder = new MediaRecorder();
                mCamera.unlock();
                mMediaRecorder.setCamera(mCamera);
                mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
                mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
//                mMediaRecorder.setVideoSize(prviewSizeList.get(0).width,prviewSizeList.get(0).height);
                mMediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());
                Toast.makeText(this, getOutputMediaFile(MEDIA_TYPE_VIDEO).toString(),Toast.LENGTH_SHORT).show();
                mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
                mMediaRecorder.setOrientationHint(rotationDegree);
                try {
                    mMediaRecorder.prepare();
                    mMediaRecorder.start();

                } catch (Exception e) {
                    releaseMediaRecorder();
                }
                isRecording = true;
            }
        });

        findViewById(R.id.btn_facing).setOnClickListener(v -> {
            //todo 切换前后摄像头
            mCamera.stopPreview();
            mCamera.release();
            if (camIdx == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                camIdx = Camera.CameraInfo.CAMERA_FACING_BACK;
            } else {
                camIdx = Camera.CameraInfo.CAMERA_FACING_FRONT;
            }
            mCamera = Camera.open(camIdx);
            try {
                mCamera.setPreviewDisplay(mSurfaceHolder);
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        findViewById(R.id.btn_zoom).setOnClickListener(v -> {
            //todo 调焦，需要判断手机是否支持

        });

        mZoomBar = findViewById(R.id.seekbar_zoom);
        mZoomBar.setMax(mCamera.getParameters().getMaxZoom());
        mZoomBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                //if (mCamera.getParameters()
                Parameters p = mCamera.getParameters();
                if (p.isSmoothZoomSupported()) {
                    p.setZoom(progress);
                    mCamera.setParameters(p);
                }
                else {
                    Toast.makeText(CustomCameraActivity.this,"不支持对焦",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isFocusing;
/*
    private boolean newFocus(int x, int y) {
        //正在对焦时返回
        if (mCamera == null || isFocusing) {
            return false;
        }
        isFocusing = true;
        setMeteringRect(x, y);
        mCameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        mCamera.cancelAutoFocus(); // 先要取消掉进程中所有的聚焦功能
        try {
            mCamera.setParameters(mCameraParameters);
            mCamera.autoFocus(autoFocusCallback);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }*/

/*
    private void setConfig() {
        //设置封装格式 默认是MP4
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        //音频编码
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        //图像编码
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        //声道
        mMediaRecorder.setAudioChannels(1);
        //设置最大录像时间 单位：毫秒
        mMediaRecorder.setMaxDuration(60 * 1000);
        //设置最大录制的大小60M 单位，字节
        mMediaRecorder.setMaxFileSize(60 * 1024 * 1024);
        //再用44.1Hz采样率
        mMediaRecorder.setAudioEncodingBitRate(22050);
        //设置帧率，该帧率必须是硬件支持的，可以通过Camera.CameraParameter.getSupportedPreviewFpsRange()方法获取相机支持的帧率
        mMediaRecorder.setVideoFrameRate(mFps);
        //设置码率
        mMediaRecorder.setVideoEncodingBitRate(500 * 1024 * 8);
        //设置视频尺寸，通常搭配码率一起使用，可调整视频清晰度
        mMediaRecorder.setVideoSize(1280, 720);
    }*/

    public Camera getCamera(int position) {
        CAMERA_TYPE = position;
        if (mCamera != null) {
            releaseCameraAndPreview();
        }
        Camera cam = Camera.open(position);

        //todo 摄像头添加属性，例是否自动对焦，设置旋转方向等
        rotationDegree = getCameraDisplayOrientation(position);
        cam.setDisplayOrientation(rotationDegree);
//        cam = setCameraParameter(cam);
        return cam;
    }

    /*private Camera setCameraParameter(Camera camera) {
        if (camera == null) return null;
        Camera.Parameters parameters = camera.getParameters();
        //获取相机支持的>=20fps的帧率，用于设置给MediaRecorder
        //因为获取的数值是*1000的，所以要除以1000
        List<int[]> previewFpsRange = parameters.getSupportedPreviewFpsRange();
        for (int[] ints : previewFpsRange) {
            if (ints[0] >= 20000) {
                mFps = ints[0] / 1000;
                break;
            }
        }
        //设置聚焦模式
        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        } else {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }


        //设置预览尺寸,因为预览的尺寸和最终是录制视频的尺寸无关，所以我们选取最大的数值
        //通常最大的是手机的分辨率，这样可以让预览画面尽可能清晰并且尺寸不变形，前提是TextureView的尺寸是全屏或者接近全屏
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        parameters.setPreviewSize(supportedPreviewSizes.get(0).width, supportedPreviewSizes.get(0).height);
        //缩短Recording启动时间
        parameters.setRecordingHint(true);
        //是否支持影像稳定能力，支持则开启
        if (parameters.isVideoStabilizationSupported())
            parameters.setVideoStabilization(true);
        camera.setParameters(parameters);
        return camera;
    }*/

    private static final int DEGREE_90 = 90;
    private static final int DEGREE_180 = 180;
    private static final int DEGREE_270 = 270;
    private static final int DEGREE_360 = 360;

    private int getCameraDisplayOrientation(int cameraId) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = DEGREE_90;
                break;
            case Surface.ROTATION_180:
                degrees = DEGREE_180;
                break;
            case Surface.ROTATION_270:
                degrees = DEGREE_270;
                break;
            default:
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % DEGREE_360;
            result = (DEGREE_360 - result) % DEGREE_360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + DEGREE_360) % DEGREE_360;
        }
        return result;
    }


    private void releaseCameraAndPreview() {
        //todo 释放camera资源
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    Camera.Size size;

    private void startPreview(SurfaceHolder holder) {
        //todo 开始预览
    }


    private MediaRecorder mMediaRecorder;

    private boolean prepareVideoRecorder() {
        //todo 准备MediaRecorder

        return true;
    }


    private void releaseMediaRecorder() {
        //todo 释放MediaRecorder
        if (mMediaRecorder != null)
            mMediaRecorder.release();
    }


    private Camera.PictureCallback mPicture = (data, camera) -> {
        File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (pictureFile == null) {
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
        } catch (IOException e) {
            Log.d("mPicture", "Error accessing file: " + e.getMessage());
        }

        mCamera.startPreview();
    };


    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) h / w;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = Math.min(w, h);

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    @Override
    protected void onStop() {
        releaseCameraAndPreview();
        releaseMediaRecorder();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        releaseMediaRecorder();
        releaseCameraAndPreview();
        super.onDestroy();
    }
}
