package io.axoniq.labs.game.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostNumberRequest {

    @NotEmpty
    private String player;

    @NotNull
    private Integer number;

}
