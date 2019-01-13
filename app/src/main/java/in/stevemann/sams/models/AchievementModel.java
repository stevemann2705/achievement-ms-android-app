package in.stevemann.sams.models;

import android.os.Parcel;
import android.os.Parcelable;

public class AchievementModel implements Parcelable {
    private final String id;
    private final String eventName;
    private final String rollNo;
    private final int semester;
    private final String sessionFrom;
    private final String name;
    private final boolean participated;
    private final String description;
    private final String shift;
    private final String sessionTo;
    private final String section;
    private final String department;
    private final String date;
    private final String rating;
    private final String approvedBy;
    private final String category;
    private final String title;
    private final String imageUrl;
    private final boolean approved;
    private final String venue;

    public AchievementModel(String id, String eventName, String rollNo, int semester, String sessionFrom, String name, boolean participated, String description, String shift, String sessionTo, String section, String department, String date, String rating, String approvedBy, String category, String title, String imageUrl, boolean approved, String venue) {
        this.id = id;
        this.eventName = eventName;
        this.rollNo = rollNo;
        this.semester = semester;
        this.sessionFrom = sessionFrom;
        this.name = name;
        this.participated = participated;
        this.description = description;
        this.shift = shift;
        this.sessionTo = sessionTo;
        this.section = section;
        this.department = department;
        this.date = date;
        this.rating = rating;
        this.approvedBy = approvedBy;
        this.category = category;
        this.title = title;
        this.imageUrl = imageUrl;
        this.approved = approved;
        this.venue = venue;
    }

    private AchievementModel(Parcel in) {
        this.id = in.readString();
        this.eventName =in.readString();
        this.rollNo = in.readString();
        this.semester = Integer.parseInt(in.readString());
        this.sessionFrom = in.readString();
        this.name = in.readString();
        this.participated = Boolean.parseBoolean(in.readString());
        this.description = in.readString();
        this.shift = in.readString();
        this.sessionTo = in.readString();
        this.section = in.readString();
        this.department = in.readString();
        this.date = in.readString();
        this.rating = in.readString();
        this.approvedBy = in.readString();
        this.category = in.readString();
        this.title = in.readString();
        this.imageUrl = in.readString();
        this.approved = Boolean.parseBoolean(in.readString());
        this.venue = in.readString();
    }

    public String getId() {
        return id;
    }

    public String getEventName() {
        return eventName;
    }

    public String getRollNo() {
        return rollNo;
    }

    public int getSemester() {
        return semester;
    }

    public String getSessionFrom() {
        return sessionFrom;
    }

    public String getName() {
        return name;
    }

    public boolean isParticipated() {
        return participated;
    }

    public String getDescription() {
        return description;
    }

    public String getShift() {
        return shift;
    }

    public String getSessionTo() {
        return sessionTo;
    }

    public String getSection() {
        return section;
    }

    public String getDepartment() {
        return department;
    }

    public String getDate() {
        return date;
    }

    public String getRating() {
        return rating;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isApproved() {
        return approved;
    }

    public String getVenue() {
        return venue;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public AchievementModel createFromParcel(Parcel in ) {
            return new AchievementModel( in );
        }

        public AchievementModel[] newArray(int size) {
            return new AchievementModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(eventName);
        dest.writeString(rollNo);
        dest.writeString(String.valueOf(semester));
        dest.writeString(sessionFrom);
        dest.writeString(name);
        dest.writeString(String.valueOf(participated));
        dest.writeString(description);
        dest.writeString(shift);
        dest.writeString(sessionTo);
        dest.writeString(section);
        dest.writeString(department);
        dest.writeString(date);
        dest.writeString(rating);
        dest.writeString(approvedBy);
        dest.writeString(category);
        dest.writeString(title);
        dest.writeString(imageUrl);
        dest.writeString(String.valueOf(approved));
        dest.writeString(venue);
    }
}
