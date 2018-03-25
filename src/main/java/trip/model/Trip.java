package trip.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import trip.util.DestinationExists;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Date;

@Data
@EqualsAndHashCode(of = "id")
@Entity
public class Trip implements Serializable {

    @Id
    @GeneratedValue
    @ApiModelProperty(notes = "Auto-generated ID of the Trip")
    private long id;
    @ApiModelProperty(notes = "Destination of the Trip")
    @DestinationExists
    private String destination;
    @ApiModelProperty(notes = "Starting date of the Trip")
    private Date startDate;
    @ApiModelProperty(notes = "End date of the Trip")
    private Date endDate;
    @ApiModelProperty(notes = "User comment/notes on the Trip", allowEmptyValue = true)
    private String comment = "";

    public Trip() {
    }

    public Trip(String destination, Date startDate, Date endDate, String comment) {
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.comment = comment;
    }

}