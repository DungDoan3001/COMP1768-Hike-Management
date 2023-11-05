package com.dungdoan.hike;

public class ObservationModel {
    private String ID;
    private String Name;
    private String Time;
    private String Image;
    private String Comment;
    private String Created_At;
    private String Updated_At;
    private String HikeID;

    public ObservationModel(String ID,
                            String name,
                            String time,
                            String image,
                            String comment,
                            String created_At,
                            String updated_At,
                            String hikeID) {
        this.ID = ID;
        this.Name = name;
        this.Time = time;
        this.Image = image;
        this.Comment = comment;
        this.Created_At = created_At;
        this.Updated_At = updated_At;
        this.HikeID = hikeID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getCreated_At() {
        return Created_At;
    }

    public void setCreated_At(String created_At) {
        Created_At = created_At;
    }

    public String getUpdated_At() {
        return Updated_At;
    }

    public void setUpdated_At(String updated_At) {
        Updated_At = updated_At;
    }

    public String getHikeID() {
        return HikeID;
    }

    public void setHikeID(String hikeID) {
        HikeID = hikeID;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }
}
