package edu.incense.survey;


import java.util.List;

import edu.incense.R;
import edu.incense.InCenseApplication;
import edu.incense.results.FileType;
import edu.incense.results.QueueFileTask;
import edu.incense.results.ResultFile;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

/*** SurveyActivity is the GUI for a given Survey
 *   To use it, first send a Survey object to the InCenseApplication with the setSurvey() method
 *   Then start the intent of this Activity. It automatically will pull the data from InCenseApplication.
 * @author Moises Perez (mxpxgx@gmail.com)
 *
 */

public class SurveyActivity extends Activity{
	
	private SurveyController surveyController;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Initialize activity.
		surveyController = InCenseApplication.getInstance().getSurveyController();
		if(surveyController == null){
			finish(); // Finish/quit this activity
		}
		
		if(surveyController.isEmpty()){
			finish(); // Finish/quit this activity
		}
		
		switch(surveyController.getType()){
		case CHECKBOXES:
			initCheckBoxes();
			break;
		case OPENTEXT:
			initOpenText();
			break;
		case OPENNUMERIC:
			initOpenNumeric();
			break;
		case RADIOBUTTONS:
			initRadioButtons();
			break;
		case NULL:
			finish(); // Finish/quit this activity
			break;
		}
		
		TextView questionTextView = (TextView) findViewById(R.id.question);
		questionTextView.setText(surveyController.getStringQuestion());
		questionTextView.setTextSize(28);
		questionTextView.setTextColor(Color.WHITE);
		//questionTextView.setTextSize(10);
		
		Button backButton = (Button) findViewById(R.id.back);
		Button skipButton = (Button) findViewById(R.id.skip);
		Button nextButton = (Button) findViewById(R.id.next);
		
		if(!surveyController.isFirstQuestion()){
			//Add click listener for BACK button
			backButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					back();
				}
			});
		} else {
			backButton.setEnabled(false);
		}
		
		if(surveyController.isQuestionSkippable()){
			//Add click listener for SKIP button
			skipButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					skip();
				}
			});
		} else {
			skipButton.setEnabled(false);
		}
		
		if(surveyController.isLastQuestion())
			nextButton.setText("Finish");
		//Add click listener for NEXT button
		nextButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				next();
			}
		});
	}
	
	private void startNewActivity(boolean available){
		if(available){
			InCenseApplication.getInstance().setSurveyController(surveyController);
			Intent intent = new Intent(InCenseApplication.getInstance(), SurveyActivity.class);
			
			startActivity(intent);
		}
		this.finish();
	}
	
	private void initOpenText(){
		setContentView(R.layout.survey_open);
		EditText openAnswer = (EditText) findViewById(R.id.open_answer);
		initEditText(openAnswer);
		if(surveyController.isAnswered()){
			openAnswer.setText(surveyController.getStringAnswer());
		}
	}
	
	private void initOpenNumeric(){
		setContentView(R.layout.survey_opennumeric);
		EditText openAnswer = (EditText) findViewById(R.id.open_answer);
		initEditText(openAnswer);
		if(surveyController.isAnswered()){
			openAnswer.setText(surveyController.getStringAnswer());
		}
	}
	
	private void initEditText(EditText et){
		et.setOnKeyListener(new OnKeyListener() {
		    public boolean onKey(View v, int keyCode, KeyEvent event) {
		        // If the event is a key-down event on the "enter" button
		        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
		          // Perform action on key press
		          EditText answer = (EditText)v;
		          surveyController.setAnswer(answer.getText().toString());
		          return true;
		        }
		        EditText answer = (EditText)v;
		        surveyController.setAnswer(answer.getText().toString());
			    //return true;
		        return false;
		    }
		});
		
		
	}
	
	private void initCheckBoxes(){
		setContentView(R.layout.survey_checkboxes);
		CompoundButton[] buttons = new CompoundButton[surveyController.getOptionsSize()];
		buttons[0] = (CheckBox) findViewById(R.id.b_answer1);
		buttons[1] = (CheckBox) findViewById(R.id.b_answer2);
		buttons[2] = (CheckBox) findViewById(R.id.b_answer3);
		buttons[3] = (CheckBox) findViewById(R.id.b_answer4);
		buttons[4] = (CheckBox) findViewById(R.id.b_answer5);
		initButtons(buttons);
		if(surveyController.isAnswered()){
			List<Integer> options = surveyController.getSelectedOptions();
			for(Integer o: options){
				buttons[o].setChecked(true);
			}
		}
	}
	
	private void initRadioButtons(){
		setContentView(R.layout.survey_radiobuttons);
		CompoundButton[] buttons = new CompoundButton[surveyController.getOptionsSize()];
		buttons[0] = (RadioButton) findViewById(R.id.b_answer1);
		buttons[1] = (RadioButton) findViewById(R.id.b_answer2);
		buttons[2] = (RadioButton) findViewById(R.id.b_answer3);
		buttons[3] = (RadioButton) findViewById(R.id.b_answer4);
		buttons[4] = (RadioButton) findViewById(R.id.b_answer5);
		initButtons(buttons);
		if(surveyController.isAnswered()){
			Log.i(getClass().getName(), "Trying to select: "+surveyController.getSelectedOption());
			buttons[surveyController.getSelectedOption()].setChecked(true);
		}
	}
	
	private void initButtons(Button[] buttons){
		String[] options = surveyController.getOptions();
		for(int i=0; (i<options.length) && (i<surveyController.getOptionsSize()); i++){
			
			buttons[i].setText(options[i]);
			buttons[i].setTextSize(22);
			buttons[i].setTextColor(Color.LTGRAY);
			
			buttons[i].setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					
					switch(view.getId()){
					case R.id.b_answer1:
						surveyController.selectOption(0);
						break;
					case R.id.b_answer2:
						surveyController.selectOption(1);
						break;
					case R.id.b_answer3:
						surveyController.selectOption(2);
						break;
					case R.id.b_answer4:
						surveyController.selectOption(3);
						break;
					case R.id.b_answer5:
						surveyController.selectOption(4);
						break;
					}
					if(view.getClass() == RadioButton.class) next();
				}
			});
		}
		
		//Remove the rest of the CompoundButtons
		for(int i=surveyController.getOptionsSize(); i<buttons.length; i++){
			//buttons[i].setClickable(false);
			Log.i(getClass().getName(), "Removing button: "+i);
			buttons[i].setVisibility(View.GONE); // It can be View.INVISIBLE too
		}
	}
	
	/*** Actions ***/
	
	private void back(){
		boolean available = surveyController.back();
		startNewActivity(available);
	}
	
	private void skip(){
		boolean available = surveyController.skip();
		startNewActivity(available);
	}
	
	private void next(){
		QuestionType type = surveyController.getType();
		if( (type == QuestionType.OPENTEXT || type == QuestionType.OPENNUMERIC) 
				&& !surveyController.isAnswered()){
			Toast.makeText(getBaseContext(),"Please provide an answer.",Toast.LENGTH_LONG).show();
			return;
		} else if( !surveyController.isAnswered()){
			Toast.makeText(getBaseContext(),"Please select an answer.",Toast.LENGTH_LONG).show();
			return;
		} else if(!surveyController.isLastQuestion()){
			boolean available = surveyController.next();
			startNewActivity(available);
		} else if(surveyController.isSurveyComplete()){
			Toast.makeText(getBaseContext(),"Survey completed!",Toast.LENGTH_LONG).show();
			//InCenseApplication.getInstance().setSurveyController(surveyController);
			ResultFile resultFile = ResultFile.createInstance(this, FileType.SURVEY);
			surveyController.saveAnswersTo(resultFile.getFileName());
			new QueueFileTask(this).execute(resultFile);
			finish();
		} else {
			Toast.makeText(getBaseContext(),"Survey not completed, please answered all the questions.",Toast.LENGTH_LONG).show();
		}
	}
}
