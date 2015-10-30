package br.com.nineninetaxis.forms;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Created by Bruno de Queiroz<creativelikeadog@gmail.com>
 */
public class DriverForm {

    @NotNull
    @NotEmpty
    public String name;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "[a-zA-Z]{3}\\-[0-9]{4}", message = "{validation.plate.invalid}")
    public String carPlate;
}
