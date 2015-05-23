package com.pp.perfectposture.web.rest;

import com.pp.perfectposture.Application;
import com.pp.perfectposture.domain.GcmCredentials;
import com.pp.perfectposture.repository.GcmCredentialsRepository;

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
 * Test class for the GcmCredentialsResource REST controller.
 *
 * @see GcmCredentialsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class GcmCredentialsResourceTest {

    private static final String DEFAULT_REG_ID = "SAMPLE_TEXT";
    private static final String UPDATED_REG_ID = "UPDATED_TEXT";

    @Inject
    private GcmCredentialsRepository gcmCredentialsRepository;

    private MockMvc restGcmCredentialsMockMvc;

    private GcmCredentials gcmCredentials;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GcmCredentialsResource gcmCredentialsResource = new GcmCredentialsResource();
        ReflectionTestUtils.setField(gcmCredentialsResource, "gcmCredentialsRepository", gcmCredentialsRepository);
        this.restGcmCredentialsMockMvc = MockMvcBuilders.standaloneSetup(gcmCredentialsResource).build();
    }

    @Before
    public void initTest() {
        gcmCredentials = new GcmCredentials();
        gcmCredentials.setRegId(DEFAULT_REG_ID);
    }

    @Test
    @Transactional
    public void createGcmCredentials() throws Exception {
        int databaseSizeBeforeCreate = gcmCredentialsRepository.findAll().size();

        // Create the GcmCredentials
        restGcmCredentialsMockMvc.perform(post("/api/gcmCredentialss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gcmCredentials)))
                .andExpect(status().isCreated());

        // Validate the GcmCredentials in the database
        List<GcmCredentials> gcmCredentialss = gcmCredentialsRepository.findAll();
        assertThat(gcmCredentialss).hasSize(databaseSizeBeforeCreate + 1);
        GcmCredentials testGcmCredentials = gcmCredentialss.get(gcmCredentialss.size() - 1);
        assertThat(testGcmCredentials.getRegId()).isEqualTo(DEFAULT_REG_ID);
    }

    @Test
    @Transactional
    public void getAllGcmCredentialss() throws Exception {
        // Initialize the database
        gcmCredentialsRepository.saveAndFlush(gcmCredentials);

        // Get all the gcmCredentialss
        restGcmCredentialsMockMvc.perform(get("/api/gcmCredentialss"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(gcmCredentials.getId().intValue())))
                .andExpect(jsonPath("$.[*].regId").value(hasItem(DEFAULT_REG_ID.toString())));
    }

    @Test
    @Transactional
    public void getGcmCredentials() throws Exception {
        // Initialize the database
        gcmCredentialsRepository.saveAndFlush(gcmCredentials);

        // Get the gcmCredentials
        restGcmCredentialsMockMvc.perform(get("/api/gcmCredentialss/{id}", gcmCredentials.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(gcmCredentials.getId().intValue()))
            .andExpect(jsonPath("$.regId").value(DEFAULT_REG_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGcmCredentials() throws Exception {
        // Get the gcmCredentials
        restGcmCredentialsMockMvc.perform(get("/api/gcmCredentialss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGcmCredentials() throws Exception {
        // Initialize the database
        gcmCredentialsRepository.saveAndFlush(gcmCredentials);

		int databaseSizeBeforeUpdate = gcmCredentialsRepository.findAll().size();

        // Update the gcmCredentials
        gcmCredentials.setRegId(UPDATED_REG_ID);
        restGcmCredentialsMockMvc.perform(put("/api/gcmCredentialss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gcmCredentials)))
                .andExpect(status().isOk());

        // Validate the GcmCredentials in the database
        List<GcmCredentials> gcmCredentialss = gcmCredentialsRepository.findAll();
        assertThat(gcmCredentialss).hasSize(databaseSizeBeforeUpdate);
        GcmCredentials testGcmCredentials = gcmCredentialss.get(gcmCredentialss.size() - 1);
        assertThat(testGcmCredentials.getRegId()).isEqualTo(UPDATED_REG_ID);
    }

    @Test
    @Transactional
    public void deleteGcmCredentials() throws Exception {
        // Initialize the database
        gcmCredentialsRepository.saveAndFlush(gcmCredentials);

		int databaseSizeBeforeDelete = gcmCredentialsRepository.findAll().size();

        // Get the gcmCredentials
        restGcmCredentialsMockMvc.perform(delete("/api/gcmCredentialss/{id}", gcmCredentials.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<GcmCredentials> gcmCredentialss = gcmCredentialsRepository.findAll();
        assertThat(gcmCredentialss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
