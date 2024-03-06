package classes;

import java.io.Serializable;
import java.util.Date;

public class Operation implements Serializable {

    private Date date;
    private String description;

    public Operation(Date date, String description) {
        this.date = date;
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }
}
