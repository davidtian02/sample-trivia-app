package com.example.sample.models;

import org.json.JSONException;
import org.json.JSONObject;

//a term is the basis of the trivia -> a trivia consists of a bunch of terms to be guessed
public class Term implements Comparable<Term>{
	private String _word;
	private int _number;
	private String _definition;
	private String _exampleWithWordHidden; //could be many, clumped into 1 container
	private String _exampleWithWordRevealed;
	private String _selfString;
	private String _answerChoice1;
	private String _answerChoice2;
	private String _answerChoice3;
	private String _answerChoice4;
	private int _correctAnswerChoice;
	
	private String _wrongAnswer1;
	private String _wrongAnswer2;
	private String _wrongAnswer3;
	private String _correctAnswer;
	
	/*
		{
			"Number" : 1
			"Definition" : "some definition",
			"ExampleRevealed" : "blah blah blah",
			"ExampleHidden" : "blah ----- blah",
			"AnswerChoice1" : "A",
			"AnswerChoice2" : "B",
			"AnswerChoice3" : "C",
			"AnswerChoice4" : "D",
			"CorrectAnswerNumber" : 0
		} 
	*/
	
	public Term(String word, JSONObject jOb) {
		try{
			_word = word;
			_number = jOb.getInt("Number");
			_definition = jOb.getString("Definition");
			_exampleWithWordRevealed = jOb.getString("ExampleRevealed");
			_exampleWithWordHidden = jOb.getString("ExampleHidden");
			_answerChoice1 = jOb.getString("AnswerChoice1");
			_answerChoice2 = jOb.getString("AnswerChoice2");
			_answerChoice3 = jOb.getString("AnswerChoice3");
			_answerChoice4 = jOb.getString("AnswerChoice4");
			_correctAnswerChoice = jOb.getInt("CorrectAnswerNumber");
		} catch (JSONException je){
			je.printStackTrace();
		}
		
		switch(_correctAnswerChoice){
		case 0: _correctAnswer = _answerChoice1;
				_wrongAnswer1 = _answerChoice2;
				_wrongAnswer2 = _answerChoice3;
				_wrongAnswer3 = _answerChoice4;
				break;
		case 1: _correctAnswer = _answerChoice2;
				_wrongAnswer1 = _answerChoice1;
				_wrongAnswer2 = _answerChoice3;
				_wrongAnswer3 = _answerChoice4;
				break;
		case 2: _correctAnswer = _answerChoice3;
				_wrongAnswer1 = _answerChoice1;
				_wrongAnswer2 = _answerChoice2;
				_wrongAnswer3 = _answerChoice4;
				break;
		case 3: _correctAnswer = _answerChoice4;
				_wrongAnswer1 = _answerChoice1;
				_wrongAnswer2 = _answerChoice2;
				_wrongAnswer3 = _answerChoice3;
				break;
		}
	}
	
	public String getDefinition(){
		return _definition;
	}
	public String getWord(){
		return _word;
	}
	public String getExampleWithWordHidden(){
		return _exampleWithWordHidden;
	}

	public String getExampleWithWordRevealed(){
		return _exampleWithWordRevealed;
	}
	
	public int getCorrectAnswerNumber() {
		return _correctAnswerChoice;
	}

	public String getWrongAnswer1() {
		return _wrongAnswer1;
	}
	
	public String getWrongAnswer2() {
		return _wrongAnswer2;
	}
	
	public String getWrongAnswer3() {
		return _wrongAnswer3;
	}
	
	public String getCorrectAnswer(){
		return _correctAnswer;
	}
	
	public String toString(){
		if(_selfString == null){
			_selfString = new String();
			_selfString = _word + "\n" + _definition + "\n" + _exampleWithWordRevealed + "\n";
		} 
		return _selfString;
	}

	@Override
	public int compareTo(Term another) {
		if(_number < another._number){
			return -1;
		} else if(_number > another._number){
			return 1;
		} else {
			return 0;
		}
	}
}
