package in.stevemann.sams.models;

import android.os.Parcel;
import android.os.Parcelable;

public class AcademicModel implements Parcelable {
    public static final Creator<AcademicModel> CREATOR = new Creator<AcademicModel>() {
        @Override
        public AcademicModel createFromParcel(Parcel in) {
            return new AcademicModel(in);
        }

        @Override
        public AcademicModel[] newArray(int size) {
            return new AcademicModel[size];
        }
    };
    private final String id;
    private final String rollNo;
    private final String name;
    private final String batch;
    private final String programme;
    private final String category;

    public AcademicModel(String id, String rollNo, String name, String batch, String programme, String category) {
        this.id = id;
        this.rollNo = rollNo;
        this.name = name;
        this.batch = batch;
        this.programme = programme;
        this.category = category;
    }

    private AcademicModel(Parcel in) {
        this.id = in.readString();
        this.rollNo = in.readString();
        this.name = in.readString();
        this.batch = in.readString();
        this.programme = in.readString();
        this.category = in.readString();
    }

    public String getId() {
        return id;
    }

    public String getRollNo() {
        return rollNo;
    }

    public String getName() {
        return name;
    }

    public String getBatch() {
        return batch;
    }

    public String getProgramme() {
        return programme;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(rollNo);
        dest.writeString(name);
        dest.writeString(batch);
        dest.writeString(programme);
        dest.writeString(category);
    }
}
