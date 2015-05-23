package com.pp.perfectposture.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pp.perfectposture.domain.Sensor;
import com.pp.perfectposture.domain.SensorValue;
import com.pp.perfectposture.repository.SensorRepository;
import com.pp.perfectposture.repository.SensorValueRepository;
import com.pp.perfectposture.service.GcmMessageService;
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
import javax.validation.Valid;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * REST controller for managing SensorValue.
 */
@RestController
@RequestMapping("/api")
public class SensorValueResource {

    private final Logger log = LoggerFactory.getLogger(SensorValueResource.class);

    @Inject
    private SensorValueRepository sensorValueRepository;
    
    @Inject
    private SensorRepository sensorRepository;
    
    @Inject
    GcmMessageService gcmMessageService;

    /**
     * POST  /sensorValues -> Create a new sensorValue.
     */
    @RequestMapping(value = "/sensorValues",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody SensorValue sensorValue) throws URISyntaxException {
        log.debug("REST request to save SensorValue : {}", sensorValue);
        if (sensorValue.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new sensorValue cannot already have an ID").build();
        }
        Sensor s = sensorRepository.findByDeviceid(sensorValue.getSensor().getDevice_id());
        sensorValue.setSensor(s);
        sensorValueRepository.save(sensorValue);
        gcmMessageService.checkPosture(s);
        return ResponseEntity.created(new URI("/api/sensorValues/" + sensorValue.getId())).build();
    }

    /**
     * PUT  /sensorValues -> Updates an existing sensorValue.
     */
    @RequestMapping(value = "/sensorValues",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody SensorValue sensorValue) throws URISyntaxException {
        log.debug("REST request to update SensorValue : {}", sensorValue);
        if (sensorValue.getId() == null) {
            return create(sensorValue);
        }
        Sensor s = sensorRepository.findByDeviceid(sensorValue.getSensor().getDevice_id());
        sensorValue.setSensor(s);
        sensorValueRepository.save(sensorValue);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /sensorValues -> get all the sensorValues.
     */
    @RequestMapping(value = "/sensorValues",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SensorValue>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
    	log.debug("Get All Sensor Values");
        Page<SensorValue> page = sensorValueRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sensorValues", offset, limit);
        return new ResponseEntity<List<SensorValue>>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sensorValues/:id -> get the "id" sensorValue.
     */
    @RequestMapping(value = "/sensorValues/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SensorValue> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get SensorValue : {}", id);
        SensorValue sensorValue = sensorValueRepository.findOne(id);
        if (sensorValue == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(sensorValue, HttpStatus.OK);
    }

    /**
     * DELETE  /sensorValues/:id -> delete the "id" sensorValue.
     */
    @RequestMapping(value = "/sensorValues/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete SensorValue : {}", id);
        sensorValueRepository.delete(id);
    }
}
