package com.pp.perfectposture.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pp.perfectposture.domain.GcmCredentials;
import com.pp.perfectposture.repository.GcmCredentialsRepository;
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
 * REST controller for managing GcmCredentials.
 */
@RestController
@RequestMapping("/api")
public class GcmCredentialsResource {

    private final Logger log = LoggerFactory.getLogger(GcmCredentialsResource.class);

    @Inject
    private GcmCredentialsRepository gcmCredentialsRepository;

    /**
     * POST  /gcmCredentialss -> Create a new gcmCredentials.
     */
    @RequestMapping(value = "/gcmCredentialss",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody GcmCredentials gcmCredentials) throws URISyntaxException {
        log.debug("REST request to save GcmCredentials : {}", gcmCredentials);
        if (gcmCredentials.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new gcmCredentials cannot already have an ID").build();
        }
        gcmCredentialsRepository.save(gcmCredentials);
        return ResponseEntity.created(new URI("/api/gcmCredentialss/" + gcmCredentials.getId())).build();
    }

    /**
     * PUT  /gcmCredentialss -> Updates an existing gcmCredentials.
     */
    @RequestMapping(value = "/gcmCredentialss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody GcmCredentials gcmCredentials) throws URISyntaxException {
        log.debug("REST request to update GcmCredentials : {}", gcmCredentials);
        if (gcmCredentials.getId() == null) {
            return create(gcmCredentials);
        }
        gcmCredentialsRepository.save(gcmCredentials);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /gcmCredentialss -> get all the gcmCredentialss.
     */
    @RequestMapping(value = "/gcmCredentialss",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<GcmCredentials>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<GcmCredentials> page = gcmCredentialsRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/gcmCredentialss", offset, limit);
        return new ResponseEntity<List<GcmCredentials>>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /gcmCredentialss/:id -> get the "id" gcmCredentials.
     */
    @RequestMapping(value = "/gcmCredentialss/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GcmCredentials> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get GcmCredentials : {}", id);
        GcmCredentials gcmCredentials = gcmCredentialsRepository.findOne(id);
        if (gcmCredentials == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(gcmCredentials, HttpStatus.OK);
    }

    /**
     * DELETE  /gcmCredentialss/:id -> delete the "id" gcmCredentials.
     */
    @RequestMapping(value = "/gcmCredentialss/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete GcmCredentials : {}", id);
        gcmCredentialsRepository.delete(id);
    }
}
