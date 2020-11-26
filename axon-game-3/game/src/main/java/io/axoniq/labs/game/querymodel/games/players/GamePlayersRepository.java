package io.axoniq.labs.game.querymodel.games.players;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GamePlayersRepository extends JpaRepository<GamePlayer, Long> {

    List<GamePlayer> findGamePlayersByGameId(String gameId);

}
