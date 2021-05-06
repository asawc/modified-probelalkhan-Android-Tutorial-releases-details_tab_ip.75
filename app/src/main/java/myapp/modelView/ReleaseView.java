package myapp.modelView;

import java.time.ZonedDateTime;

public class ReleaseView {

    private int id_release;
    private String creationDateTime;
    private String status;
    private String surname;
    private String name;

    public ReleaseView(int id, String creationDateTime, String status,String surname, String name) {
        this.id_release = id;
        this.creationDateTime = creationDateTime;
        this.status = status;
        this.surname = surname;
        this.name = name;
    }

    public int getId() {
        return id_release;
    }

    public String getCreationDateTime() {
        return creationDateTime;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() { return name; }

    public String getStatus() { return status; }
}
