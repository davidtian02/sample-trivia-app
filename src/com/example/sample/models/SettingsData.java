package com.example.sample.models;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.example.sample.controllers.DataController;

public class SettingsData {
	private static final String USER_TYPE_PRO = "USER_TYPE_PRO";
	private static final String USER_TYPE_FREE = "USER_TYPE_FREE";
	
	private static int _version;
	private static String _name;
	private static int _localScore;
	private static String _userType;
	private static int _round;
	private static int _totalRounds;
	private static boolean _hasShownUserGameFinished;
	private static boolean _hasShownUserFirstTimeThumbsUpDownThanks;
	private static boolean _hasShownUserFirstTimeDictionary;
	private static boolean _isMusicOn;
	private static boolean _isSoundOn;
	private static boolean _isVoiceOn;
	private static String _deviceModel;
	private static int _deviceOSVersion;
	
	private static JSONObject _jObjGame;
	private static JSONObject _jObjWords;
	
	
	//called if file doesn't already exist
	public static void initialize(Context ctx){
		_version = 3;
		_name = "blah";
		_localScore = 0;
		_userType = USER_TYPE_FREE;
		_round = 0;
		_totalRounds = 50;
		_hasShownUserGameFinished = false;
		_hasShownUserFirstTimeThumbsUpDownThanks = false;
		_hasShownUserFirstTimeDictionary = false;
		_isMusicOn = true;
		_isSoundOn = true;
		_isVoiceOn = true;
		_deviceModel = android.os.Build.MODEL;
		_deviceOSVersion = android.os.Build.VERSION.SDK_INT;
		_jObjGame = new JSONObject();
		_jObjWords = new JSONObject();
		saveGameToJSON(ctx);
	}
	
	//called if file already exists
	public static void initialize(JSONObject jObjGame, JSONObject jObjWords){
		_jObjGame = jObjGame;
		try {
			_version = jObjGame.getInt("version");
			_name = jObjGame.getString("name");
			_localScore = jObjGame.getInt("localScore"); 
			//TODO test this? better make sure user type is correct!!!
			_userType = jObjGame.getString("userType");
			_round = jObjGame.getInt("round");
			_totalRounds = jObjGame.getInt("totalRounds");
			_hasShownUserGameFinished = jObjGame.getBoolean("hasShownUserGameFinished");
			_hasShownUserFirstTimeThumbsUpDownThanks = jObjGame.getBoolean("hasShownUserFirstTimeThumbsUpDownThanks");
			_hasShownUserFirstTimeDictionary = jObjGame.getBoolean("hasShownUserFirstTimeDictionary");
			_isMusicOn = jObjGame.getBoolean("isMusicOn");
			_isSoundOn = jObjGame.getBoolean("isSoundOn");
			_isVoiceOn = jObjGame.getBoolean("isVoiceOn");
			_deviceModel = jObjGame.getString("deviceModel");
			_deviceOSVersion = jObjGame.getInt("deviceOSVersion");
			
			//REFACTOR
			//any corrections - seems more like a band-aid solution
			if(_version==1){
				_version = 3;
				_totalRounds = 50;
			} else  if(_version == 2){
				_version = 3;
			}
		} catch (JSONException e) {
			//file was corrupted?
			e.printStackTrace();
		} 
		_jObjWords = jObjWords;
	} 
	
	public static String getName(){
		return _name;
	}
	
	public static void updateName(Context ctx, String name){
		if(Utils.isNameValid(name)){
			_name = name;
			saveGameToJSON(ctx);
		} else {
			throw new IllegalArgumentException("Name can not contain any character of: { [ , } ] :"); 
		}
	}
	
	public static int getLocalScore(){
		return _localScore;
	}
	
	public static void updateLocalScore(Context ctx, int score){
		_localScore = score;
		saveGameToJSON(ctx);
	}
	
	public static boolean isUserTypePro(){
		return _userType.equals(USER_TYPE_PRO);
	}
	
	public static void setUserTypePro(Context ctx, boolean pro){
		_userType = pro ? USER_TYPE_PRO : USER_TYPE_FREE;
		saveGameToJSON(ctx);
	}
	
	public static int getRound(){
		return _round;
	}
	
	public static void updateRound(Context ctx, int round){
		_round = round;
		saveGameToJSON(ctx);
	}
	
	public static boolean hasShownUserGameFinished() {
		return _hasShownUserGameFinished;
	}
	
	public static void setShownUserGameFinished(Context ctx, boolean value){
		_hasShownUserGameFinished = value;
		saveGameToJSON(ctx);
	}
	
	public static boolean hasShownUserFirstTimeThumbsUpDownThanks() {
		return _hasShownUserFirstTimeThumbsUpDownThanks;
	}

	public static void setShownUserFirstTimeThumbsUpDownThanks(Context ctx,	boolean value) {
		_hasShownUserFirstTimeThumbsUpDownThanks = value;
		saveGameToJSON(ctx);
	}
	
	public static boolean hasShownUserFirstTimeDictionary() {
		return _hasShownUserFirstTimeDictionary;
	}
	
	public static void setShownUserFirstTimeDictionary(Context ctx, boolean value){
		_hasShownUserFirstTimeDictionary = value;
		saveGameToJSON(ctx);
	}
	
	public static boolean isMusicOn() {
		return _isMusicOn;
	}

	public static void toggleMusic(Context ctx) {
		//where true means it's on, and false means it's off
		_isMusicOn = !_isMusicOn;
		saveGameToJSON(ctx);
	}
	
	public static boolean isSoundOn() {
		return _isSoundOn;
	}

	public static void toggleSound(Context ctx) {
		_isSoundOn = !_isSoundOn;
		saveGameToJSON(ctx);
	}
	
	public static boolean isVoiceOn() {
		return _isVoiceOn;
	}

	public static void toggleVoice(Context ctx) {
		_isVoiceOn = !_isVoiceOn;
		saveGameToJSON(ctx);
	}
	
	//TODO if can't save properly... throw exception?
	private static void saveGameToJSON(Context ctx){
		try {
			_jObjGame.put("version", _version);
			_jObjGame.put("name", _name);
			_jObjGame.put("localScore", _localScore);
			_jObjGame.put("userType", _userType);
			_jObjGame.put("round", _round);
			_jObjGame.put("totalRounds", _totalRounds);
			_jObjGame.put("hasShownUserGameFinished", _hasShownUserGameFinished);
			_jObjGame.put("hasShownUserFirstTimeThumbsUpDownThanks", _hasShownUserFirstTimeThumbsUpDownThanks);
			_jObjGame.put("hasShownUserFirstTimeDictionary", _hasShownUserFirstTimeDictionary);
			_jObjGame.put("isMusicOn", _isMusicOn);
			_jObjGame.put("isSoundOn", _isSoundOn);
			_jObjGame.put("isVoiceOn", _isVoiceOn);
			_jObjGame.put("deviceModel", _deviceModel);
			_jObjGame.put("deviceOSVersion", _deviceOSVersion);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DataController.saveGameData(ctx, _jObjGame.toString());
	}
	
	public static void updateWordImpression(Context ctx, String term, String impression){
		try {
			((JSONObject)_jObjWords.get(term)).put("impression",impression);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DataController.saveWordData(ctx, _jObjWords.toString());
	}
	
	public static void updateWordAnswer(Context ctx, String term, String answer){
		try {
			((JSONObject)_jObjWords.get(term)).put("answered",answer);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DataController.saveWordData(ctx, _jObjWords.toString());
	}
	
	public static void createWordStat(Context ctx, String word, String answered, String impression){
		try {
			_jObjWords.put(word, new WordStat(word, answered, impression).toJSON());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DataController.saveWordData(ctx, _jObjWords.toString());
	}
	
	public static String getWordImpression(String word) {
		String result = WordStat.THUMBS_NONE;
		if(_jObjWords.has(word)){
			try {
				result = ((JSONObject)_jObjWords.get(word)).getString("impression");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static String getWordAnswered(String word) {
		String result = WordStat.ANSWERED_UNANSWERED;
		if(_jObjWords.has(word)){
			try {
				result = ((JSONObject)_jObjWords.get(word)).getString("answered");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static boolean isSettingsDataWordsInitialized(){
		return _jObjWords.length() != 0;
	}
	
	public static int getTotalRounds() {
		return _totalRounds;
	}
	
	public static class WordStat{
		public static final String THUMBS_UP = "THUMBS_UP";
		public static final String THUMBS_DOWN = "THUMBS_DOWN";
		public static final String THUMBS_NONE = "THUMBS_NONE";
		public static final String ANSWERED_RIGHT = "ANSWERED_RIGHT";
		public static final String ANSWERED_WRONG = "ANSWERED_WRONG";
		public static final String ANSWERED_UNANSWERED = "ANSWERED_UNANSWERED";
		
		private String _word;
		private String _answered;
		private String _impression;
		private JSONObject _jObjWordStat;
		public WordStat(String word, String answered, String impression){
			_word = word;
			_answered = answered;
			_impression = impression;
			_jObjWordStat = new JSONObject();
			try{
				_jObjWordStat.put("word", _word);
				_jObjWordStat.put("answered", _answered);
				_jObjWordStat.put("impression", _impression);
			} catch (JSONException je){
				je.printStackTrace();
			}
		}
		public String getWord(){
			return _word;
		}
		public String getImpression(){
			return _impression;
		}
		public void setImpression(String impression){
			_impression = impression;
			wordStatSaveMapping("impression", _impression);
		}
		public String getAnswered(){
			return _answered;
		}
		public void setAnswered(String answered){
			_answered = answered;
			wordStatSaveMapping("answered", _answered);
		}
		public JSONObject toJSON(){
			return _jObjWordStat;
		}
		private void wordStatSaveMapping(String k, String v){
			try{
				_jObjWordStat.put(k, v);
			} catch (JSONException je){
				je.printStackTrace();
			}
		}
	}
	
	//TODO this doesn't need to be used in version 1
	public static class Ranking{
		private String _highRankUser;
		private int _score;
		private JSONObject _jObjRanking;
		public Ranking(String highRankUser, int score){
			_highRankUser = highRankUser;
			_score = score;
			_jObjRanking = new JSONObject();
			try {
				_jObjRanking.put("highRankUser", _highRankUser);
				_jObjRanking.put("score", _score);
			} catch (JSONException je){
				je.printStackTrace();
			}
		}
		public String getHighRankUser(){
			return _highRankUser;
		}
		public int getHighRankUserScore(){
			return _score;
		}
		public JSONObject toJSON(){
			return _jObjRanking;
		}
	}
}

/*
 *	{
 *		version:1,
 *		name:ken ***note: you can't let them enter a comma, '{', '}', ':', '[', ']', here,
 *		localScore:88,
 *		userType:USER_TYPE_PRO | USER_TYPE_FREE,
 *		round:0,
 *		deviceModel:motorola,
 *		deviceOSVersion:10,
 *	}
 */

/*
 *	{
 *		something :
 *		{
 *			word: blah
 *			impression:THUMBS_UP | THUMBS_DOWN | THUMBS_NONE
 *			answered: ANSWERED_RIGHT | ANSWERED_WRONG | ANSWERED_UNANSWERED
 *		}
 *	}
 */