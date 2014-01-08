package com.example.sample;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

//this just shows a list of all the words in the trivia
public class WordListActivity extends ListActivity{
	private WordListAdapter mAdapter;
	private int currentPosition;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		String[] wordsListWithHeaders = TermsDictionary.getWordListWithHeaders(this);
		mAdapter = new WordListAdapter(this, R.layout.wordlist_row, wordsListWithHeaders);
		setListAdapter(mAdapter);
		getListView().setBackgroundResource(R.drawable.background);
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(!TermsDictionary.isPositionHeader(WordListActivity.this, position) && ! (SettingsData.getWordAnswered(TermsDictionary.getWordAtListPosition(WordListActivity.this,position)).equals(SettingsData.WordStat.ANSWERED_UNANSWERED))){
					String word = TermsDictionary.getWordAtListPosition(WordListActivity.this,position);
					String definition = TermsDictionary.getDefinitionAtListPosition(WordListActivity.this,position);
					String example = TermsDictionary.getExampleAtListPosition(WordListActivity.this,position);
					Intent iDetailsPage = new Intent(WordListActivity.this, DetailsPageActivity.class);
					iDetailsPage.putExtra(DetailsPageActivity.WORD_TAG, word);
					iDetailsPage.putExtra(DetailsPageActivity.DEFINITION_TAG, definition);
					iDetailsPage.putExtra(DetailsPageActivity.EXAMPLE_TAG, example);
					WordListActivity.this.startActivity(iDetailsPage);
				}
			}
		});
		
		if(!SettingsData.hasShownUserFirstTimeDictionary()){
			AlertDialog.Builder b = new AlertDialog.Builder(this);
			b.setTitle("Dictionary")
			.setMessage(getString(R.string.first_time_dictionary_message))
			.setCancelable(false)
			.setPositiveButton("Okay", null)
			.show();
			SettingsData.setShownUserFirstTimeDictionary(this, true);
		}
	}
	
	@Override
	public void onPause(){
		SoundController.pauseMusic();
		currentPosition = getListView().getFirstVisiblePosition();
		super.onPause();
	}
	
	@Override
	public void onResume(){
		SoundController.playMusic();
		getListView().setSelection(currentPosition);
		super.onResume();
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
