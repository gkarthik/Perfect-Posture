'use strict';

angular.module('perfectpostureApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('gcmCredentials', {
                parent: 'entity',
                url: '/gcmCredentials',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'perfectpostureApp.gcmCredentials.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/gcmCredentials/gcmCredentialss.html',
                        controller: 'GcmCredentialsController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('gcmCredentials');
                        return $translate.refresh();
                    }]
                }
            })
            .state('gcmCredentialsDetail', {
                parent: 'entity',
                url: '/gcmCredentials/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'perfectpostureApp.gcmCredentials.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/gcmCredentials/gcmCredentials-detail.html',
                        controller: 'GcmCredentialsDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('gcmCredentials');
                        return $translate.refresh();
                    }]
                }
            });
    });
