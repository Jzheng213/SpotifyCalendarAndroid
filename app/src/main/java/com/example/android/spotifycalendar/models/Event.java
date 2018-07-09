package com.example.android.spotifycalendar.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Event implements Parcelable {
    private Integer id;
    private String title;
    private String description;
    private Calendar startTime;
    private Calendar endTime;

    public Event(String title, String description, Calendar startTime, Calendar endTime) {
        this.id = null;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Event(Integer id, String title, String description, Calendar startTime, Calendar endTime) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }

// methods needed to make events parcelable, so this class can be passed as a element type of an arrayList
    public Event(Parcel parcel){
        this.id = parcel.readInt();
        this.title = parcel.readString();
        this.description = parcel.readString();

        long startMilliseconds = parcel.readLong();
        String startTimeZone = parcel.readString();
        this.startTime = new GregorianCalendar(TimeZone.getTimeZone(startTimeZone));
        this.startTime.setTimeInMillis(startMilliseconds);

        long endMilliseconds = parcel.readLong();
        String endTimeZone = parcel.readString();
        this.endTime = new GregorianCalendar(TimeZone.getTimeZone(endTimeZone));
        this.endTime.setTimeInMillis(endMilliseconds);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeLong(startTime.getTimeInMillis());
        dest.writeString(startTime.getTimeZone().getID());
        dest.writeLong(endTime.getTimeInMillis());
        dest.writeString(endTime.getTimeZone().getID());
    }

    public static Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source){
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size){
            return new Event[size];
        }
    };
// parcelable end
    public int getID() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

}
