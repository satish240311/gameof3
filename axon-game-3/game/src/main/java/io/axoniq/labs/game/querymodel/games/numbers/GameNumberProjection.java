package io.axoniq.labs.game.querymodel.games.numbers;

import io.axoniq.labs.game.coreapi.GameCreatedEvent;
import io.axoniq.labs.game.coreapi.NumberPostedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.Timestamp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/games/{gameId}/numbers")
public class GameNumberProjection {

    private final GameNumberRepository repository;

    public GameNumberProjection(GameNumberRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public Page<GameNumber> messagesInGame(@PathVariable String gameId,
                                           @RequestParam(defaultValue = "0") int pageId,
                                           @RequestParam(defaultValue = "10") int pageSize) {
        return repository.findAllByGameIdOrderByTimestampDesc(gameId, new PageRequest(pageId, pageSize));
    }

    @EventHandler
    public void on(GameCreatedEvent event, @Timestamp Instant timestamp) {
        repository.save(new GameNumber(event.getPlayer(), event.getGameId(), event.getNumber(), timestamp.toEpochMilli()));
    }

    @EventHandler
    public void on(NumberPostedEvent event, @Timestamp Instant timestamp) {
        repository.save(new GameNumber(event.getPlayer(), event.getGameId(), event.getNumber(), timestamp.toEpochMilli()));
    }
}
