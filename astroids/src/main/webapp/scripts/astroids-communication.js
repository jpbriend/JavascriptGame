

angular.module('AstroidsModule')
    .factory('shipDataService', function($rootScope, atmosphereService) {

            var shipDataService = {};

            var request = {
                url: '/websocket/receiveShipData',
                contentType: 'application/json',
                logLevel: 'debug',
                transport: 'websocket',
                trackMessageLength: true,
                reconnectInterval: 5000,
                enableXDR: true,
                timeout: 60000
            };

            request.onOpen = function(response){
                //$scope.model.transport = response.transport;
                //$scope.model.connected = true;
                //$scope.model.content = 'Atmosphere connected using ' + response.transport;
            };

            request.onClientTimeout = function(response){
                //$scope.model.content = 'Client closed the connection after a timeout. Reconnecting in ' + request.reconnectInterval;
                //$scope.model.connected = false;

                setTimeout(function(){
                    socket = atmosphereService.subscribe(request);
                }, request.reconnectInterval);
            };

            request.onReopen = function(response){
                //$scope.model.connected = true;
                //$scope.model.content = 'Atmosphere re-connected using ' + response.transport;
            };

            request.onMessage = function(data){
                var responseBody = data.responseBody;
                var message = atmosphere.util.parseJSON(responseBody);
                $rootScope.$emit('shipMessage', message);
            };

            request.onClose = function(response){
                //socket.push(atmosphere.util.stringifyJSON({ author: $scope.model.name, message: 'disconnecting' }));
            };

            request.onError = function(response){
                //$scope.model.content = "Sorry, but there's some problem with your socket or the server is down";
                //$scope.model.logged = false;
            };

            request.onReconnect = function(request, response){
                //$scope.model.content = 'Connection lost. Trying to reconnect ' + request.reconnectInterval;
                //$scope.model.connected = false;
            };

            var socket = atmosphereService.subscribe(request);

            shipDataService.send = function(message) {
                socket.push(atmosphere.util.stringifyJSON(message));
            };

            return shipDataService;
        }
    );