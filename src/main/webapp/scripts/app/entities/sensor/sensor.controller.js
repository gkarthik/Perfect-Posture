'use strict';

angular.module('perfectpostureApp')
    .controller('SensorController', function ($scope, Sensor, User, ParseLinks) {
        $scope.sensors = [];
        $scope.users = User.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Sensor.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.sensors = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            Sensor.update($scope.sensor,
                function () {
                    $scope.loadAll();
                    $('#saveSensorModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Sensor.get({id: id}, function(result) {
                $scope.sensor = result;
                $('#saveSensorModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Sensor.get({id: id}, function(result) {
                $scope.sensor = result;
                $('#deleteSensorConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Sensor.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteSensorConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.sensor = {device_id: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
