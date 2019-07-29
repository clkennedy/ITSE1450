const http = require('http');
const express = require('express');
const socketio = require('socket.io');

const app = express();
const server = http.creatServer(app);
const io = socketio(server);

var players = [];
var nextPlayerNum = 1;
var rooms = [];

server.listen(8080, function(){
    log("Server Running..");
})
io.on('connection', function(socket){
   
})