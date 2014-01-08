package com.example.sample.extras;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import com.example.sample.controllers.SoundController;
import com.example.sample.models.SettingsData;

public class Utils {
	private static final char[] JSONDelimiters = {',', '{', '}', ':', '[', ']'};
	public static void restartScore(Context ctx) {
		SettingsData.updateLocalScore(ctx, 0);
	}
	
	public static void save(Context ctx, String file, String data){
		FileOutputStream fos;
		try {
			fos = ctx.openFileOutput(file, Context.MODE_PRIVATE);
			fos.write(data.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isNameValid(String name) {
		for(int i=0; i<JSONDelimiters.length; i++){
			if(name.contains("" + JSONDelimiters[i])){
				return false;
			}
		}
		return true;
	}
	
	public static int getScreenWidth(Context ctx){
		DisplayMetrics displaymetrics = new DisplayMetrics();
		((Activity)ctx).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		return displaymetrics.widthPixels;
	}
	public static int getScreenHeight(Context ctx){
		DisplayMetrics displaymetrics = new DisplayMetrics();
		((Activity)ctx).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		return displaymetrics.heightPixels;
	}
	
    public static boolean onCreateOptionsMenu(Menu menu){
        String titleMusic = SettingsData.isMusicOn() ? "Turn Music Off" : "Turn Music On";
        String titleSound = SettingsData.isSoundOn() ? "Turn Sound Off" : "Turn Sound On";
        String titleVoice = SettingsData.isVoiceOn() ? "Turn Voice Off" : "Turn Voice On";
        menu.add(1, 1, Menu.FIRST + 1, titleMusic);
        menu.add(1, 2, Menu.FIRST + 2, titleSound);
        menu.add(1, 3, Menu.FIRST + 3, titleVoice);
    	return true;
    }
    
    public static boolean onOptionsItemSelected(MenuItem item, Context ctx){
    	switch(item.getItemId()){
        case 1: SoundController.toggleMusic(ctx);
        		item.setTitle( SettingsData.isMusicOn() ? "Turn Music Off" : "Turn Music On");
        		return true;
        case 2: SoundController.toggleSound(ctx);
        		item.setTitle(SettingsData.isSoundOn() ? "Turn Sound Off" : "Turn Sound On");
        		return true;
        case 3: SoundController.toggleVoice(ctx);
        		item.setTitle(SettingsData.isVoiceOn() ? "Turn Voice Off" : "Turn Voice On");
        		return true;
        }
    	return false;
    }
}
