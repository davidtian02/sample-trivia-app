package com.example.sample;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.example.sample.controllers.SoundController;

public class InfoActivity extends Activity {
	
	private RadioGroup _radioGroup;
	private HorizontalPager _hPager;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info);

		String titlesArray[] = {
									getString(R.string.info_title_app_general),
									getString(R.string.info_title_executive_producer),
									getString(R.string.info_title_lead_designer),
									getString(R.string.info_title_lead_programmer),
									getString(R.string.info_title_special_thanks),
									getString(R.string.info_title_music),
									getString(R.string.info_title_sound_correct),
									getString(R.string.info_title_sound_incorrect),
									getString(R.string.info_title_sound_finished_game),
									getString(R.string.info_title_sound_purchased_words),
									getString(R.string.info_title_word_content),
									getString(R.string.info_title_disclaimer),
									getString(R.string.info_title_EULA),
								};
		String contentsArray[] = {
									getString(R.string.info_content_app_general),
									getString(R.string.info_content_executive_producer),
									getString(R.string.info_content_lead_designer),
									getString(R.string.info_content_lead_programmer),
									getString(R.string.info_content_special_thanks),
									getString(R.string.info_content_music),
									getString(R.string.info_content_sound_correct),
									getString(R.string.info_content_sound_incorrect),
									getString(R.string.info_content_sound_finished_game),
									getString(R.string.info_content_sound_purchased_words),
									getString(R.string.info_content_word_content),
									getString(R.string.info_content_disclaimer),
									getString(R.string.info_content_EULA),
								};
		
		_hPager = (HorizontalPager) findViewById(R.id.info_horizontal_pager);
		
		LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View page;
		TextView title;
		TextView content;
		
		if(li!=null){
			for(int i=0; i<titlesArray.length; i++){
				page = li.inflate(R.layout.info_entry, null);
				title = (TextView) page.findViewById(R.id.info_entry_textview_title);
				content = (TextView) page.findViewById(R.id.info_entry_textview_content);
				title.setText(titlesArray[i]);
				content.setText(contentsArray[i]);

				_hPager.addView(page);
			}
		}
		//REFACTOR: with more time, this should be done in an array
		_radioGroup = (RadioGroup) findViewById(R.id.info_radio_group);
		_radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch(checkedId){
				case R.id.info_radio_page_1:
					_hPager.setCurrentScreen(0, true);
					break;
				case R.id.info_radio_page_2:
					_hPager.setCurrentScreen(1, true);
					break;
				case R.id.info_radio_page_3:
					_hPager.setCurrentScreen(2, true);
					break;
				case R.id.info_radio_page_4:
					_hPager.setCurrentScreen(3, true);
					break;
				case R.id.info_radio_page_5:
					_hPager.setCurrentScreen(4, true);
					break;
				case R.id.info_radio_page_6:
					_hPager.setCurrentScreen(5, true);
					break;
				case R.id.info_radio_page_7:
					_hPager.setCurrentScreen(6, true);
					break;
				case R.id.info_radio_page_8:
					_hPager.setCurrentScreen(7, true);
					break;
				case R.id.info_radio_page_9:
					_hPager.setCurrentScreen(8, true);
					break;
				case R.id.info_radio_page_10:
					_hPager.setCurrentScreen(9, true);
					break;
				case R.id.info_radio_page_11:
					_hPager.setCurrentScreen(10, true);
					break;
				case R.id.info_radio_page_12:
					_hPager.setCurrentScreen(11, true);
					break;
				case R.id.info_radio_page_13:
					_hPager.setCurrentScreen(12, true);
					break;					
				}
			}
		});
		_hPager.setOnScreenSwitchListener(new HorizontalPager.OnScreenSwitchListener() {
			@Override
			public void onScreenSwitched(int screen) {
				switch(screen){
				case 0: _radioGroup.check(R.id.info_radio_page_1);
						break;
				case 1: _radioGroup.check(R.id.info_radio_page_2);
						break;
				case 2: _radioGroup.check(R.id.info_radio_page_3);
						break;
				case 3: _radioGroup.check(R.id.info_radio_page_4);
						break;
				case 4: _radioGroup.check(R.id.info_radio_page_5);
						break;
				case 5: _radioGroup.check(R.id.info_radio_page_6);
						break;
				case 6: _radioGroup.check(R.id.info_radio_page_7);
						break;
				case 7: _radioGroup.check(R.id.info_radio_page_8);
						break;
				case 8: _radioGroup.check(R.id.info_radio_page_9);
						break;
				case 9: _radioGroup.check(R.id.info_radio_page_10);
						break;
				case 10: _radioGroup.check(R.id.info_radio_page_11);
						break;
				case 11: _radioGroup.check(R.id.info_radio_page_12);
					break;
				case 12: _radioGroup.check(R.id.info_radio_page_13);
					break;				
				}
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