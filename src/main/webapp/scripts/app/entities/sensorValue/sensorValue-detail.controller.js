'use strict';

angular.module('perfectpostureApp')
    .controller('SensorValueDetailController', function ($scope, $stateParams, SensorValue, Sensor) {
        $scope.sensorValue = {};
        $scope.load = function (id) {
            SensorValue.get({id: id}, function(result) {
              $scope.sensorValue = result;
            });
        };
        $scope.load($stateParams.id);
    });
