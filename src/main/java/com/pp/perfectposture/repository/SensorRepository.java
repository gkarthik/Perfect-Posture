package com.pp.perfectposture.repository;

import com.pp.perfectposture.domain.Sensor;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Sensor entity.
 */
public interface SensorRepository extends JpaRepository<Sensor,Long> {
	Sensor findByDeviceid(String device_id);
}
