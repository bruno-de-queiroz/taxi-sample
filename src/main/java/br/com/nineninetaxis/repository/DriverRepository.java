package br.com.nineninetaxis.repository;

import br.com.nineninetaxis.models.Driver;
import com.vividsolutions.jts.geom.Geometry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Bruno de Queiroz<creativelikeadog@gmail.com>
 */
@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    @Query("select d from Driver d where within(d.location, :filter) = true")
    List<Driver> findByArea(Geometry filter);

}
