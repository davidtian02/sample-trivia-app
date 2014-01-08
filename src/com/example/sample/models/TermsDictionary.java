package com.example.sample.models;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

//TODO don't forget to decrypt and encrypt the dictionary raw resource!!!

public class TermsDictionary {
	private static List<String> _wordCandidates;
	private static LinkedHashMap<String,Term> _terms;
	private static List<String> _wordListWithHeaders;
	private static String[] _wordListWithHeadersArray;
	private static int[] _headerPositionsList = new int[26];
	
	public static void init(Context ctx){
		if(_terms==null){
			initializeDictionaryFromJSON(ctx);
		}
	}
	
	private static void initializeDictionaryFromJSON(Context ctx){
		parseWordsFromJSON(ctx);
		initializeHeaders(ctx);
		initializeWordStats(ctx);
	}
	
	private static void parseWordsFromJSON(Context ctx){
		JSONObject jObjTerms = null;
		_terms = new LinkedHashMap<String,Term>();
		
		InputStream is = ctx.getResources().openRawResource(R.raw.trivia);
		Scanner scan = new Scanner(is);
		StringBuilder strStorage = new StringBuilder();
		try {
			while(scan.hasNextLine()){
				strStorage.append(scan.nextLine());
			}
			jObjTerms = new JSONObject(strStorage.toString());
		} catch (JSONException je){
			//shit... error in parsing
			je.printStackTrace();
		}
		
		Iterator<?> it = jObjTerms.keys();
		Term t;
		JSONObject jOb;
		String s;
		List<Term> preSorted = new ArrayList<Term>();
		try{
			while(it.hasNext()){
				s = it.next().toString();
				jOb = jObjTerms.getJSONObject( s );
				t = new Term(s,jOb);
				preSorted.add(t);
			}
		} catch (JSONException je){
			je.printStackTrace();
		}
		Collections.sort(preSorted);
		for(Term t1 : preSorted){
			_terms.put(t1.getWord(), t1);
		}
	}
	
	private static void initializeHeaders(Context ctx){	
		_wordCandidates = new ArrayList<String>();
		_wordListWithHeaders = new ArrayList<String>();
		Arrays.fill(_headerPositionsList, -1);
		for(String s : _terms.keySet()){
			_wordListWithHeaders.add(s);
			_wordCandidates.add(s);
		}
		Collections.sort(_wordListWithHeaders);

		char currentHeader = 'a' - 1; // logic depends on that 'a' > 0
		int currentPositionHeader = 0;
		char tempChar;
		String tempString;
		for(int i=0; i<_wordListWithHeaders.size(); i++){
			tempString = _wordListWithHeaders.get(i);
			tempChar = Character.isUpperCase(tempString.charAt(0)) ? Character.toLowerCase(tempString.charAt(0)) : tempString.charAt(0);
			while(tempChar > currentHeader){
				_headerPositionsList[currentPositionHeader++] = i;
				currentHeader++;
			}
		}
		for(int i=0; i<_headerPositionsList.length; i++){
			if(_headerPositionsList[i] >= 0){
				_headerPositionsList[i] += i;
			}
		}
		for(int i=0; i<_headerPositionsList.length; i++){
			if(_headerPositionsList[i] < 0){
				_wordListWithHeaders.add(new String("" + ((char)Character.toUpperCase('a' + i))));
				_headerPositionsList[i] = _terms.size() + i;
			} else {
				_wordListWithHeaders.add(_headerPositionsList[i], new String("" + ((char)Character.toUpperCase('a' + i))));
			}
		}
		
		_wordListWithHeadersArray = new String[_wordListWithHeaders.size()];
		_wordListWithHeadersArray = _wordListWithHeaders.toArray(_wordListWithHeadersArray);
	}
	
	public static Term getQuestionAt(int round){
		return _terms.get(_wordCandidates.get(round)) ;
	}
	
	public static boolean isPositionHeader(Context ctx, int position) {
		if(_terms==null){
			init(ctx);
		}
		for(int i=0; i<_headerPositionsList.length; i++){
			if(_headerPositionsList[i] > position){
				break;
			}
			if(_headerPositionsList[i] == position){
				return true;
			}
		}
		return false;
	}

	public static String getWordAtListPosition(Context ctx, int position) {
		if(_terms==null){
			init(ctx);
		}
		return _wordListWithHeaders.get(position);
	}

	public static String getDefinitionAtListPosition(Context ctx, int position) {
		if(_terms==null){
			init(ctx);
		}
		return _terms.get(_wordListWithHeaders.get(position)).getDefinition();
	}
	
	public static String getExampleAtListPosition(Context ctx, int position) {
		if(_terms==null){
			init(ctx);
		}
		return _terms.get(_wordListWithHeaders.get(position)).getExampleWithWordRevealed();
	}
	
	public static String[] getWordListWithHeaders(Context ctx){
		if(_terms==null){
			init(ctx);
		}
		return _wordListWithHeadersArray;
	}
	
	private static void initializeWordStats(Context ctx){
		if(!SettingsData.isSettingsDataWordsInitialized()){
			for(String k : _terms.keySet()){
				SettingsData.createWordStat(ctx, k, SettingsData.WordStat.ANSWERED_UNANSWERED, SettingsData.WordStat.THUMBS_NONE);
			}
		}
	}
	
	public static int size(){
		return _terms.size();
	}
}
