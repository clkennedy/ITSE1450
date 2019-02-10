var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var players = [];
var nextPlayerNum = 1;
var rooms = [];


server.listen(8080, function(){
    console.log("Server Running..");
})

io.on('connection', function(socket){
    console.log("player " + socket.id + " connected");
    socket.emit('socketID', {id: socket.id});
    socket.on('disconnect', function(){
        for(var i = 0; i< players.length; i++){
            if(players[i].id == socket.id){
                if(players[i].roomName != ""){
                    for(var j = 0; j< rooms.length; j++){
                        if(rooms[j].numOfPlayer == 0){
                            rooms.splice(j,1);
                            continue;
                        }
                        if(!rooms[j].inLobby){
                            socket.to(rooms[j].name).emit('playerDisconnectedGame', {id: socket.id, userName: players[i].userName});
                        }
                        if(rooms[j].name == players[i].roomName){
                            rooms[j].numOfPlayer -= 1;
                            if(rooms[j].host == socket.id){
                                socket.to(players[i].roomName).emit('roomDestroyed', {roomName: rooms[j].name});
                                console.log(rooms[j].name + " was Destroyed");
                                rooms.splice(j,1);
                            }
                        }
                    }
                    socket.to(players[i].roomName).emit('playerDisconnectedFromRoom', {id: socket.id});
                }
                players.splice(i,1);
            }
        }
        console.log("player " + socket.id + " disconnected");
        console.log("Total Player: " + players.length);
   });
   
   var player = new Player(socket.id,"Player"+(nextPlayerNum ++),0,0);
   players.push(player);
   console.log("Total Player: " + players.length);
   socket.emit('pushDefaultUserName', {id: socket.id, userName: player.userName});
   
   socket.on('joinRoom', function(data){
       var player = players.find(p => p.id == socket.id);
       var room = rooms.find(r => r.name == data.roomName);
       if(room != null && room.numOfPlayer < room.maxPlayer && room.inLobby){
            player.roomName = data.roomName;
            room.numOfPlayer ++;
            socket.join(room.name);
            socket.emit('successfulRoomJoin');
            console.log(player.id + " joined " + player.roomName + ": player " + 
                    rooms.find(r => r.name == data.roomName).numOfPlayer + "/" + rooms.find(r => r.name == data.roomName).maxPlayer)
            socket.to(room.name).emit("newPlayerJoinedRoom", {id: socket.id, userName: player.userName, hero: player.hero});
            socket.emit('getRoomPlayers', players.filter(p => p.roomName == room.name));
       }
       else{
           socket.emit('failedRoomJoin');
       }
   })
   
   socket.on('roomDestroyed', function(data){
       var player = players.find(p => p.id == socket.id);
       socket.leave(data.roomName);
       player.roomName = "";
   })
   
   socket.on('leaveRoom', function(){
       console.log("looking for player " + socket.id);
       var player = players.find(p => p.id == socket.id);
       players.forEach(p => console.log(p.id));
       if(player != null){
           console.log("player found, looking for room");
           room = rooms.find(r => r.name == player.roomName);
       }
            
       if(room != null && player != null){
           console.log("room found, push disconnected event");
           socket.to(room.name).emit('playerDisconnectedFromRoom', {id: socket.id});
      //console.log("set all player ready to false;");
           
        console.log("decrease num of players");
       room.numOfPlayer --;
       
       player.roomName = "";
       console.log("push current players to room");
       socket.to(room.name).emit('getRoomPlayers',  players.filter(p => p.roomName == room.name));
       player.ready = false;
       console.log("player leave room");
       socket.leave(room.name);
       console.log(socket.id + " left room: " + room.name);
       if(room.host == socket.id){
           console.log("Player was host, destroying room");
           socket.to(room.name).emit('roomDestroyed', {roomName: room.name});
           rooms.splice(rooms.indexOf(room), 1);
       }
       }
   })
   
   socket.on('joinMultiplerAgain',function(){
       var player = players.find(p => p.id == socket.id);
       if(player != null){
           console.log("pushed user name for player " + socket.id + ": " + player.userName);
            socket.emit('pushDefaultUserName', {id: socket.id, userName: player.userName});
       }
   })
   
   socket.on('createRoom', function(data){
       //socket.emit('createRooms',{id: socket.id});
        var r = new Room(data.roomName);
        var canCreate = true;
        for(var i = 0; i< rooms.length; i++){
            if(rooms[i].name == r.name){
                socket.emit('failedRoomCreation', {reason:"Duplicate Room Name Exist"});
                canCreate = false;
            }
        }
        if(canCreate){
            r.host = socket.id;
            r.numOfPlayer ++;
            r.inLobby = true;
            rooms.push(r);
            console.log(r.name + " was created/host id: " + r.host + "/numplayers: " + r.numOfPlayer);
            for(var i = 0; i < players.length; i ++){
                if(players[i].id == socket.id){
                    players[i].roomName = r.name;
                }
            }
            socket.join(r.name);
            socket.emit('successfulRoomCreation');
            socket.emit('getRoomPlayers', players.filter(p => p.roomName == r.name))
        }
   });
   
   socket.on('requestRooms', function(){
       socket.emit('requestRooms', rooms.filter(r => r.inLobby == true));
   });
   
   socket.on('updateUserName', function(data){
       var player = players.find(p => p.id == socket.id);
       var old = player.userName;
       player.userName = data.userName;
       console.log("player " + socket.id + " updated user name to " +players.find(p => p.id == socket.id).userName);
   })
   
   socket.on('flagReady', function(){
       var player = players.find(p => p.id == socket.id);
       player.ready = true;
       socket.to(player.roomName).emit('playerFlaggedReady', {id:socket.id});
        socket.emit('playerFlaggedReady', {id:socket.id});
       
   })
   socket.on('flagUnReady', function(){
       var player = players.find(p => p.id == socket.id);
       player.ready = false;
       socket.to(player.roomName).emit('playerFlaggedUnReady', {id:socket.id});
       socket.emit('playerFlaggedUnReady', {id:socket.id});
   })
   socket.on('updateHero', function(data){
       var player = players.find(p => p.id == socket.id);
       player.hero = data.hero;
       data.id = socket.id;
       socket.to(player.roomName).emit('updateHero', {id:socket.id, hero: player.hero});
       socket.emit('updateHero', {id:socket.id, hero: player.hero});
   })
   
   socket.on('getRoomPlayers', function(){
       player = players.find(p => p.id == socket.id);
       room = rooms.find(r => r.name == player.roomName);
       socket.emit('getRoomPlayers', players.filter(p => p.roomName == room.name));
   })
   socket.on('getRoomPlayersGame', function(){
       player = players.find(p => p.id == socket.id);
       room = rooms.find(r => r.name == player.roomName);
       socket.emit('getRoomPlayersGame', players.filter(p => p.roomName == room.name && p.id != socket.id));
   })
   
   socket.on('startGame', function(){
       room = rooms.find(r => r.host == socket.id);
       room.inLobby = false;
       console.log(room.name + " started Game");
   })
   
   socket.on('updateHeroPosition', function(data){
       player = players.find(p => p.id == socket.id);
       room = rooms.find(r => r.name == player.roomName);
       data.id = socket.id;
       if(room != null)
            socket.to(room.name).emit('updateHeroPosition', data);
   })
   socket.on('heroCast', function(data){
       player = players.find(p => p.id == socket.id);
       room = rooms.find(r => r.name == player.roomName);
       data.id = socket.id;
       if(room != null){
           socket.to(room.name).emit('heroCast', data);
           //console.log(socket.id + " cast " + ((data.skill == 0)? "basic":"Alternate") + " Slill");
       }  
   })
   socket.on('heroDamageTaken', function(data){
       player = players.find(p => p.id == socket.id);
       room = rooms.find(r => r.name == player.roomName);
       data.id = socket.id;
       if(room != null){
           socket.to(room.name).emit('heroDamageTaken', data);
           //console.log(socket.id + " took " + data.damage + " damage");
       }  
   })
   socket.on('heroRecover', function(data){
       player = players.find(p => p.id == socket.id);
       room = rooms.find(r => r.name == player.roomName);
       data.id = socket.id;
       if(room != null){
           socket.to(room.name).emit('heroRecover', data);
           console.log(socket.id + " recovered " + data.recover + " damage");
       }  
   })
   socket.on('heroAddPoints', function(data){
       player = players.find(p => p.id == socket.id);
       room = rooms.find(r => r.name == player.roomName);
       data.id = socket.id;
       if(room != null){
           socket.to(room.name).emit('heroAddPoints', data);
           console.log(socket.id + " recovered " + data.recover + " damage");
       }  
   })
   
   socket.on('healthPotionCreated', function(data){
       player = players.find(p => p.id == socket.id);
       room = rooms.find(r => r.name == player.roomName);
       if(room != null){
           socket.to(room.name).emit('healthPotionCreated', data);
           console.log("Health Pot " + data.id + " Created");
       }   
   })
   socket.on('healthPotionPickUp', function(data){
       player = players.find(p => p.id == socket.id);
       room = rooms.find(r => r.name == player.roomName);
       if(room != null){
           socket.to(room.name).emit('healthPotionPickUp', data);
           //console.log("Health Pot " + data.id + " picked up");
       }   
   })
   
   socket.on('enemyTargetChange', function(data){
       player = players.find(p => p.id == socket.id);
       room = rooms.find(r => r.name == player.roomName);
       if(room != null){
           socket.to(room.name).emit('enemyTargetChange', data);
           //console.log("Enemy " + data.id + " Changed Target " );
       }  
   })
   socket.on('enemyCreated', function(data){
       player = players.find(p => p.id == socket.id);
       room = rooms.find(r => r.name == player.roomName);
       if(room != null){
           socket.to(room.name).emit('enemyCreated', data);
           console.log("Enemy " + data.id + " Created");
       }   
   })
   socket.on('enemyAttack', function(data){
       player = players.find(p => p.id == socket.id);
       room = rooms.find(r => r.name == player.roomName);
       if(room != null){
           socket.to(room.name).emit('enemyAttack', data);
           //console.log("Enemy " + data.id + " Attacked " + data.target);
       }     
   })
   socket.on('enemyDamageTaken', function(data){
       player = players.find(p => p.id == socket.id);
       room = rooms.find(r => r.name == player.roomName);
       if(room != null){
           socket.to(room.name).emit('enemyDamageTaken', data);
           console.log(data.id + " enemy took " + data.damage + " damage");
       }  
   })
   socket.on('enemyDied', function(data){
       player = players.find(p => p.id == socket.id);
       room = rooms.find(r => r.name == player.roomName);
       if(room != null){
           socket.to(room.name).emit('enemyDied', data);
           socket.emit('enemyDied', data);
           console.log(data.id + " Enemy Died");
       }  
   })
   socket.on('syncEnemies', function(data){
       player = players.find(p => p.id == socket.id);
       room = rooms.find(r => r.name == player.roomName);
       if(room != null){
           socket.to(room.name).emit('syncEnemies', data);
           //console.log("syning Enemies");
       }  
   })
})

function Room(name){
    this.name = name;
    this.maxPlayer = 4;
    this.numOfPlayer = 0;
    this.host = "";
    this.inLobby = false;
}

function Player(id, userName, x, y){
    this.id = id;
    this.userName = userName;
    this.x = x;
    this.y = y;
    this.roomName = "";
    this.ready = false;
    this.hero = 0;
    this.dir = 0;
}

function Enemt(id){
    this.id = id;
    this.x = x;
    this.y = y;
    this.targetX = "";
    this.targetY = false;
    this.type = 0;
}
//socket.broadcast.emit('newPlayer', {id: socket.id});
   // socket.on('disconnect', function(){
   ///     console.log("player disconnected");
   // })