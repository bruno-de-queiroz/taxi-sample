package br.com.nineninetaxis.driver.web;

import com.vividsolutions.jts.geom.Coordinate;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.Assert;

/**
 * @Author Bruno de Queiroz<creativelikeadog@gmail.com>
 */
public class CoordinateConverter implements Converter<String, Coordinate> {

    @Override
    public Coordinate convert(String s) {
        Assert.notNull(s);
        String[] split = s.split(",");
        Assert.notEmpty(split);
        if (split.length != 2) {
            throw new IllegalArgumentException("[Assertion failed] - this array must contain 2 elements");
        }

        Double x = Double.parseDouble(split[0]);
        Double y = Double.parseDouble(split[1]);

        return new Coordinate(x, y);
    }
}
