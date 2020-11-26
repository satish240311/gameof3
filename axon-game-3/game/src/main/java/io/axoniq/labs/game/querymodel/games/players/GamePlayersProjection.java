package io.axoniq.labs.game.querymodel.games.players;

import io.axoniq.labs.game.coreapi.GameCreatedEvent;
import io.axoniq.labs.game.coreapi.PlayerJoinedGameEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/games/{gameId}/players")
public class GamePlayersProjection {

    private final GamePlayersRepository repository;

    public GamePlayersProjection(GamePlayersRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<String> participantsInGame(@PathVariable String gameId) {
        return repository.findGamePlayersByGameId(gameId)
                .stream()
                .map(GamePlayer::getPlayer).sorted().collect(toList());
    }

    @EventHandler
    public void on(GameCreatedEvent event) {
        repository.save(new GamePlayer(event.getGameId(), event.getPlayer()));
    }

    @EventHandler
    public void on(PlayerJoinedGameEvent event) {
        repository.save(new GamePlayer(event.getGameId(), event.getPlayer()));
    }

}

