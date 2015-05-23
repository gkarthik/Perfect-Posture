'use strict';

angular.module('perfectpostureApp')
    .controller('GcmCredentialsDetailController', function ($scope, $stateParams, GcmCredentials, User) {
        $scope.gcmCredentials = {};
        $scope.load = function (id) {
            GcmCredentials.get({id: id}, function(result) {
              $scope.gcmCredentials = result;
            });
        };
        $scope.load($stateParams.id);
    });
