

### Create a game

    curl --request POST  --url http://localhost:8080/games \
    --header 'content-type: application/json'  \
    --data '{"gameId":"my_game_id","name":"testgame","partecipant":"satish","number":45}'
  

### Projection - Get the games

    curl --request GET  --url http://localhost:8080/games

### Response

    [{"gameId":"my_game_id","name":"testgame","player1":"satish","player2":null,"number":45}]

### Get the events

    curl --request GET  --url http://localhost:8080/events/my_game_id
    
### Response
    
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


### Get the participants

    curl --request GET  --url http://localhost:8080/games/my_game_id/players

### Response

    ["satish"]

### Get the numbers played

    curl --request GET  --url http://localhost:8080/games/my_game_id/numbers

### Response

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


### Join the game

    curl --request POST  --url http://localhost:8080/games/my_game_id/players \
        --header 'content-type: application/json'  \
        --data '{"name":"reddy"}'
        
        
#### Check the players

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
    
    
#### POST a winner number    
    
    curl --request POST  --url http://localhost:8080/games/my_game_id/numbers \
            --header 'content-type: application/json'  \
            --data '{"player":"reddy", "number": 3}'
      

    {
        "content": [{
            "id": 2,
            "timestamp": 1534826683193,
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