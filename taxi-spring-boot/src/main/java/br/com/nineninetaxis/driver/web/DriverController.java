package br.com.nineninetaxis.driver.web;

import br.com.nineninetaxis.driver.domain.Driver;
import br.com.nineninetaxis.driver.domain.DriverRepository;
import br.com.nineninetaxis.driver.domain.DriverResource;
import br.com.nineninetaxis.driver.domain.DriverStatusResource;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Bruno de Queiroz<creativelikeadog@gmail.com>
 */
@RestController
@RequestMapping(value = "/drivers", produces = MediaType.APPLICATION_JSON_VALUE)
public class DriverController {

    @Autowired
    private GeometryFactory geometryFactory;

    @Autowired
    private DriverRepository repository;

    @RequestMapping(method = RequestMethod.GET)
    public List<DriverResource> list() {
        return repository.findAll()
                .stream()
                .map(DriverResource::new)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/inArea", method = RequestMethod.GET)
    public List<DriverStatusResource> byArea(@RequestParam(value = "sw", required = true) Coordinate sw,
                                             @RequestParam(value = "ne", required = true) Coordinate ne) {

        Polygon polygon = geometryFactory.createPolygon(
                new Coordinate[]{
                        new Coordinate(sw.getOrdinate(0), sw.getOrdinate(1)),
                        new Coordinate(ne.getOrdinate(0), sw.getOrdinate(1)),
                        new Coordinate(ne.getOrdinate(0), ne.getOrdinate(1)),
                        new Coordinate(sw.getOrdinate(0), ne.getOrdinate(1)),
                        new Coordinate(sw.getOrdinate(0), sw.getOrdinate(1)),
                }
        );

        return repository.findByArea(polygon)
                .stream()
                .map(DriverStatusResource::new)
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@Valid @RequestBody DriverResource resource) {
        repository.save(new Driver(resource.getName(), resource.getCarPlate()));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public DriverResource one(@PathVariable Long id) {
        return new DriverResource(repository.getOne(id));
    }

    @RequestMapping(value = "/{id}/status", method = RequestMethod.GET)
    public DriverStatusResource status(@PathVariable Long id) {
        return new DriverStatusResource(this.repository.getOne(id));
    }

    @RequestMapping(value = "{id}/status", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@PathVariable Long id, @Valid @RequestBody DriverStatusResource resource) {

        Driver driver = this.repository.getOne(id);

        Point location = geometryFactory.createPoint(
                new Coordinate(
                        resource.getLatitude(),
                        resource.getLongitude()
                )
        );

        driver.setAvailable(resource.getDriverAvailable());
        driver.setLocation(location);
        repository.save(driver);
    }

}
