package net.jakebrennan.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.jakebrennan.domain.ArmlGameScore;
import net.jakebrennan.repository.ArmlGameScoreRepository;
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
 * REST controller for managing {@link net.jakebrennan.domain.ArmlGameScore}.
 */
@RestController
@RequestMapping("/api/arml-game-scores")
@Transactional
public class ArmlGameScoreResource {

    private final Logger log = LoggerFactory.getLogger(ArmlGameScoreResource.class);

    private static final String ENTITY_NAME = "armlGameScore";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArmlGameScoreRepository armlGameScoreRepository;

    public ArmlGameScoreResource(ArmlGameScoreRepository armlGameScoreRepository) {
        this.armlGameScoreRepository = armlGameScoreRepository;
    }

    /**
     * {@code POST  /arml-game-scores} : Create a new armlGameScore.
     *
     * @param armlGameScore the armlGameScore to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new armlGameScore, or with status {@code 400 (Bad Request)} if the armlGameScore has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ArmlGameScore> createArmlGameScore(@Valid @RequestBody ArmlGameScore armlGameScore) throws URISyntaxException {
        log.debug("REST request to save ArmlGameScore : {}", armlGameScore);
        if (armlGameScore.getId() != null) {
            throw new BadRequestAlertException("A new armlGameScore cannot already have an ID", ENTITY_NAME, "idexists");
        }
        armlGameScore = armlGameScoreRepository.save(armlGameScore);
        return ResponseEntity.created(new URI("/api/arml-game-scores/" + armlGameScore.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, armlGameScore.getId().toString()))
            .body(armlGameScore);
    }

    /**
     * {@code PUT  /arml-game-scores/:id} : Updates an existing armlGameScore.
     *
     * @param id the id of the armlGameScore to save.
     * @param armlGameScore the armlGameScore to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated armlGameScore,
     * or with status {@code 400 (Bad Request)} if the armlGameScore is not valid,
     * or with status {@code 500 (Internal Server Error)} if the armlGameScore couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ArmlGameScore> updateArmlGameScore(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ArmlGameScore armlGameScore
    ) throws URISyntaxException {
        log.debug("REST request to update ArmlGameScore : {}, {}", id, armlGameScore);
        if (armlGameScore.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, armlGameScore.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!armlGameScoreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        armlGameScore = armlGameScoreRepository.save(armlGameScore);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, armlGameScore.getId().toString()))
            .body(armlGameScore);
    }

    /**
     * {@code PATCH  /arml-game-scores/:id} : Partial updates given fields of an existing armlGameScore, field will ignore if it is null
     *
     * @param id the id of the armlGameScore to save.
     * @param armlGameScore the armlGameScore to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated armlGameScore,
     * or with status {@code 400 (Bad Request)} if the armlGameScore is not valid,
     * or with status {@code 404 (Not Found)} if the armlGameScore is not found,
     * or with status {@code 500 (Internal Server Error)} if the armlGameScore couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ArmlGameScore> partialUpdateArmlGameScore(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ArmlGameScore armlGameScore
    ) throws URISyntaxException {
        log.debug("REST request to partial update ArmlGameScore partially : {}, {}", id, armlGameScore);
        if (armlGameScore.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, armlGameScore.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!armlGameScoreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ArmlGameScore> result = armlGameScoreRepository
            .findById(armlGameScore.getId())
            .map(existingArmlGameScore -> {
                if (armlGameScore.getScore() != null) {
                    existingArmlGameScore.setScore(armlGameScore.getScore());
                }

                return existingArmlGameScore;
            })
            .map(armlGameScoreRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, armlGameScore.getId().toString())
        );
    }

    /**
     * {@code GET  /arml-game-scores} : get all the armlGameScores.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of armlGameScores in body.
     */
    @GetMapping("")
    public List<ArmlGameScore> getAllArmlGameScores() {
        log.debug("REST request to get all ArmlGameScores");
        return armlGameScoreRepository.findAll();
    }

    /**
     * {@code GET  /arml-game-scores/:id} : get the "id" armlGameScore.
     *
     * @param id the id of the armlGameScore to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the armlGameScore, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArmlGameScore> getArmlGameScore(@PathVariable("id") Long id) {
        log.debug("REST request to get ArmlGameScore : {}", id);
        Optional<ArmlGameScore> armlGameScore = armlGameScoreRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(armlGameScore);
    }

    /**
     * {@code DELETE  /arml-game-scores/:id} : delete the "id" armlGameScore.
     *
     * @param id the id of the armlGameScore to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArmlGameScore(@PathVariable("id") Long id) {
        log.debug("REST request to delete ArmlGameScore : {}", id);
        armlGameScoreRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
