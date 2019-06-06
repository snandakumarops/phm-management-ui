package com.redhat.makerchecker.ui;

public class TaskSummaryObject {

    private String taskId;
    private String taskContainerId;
    private String summaryOfChanges;
    private String processInstanceId;

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
