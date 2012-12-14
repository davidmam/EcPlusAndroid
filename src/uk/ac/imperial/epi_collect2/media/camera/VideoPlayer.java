package uk.ac.imperial.epi_collect2.media.camera;

import uk.ac.imperial.epi_collect2.R;
import uk.ac.imperial.epi_collect2.util.db.DBAccess;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.graphics.Color;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;


public class VideoPlayer extends Activity {

	     private VideoView videoView; 
	     private String videodir;
	     private ImageButton endButton; // playButton, 
	     private boolean isplaying = false; //, ispaused = false;
	     private TextView playtv;
	     
	     /** Called when the activity is first created. */ 
	     @Override 
	     public void onCreate(Bundle savedInstanceState) { 
	          super.onCreate(savedInstanceState); 
	          requestWindowFeature(Window.FEATURE_NO_TITLE); 
	          setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); 
	          setContentView(uk.ac.imperial.epi_collect2.R.layout.video_player); 
	          	         		         
	          //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	          videoView = (VideoView)findViewById(uk.ac.imperial.epi_collect2.R.id.videoView); 
	         
	          //playButton = (ImageButton) findViewById(uk.ac.imperial.epi_collect2.R.id.playbut);
	          endButton = (ImageButton) findViewById(uk.ac.imperial.epi_collect2.R.id.endbut);
	          playtv = (TextView)findViewById(uk.ac.imperial.epi_collect2.R.id.playtext);
	          playtv.setTextColor(Color.RED);
	          
	         DBAccess dbAccess = new DBAccess(this);
	  	     dbAccess.open();
	  	     
	         videodir = Environment.getExternalStorageDirectory()+"/EpiCollect/videodir_epicollect_" + dbAccess.getProject(); //this.getResources().getString(this.getResources().getIdentifier(this.getPackageName()+":string/project", null, null));
	          
	         Bundle extras = getIntent().getExtras();
	         if (extras != null && extras.getString("VIDEO_ID") != null){
	        	 //Log.i("VIDEO FILE", extras.getString("VIDEO_ID"));
	        	 videoView.setVideoURI(Uri.parse(videodir+"/"+extras.getString("VIDEO_ID")));
	         }
	         else{
	        	 showAlert(this.getResources().getString(R.string.video_problem)); //"Problem with video!");
	         }
	         
	         endButton.setImageResource(uk.ac.imperial.epi_collect2.R.drawable.eject24);
	         
	         videoView.start();
	         isplaying = true;
	         //playButton.setImageResource(uk.ac.imperial.epi_collect2.R.drawable.pause24);

	         //playButton..setText("Pause");
	         
	         //videoView.requestFocus();
	         
	         MediaController mc = new MediaController(this);
	         videoView.setMediaController(mc);
	         
	         /*playButton.setOnClickListener(new View.OnClickListener() {

	              public void onClick(View arg0) {
	            	  //startRecording();
	            	  setPlay();
	              }
	             
	          }); */
	         
	         endButton.setOnClickListener(new View.OnClickListener() {

	              public void onClick(View arg0) {
	            	  //startRecording();
	            	  endVideo();
	              }
	             
	          });
	         
	         
	         videoView.setOnCompletionListener(new OnCompletionListener(){
	        	 public void onCompletion(MediaPlayer mp) {
	        		 // TODO Auto-generated method stub
	        		 //playButton.setText("Play");
	        		 //playButton.setImageResource(uk.ac.imperial.epi_collect2.R.drawable.play24);
	        		 isplaying = false;
	        	 }
	         });
	         
	     } 
	     
	    /* private void setPlay(){
	    	 
	    	 if(!isplaying){
	    		 videoView.start();
	    		 //playButton.setText("Pause");
	    		 //playButton.setImageResource(uk.ac.imperial.epi_collect2.R.drawable.pause24);
	    		 playtv.setText("");
	    		 isplaying = true;
	    	 }
	    	 else{
	    		 videoView.pause();
	    		 //playButton.setText("Play");
	    		//playButton.setImageResource(uk.ac.imperial.epi_collect2.R.drawable.play24);
	    		 playtv.setText("PAUSED");
	    		 isplaying = false;
	    	 }
	     } */
	     
	     private void endVideo(){
	    	 playtv.setText("");
	    	 if(isplaying)
	    		 videoView.stopPlayback();

       	  	Bundle extras = getIntent().getExtras();
       	  	this.getIntent().putExtras(extras);
       	  	setResult(RESULT_OK, this.getIntent());
       	  	finish();  
	     }
	     
	     public void showAlert(String result){
     		new AlertDialog.Builder(this)
     	    .setTitle(R.string.error) //"Error")
     	    .setMessage(result)
     	    .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {

     	         public void onClick(DialogInterface dialog, int whichButton) {
     	        	endVideo();
     	         }
     	    }).show();	
     	}
	     
	     
	     
	     
}