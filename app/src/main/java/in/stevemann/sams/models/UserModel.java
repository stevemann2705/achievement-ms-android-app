package in.stevemann.sams.models;

public class UserModel {

    private static String userId;
    private static String token;
    private static String email;
    private static String firstName;
    private static String lastName;

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        UserModel.userId = userId;
    }

    public static String getFirstName() {
        return firstName;
    }

    public static void setFirstName(String firstName) {
        UserModel.firstName = firstName;
    }

    public static String getLastName() {
        return lastName;
    }

    public static void setLastName(String lastName) {
        UserModel.lastName = lastName;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {

        UserModel.email = email;
    }


    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        UserModel.token = token;
    }
}
