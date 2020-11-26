package io.axoniq.labs.game.querymodel.games.players;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"gameId", "player"}))
public class GamePlayer {

    @Id
    @GeneratedValue
    private Long id;

    private String gameId;
    private String player;

    public GamePlayer(String gameId, String player) {
        this.gameId = gameId;
        this.player = player;
    }

}
