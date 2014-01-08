package com.example.sample.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout.LayoutParams;

import com.example.sample.LevelsActivity;
import com.example.sample.extras.Utils;
import com.example.sample.models.SettingsData;
import com.example.sample.models.SettingsData.WordStat;
import com.example.sample.models.TermsDictionary;

public class LevelView extends View {
	private final int NUM_OF_FRAMES_X = 5;
	private final int NUM_OF_FRAMES_Y = 2;
	
	private Context _ctx;
	private CGRect _frame;

	private int _level;
	//REFACTOR: this part
	private Button _round1;
	private Button _round2;
	private Button _round3;
	private Button _round4;
	private Button _round5;
	private Button _round6;
	private Button _round7;
	private Button _round8;
	private Button _round9;
	private Button _round10;
	
	private Button[] _buttonsList;
	
	View _view;
	public LevelView(Context ctx){
		super(ctx);
		_ctx = ctx;
		_view = LayoutInflater.from(ctx).inflate(R.layout.level, null);
		_level = -1;
	}
	
	public LevelView(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);
		_ctx = ctx;
		_view = LayoutInflater.from(ctx).inflate(R.layout.level, null);
		_level = -1;
	}
	
	public void setLevel(int level){
		_level = level;
	}
	
	public void unlockRounds(int rounds, boolean lastRound){
		String answered;
		String word;
		//see which buttons should be unlocked
		for(int i=0; i<rounds; i++){
			word = TermsDictionary.getQuestionAt((_level-1)*LevelsActivity.ROUNDS_PER_LEVEL + i).getWord();
			answered = SettingsData.getWordAnswered( word );
			if(answered.equals(WordStat.ANSWERED_RIGHT)){
				_buttonsList[i].setBackgroundResource(R.drawable.rounds_screen_image_round_completed_correct);
			} else if (answered.equals(WordStat.ANSWERED_WRONG)){
				_buttonsList[i].setBackgroundResource(R.drawable.rounds_screen_image_round_completed_incorrect);
			} else {
				//logic error... should never be this case...
			}
		}
		if(!lastRound){
			_buttonsList[rounds].setBackgroundResource(R.drawable.rounds_screen_image_round_incomplete);
		} else {
			answered = SettingsData.getWordAnswered( TermsDictionary.getQuestionAt((_level-1)*LevelsActivity.ROUNDS_PER_LEVEL + rounds).getWord());
			if(answered.equals(WordStat.ANSWERED_RIGHT)){
				_buttonsList[rounds].setBackgroundResource(R.drawable.rounds_screen_image_round_completed_correct);
			} else if (answered.equals(WordStat.ANSWERED_WRONG)){
				_buttonsList[rounds].setBackgroundResource(R.drawable.rounds_screen_image_round_completed_incorrect);
			}
		}
	}
	
	public void initRounds(View.OnClickListener[] onClickListeners){
		int screen_height = Utils.getScreenHeight(_ctx);
		int screen_width = Utils.getScreenWidth(_ctx);
		
		//TODO isn't there a better way to do this?
		//with adjustments, cus KindleFire has that huge bar on bottom, and messes things up
		screen_height = (screen_height*3)/4;
		screen_width = (screen_width*4)/5;
		int x_offset = (screen_width*1)/10;
		
		_round1 = (Button) _view.findViewById(R.id.round_button_1);
		_round2 = (Button) _view.findViewById(R.id.round_button_2);
		_round3 = (Button) _view.findViewById(R.id.round_button_3);
		_round4 = (Button) _view.findViewById(R.id.round_button_4);
		_round5 = (Button) _view.findViewById(R.id.round_button_5);
		_round6 = (Button) _view.findViewById(R.id.round_button_6);
		_round7 = (Button) _view.findViewById(R.id.round_button_7);
		_round8 = (Button) _view.findViewById(R.id.round_button_8);
		_round9 = (Button) _view.findViewById(R.id.round_button_9);
		_round10 = (Button) _view.findViewById(R.id.round_button_10);
		
		_buttonsList = new Button[]{_round1,_round2,_round3,_round4,_round5,_round6,_round7,_round8,_round9,_round10};
		
		int image_width = _round1.getBackground().getMinimumWidth();
		int image_height = _round1.getBackground().getMinimumHeight();
		int frame_width = screen_width/NUM_OF_FRAMES_X;
		int frame_height = screen_height/NUM_OF_FRAMES_Y;
		
		for(int i=0; i<_buttonsList.length; i++){
			LayoutParams lp = new LayoutParams(image_width, image_height);
			int x = (i%NUM_OF_FRAMES_X)*frame_width;
			int y = (i/NUM_OF_FRAMES_X)*frame_height;
			_frame = new CGRect( x, y, frame_width, frame_height );
			_frame.calcImageCoordinates(image_width, image_height);
			lp.setMargins(_frame.getImageX() + x_offset, _frame.getImageY(), 0, 0);
			_buttonsList[i].setLayoutParams(lp);
		}
		for(int i=0; i<onClickListeners.length;i++){
			_buttonsList[i].setOnClickListener(onClickListeners[i]);
		}
	}
	
	public View getCurrentView(){
		return _view;
	}
	
	public int getLevel(){
		return _level;
	}
	
	private static class CGRect{
		private int _x;
		private int _y;
		private int _width;
		private int _height;
		private int _imageX;
		private int _imageY;
		public CGRect(int x, int y, int width, int height){
			_x = x;
			_y = y;
			_width = width;
			_height = height;
		}
		public void calcImageCoordinates(int imageWidth, int imageHeight){
			_imageX = _x + (_width/2) - (imageWidth/2);
			_imageY = _y + (_height/2) - (imageHeight/2);
		}
		public int getImageX(){
			return _imageX;
		}
		public int getImageY(){
			return _imageY;
		}
	}
}
