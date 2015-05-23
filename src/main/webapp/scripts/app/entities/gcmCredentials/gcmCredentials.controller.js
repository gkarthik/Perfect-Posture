'use strict';

angular.module('perfectpostureApp')
    .controller('GcmCredentialsController', function ($scope, GcmCredentials, User, ParseLinks) {
        $scope.gcmCredentialss = [];
        $scope.users = User.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            GcmCredentials.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.gcmCredentialss = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            GcmCredentials.update($scope.gcmCredentials,
                function () {
                    $scope.loadAll();
                    $('#saveGcmCredentialsModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            GcmCredentials.get({id: id}, function(result) {
                $scope.gcmCredentials = result;
                $('#saveGcmCredentialsModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            GcmCredentials.get({id: id}, function(result) {
                $scope.gcmCredentials = result;
                $('#deleteGcmCredentialsConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            GcmCredentials.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteGcmCredentialsConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.gcmCredentials = {regId: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
