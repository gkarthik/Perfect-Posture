package com.pp.perfectposture.web.rest;

import com.pp.perfectposture.Application;
import com.pp.perfectposture.domain.Sensor;
import com.pp.perfectposture.repository.SensorRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SensorResource REST controller.
 *
 * @see SensorResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SensorResourceTest {

    private static final String DEFAULT_DEVICE_ID = "SAMPLE_TEXT";
    private static final String UPDATED_DEVICE_ID = "UPDATED_TEXT";

    @Inject
    private SensorRepository sensorRepository;

    private MockMvc restSensorMockMvc;

    private Sensor sensor;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SensorResource sensorResource = new SensorResource();
        ReflectionTestUtils.setField(sensorResource, "sensorRepository", sensorRepository);
        this.restSensorMockMvc = MockMvcBuilders.standaloneSetup(sensorResource).build();
    }

    @Before
    public void initTest() {
        sensor = new Sensor();
        sensor.setDevice_id(DEFAULT_DEVICE_ID);
    }

    @Test
    @Transactional
    public void createSensor() throws Exception {
        int databaseSizeBeforeCreate = sensorRepository.findAll().size();

        // Create the Sensor
        restSensorMockMvc.perform(post("/api/sensors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sensor)))
                .andExpect(status().isCreated());

        // Validate the Sensor in the database
        List<Sensor> sensors = sensorRepository.findAll();
        assertThat(sensors).hasSize(databaseSizeBeforeCreate + 1);
        Sensor testSensor = sensors.get(sensors.size() - 1);
        assertThat(testSensor.getDevice_id()).isEqualTo(DEFAULT_DEVICE_ID);
    }

    @Test
    @Transactional
    public void getAllSensors() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensors
        restSensorMockMvc.perform(get("/api/sensors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(sensor.getId().intValue())))
                .andExpect(jsonPath("$.[*].device_id").value(hasItem(DEFAULT_DEVICE_ID.toString())));
    }

    @Test
    @Transactional
    public void getSensor() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get the sensor
        restSensorMockMvc.perform(get("/api/sensors/{id}", sensor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(sensor.getId().intValue()))
            .andExpect(jsonPath("$.device_id").value(DEFAULT_DEVICE_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSensor() throws Exception {
        // Get the sensor
        restSensorMockMvc.perform(get("/api/sensors/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSensor() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

		int databaseSizeBeforeUpdate = sensorRepository.findAll().size();

        // Update the sensor
        sensor.setDevice_id(UPDATED_DEVICE_ID);
        restSensorMockMvc.perform(put("/api/sensors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sensor)))
                .andExpect(status().isOk());

        // Validate the Sensor in the database
        List<Sensor> sensors = sensorRepository.findAll();
        assertThat(sensors).hasSize(databaseSizeBeforeUpdate);
        Sensor testSensor = sensors.get(sensors.size() - 1);
        assertThat(testSensor.getDevice_id()).isEqualTo(UPDATED_DEVICE_ID);
    }

    @Test
    @Transactional
    public void deleteSensor() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

		int databaseSizeBeforeDelete = sensorRepository.findAll().size();

        // Get the sensor
        restSensorMockMvc.perform(delete("/api/sensors/{id}", sensor.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Sensor> sensors = sensorRepository.findAll();
        assertThat(sensors).hasSize(databaseSizeBeforeDelete - 1);
    }
}
