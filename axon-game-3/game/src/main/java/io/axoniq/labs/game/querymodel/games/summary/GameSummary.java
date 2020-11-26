package io.axoniq.labs.game.querymodel.games.summary;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class GameSummary {

    @Id
    @NotNull
    private String gameId;

    @NotNull
    private String name;

    @NotNull
    private String player1;

    private String player2;

    @NotNull
    private Integer number;

    @NotNull
    private String lastMovePlayer;

    private String winner;

    public GameSummary(String gameId, String name, String player1, Integer number, String lastMovePlayer) {
        this.gameId = gameId;
        this.name = name;
        this.player1 = player1;
        this.number = number;
        this.lastMovePlayer = lastMovePlayer;
    }

}
