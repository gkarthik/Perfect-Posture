'use strict';

angular.module('perfectpostureApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


