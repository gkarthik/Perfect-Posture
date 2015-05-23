'use strict';

angular.module('perfectpostureApp')
    .factory('SensorValue', function ($resource) {
        return $resource('api/sensorValues/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.timestamp = new Date(data.timestamp);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
