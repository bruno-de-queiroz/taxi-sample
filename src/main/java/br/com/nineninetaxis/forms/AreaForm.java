package br.com.nineninetaxis.forms;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import javax.validation.constraints.NotNull;

/**
 * Created by Bruno de Queiroz<creativelikeadog@gmail.com>
 */
public class AreaForm {

    @NotNull
    public String sw;

    @NotNull
    public String ne;

    @NotNull(message = "validation.geometry.notnull")
    public Geometry getGeometry() {

        String[] sw = this.sw.split(",");
        String[] ne = this.ne.split(",");

        if (sw.length < 2) {
            return null;
        }

        if (ne.length < 2) {
            return null;
        }

        WKTReader fromText = new WKTReader();

        try {
            return fromText.read(
                    String.format(
                            "BOX(%s %s, %s %s)",
                            sw[0],
                            sw[1],
                            ne[0],
                            ne[1]
                    )
            );
        } catch (ParseException e) {
            return null;
        }
    }
}
