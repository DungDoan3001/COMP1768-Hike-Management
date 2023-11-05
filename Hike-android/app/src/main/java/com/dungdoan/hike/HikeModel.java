package com.dungdoan.hike;

public class HikeModel {
    private String ID;
    private String Name;
    private String Location;
    private String Date;
    private String Length;
    private String Parking;
    private String Level;
    private String Description;
    private String Image;
    private String Created_At;
    private String Updated_At;

    public HikeModel(String ID,
                     String name,
                     String location,
                     String date,
                     String length,
                     String parking,
                     String level,
                     String description,
                     String image,
                     String created_At,
                     String updated_At) {
        this.ID = ID;
        this.Name = name;
        this.Location = location;
        this.Date = date;
        this.Length = length;
        this.Parking = parking;
        this.Level = level;
        this.Description = description;
        this.Image = image;
        this.Created_At = created_At;
        this.Updated_At = updated_At;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public String getLocation() {
        return Location;
    }

    public String getDate() {
        return Date;
    }

    public String getLength() {
        return Length;
    }

    public String getParking() {
        return Parking;
    }

    public String getLevel() {
        return Level;
    }

    public String getDescription() {
        return Description;
    }

    public String getImage() {
        return Image;
    }

    public String getCreated_At() {
        return Created_At;
    }

    public String getUpdated_At() {
        return Updated_At;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setLength(String length) {
        Length = length;
    }

    public void setParking(String parking) {
        Parking = parking;
    }

    public void setLevel(String level) {
        Level = level;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setImage(String image) {
        Image = image;
    }

    public void setCreated_At(String created_At) {
        Created_At = created_At;
    }

    public void setUpdated_At(String updated_At) {
        Updated_At = updated_At;
    }
}
