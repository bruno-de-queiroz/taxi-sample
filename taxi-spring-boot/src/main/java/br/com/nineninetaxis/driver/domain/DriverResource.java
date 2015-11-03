package br.com.nineninetaxis.driver.domain;

import br.com.nineninetaxis.driver.web.DriverController;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.hateoas.ResourceSupport;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * @Author Bruno de Queiroz<creativelikeadog@gmail.com>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DriverResource extends ResourceSupport {

    private Long driverId;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    @Pattern(regexp = "[a-zA-Z]{3}\\-[0-9]{4}")
    private String carPlate;

    public DriverResource() {
    }

    public DriverResource(Driver driver) {
        this.driverId = driver.getId();
        this.name = driver.getName();
        this.carPlate = driver.getPlate();
        add(linkTo(methodOn(DriverController.class).one(driver.getId())).withSelfRel());
    }

    public Long getDriverId() {
        return driverId;
    }

    public String getName() {
        return name;
    }

    public String getCarPlate() {
        return carPlate;
    }
}

