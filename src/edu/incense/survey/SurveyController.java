package edu.incense.survey;

import java.util.List;
import java.util.Stack;

public class SurveyController {
    public final static int MAX_OPTIONS = 5;
    private Survey survey;
    private Answer[] answers;
    private int index;
    private Stack<Integer> surveyPath;

    public SurveyController(Survey survey) {
        this.survey = survey;
        answers = new Answer[getSize()];
        surveyPath = new Stack<Integer>();
        index = 0;
    }

    public SurveyController(Survey survey, Answer[] answers) {
        this(survey);
        this.answers = answers;
    }

    public void saveAnswersTo(String fileName) {
        JsonSurvey jsonSurvey = new JsonSurvey();
        jsonSurvey.toJson(fileName, answers);
    }

    public boolean isEmpty() {
        if (survey == null) {
            return true;
        }
        if (getQuestion() == null) {
            return true;
        }
        return false;
    }

    /***
     * Initializes answer if necessary
     * 
     * @param index
     *            - index of the answer to initialize
     */
    private void initAnswer(int index) {
        if (answers[index] == null) {
            answers[index] = new Answer();
        }
    }

    /***
     * Returns current answer
     * 
     * @return Answer - current answer
     */
    private Answer getAnswer() {
        initAnswer(index);
        return answers[index];
    }

    public String getStringAnswer() {
        return getAnswer().getAnswer();
    }

    public void setAnswer(int answer) {
        initAnswer(index);
        getAnswer().setAnswer(answer);
    }

    public void setAnswer(String answer) {
        initAnswer(index);
        getAnswer().setAnswer(answer);
    }

    public void selectOption(int option) {
        initAnswer(index);
        getAnswer().selectOption(option, getQuestion().getType());
    }

    public boolean deselectOption(int option) {
        initAnswer(index);
        return deselectOption(option);
    }

    public List<Integer> getSelectedOptions() {
        return getAnswer().getSelectedOptions();
    }

    public int getSelectedOption() {
        return getAnswer().getSelectedOption();
    }

    public boolean isAnswered() {
        return getAnswer().isAnswered();
    }

    // Get current question
    private Question getQuestion() {
        return survey.getQuestion(index);
    }

    public String getStringQuestion() {
        return getQuestion().getQuestion();
    }

    public boolean isFirstQuestion() {
        if (index == 0)
            return true;
        return false;
    }

    public boolean isQuestionSkippable() {
        return getQuestion().isSkippable();
    }

    public boolean isLastQuestion() {
        if (index == (getSize() - 1))
            return true;
        return false;
    }

    public boolean isSurveyComplete() {
        if ((index + 1) < getSize()) {
            return false;
        }
        for (int i = 0; i < answers.length; i++) {
            if (answers[i] == null)
                return false;
            if (!answers[i].isSkipped() && !answers[i].isAnswered())
                return false;
        }
        return true;
    }

    public String[] getOptions() {
        return getQuestion().getOptions();
    }

    public int getOptionsSize() {
        if (getOptions().length > MAX_OPTIONS) {
            return MAX_OPTIONS;
        } else {
            return getOptions().length;
        }
    }

    public QuestionType getType() {
        return getQuestion().getType();
    }

    // Actions
    public boolean next() {
        surveyPath.push(index);
        int[] nextQuestions = getQuestion().getNextQuestions();
        if (getType() == QuestionType.RADIOBUTTONS && nextQuestions != null
                && isAnswered()) {
            index = nextQuestions[getAnswer().getSelectedOption()];
        } else {
            index = index < getSize() ? index + 1 : index;
        }
        if (surveyPath.peek() == index)
            return false;
        return true;
    }

    public boolean back() {
        if (surveyPath.empty())
            return false;
        else
            index = surveyPath.pop();
        return true;
    }

    public boolean skip() {
        if (isQuestionSkippable()) {
            getAnswer().setSkipped(true);
            return next();
        }
        return false;
    }

    private int getSize() {
        return survey.getSize();
    }
}
