package com.bogueratcreations.eaftoolkit;

/**
 * Created by TheBogueRat on 6/10/2015.
 */
public class CSECquestions {
    public int id;
    public String ProgramID, ProgramName, Qnum, Question, Reference;
    public int Complies, UserMod;

    public CSECquestions(){}

    @Override
    public String toString() {
        return "CSECquestions [id=" + id
                + ",ProgramID=" + ProgramID
                + ",ProgramName=" + ProgramName
                + ",Qnum=" + Qnum
                + ",Question=" + Question
                + ",Reference=" + Reference
                + ",Complies=" + Complies
                + ",UserMod=" + UserMod
                + "]";
    }
}
