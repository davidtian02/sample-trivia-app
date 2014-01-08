package com.example.sample.controllers;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

//REFACTOR: this could have been done much better in all the activities if the music was used in a service. this was done in a rush. but at least it works for now.
public class SoundController {
	
	private static MediaPlayer _musicPlayer;
	private static boolean _isMusicPlayerPrepared;
	private static MediaPlayer _correctAnswerPlayer;
	private static MediaPlayer _wrongAnswerPlayer;
	private static MediaPlayer _userFinishedGame;
	private static MediaPlayer _userBoughtWords;
	private static TextToSpeech _tts;
	private static boolean _isTtsInitialized;
	
	public static void init(Context ctx){
		_isMusicPlayerPrepared = false;
		_musicPlayer = new MediaPlayer();
		_correctAnswerPlayer = new MediaPlayer();
		_wrongAnswerPlayer = new MediaPlayer();
		_userFinishedGame = new MediaPlayer();
		_userBoughtWords = new MediaPlayer();
		_isTtsInitialized = false;
		
		_tts = new TextToSpeech(ctx, new OnInitListener() {
			@Override
			public void onInit(int status) {
				_isTtsInitialized = true;
			}
		});
		
		AssetFileDescriptor descriptor;
		try {
			descriptor = ctx.getAssets().openFd(".ogg");
			_musicPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
			_musicPlayer.setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					_isMusicPlayerPrepared = true;
					_musicPlayer.start();
					_musicPlayer.pause();
				}
			});
			_musicPlayer.prepareAsync();
			_musicPlayer.setLooping(true);
			
			descriptor = ctx.getAssets().openFd("sounds/.ogg");
			_correctAnswerPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
			_correctAnswerPlayer.prepare();
			
			descriptor = ctx.getAssets().openFd("sounds/.ogg");
			_wrongAnswerPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
			_wrongAnswerPlayer.prepare();
			
			descriptor = ctx.getAssets().openFd("sounds/.ogg");
			_userFinishedGame.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
			_userFinishedGame.prepare();
			
			descriptor = ctx.getAssets().openFd("sounds/.ogg");
			_userBoughtWords.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
			_userBoughtWords.prepare();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}
	
	public static void pauseMusic(){
		if(_musicPlayer.isPlaying() && _isMusicPlayerPrepared){
			_musicPlayer.pause();
		}
		try{
			_musicPlayer.pause();
		} catch(IllegalStateException ise){
			ise.printStackTrace();
		}
	}
	
	//FIXME this mechanism doesn't work perfectly 1% of the time, with the pausing and re-playing since sometimes
	//		the music either just keeps playing after app is exited... and sometimes, you can hear
	//		the 10 ms pause when music suddenly pauses from 1 activity before the next activity picks back up
	public static void playMusic(){
		if(!SettingsData.isMusicOn()){
			return;
		}
		if(_isMusicPlayerPrepared && !_musicPlayer.isPlaying()){
			_musicPlayer.start();
		}
		try{
			_musicPlayer.start();
		} catch(IllegalStateException ise){
			ise.printStackTrace();
		}
	}
	
	public static void playCorrectAnswer(){
		if(!SettingsData.isSoundOn()){
			return;
		}
		if(_correctAnswerPlayer.isPlaying()){
			_correctAnswerPlayer.seekTo(0);
		}
		_correctAnswerPlayer.start();
	}
	
	public static void playWrongAnswer(){
		if(!SettingsData.isSoundOn()){
			return;
		}
		if(_wrongAnswerPlayer.isPlaying()){
			_wrongAnswerPlayer.seekTo(0);
		}
		_wrongAnswerPlayer.start();
	}
	
	public static void playUserFinishedGame() {
		if(!SettingsData.isSoundOn()){
			return;
		}
		if(_correctAnswerPlayer.isPlaying()){
			_correctAnswerPlayer.pause();
		}
		if(_wrongAnswerPlayer.isPlaying()){
			_wrongAnswerPlayer.pause();
		}
		_userFinishedGame.start();
	}
	
	public static void playUserBoughtWords() {
		if(!SettingsData.isSoundOn()){
			return;
		}
		if(!_userBoughtWords.isPlaying()){
			_userBoughtWords.start(); 
		}
	}
	
	public static void tearDown(){
		if(_isMusicPlayerPrepared){
			_isMusicPlayerPrepared = false;
			_musicPlayer.stop();
			_musicPlayer.release();
		}
		if(_tts!=null && _isTtsInitialized){
			_tts.stop();
			_tts.shutdown();
		}
	}

	public static void readOutLoud(Context ctx,	String phrase) {
		if(!SettingsData.isVoiceOn()){
			return;
		}
		if(_isTtsInitialized){
			_tts.speak(phrase, TextToSpeech.QUEUE_FLUSH, null);
		}
	}
	
	public static void pauseReadOutLoud(Context ctx){
		if(!SettingsData.isVoiceOn()){
			return;
		}
		if(_isTtsInitialized && _tts.isSpeaking()){
			_tts.stop();
		}
	}

	public static void toggleMusic(Context ctx) {
		if(SettingsData.isMusicOn()){
			SettingsData.toggleMusic(ctx);
			pauseMusic();
		} else {
			SettingsData.toggleMusic(ctx);
			playMusic();
		}
	}
	
	public static void toggleSound(Context ctx){
		SettingsData.toggleSound(ctx);
	}
	
	public static void toggleVoice(Context ctx){
		if(_tts.isSpeaking() && SettingsData.isVoiceOn()){
			_tts.stop();
		}
		SettingsData.toggleVoice(ctx);
	}
}
