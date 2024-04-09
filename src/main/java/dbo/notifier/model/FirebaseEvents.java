package dbo.notifier.model;

public class FirebaseEvents {
    public String uuid;
    public  FirebaseEvent fe;

    @Override
    public String toString() {
        return "FirebaseEvents{" +
                "uuid='" + uuid + '\'' +
                ", fe=" + fe +
                '}';
    }
}
