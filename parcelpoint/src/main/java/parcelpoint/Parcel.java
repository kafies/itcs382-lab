package parcelpoint;

public class Parcel {
    public enum Size { SMALL, MEDIUM, LARGE }
    private String code;
    private String studentId;
    private Size size;          // 1 = small, 2 = medium, 3 = large
    private String status;

    public String getStatus() { return status; }
    public void markStored() { status = "STORED"; }
    public void markPickedUp() { status = "PICKED_UP"; }

    public Parcel(String code, String studentId, Size size) {
        this.code = code;
        this.studentId = studentId;
        this.size = size;
        this.status = "NEW";
    }

    public String getCode() {
        return code;
    }

    public String getStudentId() {
        return studentId;
    }

    public Size getSize() {
        return size;
    }
}
