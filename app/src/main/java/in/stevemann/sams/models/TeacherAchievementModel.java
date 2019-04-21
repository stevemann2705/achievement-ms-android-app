package in.stevemann.sams.models;

import android.os.Parcel;
import android.os.Parcelable;

public class TeacherAchievementModel implements Parcelable {

    private String id;
    private String user;
    private String taType;
    private String subType;
    private boolean international;
    private String topic;
    private String published;
    private boolean sponsored;
    private boolean reviewed;
    private String date;
    private String description;
    private boolean msi;
    private String place;

    public TeacherAchievementModel(String id, String user, String taType, String subType,
                                   boolean international, String topic, String published,
                                   boolean sponsored, boolean reviewed, String date,
                                   String description, boolean msi, String place) {
        this.id = id;
        this.user = user;
        this.taType = taType;
        this.subType = subType;
        this.international = international;
        this.topic = topic;
        this.published = published;
        this.sponsored = sponsored;
        this.reviewed = reviewed;
        this.date = date;
        this.description = description;
        this.msi = msi;
        this.place = place;
    }

    public TeacherAchievementModel(Parcel in) {
        this.id = in.readString();
        this.user = in.readString();
        this.taType = in.readString();
        this.subType = in.readString();
        this.international = Boolean.parseBoolean(in.readString());
        this.topic = in.readString();
        this.published = in.readString();
        this.sponsored = Boolean.parseBoolean(in.readString());
        this.reviewed = Boolean.parseBoolean(in.readString());
        this.date = in.readString();
        this.description = in.readString();
        this.msi = Boolean.parseBoolean(in.readString());
        this.place = in.readString();
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTaType() {
        return taType;
    }

    public void setTaType(String taType) {
        this.taType = taType;
    }

    public boolean isInternational() {
        return international;
    }

    public void setInternational(boolean international) {
        this.international = international;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public boolean isSponsored() {
        return sponsored;
    }

    public void setSponsored(boolean sponsored) {
        this.sponsored = sponsored;
    }

    public boolean isReviewed() {
        return reviewed;
    }

    public void setReviewed(boolean reviewed) {
        this.reviewed = reviewed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isMsi() {
        return msi;
    }

    public void setMsi(boolean msi) {
        this.msi = msi;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public TeacherAchievementModel createFromParcel(Parcel in) {
            return new TeacherAchievementModel(in);
        }

        public TeacherAchievementModel[] newArray(int size) {
            return new TeacherAchievementModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(user);
        dest.writeString(taType);
        dest.writeString(subType);
        dest.writeString(String.valueOf(international));
        dest.writeString(topic);
        dest.writeString(published);
        dest.writeString(String.valueOf(sponsored));
        dest.writeString(String.valueOf(reviewed));
        dest.writeString(date);
        dest.writeString(description);
        dest.writeString(String.valueOf(msi));
        dest.writeString(place);
    }
}
