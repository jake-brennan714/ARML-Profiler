package net.jakebrennan.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.jakebrennan.domain.ArmlGame;
import net.jakebrennan.repository.ArmlGameRepository;
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
 * REST controller for managing {@link net.jakebrennan.domain.ArmlGame}.
 */
@RestController
@RequestMapping("/api/arml-games")
@Transactional
public class ArmlGameResource {

    private final Logger log = LoggerFactory.getLogger(ArmlGameResource.class);

    private static final String ENTITY_NAME = "armlGame";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArmlGameRepository armlGameRepository;

    public ArmlGameResource(ArmlGameRepository armlGameRepository) {
        this.armlGameRepository = armlGameRepository;
    }

    /**
     * {@code POST  /arml-games} : Create a new armlGame.
     *
     * @param armlGame the armlGame to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new armlGame, or with status {@code 400 (Bad Request)} if the armlGame has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ArmlGame> createArmlGame(@Valid @RequestBody ArmlGame armlGame) throws URISyntaxException {
        log.debug("REST request to save ArmlGame : {}", armlGame);
        if (armlGame.getId() != null) {
            throw new BadRequestAlertException("A new armlGame cannot already have an ID", ENTITY_NAME, "idexists");
        }
        armlGame = armlGameRepository.save(armlGame);
        return ResponseEntity.created(new URI("/api/arml-games/" + armlGame.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, armlGame.getId().toString()))
            .body(armlGame);
    }

    /**
     * {@code PUT  /arml-games/:id} : Updates an existing armlGame.
     *
     * @param id the id of the armlGame to save.
     * @param armlGame the armlGame to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated armlGame,
     * or with status {@code 400 (Bad Request)} if the armlGame is not valid,
     * or with status {@code 500 (Internal Server Error)} if the armlGame couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ArmlGame> updateArmlGame(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ArmlGame armlGame
    ) throws URISyntaxException {
        log.debug("REST request to update ArmlGame : {}, {}", id, armlGame);
        if (armlGame.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, armlGame.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!armlGameRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        armlGame = armlGameRepository.save(armlGame);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, armlGame.getId().toString()))
            .body(armlGame);
    }

    /**
     * {@code PATCH  /arml-games/:id} : Partial updates given fields of an existing armlGame, field will ignore if it is null
     *
     * @param id the id of the armlGame to save.
     * @param armlGame the armlGame to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated armlGame,
     * or with status {@code 400 (Bad Request)} if the armlGame is not valid,
     * or with status {@code 404 (Not Found)} if the armlGame is not found,
     * or with status {@code 500 (Internal Server Error)} if the armlGame couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ArmlGame> partialUpdateArmlGame(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ArmlGame armlGame
    ) throws URISyntaxException {
        log.debug("REST request to partial update ArmlGame partially : {}, {}", id, armlGame);
        if (armlGame.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, armlGame.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!armlGameRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ArmlGame> result = armlGameRepository
            .findById(armlGame.getId())
            .map(existingArmlGame -> {
                if (armlGame.getGameID() != null) {
                    existingArmlGame.setGameID(armlGame.getGameID());
                }

                return existingArmlGame;
            })
            .map(armlGameRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, armlGame.getId().toString())
        );
    }

    /**
     * {@code GET  /arml-games} : get all the armlGames.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of armlGames in body.
     */
    @GetMapping("")
    public List<ArmlGame> getAllArmlGames() {
        log.debug("REST request to get all ArmlGames");
        return armlGameRepository.findAll();
    }

    /**
     * {@code GET  /arml-games/:id} : get the "id" armlGame.
     *
     * @param id the id of the armlGame to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the armlGame, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArmlGame> getArmlGame(@PathVariable("id") Long id) {
        log.debug("REST request to get ArmlGame : {}", id);
        Optional<ArmlGame> armlGame = armlGameRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(armlGame);
    }

    /**
     * {@code DELETE  /arml-games/:id} : delete the "id" armlGame.
     *
     * @param id the id of the armlGame to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArmlGame(@PathVariable("id") Long id) {
        log.debug("REST request to delete ArmlGame : {}", id);
        armlGameRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
