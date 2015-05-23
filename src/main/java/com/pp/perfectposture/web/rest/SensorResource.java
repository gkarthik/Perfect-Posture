package com.pp.perfectposture.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pp.perfectposture.domain.Sensor;
import com.pp.perfectposture.repository.SensorRepository;
import com.pp.perfectposture.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * REST controller for managing Sensor.
 */
@RestController
@RequestMapping("/api")
public class SensorResource {

    private final Logger log = LoggerFactory.getLogger(SensorResource.class);

    @Inject
    private SensorRepository sensorRepository;

    /**
     * POST  /sensors -> Create a new sensor.
     */
    @RequestMapping(value = "/sensors",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Sensor sensor) throws URISyntaxException {
        log.debug("REST request to save Sensor : {}", sensor);
        if (sensor.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new sensor cannot already have an ID").build();
        }
        sensorRepository.save(sensor);
        return ResponseEntity.created(new URI("/api/sensors/" + sensor.getId())).build();
    }

    /**
     * PUT  /sensors -> Updates an existing sensor.
     */
    @RequestMapping(value = "/sensors",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Sensor sensor) throws URISyntaxException {
        log.debug("REST request to update Sensor : {}", sensor);
        if (sensor.getId() == null) {
            return create(sensor);
        }
        sensorRepository.save(sensor);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /sensors -> get all the sensors.
     */
    @RequestMapping(value = "/sensors",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Sensor>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Sensor> page = sensorRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sensors", offset, limit);
        return new ResponseEntity<List<Sensor>>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sensors/:id -> get the "id" sensor.
     */
    @RequestMapping(value = "/sensors/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sensor> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Sensor : {}", id);
        Sensor sensor = sensorRepository.findOne(id);
        if (sensor == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(sensor, HttpStatus.OK);
    }

    /**
     * DELETE  /sensors/:id -> delete the "id" sensor.
     */
    @RequestMapping(value = "/sensors/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Sensor : {}", id);
        sensorRepository.delete(id);
    }
}
