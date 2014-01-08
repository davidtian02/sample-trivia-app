package com.example.sample.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

//this is for controlling data
public class DataController {
	private static final String GAME_DATA_FILE = "GAMEDATA.txt";
	private static final String WORD_DATA_FILE = "WORDDATA.txt";
	
	public static void loadData(Context ctx){
		String gameData = new String();
		JSONObject jObjGame = null;
		try {
			FileInputStream fis = ctx.openFileInput(GAME_DATA_FILE);
			Scanner s = new Scanner(fis);
			while(s.hasNextLine()){
				gameData += s.nextLine();
			}
			jObjGame = new JSONObject(gameData);
		} catch (FileNotFoundException e) {
			SettingsData.initialize(ctx);
			return;
		} catch (JSONException je){

		}
		
		JSONObject jObjWords = new JSONObject();
		String wordData = new String();
		try{
			FileInputStream fis = ctx.openFileInput(WORD_DATA_FILE);
			Scanner s = new Scanner(fis);
			while(s.hasNextLine()){
				wordData += s.nextLine();
			}
			jObjWords = new JSONObject(wordData);
		} catch (FileNotFoundException e){

		} catch (JSONException je){

		}
		
		SettingsData.initialize(jObjGame, jObjWords);
	}
	
	public static void saveGameData(Context ctx, String data){
		Utils.save(ctx, GAME_DATA_FILE, data);
	}
	
	public static void saveWordData(Context ctx, String data){
		Utils.save(ctx, WORD_DATA_FILE, data);
	}

	public static void resetData(Context ctx) {
		SettingsData.updateLocalScore(ctx, 0);
		SettingsData.updateRound(ctx, 0);
		SettingsData.setShownUserFirstTimeDictionary(ctx, false);
		SettingsData.setShownUserFirstTimeThumbsUpDownThanks(ctx, false);
		SettingsData.setShownUserGameFinished(ctx, false);
		SettingsData.setShownUserNSFWWarning(ctx, false);
		String term;
		for(int i=0; i<TermsDictionary.size(); i++){
			term = TermsDictionary.getQuestionAt(i).getWord();
			SettingsData.updateWordAnswer(ctx, term, WordStat.ANSWERED_UNANSWERED);
			SettingsData.updateWordImpression(ctx, term, WordStat.THUMBS_NONE);
		}
	}
}
