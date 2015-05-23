package com.pp.perfectposture.repository;

import com.pp.perfectposture.domain.GcmCredentials;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the GcmCredentials entity.
 */
public interface GcmCredentialsRepository extends JpaRepository<GcmCredentials,Long> {

}
