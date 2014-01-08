package com.example.sample;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

//REFACTOR: admittedly, this was done in a rush, so could have made things slightly less tight-coupling
//this activity shows a scrolly view of all the words, set out in pages
public class LevelsActivity extends Activity {
	public static final int ROUNDS_PER_LEVEL = 10;
	public static final String START_AT_ROUND = "START_AT_ROUND";
	private static List<View.OnClickListener> _listenersList;
	
	private LevelView _level1;
	private LevelView _level2;
	private LevelView _level3;
	private LevelView _level4;
	private LevelView _level5;
	
	private RadioGroup _radioGroup;
	
	HorizontalPager _hPager;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		init();
		
		setContentView(R.layout.levels_shell);
		_hPager = (HorizontalPager) findViewById(R.id.levels_shell_horizontal_pager);
		_radioGroup = (RadioGroup) findViewById(R.id.levels_shell_radio_group);
		_radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch(checkedId){
				case R.id.levels_shell_radio_page_1:
					_hPager.setCurrentScreen(0, true);
					break;
				case R.id.levels_shell_radio_page_2:
					_hPager.setCurrentScreen(1, true);
					break;
				case R.id.levels_shell_radio_page_3:
					_hPager.setCurrentScreen(2, true);
					break;
				case R.id.levels_shell_radio_page_4:
					_hPager.setCurrentScreen(3, true);
					break;
				case R.id.levels_shell_radio_page_5:
					_hPager.setCurrentScreen(4, true);
					break;					
				}
			}
		});
		
		_level1 = new LevelView(this);
		_level1.setLevel(1);
		_level2 = new LevelView(this);
		_level2.setLevel(2);
		_level3 = new LevelView(this);
		_level3.setLevel(3);
		_level4 = new LevelView(this);
		_level4.setLevel(4);
		_level5 = new LevelView(this);
		_level5.setLevel(5);
		
		_level1.initRounds(getOnClickListeners(0,ROUNDS_PER_LEVEL));
		_level2.initRounds(getOnClickListeners(ROUNDS_PER_LEVEL, ROUNDS_PER_LEVEL*2));
		_level3.initRounds(getOnClickListeners(ROUNDS_PER_LEVEL*2, ROUNDS_PER_LEVEL*3));
		_level4.initRounds(getOnClickListeners(ROUNDS_PER_LEVEL*3, ROUNDS_PER_LEVEL*4));
		_level5.initRounds(getOnClickListeners(ROUNDS_PER_LEVEL*4, ROUNDS_PER_LEVEL*5));
		
		updateLevelsView();
		
		_hPager.addView(_level1.getCurrentView());
		_hPager.addView(_level2.getCurrentView());
		_hPager.addView(_level3.getCurrentView());
		_hPager.addView(_level4.getCurrentView());
		_hPager.addView(_level5.getCurrentView());		
		
		_hPager.setOnScreenSwitchListener(new HorizontalPager.OnScreenSwitchListener() {
			@Override
			public void onScreenSwitched(int screen) {
				switch(screen){
				case 0: _radioGroup.check(R.id.levels_shell_radio_page_1);
						_hPager.setCurrentScreen(0, true);
						break;
				case 1: _radioGroup.check(R.id.levels_shell_radio_page_2);
						_hPager.setCurrentScreen(1, true);
						break;
				case 2: _radioGroup.check(R.id.levels_shell_radio_page_3);
						_hPager.setCurrentScreen(2, true);
						break;
				case 3: _radioGroup.check(R.id.levels_shell_radio_page_4);
						_hPager.setCurrentScreen(3, true);
						break;
				case 4: _radioGroup.check(R.id.levels_shell_radio_page_5);
						_hPager.setCurrentScreen(4, true);
						break;				
				}
			}
		});
		
		switch (getCurrentLevel()) {
		case 1: _radioGroup.check(R.id.levels_shell_radio_page_1);
			break;
		case 2: _radioGroup.check(R.id.levels_shell_radio_page_2);	
			break;
		case 3: _radioGroup.check(R.id.levels_shell_radio_page_3);
			break;
		case 4: _radioGroup.check(R.id.levels_shell_radio_page_4);	
			break;
		case 5: _radioGroup.check(R.id.levels_shell_radio_page_5);	
			break;
		}
		
		if(!SettingsData.hasShownUserNSFWWarning()){
			AlertDialog.Builder b = new AlertDialog.Builder(this);
			b.setTitle("WARNING - NSFW!")
			.setMessage(getResources().getString(R.string.nsfw_message))
			.setCancelable(false)
			.setPositiveButton("I Accept", null)
			.show();
			SettingsData.setShownUserNSFWWarning(this, true);
		}
	}
	
	@Override
	public void onPause(){
		SoundController.pauseMusic();
		super.onPause();
	}
	
	@Override
	public void onResume(){
		SoundController.playMusic();
		updateLevelsView();
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
	
    //============================================= Display logic =============================================
    
	private void updateLevelsView(){
		if(getCurrentLevel() == 1){
			_radioGroup.check(R.id.levels_shell_radio_page_1);
			_hPager.setCurrentScreen(0, true);
			_level1.unlockRounds(SettingsData.getRound(), false);
		} else if (getCurrentLevel() == 2) {
			_radioGroup.check(R.id.levels_shell_radio_page_2);
			_hPager.setCurrentScreen(1, true);
			_level1.unlockRounds(ROUNDS_PER_LEVEL-1, true);
			_level2.unlockRounds(SettingsData.getRound()%ROUNDS_PER_LEVEL, false);
		} else if (getCurrentLevel() == 3) {
			_radioGroup.check(R.id.levels_shell_radio_page_3);
			_hPager.setCurrentScreen(2, true);
			_level1.unlockRounds(ROUNDS_PER_LEVEL-1, true);
			_level2.unlockRounds(ROUNDS_PER_LEVEL-1, true);
			_level3.unlockRounds(SettingsData.getRound()%ROUNDS_PER_LEVEL, false);
		} else if (getCurrentLevel() == 4) {
			_radioGroup.check(R.id.levels_shell_radio_page_4);
			_hPager.setCurrentScreen(3, true);
			_level1.unlockRounds(ROUNDS_PER_LEVEL-1, true);
			_level2.unlockRounds(ROUNDS_PER_LEVEL-1, true);
			_level3.unlockRounds(ROUNDS_PER_LEVEL-1, true);
			_level4.unlockRounds(SettingsData.getRound()%ROUNDS_PER_LEVEL, false);
		} else if (getCurrentLevel() == 5) {
			_radioGroup.check(R.id.levels_shell_radio_page_5);
			_hPager.setCurrentScreen(4, true);
			_level1.unlockRounds(ROUNDS_PER_LEVEL-1, true);
			_level2.unlockRounds(ROUNDS_PER_LEVEL-1, true);
			_level3.unlockRounds(ROUNDS_PER_LEVEL-1, true);
			_level4.unlockRounds(ROUNDS_PER_LEVEL-1, true);
			_level5.unlockRounds(SettingsData.getRound()%ROUNDS_PER_LEVEL, false);
		} else {
			//game is finished :)
			if(!SettingsData.hasShownUserGameFinished()){
				showUserGameFinished();
				SettingsData.setShownUserGameFinished(this, true);
			}
			_level1.unlockRounds(ROUNDS_PER_LEVEL-1, true);
			_level2.unlockRounds(ROUNDS_PER_LEVEL-1, true);
			_level3.unlockRounds(ROUNDS_PER_LEVEL-1, true);
			_level4.unlockRounds(ROUNDS_PER_LEVEL-1, true);
			_level5.unlockRounds(ROUNDS_PER_LEVEL-1, true);
		}
	}
	
	private void init(){
		if(_listenersList!=null){
			return;
		}
		_listenersList = new ArrayList<View.OnClickListener>();
		for(int i=0; i<SettingsData.getTotalRounds(); i++){
			final int tempI = i;
			_listenersList.add(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(tempI <= SettingsData.getRound()){
						Intent iGameActivity = new Intent(LevelsActivity.this,GameActivity.class);
						iGameActivity.putExtra(START_AT_ROUND, tempI);
						LevelsActivity.this.startActivity(iGameActivity);
					}
				}
			});
		}
	}
	
	//start and end are not... inclusive... this is a range of the rounds
	public static View.OnClickListener[] getOnClickListeners(int start, int end){
		View.OnClickListener[] list = new View.OnClickListener[end-start];
		for(int i=0; i<end-start; i++){
			list[i] = _listenersList.get(start+i);
		}
		return list;
	}
	
	private void showUserGameFinished(){
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setTitle("Completed Challenge")
		.setMessage(getResources().getString(R.string.victory_message))
		.setCancelable(false)
		.setPositiveButton("Woot!", null)
		.show();
		SoundController.playUserFinishedGame();
	}
	
	private int getCurrentLevel(){
		if(SettingsData.getRound() < ROUNDS_PER_LEVEL){
			return 1;
		} else if(SettingsData.getRound() >= ROUNDS_PER_LEVEL && SettingsData.getRound() < ROUNDS_PER_LEVEL*2 ){
			return 2;
		} else if(SettingsData.getRound() >= (ROUNDS_PER_LEVEL*2) && SettingsData.getRound() < ROUNDS_PER_LEVEL*3 ){
			return 3;
		} else if(SettingsData.getRound() >= (ROUNDS_PER_LEVEL*3) && SettingsData.getRound() < ROUNDS_PER_LEVEL*4 ){
			return 4;
		} else if(SettingsData.getRound() >= (ROUNDS_PER_LEVEL*4) && SettingsData.getRound() < ROUNDS_PER_LEVEL*5 ){
			return 5;
		} else {

			return 6;
		}
	}
}
