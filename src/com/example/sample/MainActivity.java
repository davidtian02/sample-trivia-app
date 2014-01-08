package com.example.sample;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	private Button _gotoRoundsActivity;
	private Button _gotoScoreActivity;
	private Button _gotoWordListActivity;
	
	private volatile ImageView iv;
	private volatile long timeSlept = 0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Tapjoy integration
        TapjoyConnect.requestTapjoyConnect(this, "", "");
        
		//ABSOLUTELY CRITICAL!!!
        DataController.loadData(MainActivity.this);
        TermsDictionary.init(MainActivity.this);
        SoundController.init(this);
        
        iv = new ImageButton(this);
        iv.setBackgroundResource(R.drawable.splash_screen);
        setContentView(iv);
    	Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation);
    	iv.startAnimation(fadeInAnimation);
        
        new waitOnSplashScreen().execute();
    }
    
    @Override
    public void onResume(){
    	SoundController.playMusic();
    	super.onResume();
    }
    
    @Override
    public void onPause(){
    	SoundController.pauseMusic();
    	super.onPause();
    }
    
    @Override
    public void onDestroy(){
    	SoundController.tearDown();
    	super.onDestroy();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
    	return Utils.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	if(Utils.onOptionsItemSelected(item, this)){
    		return true;
    	} else {
    		return super.onOptionsItemSelected(item);
    	}
    }
    
    //================================== Splash Screen ==================================
    
    private class waitOnSplashScreen extends AsyncTask<Void, Void, Void>{
		@Override
		protected Void doInBackground(Void... arg0) {
			timeSlept = 0;
			long now = System.currentTimeMillis();

	        long later = System.currentTimeMillis();
	        timeSlept = later - now;
	        try{
	        	while(timeSlept < 3000){
	        		Thread.sleep(100);
	        		timeSlept += 100;
	        	}
	        	MainActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Animation fadeOutAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out_animation);
						iv.startAnimation(fadeOutAnimation);
					}
				});
	        	while(timeSlept < 100){
	        		Thread.sleep(100);
	        		timeSlept += 100;
	        	}
	        } catch (InterruptedException ie){
	        	ie.printStackTrace();
	        }
	        
			return null;
		}
		
    	@Override
    	protected void onPostExecute(Void param){
    		setContentView(R.layout.main);
            
    		SoundController.playMusic();
    		
            _gotoRoundsActivity = (Button) findViewById(R.id.main_button_goto_rounds_activity);
            _gotoRoundsActivity.setOnClickListener(new View.OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				MainActivity.this.startActivity(new Intent(MainActivity.this,LevelsActivity.class));
    			}
    		});
            
            _gotoScoreActivity = (Button) findViewById(R.id.main_button_goto_score_activity);
            _gotoScoreActivity.setOnClickListener(new View.OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				MainActivity.this.startActivity(new Intent(MainActivity.this,ScoreActivity.class));
    			}
    		});
            
            _gotoWordListActivity = (Button) findViewById(R.id.main_button_goto_dictionary_activity);
            _gotoWordListActivity.setOnClickListener(new View.OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				MainActivity.this.startActivity(new Intent(MainActivity.this,WordListActivity.class));
    			}
    		});
            
            TextView tv_title = (TextView) findViewById(R.id.main_textview_title_box);
            tv_title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/ChocolateCooky.ttf"));
    	}
    }
}