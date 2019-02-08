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
       if(room != null && room.numOfPlayer < room.maxPlayer){
            player.roomName = data.roomName;
            room.numOfPlayer ++;
            socket.join(room.name);
            socket.emit('successfulRoomJoin');
            console.log(player.id + " joined " + rooms.find(r => r.name == data.roomName).name + ": player " + 
                    rooms.find(r => r.name == data.roomName).numOfPlayer + "/" + rooms.find(r => r.name == data.roomName).maxPlayer)
            socket.to(room.name).emit("newPlayerJoinedRoom", {id: socket.id, userName: player.userName});
            socket.emit('getRoomPlayers', players.filter(p => p.roomName == room.name));
       }
   })
   
   socket.on('roomDestroyed', function(data){
       var player = players.find(p => p.id == socket.id);
       socket.leave(data.roomName);
       player.roomName = "";
   })
   
   socket.on('leaveRoom', function(){
       var player = players.find(p => p.id == socket.id);
       socket.to(player.roomName).emit('playerDisconnectedFromRoom', {id: socket.id});
       
       room = rooms.find(r => r.name == player.roomName);
       room.numOfPlayer --;
       if(room.host == socket.id){
           socket.to(player.roomName).emit('roomDestroyed', {roomName: room.name});
           rooms.splice(rooms.indexOf(room), 1);
       }
       socket.leave(player.roomName);
       player.roomName = "";
   })
   
   socket.on('joinMultiplerAgain',function(){
       var player = players.find(p => p.id == socket.id);
       console.log("pushed user name for player " + socket.id + ": " + player.userName);
       socket.emit('pushDefaultUserName', {id: socket.id, userName: player.userName});
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
       socket.emit('requestRooms', rooms);
   });
   
   socket.on('updateUserName', function(data){
       var player = players.find(p => p.id == socket.id);
       var old = player.userName;
       player.userName = data.userName;
       console.log("player " + socket.id + " updated user name to " +players.find(p => p.id == socket.id).userName);
   })
   
   socket.on('flagReady', function(){
       var player = players.find(p => p.id = socket.id);
       player.ready = true;
       socket.to(player.roomName).emit('playerFlaggedReady', {id:socket.id});
        socket.emit('playerFlaggedReady', {id:socket.id});
       
   })
   socket.on('flagUnReady', function(){
       var player = players.find(p => p.id = socket.id);
       player.ready = false;
       socket.to(player.roomName).emit('playerFlaggedUnReady', {id:socket.id});
       socket.emit('playerFlaggedUnReady', {id:socket.id});
       
   })
   
})

function Room(name){
    this.name = name;
    this.maxPlayer = 4;
    this.numOfPlayer = 0;
    this.host = "";
}

function Player(id, userName, x, y){
    this.id = id;
    this.userName = userName;
    this.x = x;
    this.y = y;
    this.roomName = "";
    this.ready = false;
    this.hero = 0;
}
//socket.broadcast.emit('newPlayer', {id: socket.id});
   // socket.on('disconnect', function(){
   ///     console.log("player disconnected");
   // })