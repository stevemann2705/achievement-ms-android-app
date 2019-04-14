package in.stevemann.sams.models;

public class TeacherAchievement {
    private static String user;
    private static String taType;
    private static boolean international;
    private static String topic;
    private static String published;
    private static boolean sponsored;
    private static boolean reviewed;
    private static String date;
    private static String description;
    private static boolean msi;
    private static String place;

    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        TeacherAchievement.user = user;
    }

    public static String getTaType() {
        return taType;
    }

    public static void setTaType(String taType) {
        TeacherAchievement.taType = taType;
    }

    public static boolean isInternational() {
        return international;
    }

    public static void setInternational(boolean international) {
        TeacherAchievement.international = international;
    }

    public static String getTopic() {
        return topic;
    }

    public static void setTopic(String topic) {
        TeacherAchievement.topic = topic;
    }

    public static String getPublished() {
        return published;
    }

    public static void setPublished(String published) {
        TeacherAchievement.published = published;
    }

    public static boolean isSponsored() {
        return sponsored;
    }

    public static void setSponsored(boolean sponsored) {
        TeacherAchievement.sponsored = sponsored;
    }

    public static boolean isReviewed() {
        return reviewed;
    }

    public static void setReviewed(boolean reviewed) {
        TeacherAchievement.reviewed = reviewed;
    }

    public static String getDate() {
        return date;
    }

    public static void setDate(String date) {
        TeacherAchievement.date = date;
    }

    public static String getDescription() {
        return description;
    }

    public static void setDescription(String description) {
        TeacherAchievement.description = description;
    }

    public static boolean isMsi() {
        return msi;
    }

    public static void setMsi(boolean msi) {
        TeacherAchievement.msi = msi;
    }

    public static String getPlace() {
        return place;
    }

    public static void setPlace(String place) {
        TeacherAchievement.place = place;
    }
}
