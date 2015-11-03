package br.com.nineninetaxis.driver.web;

import com.vividsolutions.jts.geom.Coordinate;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * @Author Bruno de Queiroz<creativelikeadog@gmail.com>
 */
public class CoordinateConverterTest {

    private CoordinateConverter converter;

    @Before
    public void createInstance() {
        this.converter = new CoordinateConverter();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertWithOneStringNotNumber() {
        converter.convert("asdaba");
    }

    @Test(expected = NumberFormatException.class)
    public void testConvertWithStringNotNumber() {
        converter.convert("asdaba,acasa");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertWithOneBoolean() {
        converter.convert("true");
    }

    @Test(expected = NumberFormatException.class)
    public void testConvertWithBoolean() {
        converter.convert("true,false");
    }

    @Test
    public void testConvert() {
        double latitude = -23.4565;
        double longitude = -46.3424;

        Coordinate convert = converter.convert(String.format("%s,%s", latitude, longitude));
        assertNotNull(convert);
        assertEquals(convert.getOrdinate(0), latitude);
        assertEquals(convert.getOrdinate(1), longitude);
    }

}