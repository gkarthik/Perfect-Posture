'use strict';

angular.module('perfectpostureApp')
    .controller('SensorValueController', function ($scope, SensorValue, Sensor, ParseLinks) {
        $scope.sensorValues = [];
        $scope.sensors = Sensor.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            SensorValue.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.sensorValues = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            SensorValue.update($scope.sensorValue,
                function () {
                    $scope.loadAll();
                    $('#saveSensorValueModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            SensorValue.get({id: id}, function(result) {
                $scope.sensorValue = result;
                $('#saveSensorValueModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            SensorValue.get({id: id}, function(result) {
                $scope.sensorValue = result;
                $('#deleteSensorValueConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            SensorValue.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteSensorValueConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.sensorValue = {sen1: null, sen2: null, sen3: null, sen4: null, timestamp: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
