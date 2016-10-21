package com.bogueratcreations.eaftoolkit.Inspections;

import com.bogueratcreations.eaftoolkit.Inspections.Program;
import com.bogueratcreations.eaftoolkit.Inspections.Question;

import java.util.ArrayList;

/**
 * Created by jodyroth on 10/23/15.
 * Interface for InspectDBHandler.class
 */
interface InspectListener {

    void setTableName(String tableName);

    void addQuestion(Question question);

    ArrayList<Question> getAllQuestion();

    int getQuestionCount();

    // Retrieves questions for a specific program;
    ArrayList<Question> getQuestions(String program);

    // Retrieves unique program names
    ArrayList<Program> getPrograms();
}