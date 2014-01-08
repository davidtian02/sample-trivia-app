package com.example.sample.views;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sample.models.SettingsData;
import com.example.sample.models.TermsDictionary;

public class WordListAdapter extends ArrayAdapter<String> {
	private Context _context;
	private String[] _values;
	public static final String WORD_LOCKED = "-----";
	
	public WordListAdapter(Context context, int textViewResourceId,	String[] values) {
		super(context, textViewResourceId, values);
		_context = context;
		_values = values;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		LayoutInflater li;
		if(convertView == null){
			li = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = li.inflate(R.layout.wordlist_row, null);
		}
		
		TextView tv = (TextView) convertView.findViewById(R.id.wordlist_row_textview_content);
		
		if(TermsDictionary.isPositionHeader(_context,position)){
			convertView.setBackgroundResource(R.drawable.wordlist_row_screen_header);
			tv.setTypeface(Typeface.DEFAULT_BOLD);
			tv.setText(_values[position]);
		} else {
			convertView.setBackgroundResource(R.drawable.wordlist_row_screen_regular);
			tv.setTypeface(Typeface.DEFAULT);
			String word = SettingsData.getWordAnswered(_values[position]);
			if(word.equals(SettingsData.WordStat.ANSWERED_RIGHT)){
				tv.setText(_values[position]);
			} else if (word.equals(SettingsData.WordStat.ANSWERED_WRONG)){
				tv.setText(_values[position]);
			} else {
				tv.setText(WORD_LOCKED);
			}
		}
		return convertView;
	}

	@Override
	public int getCount(){
		return _values.length;
	}	
}
