package net.jakebrennan.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.jakebrennan.domain.ArmlProfile;
import net.jakebrennan.repository.ArmlProfileRepository;
import net.jakebrennan.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link net.jakebrennan.domain.ArmlProfile}.
 */
@RestController
@RequestMapping("/api/arml-profiles")
@Transactional
public class ArmlProfileResource {

    private final Logger log = LoggerFactory.getLogger(ArmlProfileResource.class);

    private static final String ENTITY_NAME = "armlProfile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArmlProfileRepository armlProfileRepository;

    public ArmlProfileResource(ArmlProfileRepository armlProfileRepository) {
        this.armlProfileRepository = armlProfileRepository;
    }

    /**
     * {@code POST  /arml-profiles} : Create a new armlProfile.
     *
     * @param armlProfile the armlProfile to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new armlProfile, or with status {@code 400 (Bad Request)} if the armlProfile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ArmlProfile> createArmlProfile(@Valid @RequestBody ArmlProfile armlProfile) throws URISyntaxException {
        log.debug("REST request to save ArmlProfile : {}", armlProfile);
        if (armlProfile.getId() != null) {
            throw new BadRequestAlertException("A new armlProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        armlProfile = armlProfileRepository.save(armlProfile);
        return ResponseEntity.created(new URI("/api/arml-profiles/" + armlProfile.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, armlProfile.getId().toString()))
            .body(armlProfile);
    }

    /**
     * {@code PUT  /arml-profiles/:id} : Updates an existing armlProfile.
     *
     * @param id the id of the armlProfile to save.
     * @param armlProfile the armlProfile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated armlProfile,
     * or with status {@code 400 (Bad Request)} if the armlProfile is not valid,
     * or with status {@code 500 (Internal Server Error)} if the armlProfile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ArmlProfile> updateArmlProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ArmlProfile armlProfile
    ) throws URISyntaxException {
        log.debug("REST request to update ArmlProfile : {}, {}", id, armlProfile);
        if (armlProfile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, armlProfile.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!armlProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        armlProfile = armlProfileRepository.save(armlProfile);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, armlProfile.getId().toString()))
            .body(armlProfile);
    }

    /**
     * {@code PATCH  /arml-profiles/:id} : Partial updates given fields of an existing armlProfile, field will ignore if it is null
     *
     * @param id the id of the armlProfile to save.
     * @param armlProfile the armlProfile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated armlProfile,
     * or with status {@code 400 (Bad Request)} if the armlProfile is not valid,
     * or with status {@code 404 (Not Found)} if the armlProfile is not found,
     * or with status {@code 500 (Internal Server Error)} if the armlProfile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ArmlProfile> partialUpdateArmlProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ArmlProfile armlProfile
    ) throws URISyntaxException {
        log.debug("REST request to partial update ArmlProfile partially : {}, {}", id, armlProfile);
        if (armlProfile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, armlProfile.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!armlProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ArmlProfile> result = armlProfileRepository
            .findById(armlProfile.getId())
            .map(existingArmlProfile -> {
                if (armlProfile.getWinRate() != null) {
                    existingArmlProfile.setWinRate(armlProfile.getWinRate());
                }
                if (armlProfile.getFeedRate() != null) {
                    existingArmlProfile.setFeedRate(armlProfile.getFeedRate());
                }
                if (armlProfile.getCallRate() != null) {
                    existingArmlProfile.setCallRate(armlProfile.getCallRate());
                }
                if (armlProfile.getRiiRate() != null) {
                    existingArmlProfile.setRiiRate(armlProfile.getRiiRate());
                }
                if (armlProfile.getFeedEV() != null) {
                    existingArmlProfile.setFeedEV(armlProfile.getFeedEV());
                }

                return existingArmlProfile;
            })
            .map(armlProfileRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, armlProfile.getId().toString())
        );
    }

    /**
     * {@code GET  /arml-profiles} : get all the armlProfiles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of armlProfiles in body.
     */
    @GetMapping("")
    public List<ArmlProfile> getAllArmlProfiles() {
        log.debug("REST request to get all ArmlProfiles");
        return armlProfileRepository.findAll();
    }

    /**
     * {@code GET  /arml-profiles/:id} : get the "id" armlProfile.
     *
     * @param id the id of the armlProfile to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the armlProfile, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArmlProfile> getArmlProfile(@PathVariable("id") Long id) {
        log.debug("REST request to get ArmlProfile : {}", id);
        Optional<ArmlProfile> armlProfile = armlProfileRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(armlProfile);
    }

    /**
     * {@code DELETE  /arml-profiles/:id} : delete the "id" armlProfile.
     *
     * @param id the id of the armlProfile to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArmlProfile(@PathVariable("id") Long id) {
        log.debug("REST request to delete ArmlProfile : {}", id);
        armlProfileRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
