//package main.java.com.llm.llmReativa.Communication;

package com.llm.llmReativa.Communication;

public class AnalysisResponse {
    private String analysis;
    private String error;
    private boolean success;

    // Constructors
    public AnalysisResponse() {}

    public AnalysisResponse(String analysis, String error, boolean success) {
        this.analysis = analysis;
        this.error = error;
        //this.success = success;
        this.success = success;
    }

    // Getters and setters
    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}