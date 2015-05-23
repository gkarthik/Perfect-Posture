package com.pp.perfectposture.repository;

import com.pp.perfectposture.domain.SensorValue;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SensorValue entity.
 */
public interface SensorValueRepository extends JpaRepository<SensorValue,Long> {

}
