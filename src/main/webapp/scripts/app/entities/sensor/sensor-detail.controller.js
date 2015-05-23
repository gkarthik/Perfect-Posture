'use strict';

angular.module('perfectpostureApp')
    .controller('SensorDetailController', function ($scope, $stateParams, Sensor, User) {
        $scope.sensor = {};
        $scope.load = function (id) {
            Sensor.get({id: id}, function(result) {
              $scope.sensor = result;
            });
        };
        $scope.load($stateParams.id);
    });
