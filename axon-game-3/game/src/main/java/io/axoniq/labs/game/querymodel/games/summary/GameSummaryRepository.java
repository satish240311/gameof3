package io.axoniq.labs.game.querymodel.games.summary;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GameSummaryRepository extends JpaRepository<GameSummary, String> {
}
