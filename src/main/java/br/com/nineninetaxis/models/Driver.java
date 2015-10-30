package br.com.nineninetaxis.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vividsolutions.jts.geom.Point;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * Created by Bruno de Queiroz<creativelikeadog@gmail.com>
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "plate"})})
public class Driver {

    @JsonProperty("driverId")
    @Id
    @GeneratedValue
    private Long id;

    @JsonProperty("driverAvailable")
    @Column
    private boolean available;

    @Column(nullable = false)
    private String name;

    @JsonProperty("carPlate")
    @Column(nullable = false)
    private String plate;

    @JsonIgnore
    @Column
    @Type(type = "org.hibernate.spatial.GeometryType")
    private Point location;

    @JsonProperty("latitude")
    @Transient
    public Double getLatitude() {
        return this.location == null ? null : this.location.getX();
    }

    @JsonProperty("longitude")
    @Transient
    public Double getLongitude() {
        return this.location == null ? null : this.location.getY();
    }

    public Driver() {
    }

    public Driver(String name, String plate) {
        this.name = name;
        this.plate = plate;
    }

    public Driver(Long id, Boolean available, Point location) {
        this.id = id;
        this.available = available;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }
}
