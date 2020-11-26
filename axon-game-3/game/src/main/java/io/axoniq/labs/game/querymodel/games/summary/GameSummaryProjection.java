package io.axoniq.labs.game.querymodel.games.summary;

import io.axoniq.labs.game.coreapi.GameCreatedEvent;
import io.axoniq.labs.game.coreapi.NumberPostedEvent;
import io.axoniq.labs.game.coreapi.PlayerJoinedGameEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.util.List;

@RestController
@RequestMapping("/games")
public class GameSummaryProjection {

    private final GameSummaryRepository gameSummaryRepository;
    private final EntityManager entityManager;

    public GameSummaryProjection(GameSummaryRepository gameSummaryRepository, EntityManager entityManager) {
        this.gameSummaryRepository = gameSummaryRepository;
        this.entityManager = entityManager;
    }

    @GetMapping
    public List<GameSummary> listGames() {
        return gameSummaryRepository.findAll();
    }

    @EventHandler
    public void on(GameCreatedEvent event) {
        gameSummaryRepository.save(new GameSummary(event.getGameId(), event.getName(), event.getPlayer(), event.getNumber(), event.getPlayer()));
    }

    @EventHandler
    public void on(PlayerJoinedGameEvent event) {
        gameSummaryRepository.findOne(event.getGameId()).setPlayer2(event.getPlayer());
    }

    @EventHandler
    public void on(NumberPostedEvent event) {
        final GameSummary one = gameSummaryRepository.findOne(event.getGameId());
        one.setLastMovePlayer(event.getPlayer());
        one.setNumber(event.getNumber());
        if (event.getNumber() / 3 == 1)
            one.setWinner(event.getPlayer());

    }
}


