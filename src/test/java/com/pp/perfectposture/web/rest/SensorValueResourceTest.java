package com.pp.perfectposture.web.rest;

import com.pp.perfectposture.Application;
import com.pp.perfectposture.domain.SensorValue;
import com.pp.perfectposture.repository.SensorValueRepository;

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
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SensorValueResource REST controller.
 *
 * @see SensorValueResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SensorValueResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


    private static final Long DEFAULT_SEN1 = 0L;
    private static final Long UPDATED_SEN1 = 1L;

    private static final Long DEFAULT_SEN2 = 0L;
    private static final Long UPDATED_SEN2 = 1L;

    private static final Long DEFAULT_SEN3 = 0L;
    private static final Long UPDATED_SEN3 = 1L;

    private static final Long DEFAULT_SEN4 = 0L;
    private static final Long UPDATED_SEN4 = 1L;

    private static final DateTime DEFAULT_TIMESTAMP = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_TIMESTAMP = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_TIMESTAMP_STR = dateTimeFormatter.print(DEFAULT_TIMESTAMP);

    @Inject
    private SensorValueRepository sensorValueRepository;

    private MockMvc restSensorValueMockMvc;

    private SensorValue sensorValue;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SensorValueResource sensorValueResource = new SensorValueResource();
        ReflectionTestUtils.setField(sensorValueResource, "sensorValueRepository", sensorValueRepository);
        this.restSensorValueMockMvc = MockMvcBuilders.standaloneSetup(sensorValueResource).build();
    }

    @Before
    public void initTest() {
        sensorValue = new SensorValue();
        sensorValue.setSen1(DEFAULT_SEN1);
        sensorValue.setSen2(DEFAULT_SEN2);
        sensorValue.setSen3(DEFAULT_SEN3);
        sensorValue.setSen4(DEFAULT_SEN4);
        sensorValue.setTimestamp(DEFAULT_TIMESTAMP);
    }

    @Test
    @Transactional
    public void createSensorValue() throws Exception {
        int databaseSizeBeforeCreate = sensorValueRepository.findAll().size();

        // Create the SensorValue
        restSensorValueMockMvc.perform(post("/api/sensorValues")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sensorValue)))
                .andExpect(status().isCreated());

        // Validate the SensorValue in the database
        List<SensorValue> sensorValues = sensorValueRepository.findAll();
        assertThat(sensorValues).hasSize(databaseSizeBeforeCreate + 1);
        SensorValue testSensorValue = sensorValues.get(sensorValues.size() - 1);
        assertThat(testSensorValue.getSen1()).isEqualTo(DEFAULT_SEN1);
        assertThat(testSensorValue.getSen2()).isEqualTo(DEFAULT_SEN2);
        assertThat(testSensorValue.getSen3()).isEqualTo(DEFAULT_SEN3);
        assertThat(testSensorValue.getSen4()).isEqualTo(DEFAULT_SEN4);
        assertThat(testSensorValue.getTimestamp().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllSensorValues() throws Exception {
        // Initialize the database
        sensorValueRepository.saveAndFlush(sensorValue);

        // Get all the sensorValues
        restSensorValueMockMvc.perform(get("/api/sensorValues"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(sensorValue.getId().intValue())))
                .andExpect(jsonPath("$.[*].sen1").value(hasItem(DEFAULT_SEN1.intValue())))
                .andExpect(jsonPath("$.[*].sen2").value(hasItem(DEFAULT_SEN2.intValue())))
                .andExpect(jsonPath("$.[*].sen3").value(hasItem(DEFAULT_SEN3.intValue())))
                .andExpect(jsonPath("$.[*].sen4").value(hasItem(DEFAULT_SEN4.intValue())))
                .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP_STR)));
    }

    @Test
    @Transactional
    public void getSensorValue() throws Exception {
        // Initialize the database
        sensorValueRepository.saveAndFlush(sensorValue);

        // Get the sensorValue
        restSensorValueMockMvc.perform(get("/api/sensorValues/{id}", sensorValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(sensorValue.getId().intValue()))
            .andExpect(jsonPath("$.sen1").value(DEFAULT_SEN1.intValue()))
            .andExpect(jsonPath("$.sen2").value(DEFAULT_SEN2.intValue()))
            .andExpect(jsonPath("$.sen3").value(DEFAULT_SEN3.intValue()))
            .andExpect(jsonPath("$.sen4").value(DEFAULT_SEN4.intValue()))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP_STR));
    }

    @Test
    @Transactional
    public void getNonExistingSensorValue() throws Exception {
        // Get the sensorValue
        restSensorValueMockMvc.perform(get("/api/sensorValues/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSensorValue() throws Exception {
        // Initialize the database
        sensorValueRepository.saveAndFlush(sensorValue);

		int databaseSizeBeforeUpdate = sensorValueRepository.findAll().size();

        // Update the sensorValue
        sensorValue.setSen1(UPDATED_SEN1);
        sensorValue.setSen2(UPDATED_SEN2);
        sensorValue.setSen3(UPDATED_SEN3);
        sensorValue.setSen4(UPDATED_SEN4);
        sensorValue.setTimestamp(UPDATED_TIMESTAMP);
        restSensorValueMockMvc.perform(put("/api/sensorValues")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sensorValue)))
                .andExpect(status().isOk());

        // Validate the SensorValue in the database
        List<SensorValue> sensorValues = sensorValueRepository.findAll();
        assertThat(sensorValues).hasSize(databaseSizeBeforeUpdate);
        SensorValue testSensorValue = sensorValues.get(sensorValues.size() - 1);
        assertThat(testSensorValue.getSen1()).isEqualTo(UPDATED_SEN1);
        assertThat(testSensorValue.getSen2()).isEqualTo(UPDATED_SEN2);
        assertThat(testSensorValue.getSen3()).isEqualTo(UPDATED_SEN3);
        assertThat(testSensorValue.getSen4()).isEqualTo(UPDATED_SEN4);
        assertThat(testSensorValue.getTimestamp().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void deleteSensorValue() throws Exception {
        // Initialize the database
        sensorValueRepository.saveAndFlush(sensorValue);

		int databaseSizeBeforeDelete = sensorValueRepository.findAll().size();

        // Get the sensorValue
        restSensorValueMockMvc.perform(delete("/api/sensorValues/{id}", sensorValue.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<SensorValue> sensorValues = sensorValueRepository.findAll();
        assertThat(sensorValues).hasSize(databaseSizeBeforeDelete - 1);
    }
}
