'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');



var CHAT_URL = "/ws";
var CHAT_PUBLISH_URL = "/queue/";
var CHAT_SUBSCRIBE_URL = "/topic/";


var stompClient = null;
var username = null;
var room = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

    
// if(stompClient == null){
// }


////////////////////////////////////////////////
//
////////////////////////////////////////////////
function connect(event) {
	
	event.preventDefault();
	
    username = document.querySelector('#name').value.trim();
    room = document.querySelector('#room').value.trim();

    if(username != '' && room != ''){

        if(stompClient !=null) {
            onConnected();

        }else{
            console.log('STOMP: Attempting connection', CHAT_URL);

            // recreate the stompClient to use a new WebSocket
            var socket = new SockJS(CHAT_URL);
            stompClient = Stomp.over(socket);
            // stompClient.heartbeat.outgoing = 0;
            // stompClient.heartbeat.incoming = 0;
            stompClient.connect({}, onConnected, onError);
        }
    }
}  

function onConnected() {
	
    console.log('STOMP: onConnected');

    // Tell your username to the server
    stompClient.send(CHAT_PUBLISH_URL + room, {}, JSON.stringify({sender: username, contentType: 'JOIN'}) )
    
    // Subscribe to the Public Topic
    var subscribeId = room+"/"+username;
    stompClient.subscribe(CHAT_SUBSCRIBE_URL + room, onMessageReceived, {id : subscribeId});

    usernamePage.classList.add('hidden');
    chatPage.classList.remove('hidden');
    connectingElement.classList.add('hidden');
}

////////////////////////////////////////////////
//
////////////////////////////////////////////////
function disconnect(event) {

    if (stompClient == null) return;
    console.log('STOMP: disconnect');
    
    var subscribeId = room+"/"+username;
    stompClient.unsubscribe(subscribeId, {});

    stompClient.send(CHAT_PUBLISH_URL + room, {}, JSON.stringify({sender: username, contentType: 'LEAVE'}) )
    
    onDisconnected();


    event.preventDefault();
}


function onDisconnected() {
	
    console.log('STOMP: onDisconnected');
	
    username = null;
    room = null;
    
    usernamePage.classList.remove('hidden');
    chatPage.classList.add('hidden');
    messageArea.innerHTML = '';
}



function onError(error) {
    console.log('STOMP: onError');

    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    if (stompClient == null) return;

    console.log('STOMP: sendMessage');

    var messageContent = messageInput.value.trim();

    if(messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            contentType: 'CHAT',
            content: messageInput.value
        };

        stompClient.send(CHAT_PUBLISH_URL + room, {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}


function onMessageReceived(payload) {
	
    console.log('STOMP: onMessageReceived');
	
    var message = JSON.parse(payload.body);

    var messageElement = document.createElement('li');

    if(message.contentType === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' joined!';
        
    } else if (message.contentType === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left!';
    
    } else if (message.contentType === 'CHAT') {

    	if(message.sender != username) {
        	var chatMessage = {
                sender: username,
                contentType: 'READ',
                content: message.id,
        	};
        	stompClient.send(CHAT_PUBLISH_URL + room, {}, JSON.stringify(chatMessage));
    	}
    	
    	
    	
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    } else {
    	return;
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}


function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}

usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', sendMessage, true)
messageForm.addEventListener('reset', disconnect, true)
