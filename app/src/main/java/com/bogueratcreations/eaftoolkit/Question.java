package com.bogueratcreations.eaftoolkit;

class Question {

    private int id;
    private String program;
    private String question;
    private String reference;

    public Question() {
    }

    public Question(String program, String question, String reference) {
        this.program = program;
        this.question = question;
        this.reference = reference;
    }

    public Question(int id, String program, String question, String reference) {
        this.id = id;
        this.program = program;
        this.question = question;
        this.reference = reference;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

}
