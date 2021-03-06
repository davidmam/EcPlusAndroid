package uk.ac.imperial.epi_collect2.media.camera;

import java.io.File;
import java.io.IOException;

import uk.ac.imperial.epi_collect2.Epi_collect;
import uk.ac.imperial.epi_collect2.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class CustomVideoCamera extends Activity implements SurfaceHolder.Callback{

	private static final String TAG = "CAMERA_TUTORIAL";

	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	private Camera camera;
	private boolean previewRunning;

        //@Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(uk.ac.imperial.epi_collect2.R.layout.surface);
                surfaceView = (SurfaceView) findViewById(uk.ac.imperial.epi_collect2.R.id.surface_camera);
                surfaceHolder = surfaceView.getHolder();
                surfaceHolder.addCallback(this);
                surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

    //@Override
	public void surfaceCreated(SurfaceHolder holder) {
		camera = Camera.open();
		if (camera != null){
			Camera.Parameters params = camera.getParameters();
			camera.setParameters(params);
		}
		else {
			Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.no_camera), Toast.LENGTH_LONG).show();
			finish();
		}
	}

	//@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		if (previewRunning){
			camera.stopPreview();
		}
		Camera.Parameters p = camera.getParameters();
		p.setPreviewSize(width, height);
		p.setPreviewFormat(PixelFormat.JPEG);
		camera.setParameters(p);

		try {
			camera.setPreviewDisplay(holder);
			camera.startPreview();
			previewRunning = true;
		}
		catch (IOException e) {
			Log.e(TAG,e.getMessage());
			e.printStackTrace();
		}
	}

	//@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		camera.stopPreview();
		previewRunning = false;
		camera.release();
	}
	
	private MediaRecorder mediaRecorder;
	//private final int maxDurationInMs = 20000;
	//private final long maxFileSizeInBytes = 500000;
	private final int videoFramesPerSecond = 30; //20;

	public boolean startRecording(){
		try {
			camera.unlock();

			mediaRecorder = new MediaRecorder();

			mediaRecorder.setCamera(camera);
			mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

			mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);

			//mediaRecorder.setMaxDuration(maxDurationInMs);
			
			CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);

			mediaRecorder.setProfile(profile);

			File tempFile = new File(Epi_collect.appFiles, "temp.mpg");
			mediaRecorder.setOutputFile(tempFile.getPath());

			mediaRecorder.setVideoFrameRate(videoFramesPerSecond);
			mediaRecorder.setVideoSize(surfaceView.getWidth(), surfaceView.getHeight());

			mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
			mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);

			mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());

			//mediaRecorder.setMaxFileSize(maxFileSizeInBytes);

            mediaRecorder.prepare();
			mediaRecorder.start();

			return true;
		} catch (IllegalStateException e) {
			Log.e(TAG,e.getMessage());
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			Log.e(TAG,e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	public void stopRecording(){
		mediaRecorder.stop();
		camera.lock();
	}


}
