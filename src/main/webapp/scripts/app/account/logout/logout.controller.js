'use strict';

angular.module('perfectpostureApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
