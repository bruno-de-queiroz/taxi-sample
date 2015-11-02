package br.com.nineninetaxis.driver.domain;

import com.vividsolutions.jts.geom.Geometry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Bruno de Queiroz<creativelikeadog@gmail.com>
 */
@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    @Query("select d from Driver d where within(d.status.location, :filter) = true")
    List<Driver> findByArea(@Param("filter") Geometry filter);

}
