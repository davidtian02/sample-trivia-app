package com.example.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

//this handles the game logic
public class GameActivity extends Activity {
	private static final int DELAY_TIME_ANSWER = 300;
	
	private Button _answerChoice1;
	private Button _answerChoice2;
	private Button _answerChoice3;
	private Button _answerChoice4;
	private TextView _definition;
	private TextView _example;
	private int _currentPoints;
	private Button _correctAnswer;
	private Button _wrongAnswer1;
	private Button _wrongAnswer2;
	private Button _wrongAnswer3;
	private int _correctAnswerDrawable;
	private Dialog _screenBlocker;
	private ImageView _thumbsUp; 
	private ImageView _thumbsDown;
	private int _currentThumbsUpImage;
	private int _currentThumbsDownImage;
	private Term _nextQuestion;
	private int _roundNumber;
	private int _currentWord;
	private int _wordsMissed;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		_roundNumber = getIntent().getIntExtra(LevelsActivity.START_AT_ROUND, -1);
		_currentWord = _roundNumber;
		_wordsMissed = 0;
		initializeGame();
	}
	
	@Override
	public void onResume(){
		SoundController.playMusic();
		super.onResume();
	}
	
	@Override
	public void onPause(){
		SoundController.pauseMusic();
		SoundController.pauseReadOutLoud(this);
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
	
    //================================ Game Logic ================================
    
	private void initializeGame(){
		_answerChoice1 = (Button) findViewById(R.id.game_button_answer_choice_1);
		_answerChoice2 = (Button) findViewById(R.id.game_button_answer_choice_2);
		_answerChoice3 = (Button) findViewById(R.id.game_button_answer_choice_3);
		_answerChoice4 = (Button) findViewById(R.id.game_button_answer_choice_4);
		_definition = (TextView) findViewById(R.id.game_textview_definition);
		_example = (TextView) findViewById(R.id.game_textview_example);
		_currentPoints = 10;
		
		initAnswerChoiceAction(_answerChoice1, R.drawable.game_screen_button_top_left_incorrect);
		initAnswerChoiceAction(_answerChoice2, R.drawable.game_screen_button_top_right_incorrect);
		initAnswerChoiceAction(_answerChoice3, R.drawable.game_screen_button_bottom_left_incorrect);
		initAnswerChoiceAction(_answerChoice4, R.drawable.game_screen_button_bottom_right_incorrect);
		
		_screenBlocker = new Dialog(this, android.R.style.Theme_Panel);
		
		_thumbsUp = (ImageView) findViewById(R.id.game_imageview_thumbs_up);
		_thumbsDown = (ImageView) findViewById(R.id.game_imageview_thumbs_down);
		
		nextRound();
	}
	
	private void nextRound(){
		_nextQuestion = TermsDictionary.getQuestionAt(_roundNumber);
		int correctAnswerNumber = _nextQuestion.getCorrectAnswerNumber();
		
		switch(correctAnswerNumber){
		case 0: _correctAnswer = _answerChoice1;
				_wrongAnswer1 = _answerChoice2;
				_wrongAnswer2 = _answerChoice3;
				_wrongAnswer3 = _answerChoice4;
				_correctAnswerDrawable = R.drawable.game_screen_button_top_left_correct;
				break;
		case 1: _correctAnswer = _answerChoice2;
				_wrongAnswer1 = _answerChoice1;
				_wrongAnswer2 = _answerChoice3;
				_wrongAnswer3 = _answerChoice4;
				_correctAnswerDrawable = R.drawable.game_screen_button_top_right_correct;
				break;
		case 2: _correctAnswer = _answerChoice3;
				_wrongAnswer1 = _answerChoice1;
				_wrongAnswer2 = _answerChoice2;
				_wrongAnswer3 = _answerChoice4;
				_correctAnswerDrawable = R.drawable.game_screen_button_bottom_left_correct;
				break;
		case 3: _correctAnswer = _answerChoice4;
				_wrongAnswer1 = _answerChoice1;
				_wrongAnswer2 = _answerChoice2;
				_wrongAnswer3 = _answerChoice3;
				_correctAnswerDrawable = R.drawable.game_screen_button_bottom_right_correct;
				break;
		}
		
		_correctAnswer.setText(_nextQuestion.getWord()); //the first is always the correct one
		_definition.setText(_nextQuestion.getDefinition());
		_example.setText(_nextQuestion.getExampleWithWordHidden());
		_wrongAnswer1.setText(_nextQuestion.getWrongAnswer1());
		_wrongAnswer2.setText(_nextQuestion.getWrongAnswer2());
		_wrongAnswer3.setText(_nextQuestion.getWrongAnswer3());
		
		_answerChoice1.setBackgroundResource(R.drawable.game_screen_button_top_left);
		_answerChoice2.setBackgroundResource(R.drawable.game_screen_button_top_right);
		_answerChoice3.setBackgroundResource(R.drawable.game_screen_button_bottom_left);
		_answerChoice4.setBackgroundResource(R.drawable.game_screen_button_bottom_right);

		updateThumbsUpThumbsDown();
		
		if(_roundNumber < SettingsData.getRound()){
			_correctAnswer.setBackgroundResource(_correctAnswerDrawable);
			_answerChoice1.setOnClickListener(null);
			_answerChoice2.setOnClickListener(null);
			_answerChoice3.setOnClickListener(null);
			_answerChoice4.setOnClickListener(null);
		}
		
		SoundController.readOutLoud(this,_nextQuestion.getDefinition());
	}

	private boolean isAnswerChoiceCorrect(Button choice){
		return choice == _correctAnswer;
	}
	
	private void showResponseCorrect(){
		SoundController.playCorrectAnswer();
		_correctAnswer.setBackgroundResource(_correctAnswerDrawable);
		SettingsData.updateLocalScore(this, SettingsData.getLocalScore() + _currentPoints);
		_currentPoints = 10;
		_screenBlocker.show();
		new pauseToShowUserResponse().execute(DELAY_TIME_ANSWER);
		SettingsData.updateWordAnswer(this, _correctAnswer.getText().toString(), SettingsData.WordStat.ANSWERED_RIGHT);
	}
	
	private void showResponseWrong(final Button button, final int wrongAnswerDrawable){
		SoundController.playWrongAnswer();
		button.setBackgroundResource(wrongAnswerDrawable);
		_screenBlocker.show();
		new pauseToShowUserResponse().execute(DELAY_TIME_ANSWER);
		SettingsData.updateWordAnswer(this, _correctAnswer.getText().toString(), SettingsData.WordStat.ANSWERED_WRONG);
	}
	
	private void endRound(boolean isVictory){
		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				GameActivity.this.finish();
			}
		};
		String displayMessage;
		String title;
		if(isVictory){
			title = "Congrats!";
			displayMessage = getResources().getString(R.string.game_correct_response);
		} else {
			title = "Wrong Answer!";
			displayMessage = getResources().getString(R.string.game_incorrect_response);;
		}
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setTitle(title)
		.setMessage(displayMessage)
		.setCancelable(false)
		.setPositiveButton("Next", listener)
		.show();
	}

	private void initAnswerChoiceAction(final Button button, final int wrongAnswerDrawable){
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SoundController.pauseReadOutLoud(GameActivity.this);
				if(isAnswerChoiceCorrect(button)){
					showResponseCorrect();
				} else {
					_wordsMissed++;
					showResponseWrong(button, wrongAnswerDrawable);
				}
			}
		});
	}
	
	private void updateThumbsUpThumbsDown() {
		String status = SettingsData.getWordImpression(_correctAnswer.getText().toString());
		if(status.equals(SettingsData.WordStat.THUMBS_UP)){
			_thumbsUp.setImageResource(R.drawable.game_screen_image_thumbs_up_pressed);
			_thumbsDown.setImageResource(R.drawable.game_screen_image_thumbs_down_unpressed);
			_currentThumbsUpImage = R.drawable.game_screen_image_thumbs_up_pressed;
			_currentThumbsDownImage = R.drawable.game_screen_image_thumbs_down_unpressed;
		} else if (status.equals(SettingsData.WordStat.THUMBS_DOWN)){
			_thumbsUp.setImageResource(R.drawable.game_screen_image_thumbs_up_unpressed);
			_thumbsDown.setImageResource(R.drawable.game_screen_image_thumbs_down_pressed);
			_currentThumbsUpImage = R.drawable.game_screen_image_thumbs_up_unpressed;
			_currentThumbsDownImage = R.drawable.game_screen_image_thumbs_down_pressed;
		} else {
			_thumbsUp.setImageResource(R.drawable.game_screen_image_thumbs_up_unpressed);
			_thumbsDown.setImageResource(R.drawable.game_screen_image_thumbs_down_unpressed);
			_currentThumbsUpImage = R.drawable.game_screen_image_thumbs_up_unpressed;
			_currentThumbsDownImage = R.drawable.game_screen_image_thumbs_down_unpressed;
		}
		_thumbsUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!SettingsData.hasShownUserFirstTimeThumbsUpDownThanks()){
					showUserFirstTimeThumbsUpDownThanks();
				}
				if(_currentThumbsUpImage == R.drawable.game_screen_image_thumbs_up_unpressed){
					_thumbsUp.setImageResource(R.drawable.game_screen_image_thumbs_up_pressed);
					_thumbsDown.setImageResource(R.drawable.game_screen_image_thumbs_down_unpressed);
					SettingsData.updateWordImpression(GameActivity.this, _correctAnswer.getText().toString(), SettingsData.WordStat.THUMBS_UP);
					_currentThumbsUpImage = R.drawable.game_screen_image_thumbs_up_pressed;
					_currentThumbsDownImage = R.drawable.game_screen_image_thumbs_down_unpressed;
				} else {
					_thumbsUp.setImageResource(R.drawable.game_screen_image_thumbs_up_unpressed);
					SettingsData.updateWordImpression(GameActivity.this, _correctAnswer.getText().toString(), SettingsData.WordStat.THUMBS_NONE);
					_currentThumbsUpImage = R.drawable.game_screen_image_thumbs_up_unpressed;
				}
			}
		});
		_thumbsDown.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!SettingsData.hasShownUserFirstTimeThumbsUpDownThanks()){
					showUserFirstTimeThumbsUpDownThanks();
				}
				if(_currentThumbsDownImage == R.drawable.game_screen_image_thumbs_down_unpressed){
					_thumbsUp.setImageResource(R.drawable.game_screen_image_thumbs_up_unpressed);
					_thumbsDown.setImageResource(R.drawable.game_screen_image_thumbs_down_pressed);
					SettingsData.updateWordImpression(GameActivity.this, _correctAnswer.getText().toString(), SettingsData.WordStat.THUMBS_DOWN);
					_currentThumbsUpImage = R.drawable.game_screen_image_thumbs_up_unpressed;
					_currentThumbsDownImage = R.drawable.game_screen_image_thumbs_down_pressed;
				} else {
					_thumbsDown.setImageResource(R.drawable.game_screen_image_thumbs_down_unpressed);
					SettingsData.updateWordImpression(GameActivity.this, _correctAnswer.getText().toString(), SettingsData.WordStat.THUMBS_NONE);
					_currentThumbsDownImage = R.drawable.game_screen_image_thumbs_down_unpressed;
				}
			}
		});
	}
	
	private void showUserFirstTimeThumbsUpDownThanks(){
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setTitle("Thanks for voting!")
		.setMessage("Thank you for providing feedback on the words. We will use your preferences to improve our products for the future!")
		.setCancelable(false)
		.setPositiveButton("Okay", null)
		.show();
		SettingsData.setShownUserFirstTimeThumbsUpDownThanks(this, true);
	}

	//introducing a slight delay so the user can get to see what answer they picked
	private class pauseToShowUserResponse extends AsyncTask<Integer, Void, Void>{
		@Override
		protected Void doInBackground(Integer... params) {
			try {
				Thread.sleep(params[0]);
			} catch (InterruptedException ie) {
				//normal...
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void dontNeedThisParam){
			if(_screenBlocker.isShowing()){
				_screenBlocker.dismiss();
			}
			
			_currentWord++;
			if(_currentWord >= _roundNumber + RoundsData.WORDS_PER_ROUND){
				endRound( _wordsMissed == 0 );
				SettingsData.updateRound(GameActivity.this, SettingsData.getRound()+1);
			} else {
				nextRound();
			}
		}
	}
}
