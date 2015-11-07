package com.bogueratcreations.eaftoolkit;

import java.util.ArrayList;

/**
 * Created by jodyroth on 10/23/15.
 * Interface for InspectDBHandler.class
 */
public interface InspectListener {

    void setTableName(String tableName);

    void addQuestion(Question question);

    ArrayList<Question> getAllQuestion();

    int getQuestionCount();

    // Retrieves questions for a specific program;
    ArrayList<Question> getQuestions(String program);

    // Retrieves unique program names
    ArrayList<Program> getPrograms();
}