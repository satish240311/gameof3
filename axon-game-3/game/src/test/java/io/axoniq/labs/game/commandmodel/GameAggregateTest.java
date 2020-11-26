package io.axoniq.labs.game.commandmodel;


import io.axoniq.labs.game.coreapi.*;
import io.axoniq.labs.game.exceptions.CannotJoinTwiceException;
import io.axoniq.labs.game.exceptions.CannotPlayANumberTwice;
import io.axoniq.labs.game.exceptions.GameAlreadyWithTwoPlayersException;
import org.axonframework.commandhandling.model.AggregateNotFoundException;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.Before;
import org.junit.Test;

public class GameAggregateTest {
    private AggregateTestFixture<GameAggregate> testFixture;

    @Before
    public void setUp() {
        testFixture = new AggregateTestFixture<>(GameAggregate.class);
    }

    @Test
    public void testCreateGame() {
        testFixture.givenNoPriorActivity()
                .when(new CreateGameCommand("gameId", "testgame", "antonio", 57))
                .expectEvents(new GameCreatedEvent("gameId", "testgame", "antonio", 57));
    }

    @Test
    public void testCreateGameNoDivisibleBy3() {
        testFixture.givenNoPriorActivity()
                .when(new CreateGameCommand("gameId", "testgame", "antonio", 56))
                .expectNoEvents();
    }

    @Test
    public void testJoinWithoutCreationCreateGame() {
        testFixture.givenNoPriorActivity()
                .when(new JoinGameCommand("player", "gameId"))
                .expectNoEvents().expectException(AggregateNotFoundException.class);
    }

    @Test
    public void testJoinGame() {
        testFixture.given(new GameCreatedEvent("gameId", "testgame", "antonio", 56))
                .when(new JoinGameCommand("player", "gameId"))
                .expectEvents(new PlayerJoinedGameEvent("player", "gameId"));
    }

    @Test
    public void testNoMoreThenTwoCanJoinGame() {
        testFixture.given(
                new GameCreatedEvent("gameId", "testgame", "antonio", 56),
                new PlayerJoinedGameEvent("pippo", "gameId"))
                .when(new JoinGameCommand("participant2", "gameId"))
                .expectNoEvents().expectException(GameAlreadyWithTwoPlayersException.class);
    }

    @Test
    public void testPostNumberTwideSamePlayer() {
        testFixture.given(new GameCreatedEvent("gameId", "testgame", "antonio", 56),
                new PlayerJoinedGameEvent("player", "gameId"))
                .when(new PostNumberCommand("antonio", "gameId", 55))
                .expectNoEvents().expectException(CannotPlayANumberTwice.class);
    }

    @Test
    public void testPostNumber() {
        testFixture.given(new GameCreatedEvent("gameId", "testgame", "antonio", 57),
                new PlayerJoinedGameEvent("player", "gameId"))
                .when(new PostNumberCommand("player", "gameId", 12))
                .expectEvents(new NumberPostedEvent("player", "gameId", 12));
    }

    @Test
    public void testPostNumberWinner() {
        testFixture.given(new GameCreatedEvent("gameId", "testgame", "antonio", 57),
                new PlayerJoinedGameEvent("player", "gameId"))
                .when(new PostNumberCommand("player", "gameId", 3))
                .expectEvents(new NumberPostedEvent("player", "gameId", 3));
    }

    @Test
    public void testCannotJoinGameTwice() {
        testFixture.given(new GameCreatedEvent("gameId", "testgame", "antonio", 56))
                .when(new JoinGameCommand("antonio", "gameId"))
                .expectNoEvents()
                .expectException(CannotJoinTwiceException.class);
    }
}
