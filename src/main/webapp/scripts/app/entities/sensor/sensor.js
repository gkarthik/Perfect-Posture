'use strict';

angular.module('perfectpostureApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('sensor', {
                parent: 'entity',
                url: '/sensor',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'perfectpostureApp.sensor.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/sensor/sensors.html',
                        controller: 'SensorController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('sensor');
                        return $translate.refresh();
                    }]
                }
            })
            .state('sensorDetail', {
                parent: 'entity',
                url: '/sensor/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'perfectpostureApp.sensor.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/sensor/sensor-detail.html',
                        controller: 'SensorDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('sensor');
                        return $translate.refresh();
                    }]
                }
            });
    });
