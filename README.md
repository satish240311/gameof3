
# Game of 3 Code challenge
The goal here is to demonstrate the possible implementation of the backend for the Game Of Three 

# GAME of Three solution

In this example, I have tried to keep the solution to the very minimal focusing on the DDD/CQRS aspects rather than actually fullfilling the full game which is quite trivial and not really the real meat of the test.

So I followed the Axon proposed approach:

## AGGREGATE
Decide what is the Root Aggregate: in this example it is clearly the Game and then attached to it the players, the current latest number and so on.

The Aggregate is implemented in:

GameAggregate.java
This is a clear example of a Non-Anemic model implementation.

Command/Events as messages.
Define in one Kotlin file the command and events that mode the state of the system:

game/coreapi/message.kt 
Kotlin is particularly effective to provide in very concise way all the commands and events in the system.

Command and Query models
game/commandmodel
game/querymodel
EventSourcing
I adoped the default embedded EventSource provided by Axon to keep it simple

## TEST FIRST
Axon provide test fictures that allow the user to write tests in given()/when()/expect() fashion which is formidable because allows the programmer to write the tests thinking about commands and events (or error) to be expected.

test/jave/io/axoniq/labs/game/commandmodel/GameAggregateTest.java 

# Installation
This project requires JDK 8 and maven for building and testing it.

## Install Java on linux using this command:

	sudo apt-get install openjdk-8-jdk

## Install maven

	sudo apt-get install maven

This project contains the source code for the Game of 3 Axon test.

#  How to build

	
	cd axon-game-3
	mvn clean package

# How to run

The app is composed of a remote H2 database which needs to be executed in standalone mode and the Game of 3 server itself.

In order to do these operation the quickest is to start your intellij project:

Open a terminal in the directory: axon-game-3 and import the project:

idea .
In the Project window navigate to the directory:

	axon-game-3/game/src/main/java/io/axoniq/labs/game

Then run the database:

From IntelliJ Run the 'Server'
If correctly started you will see on console the message:

Database running on port 9092
Press any key to shut down
Then start the Game Of Three backend:

# GameOfThree

## How to play
I did not pay much attention on the actual details of the game. I preferred to spend time to exepriment with AXON (my first attempt to actually use it) rather then spending time on a trivial game.

However I tried to put a reasonable amount of checks in order to create a game by a player with an initial number, allow an other user to join ann added few checks on the numbers sent by them from an UI (that I did not implement).

Also I did not pay much attention to the automatic mode or not. That would probably be also an UI side of logic: in the UI the player will take his decision {-1, 0 +1} manually or automatically

My intention was to model the information on the backend side until a player is a winner.

So I provided a bunch of CURL that can be executed which I think should provide a good grasp of the solution proposed and the awesomeness of AxonIQ

# CURL

Create a game
	curl --request POST  --url http://localhost:8080/games \
	--header 'content-type: application/json'  \
	--data '{"gameId":"my_game_id","name":"testgame","partecipant":"satish","number":45}'

Projection - Get the games
	curl --request GET  --url http://localhost:8080/games

Response
	[{
		"gameId": "my_game_id",
		"name": "testgame",
		"player1": "satish",
		"player2": null,
		"number": 45,
		"lastMovePlayer": "s",
		"winner": null
	}]

## Get the events
With this request we can query Axon Event source to return us the events in the db.

	curl --request GET  --url http://localhost:8080/events/my_game_id
Response
	[{
		"type": "GameAggregate",
		"aggregateIdentifier": "my_game_id",
		"sequenceNumber": 0,
		"timestamp": {
			"nano": 191000000,
			"epochSecond": 1534825462
		},
		"identifier": "0f761a37-61fa-4658-9fb0-89562293a62e",
		"metaData": {
			"traceId": "92f8ab02-39ee-4a1d-af6f-82a9fb6ff16d",
			"correlationId": "92f8ab02-39ee-4a1d-af6f-82a9fb6ff16d"
		},
		"payload": {
			"gameId": "my_game_id",
			"name": "testgame",
			"player": "satish",
			"number": 45
		},
		"payloadType": "io.axoniq.labs.game.coreapi.GameCreatedEvent"
	    }]

## Get the participants
	curl --request GET  --url http://localhost:8080/games/my_game_id/players
Response

	["satish"]

## Get the numbers played
	curl --request GET  --url http://localhost:8080/games/my_game_id/numbers

Response

	{
	    "content": [{
		"timestamp": 1534821828881,
		"gameId": "my_game_id",
		"number": 45,
		"player": "satish"
	    }],
	    "last": true,
	    "totalPages": 1,
	    "totalElements": 1,
	    "sort": null,
	    "size": 10,
	    "number": 0,
	    "numberOfElements": 1,
	    "first": true
	}

## Join the game
	curl --request POST  --url http://localhost:8080/games/my_game_id/players \
	    --header 'content-type: application/json'  \
	    --data '{"name":"reddy"}'

## Check the players

	curl --request GET  --url http://localhost:8080/games/my_game_id/players

	["satish","reddy"]

If we lookup againt the events happened in the past now we will find the creation and the joining of the game

	curl --request GET  --url http://localhost:8080/events/my_game_id

	[{
		"type": "GameAggregate",
		"aggregateIdentifier": "my_game_id",
		"sequenceNumber": 0,
		"identifier": "31cd0c2e-f41a-4bc0-a470-e2bb12319af1",
		"timestamp": {
			"nano": 598000000,
			"epochSecond": 1534824313
		},
		"metaData": {
			"traceId": "e8e4bb1c-2b5e-4d1a-a321-9aed8da5a0c2",
			"correlationId": "e8e4bb1c-2b5e-4d1a-a321-9aed8da5a0c2"
		},
		"payload": {
			"gameId": "my_game_id",
			"name": "testgame",
			"player": "satish",
			"number": 45
		},
		"payloadType": "io.axoniq.labs.game.coreapi.GameCreatedEvent"
	}, {
		"type": "GameAggregate",
		"aggregateIdentifier": "my_game_id",
		"sequenceNumber": 1,
		"identifier": "adb82db9-07a3-4e1e-b072-0acde02d6fe1",
		"timestamp": {
			"nano": 725000000,
			"epochSecond": 1534824318
		},
		"metaData": {
			"traceId": "7cc59f09-e965-4615-84b8-76c65b6d7890",
			"correlationId": "7cc59f09-e965-4615-84b8-76c65b6d7890"
		},
		"payload": {
			"player": "reddy",
			"gameId": "my_game_id"
		},
		"payloadType": "io.axoniq.labs.game.coreapi.PlayerJoinedGameEvent"
	}]
	
## POST a winner number

	curl --request POST  --url http://localhost:8080/games/my_game_id/numbers \
		--header 'content-type: application/json'  \
		--data '{"player":"reddy", "number": 3}'
  

	{
	    "content": [{
		"id": 2,
		"timestamp": 1534826683173,
		"gameId": "my_game_id",
		"number": 3,
		"participant": "reddy"
	    }, {
		"id": 1,
		"timestamp": 1534826619345,
		"gameId": "my_game_id",
		"number": 45,
		"participant": "satish"
	    }],
	    "totalPages": 1,
	    "totalElements": 2,
	    "last": true,
	    "sort": null,
	    "size": 10,
	    "number": 0,
	    "numberOfElements": 2,
	    "first": true
	}


And Winner is: reddy
# gameof3
