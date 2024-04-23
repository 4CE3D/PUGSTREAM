var stompClient = null;
var previousState = null; // Variable to store the previous state of the video

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/stomp-endpoint');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe("/topic/commands/"+ id, function (greeting) {
            showGreeting(JSON.parse(greeting.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/controls/"+id, {}, JSON.stringify({'name': previousState, 'token':token}));
}

function sendTime(time) {
    stompClient.send("/app/controls/"+id, {}, JSON.stringify({'name': time, 'token':token}));
}

function showGreeting(message) {
    if (message.message === "true") {
            var video = document.querySelector("video");
            if (video) {
                video.pause();
            }
        }
    else if (message.message === "false") {
        var video = document.querySelector("video");
                    if (video) {
                        video.play();
                    }
    }
    else
    {
       var video = document.querySelector("video");
       if(video){
       if(Math.abs(message.message - video.currentTime) > 2)
       {
       video.currentTime = message.message;
       }

       }
    }

}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });

    // Call connect function when the page loads
    connect();
    console.log("connected?");

    // Check if the video state changes and send message if it does
    setInterval(function() {
        let video = document.querySelector("video");
        if (video) {
            sendTime(video.currentTime);
            var currentState = video.paused;
            if (currentState !== previousState) {
                previousState = currentState;
                sendName();
            }
        }
    }, 1000); // Check every second
});
