package com.example.android.spotifycalendar;
import org.w3c.dom.Text;

import java.util.Date;

public class Event {
    private String Title;
    private String Description;
    private Date StartDate;
    private Date EndDate;

    public Event(String title, String description, Date startDate, Date endDate) {
        Title = title;
        Description = description;
        StartDate = startDate;
        EndDate = endDate;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Date getStartDate() {
        return StartDate;
    }

    public void setStartDate(Date startDate) {
        StartDate = startDate;
    }

    public Date getEndDate() {
        return EndDate;
    }

    public void setEndDate(Date endDate) {
        EndDate = endDate;
    }
}
