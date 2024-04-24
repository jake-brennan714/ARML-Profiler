package net.jakebrennan.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import net.jakebrennan.domain.ArmlPlayer;
import net.jakebrennan.repository.ArmlPlayerRepository;
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
 * REST controller for managing {@link net.jakebrennan.domain.ArmlPlayer}.
 */
@RestController
@RequestMapping("/api/arml-players")
@Transactional
public class ArmlPlayerResource {

    private final Logger log = LoggerFactory.getLogger(ArmlPlayerResource.class);

    private static final String ENTITY_NAME = "armlPlayer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArmlPlayerRepository armlPlayerRepository;

    public ArmlPlayerResource(ArmlPlayerRepository armlPlayerRepository) {
        this.armlPlayerRepository = armlPlayerRepository;
    }

    /**
     * {@code POST  /arml-players} : Create a new armlPlayer.
     *
     * @param armlPlayer the armlPlayer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new armlPlayer, or with status {@code 400 (Bad Request)} if the armlPlayer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ArmlPlayer> createArmlPlayer(@Valid @RequestBody ArmlPlayer armlPlayer) throws URISyntaxException {
        log.debug("REST request to save ArmlPlayer : {}", armlPlayer);
        if (armlPlayer.getId() != null) {
            throw new BadRequestAlertException("A new armlPlayer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        armlPlayer = armlPlayerRepository.save(armlPlayer);
        return ResponseEntity.created(new URI("/api/arml-players/" + armlPlayer.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, armlPlayer.getId().toString()))
            .body(armlPlayer);
    }

    /**
     * {@code PUT  /arml-players/:id} : Updates an existing armlPlayer.
     *
     * @param id the id of the armlPlayer to save.
     * @param armlPlayer the armlPlayer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated armlPlayer,
     * or with status {@code 400 (Bad Request)} if the armlPlayer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the armlPlayer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ArmlPlayer> updateArmlPlayer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ArmlPlayer armlPlayer
    ) throws URISyntaxException {
        log.debug("REST request to update ArmlPlayer : {}, {}", id, armlPlayer);
        if (armlPlayer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, armlPlayer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!armlPlayerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        armlPlayer = armlPlayerRepository.save(armlPlayer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, armlPlayer.getId().toString()))
            .body(armlPlayer);
    }

    /**
     * {@code PATCH  /arml-players/:id} : Partial updates given fields of an existing armlPlayer, field will ignore if it is null
     *
     * @param id the id of the armlPlayer to save.
     * @param armlPlayer the armlPlayer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated armlPlayer,
     * or with status {@code 400 (Bad Request)} if the armlPlayer is not valid,
     * or with status {@code 404 (Not Found)} if the armlPlayer is not found,
     * or with status {@code 500 (Internal Server Error)} if the armlPlayer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ArmlPlayer> partialUpdateArmlPlayer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ArmlPlayer armlPlayer
    ) throws URISyntaxException {
        log.debug("REST request to partial update ArmlPlayer partially : {}, {}", id, armlPlayer);
        if (armlPlayer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, armlPlayer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!armlPlayerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ArmlPlayer> result = armlPlayerRepository
            .findById(armlPlayer.getId())
            .map(existingArmlPlayer -> {
                if (armlPlayer.getPlayerID() != null) {
                    existingArmlPlayer.setPlayerID(armlPlayer.getPlayerID());
                }
                if (armlPlayer.getFirstName() != null) {
                    existingArmlPlayer.setFirstName(armlPlayer.getFirstName());
                }
                if (armlPlayer.getLastName() != null) {
                    existingArmlPlayer.setLastName(armlPlayer.getLastName());
                }
                if (armlPlayer.getTenhouName() != null) {
                    existingArmlPlayer.setTenhouName(armlPlayer.getTenhouName());
                }
                if (armlPlayer.getLeague() != null) {
                    existingArmlPlayer.setLeague(armlPlayer.getLeague());
                }

                return existingArmlPlayer;
            })
            .map(armlPlayerRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, armlPlayer.getId().toString())
        );
    }

    /**
     * {@code GET  /arml-players} : get all the armlPlayers.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of armlPlayers in body.
     */
    @GetMapping("")
    public List<ArmlPlayer> getAllArmlPlayers(
        @RequestParam(name = "filter", required = false) String filter,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        if ("armlprofile-is-null".equals(filter)) {
            log.debug("REST request to get all ArmlPlayers where armlProfile is null");
            return StreamSupport.stream(armlPlayerRepository.findAll().spliterator(), false)
                .filter(armlPlayer -> armlPlayer.getArmlProfile() == null)
                .toList();
        }
        log.debug("REST request to get all ArmlPlayers");
        if (eagerload) {
            return armlPlayerRepository.findAllWithEagerRelationships();
        } else {
            return armlPlayerRepository.findAll();
        }
    }

    /**
     * {@code GET  /arml-players/:id} : get the "id" armlPlayer.
     *
     * @param id the id of the armlPlayer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the armlPlayer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArmlPlayer> getArmlPlayer(@PathVariable("id") Long id) {
        log.debug("REST request to get ArmlPlayer : {}", id);
        Optional<ArmlPlayer> armlPlayer = armlPlayerRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(armlPlayer);
    }

    /**
     * {@code DELETE  /arml-players/:id} : delete the "id" armlPlayer.
     *
     * @param id the id of the armlPlayer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArmlPlayer(@PathVariable("id") Long id) {
        log.debug("REST request to delete ArmlPlayer : {}", id);
        armlPlayerRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
