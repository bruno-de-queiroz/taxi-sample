package br.com.nineninetaxis.driver.domain;

import com.vividsolutions.jts.geom.Point;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * Created by Bruno de Queiroz<creativelikeadog@gmail.com>
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = Driver.CONSTRAINT_NAME, columnNames = {"plate"})})
public class Driver {

    public static final String CONSTRAINT_NAME = "unique_plate";

    @Embeddable
    public static class DriverStatus {

        @Column
        private boolean available;

        @Column(columnDefinition = "Geometry", nullable = true)
        @Type(type = "org.hibernate.spatial.GeometryType")
        private Point location;

        public DriverStatus() {
        }

        public DriverStatus(boolean available, Point location) {
            this.available = available;
            this.location = location;
        }

        public boolean isAvailable() {
            return available;
        }

        public void setAvailable(boolean available) {
            this.available = available;
        }

        public Point getLocation() {
            return location;
        }

        public void setLocation(Point location) {
            this.location = location;
        }
    }

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String plate;

    @Embedded
    private DriverStatus status = new DriverStatus();

    public Driver() {
    }

    public Driver(String name, String plate) {
        this.name = name;
        this.plate = plate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Basic
    public boolean isAvailable() {
        return this.status.isAvailable();
    }

    public void setAvailable(boolean available) {
        this.status.setAvailable(available);
    }

    @Basic
    public Point getLocation() {
        return this.status.getLocation();
    }

    public void setLocation(Point location) {
        this.status.setLocation(location);
    }
}
