package br.com.nineninetaxis.driver.config;

import br.com.nineninetaxis.driver.util.GeoDBInMemoryDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

/**
 * @Author Bruno de Queiroz<creativelikeadog@gmail.com>
 */

@Configuration
@Profile("test")
public class DriverTestConfig {

    @Bean
    public DataSource dataSource() {
        return new GeoDBInMemoryDataSource();
    }

}
