

angular.module('AstroidsModule')
    .factory('shipDataService', function($rootScope, atmosphereService) {

        var shipDataService = {};

        var socket = {};

        var request = {
                url: '/websocket/receiveShipData',
                contentType: 'application/json',
                logLevel: 'debug',
                transport: 'websocket',
                trackMessageLength: true,
                reconnectInterval: 30000,
                enableXDR: true,
                timeout: 60000
            };

            request.onOpen = function(response){};

            request.onClientTimeout = function(response) {
                setTimeout(function(){
                    socket = atmosphereService.subscribe(request);
                }, request.reconnectInterval);
            };

            request.onReopen = function(response){};

            request.onMessage = function(data){
                var responseBody = data.responseBody;
                var message = atmosphere.util.parseJSON(responseBody);
                $rootScope.$emit('shipMessage', message);
            };

            request.onClose = function(response){};

            request.onError = function(response){};

            request.onReconnect = function(request, response){};

            socket = atmosphereService.subscribe(request);

            shipDataService.send = function(message) {
                socket.push(atmosphere.util.stringifyJSON(message));
            };

            return shipDataService;
        }
    )

    .factory('connectionsService', function($rootScope, atmosphereService) {

        var connectionsService = {};

        var connectionSocket = {};

        var request = {
            url: '/websocket/connections',
            contentType: 'application/json',
            logLevel: 'debug',
            transport: 'websocket',
            trackMessageLength: true,
            reconnectInterval: 30000,
            enableXDR: true,
            timeout: 60000
        };

        request.onOpen = function(response){};

        request.onClientTimeout = function(response){
            setTimeout(function(){
                connectionSocket = atmosphereService.subscribe(request);
            }, request.reconnectInterval);
        };

        request.onReopen = function(response){};

        request.onMessage = function(data){
            var responseBody = data.responseBody;
            var message = atmosphere.util.parseJSON(responseBody);
            $rootScope.$emit('connections', message);
        };

        request.onClose = function(response){
            console.log('Closing socket connection for client ' + $rootScope.clientId);
            // socket.push(atmosphere.util.stringifyJSON({ action: 'disconnection', target: $rootScope.clientId }));
        };

        request.onError = function(response){};

        request.onReconnect = function(request, response){};

        connectionSocket = atmosphereService.subscribe(request);

        connectionsService.send = function(message) {
            connectionSocket.push(atmosphere.util.stringifyJSON(message));
        };


        return connectionsService;
    }
)
    .factory('leapService', function($rootScope) {
        var leapController = new Leap.Controller({
            host: '127.0.0.1',
            port: 6437,
            enableGestures: false,
            frameEventName: 'animationFrame',
            useAllPlugins: true
        });

        leapController.connect();

        return leapController;
    }

);