package com.example.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.sample.controllers.SoundController;
import com.example.sample.extras.Utils;

//this activity is for when user clicks on a word from the WordListActivity and shows the users the trivia word with its definition
public class DetailsPageActivity extends Activity {
	public static final String WORD_TAG = "WORD_TAG";
	public static final String DEFINITION_TAG = "DEFINITION_TAG";
	public static final String EXAMPLE_TAG = "EXAMPLE_TAG";
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_page);
		
		final TextView tv_word = (TextView) findViewById(R.id.details_page_textview_word);
		TextView tv_dictionary = (TextView) findViewById(R.id.details_page_textview_definition);
		TextView tv_examples = (TextView) findViewById(R.id.details_page_textview_example);
		
		tv_word.setText(getIntent().getStringExtra(WORD_TAG));
		tv_dictionary.setText(getIntent().getStringExtra(DEFINITION_TAG));
		tv_examples.setText(getIntent().getStringExtra(EXAMPLE_TAG));
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
