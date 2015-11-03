package br.com.nineninetaxis.driver.domain;

import br.com.nineninetaxis.driver.web.DriverController;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.vividsolutions.jts.geom.Point;
import org.springframework.hateoas.ResourceSupport;

import javax.validation.constraints.NotNull;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * @Author Bruno de Queiroz<creativelikeadog@gmail.com>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DriverStatusResource extends ResourceSupport {

    private Long driverId;

    @NotNull
    private Boolean driverAvailable;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    public DriverStatusResource() {
    }

    public DriverStatusResource(Driver driver) {
        Point location = driver.getLocation();
        this.driverId = driver.getId();
        this.driverAvailable = driver.isAvailable();
        this.latitude = location == null ? null : location.getX();
        this.longitude = location == null ? null : location.getY();
        add(linkTo(methodOn(DriverController.class).one(this.driverId)).withSelfRel());
    }

    public Long getDriverId() {
        return driverId;
    }

    public Boolean getDriverAvailable() {
        return driverAvailable;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

}
