package com.bogueratcreations.eaftoolkit.Inspections;

public class Program {

    private int id;
    private String program;

    public Program() {
    }

    public Program(String program, String question, String reference) {
        this.program = program;
    }

    public Program(int id, String program, String question, String reference) {
        this.id = id;
        this.program = program;
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

    public void setProgram(String program){
        this.program = program;
    }
}
