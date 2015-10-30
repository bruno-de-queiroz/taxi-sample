package br.com.nineninetaxis.forms;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import javax.validation.constraints.NotNull;

/**
 * Created by Bruno de Queiroz<creativelikeadog@gmail.com>
 */
public class DriverStatusForm {

    @NotNull
    public Long driverId;

    @NotNull
    public Boolean driverAvailable;

    @NotNull
    public Double latitude;

    @NotNull
    public Double longitude;

    @NotNull
    public Point getLocation() {
        WKTReader fromText = new WKTReader();
        try {
            return (Point) fromText.read(String.format("POINT(%s %s)", this.latitude, this.longitude));
        } catch (ParseException e) {
            return null;
        }
    }
}
