package com.example.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//this merely displays the user score or leads them to an extra "info" activity
public class ScoreActivity extends Activity {
	private static final String SCORE_PREFIX = "Score: ";
	private static int MAX_SCORE;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.score);
		
		MAX_SCORE = SettingsData.isUserTypePro() ? 5000 : 500;
		
		final TextView tv_score = (TextView) findViewById(R.id.score_box);
		if(SettingsData.getLocalScore() >= MAX_SCORE){
			tv_score.setText(SCORE_PREFIX + "something");
		} else {
			tv_score.setText(SCORE_PREFIX + SettingsData.getLocalScore());
		}
		
		Button b_reset = (Button) findViewById(R.id.score_button_reset);
		Button b_info = (Button) findViewById(R.id.score_button_info);
		
		b_reset.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder b = new AlertDialog.Builder(ScoreActivity.this);
				b.setTitle("Reset Everything?")
				.setMessage("Are you sure you want to reset all levels and score?")
				.setCancelable(true)
				.setPositiveButton("Reset", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						DataController.resetData(ScoreActivity.this);
						tv_score.setText(SCORE_PREFIX + "0");
					}
				})
				.setNegativeButton("No", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				})
				.show();
				
			}
		});
		
		b_info.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ScoreActivity.this.startActivity(new Intent(ScoreActivity.this, InfoActivity.class));
			}
		});
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
}
