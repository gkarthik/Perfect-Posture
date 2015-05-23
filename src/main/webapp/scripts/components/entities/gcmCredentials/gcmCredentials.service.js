'use strict';

angular.module('perfectpostureApp')
    .factory('GcmCredentials', function ($resource) {
        return $resource('api/gcmCredentialss/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
