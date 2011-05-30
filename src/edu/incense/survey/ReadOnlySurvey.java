/**
 * 
 */
package edu.incense.survey;

import java.util.List;

/**
 * @author mxpxgx
 *
 */
public interface ReadOnlySurvey {
    public String getTitle();
    public Question getQuestion(int index);
    public List<Question> getQuestions();
    public int getId();
    public int getSize();
}
