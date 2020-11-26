package io.axoniq.labs.game.commandmodel;

import io.axoniq.labs.game.coreapi.*;
import io.axoniq.labs.game.exceptions.CannotJoinTwiceException;
import io.axoniq.labs.game.exceptions.CannotPlayANumberTwice;
import io.axoniq.labs.game.exceptions.GameAlreadyWithTwoPlayersException;
import io.axoniq.labs.game.exceptions.NumberIsNotDivisibleBy3;
import lombok.Data;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate
@Data
public class GameAggregate {

    @AggregateIdentifier
    private String gameId;
    private String name;
    private Set<String> players;
    private Integer number;
    private String lastMovePlayer;
    private String winner;

    public GameAggregate() {
    }

    @CommandHandler
    public GameAggregate(CreateGameCommand command) throws NumberIsNotDivisibleBy3 {

        if (command.getNumber() % 3 != 0)
            throw new NumberIsNotDivisibleBy3();

        apply(new GameCreatedEvent(command.getGameId(), command.getName(), command.getPlayer(), command.getNumber()));
    }

    @CommandHandler
    public void handle(JoinGameCommand command) throws GameAlreadyWithTwoPlayersException, CannotJoinTwiceException {
        if (players.contains(command.getPlayer()))
            throw new CannotJoinTwiceException();
        else if (players.size() == 2)
            throw new GameAlreadyWithTwoPlayersException();

        apply(new PlayerJoinedGameEvent(command.getPlayer(), gameId));
    }

    @CommandHandler
    public void handle(PostNumberCommand command) throws CannotPlayANumberTwice, NumberIsNotDivisibleBy3 {
        Assert.state(players.contains(command.getPlayer()),
                "You cannot post number unless you've joined the game");

        if (command.getPlayer().equals(lastMovePlayer))
            throw new CannotPlayANumberTwice();

        if (command.getNumber() % 3 != 0)
            throw new NumberIsNotDivisibleBy3();


        apply(new NumberPostedEvent(command.getPlayer(), gameId, command.getNumber()));
    }

    @EventSourcingHandler
    protected void on(GameCreatedEvent event) {
        this.gameId = event.getGameId();
        this.name = event.getName();
        this.players = new HashSet<>();
        players.add(event.getPlayer());
        this.number = event.getNumber();
        this.lastMovePlayer = event.getPlayer();
    }

    @EventSourcingHandler
    protected void on(PlayerJoinedGameEvent event) {
        this.players.add(event.getPlayer());
    }

    @EventSourcingHandler
    public void on(NumberPostedEvent event) {
        this.lastMovePlayer = event.getPlayer();
        this.number = event.getNumber();
        if (event.getNumber() / 3 == 1) {
            System.out.println("And Winner is: " + event.getPlayer());
            this.winner = event.getPlayer();
        }
    }

}
