package io.axoniq.labs.game.querymodel.games.numbers;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class GameNumber {

    @Id
    @GeneratedValue
    private Long id;

    private long timestamp;
    private String gameId;
    private Integer number;
    private String participant;

    public GameNumber(String participant, String gameId, Integer number, long timestamp) {
        this.participant = participant;
        this.gameId = gameId;
        this.number = number;
        this.timestamp = timestamp;
    }

}
