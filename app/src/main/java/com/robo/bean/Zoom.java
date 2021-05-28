package com.robo.bean;

public class Zoom {
    public String meetingId;
    public String meetingPassword;

    public Zoom() {
    }

    public Zoom(String meetingId, String meetingPassword) {
        this.meetingId = meetingId;
        this.meetingPassword = meetingPassword;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getMeetingPassword() {
        return meetingPassword;
    }

    public void setMeetingPassword(String meetingPassword) {
        this.meetingPassword = meetingPassword;
    }
}
