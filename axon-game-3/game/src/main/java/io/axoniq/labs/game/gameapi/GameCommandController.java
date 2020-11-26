package io.axoniq.labs.game.gameapi;

import io.axoniq.labs.game.beans.Game;
import io.axoniq.labs.game.beans.Player;
import io.axoniq.labs.game.beans.PostNumberRequest;
import io.axoniq.labs.game.coreapi.CreateGameCommand;
import io.axoniq.labs.game.coreapi.JoinGameCommand;
import io.axoniq.labs.game.coreapi.PostNumberCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;
import java.util.concurrent.Future;

@RestController
public class GameCommandController {

    private final CommandGateway commandGateway;

    public GameCommandController(@SuppressWarnings("SpringJavaAutowiringInspection") CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping("/games")
    public Future<String> createGame(@RequestBody @Valid Game game) {
        Assert.notNull(game.getName(), "name is mandatory for game");
        Assert.notNull(game.getNumber(), "Number is mandatory for game");
        Assert.notNull(game.getPartecipant(), "Player is mandatory for game");
        String gameId = game.getGameId() == null ? UUID.randomUUID().toString() : game.getGameId();
        return commandGateway.send(new CreateGameCommand(gameId, game.getName(), game.getPartecipant(), game.getNumber()));
    }

    @PostMapping("/games/{gameId}/players")
    public Future<Void> joinGame(@PathVariable String gameId, @RequestBody @Valid Player participant) {
        Assert.notNull(participant.getName(), "name is mandatory for a game");
        return commandGateway.send(new JoinGameCommand(participant.getName(), gameId));
    }

    @PostMapping("/games/{gameId}/numbers")
    public Future<Void> postNumber(@PathVariable String gameId, @RequestBody @Valid PostNumberRequest message) {
        Assert.notNull(message.getPlayer(), "'name' missing - please provide a player name");
        Assert.notNull(message.getNumber(), "'number' missing - please provide a number to post");

        return commandGateway.send(new PostNumberCommand(message.getPlayer(), gameId, message.getNumber()));
    }

}
