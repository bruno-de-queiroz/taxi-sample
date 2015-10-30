package br.com.nineninetaxis.controllers;

import br.com.nineninetaxis.forms.AreaForm;
import br.com.nineninetaxis.forms.DriverForm;
import br.com.nineninetaxis.forms.DriverStatusForm;
import br.com.nineninetaxis.models.Driver;
import br.com.nineninetaxis.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by Bruno de Queiroz<creativelikeadog@gmail.com>
 */
@RestController
@RequestMapping(value = "/drivers", produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Driver.class)
public class DriverController {

    @Autowired
    private DriverRepository repository;

    @Autowired
    private EntityLinks entityLinks;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public HttpEntity<Resource<Driver>> one(@PathVariable Long id) {

        Resource<Driver> resource = new Resource<>(repository.findOne(id));
        resource.add(entityLinks.linkToCollectionResource(Driver.class));

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity<Resources<Driver>> list() {
        return this.resources(repository.findAll());
    }

    @RequestMapping(value = "/inArea", method = RequestMethod.GET)
    public HttpEntity<Resources<Driver>> findByArea(@RequestBody @Valid AreaForm area) {
        return this.resources(repository.findByArea(area.getGeometry()));
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@RequestBody @Valid DriverForm form) {
        repository.save(new Driver(form.name, form.carPlate));
    }

    @RequestMapping(value = "/status", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody @Valid DriverStatusForm form) {
        repository.save(new Driver(form.driverId, form.driverAvailable, form.getLocation()));
    }

    private ResponseEntity<Resources<Driver>> resources(Iterable<Driver> drivers) {

        Resources<Driver> resources = new Resources<>(drivers);
        resources.add(entityLinks.linkToCollectionResource(Driver.class));

        return new ResponseEntity<>(resources, HttpStatus.OK);
    }
}
