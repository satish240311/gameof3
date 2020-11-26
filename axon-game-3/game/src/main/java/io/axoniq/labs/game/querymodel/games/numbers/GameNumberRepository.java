package io.axoniq.labs.game.querymodel.games.numbers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameNumberRepository extends JpaRepository<GameNumber, Long> {

    Page<GameNumber> findAllByGameIdOrderByTimestampDesc(String gameId, Pageable pageable);

}
