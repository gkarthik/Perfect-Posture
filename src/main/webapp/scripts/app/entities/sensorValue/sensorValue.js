'use strict';

angular.module('perfectpostureApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('sensorValue', {
                parent: 'entity',
                url: '/sensorValue',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'perfectpostureApp.sensorValue.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/sensorValue/sensorValues.html',
                        controller: 'SensorValueController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('sensorValue');
                        return $translate.refresh();
                    }]
                }
            })
            .state('sensorValueDetail', {
                parent: 'entity',
                url: '/sensorValue/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'perfectpostureApp.sensorValue.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/sensorValue/sensorValue-detail.html',
                        controller: 'SensorValueDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('sensorValue');
                        return $translate.refresh();
                    }]
                }
            });
    });
