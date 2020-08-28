package com.redhat.phmprocess.ui;

public class TaskSummaryObject {

    private String taskId;
    private String taskContainerId;
    private String summaryOfChanges;
    private String processInstanceId;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    private String owner;



    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskContainerId() {
        return taskContainerId;
    }

    public void setTaskContainerId(String taskContainerId) {
        this.taskContainerId = taskContainerId;
    }

    public String getSummaryOfChanges() {
        return summaryOfChanges;
    }

    public void setSummaryOfChanges(String summaryOfChanges) {
        this.summaryOfChanges = summaryOfChanges;
    }


    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
}
