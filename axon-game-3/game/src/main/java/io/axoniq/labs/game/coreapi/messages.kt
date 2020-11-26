package io.axoniq.labs.game.coreapi

import org.axonframework.commandhandling.TargetAggregateIdentifier

data class CreateGameCommand(@TargetAggregateIdentifier val gameId: String, val name: String, val player: String, val number: Int)
data class JoinGameCommand(val player: String, @TargetAggregateIdentifier val gameId: String)
data class PostNumberCommand(val player: String, @TargetAggregateIdentifier val gameId: String, val number: Int)

data class GameCreatedEvent(val gameId: String, val name: String, val player: String, val number: Int)
data class PlayerJoinedGameEvent(val player: String, val gameId: String)
data class NumberPostedEvent(val player: String, val gameId: String, val number: Int)

