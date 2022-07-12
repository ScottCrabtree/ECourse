/* global angular, gapi */

require('angular');
require('angular-sanitize');
require('angular-cookies');
require('angular-ui-router');
require('angular-route');

angular.module("ECourseApp", ['ngRoute', 'ngSanitize', 'ngCookies']);

angular.module('ECourseApp').config(function ($routeProvider) {
    $routeProvider
            .when('/', {
                templateUrl: 'pages/main.html',
                controller: 'ECourseHomeController'
            })
            .when('/login', {
                templateUrl: 'pages/login.html',
                controller: 'GoogleSignonController'
            })
            .when('/lesson/:lessonId', {
                templateUrl: 'pages/lesson.html',
                controller: 'ECourseLessonController'
            })
            ;
});

var google_sign_in = false; // assume

function do_click_google_signin() {
    google_sign_in = true;
}

function onSuccess(googleUser) {
    if (google_sign_in) {
        console.log('Logged in as: ' + googleUser.getBasicProfile().getName());
        onGoogleSignIn(googleUser);
    } else {
        console.log('disconnect current Google user');
        gapi.auth2.getAuthInstance().disconnect();
    }
}
function onFailure(error) {
    console.log(error);
}
function renderButton() {
    gapi.signin2.render('google-button-signin2', {
        'scope': 'profile email',
        'width': 240,
        'height': 50,
        'longtitle': true,
        'theme': 'light',
        'onsuccess': onSuccess,
        'onfailure': onFailure
    });
}


var onGoogleSignIn;
angular.module("ECourseApp").controller('GoogleSignonController', function ($scope, $http, $rootScope) {
    console.log('google signon controller started');
    $scope.googleSignon = function (googleUser) {
        console.log('google onSignIn');
        var profile = googleUser.getBasicProfile();
        console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
        console.log('Name: ' + profile.getName());
        console.log('Image URL: ' + profile.getImageUrl());
        console.log('Email: ' + profile.getEmail()); // This is null if the 'email' scope is not present.            

        console.log('onSignIn called in controller');
        $scope.googleUser = googleUser;
        var idToken = googleUser.getAuthResponse().id_token;
        console.log('id token = ' + idToken);
        $http({
            method: 'POST',
            data: {
                googleAuthToken: idToken
            },
            url: 'resources/user/oauthconnect'
        }).then(function (response) {
            if (response.data) {
                console.log('Google login success');
                let locationPath = window.location.href;
                if (locationPath.indexOf('?') !== -1) {
                    locationPath = locationPath.substring(0, locationPath.indexOf('?'));
                }
                locationPath += ("?session_id=" + response.data.sessionId);
                window.location.href = locationPath;
            } else {
                console.log('connect failure');
            }
        }, function () {
            console.log('connect failure');
        });
    };
    onGoogleSignIn = $scope.googleSignon.bind(this);
});


angular.module("ECourseApp").controller('ECourseLoginController', function ($scope, $http, $rootScope) {

    $http({
        method: 'GET',
        url: 'resources/ecourse/version'
    }).then(function (response) {
        $scope.productVersion = response.data.version;
        console.log('version ' + $scope.productVersion);
    });

});

angular.module("ECourseApp").controller('ECourseHomeController', function ($scope, $http, $rootScope) {
    console.log('started Happy Brain Science ECourse controller');
    delete $scope.errorMessage;
    let userLocale = 'en-US';
    if (navigator.language) {
        userLocale = navigator.language;
        console.log('detected user preferred locale in browser ' + userLocale);
    }
    $http({
        method: 'GET',
        url: 'resources/ecourse/captions'
    }).then(function (response) {
        $scope.captions = response.data;
        console.log('captions loaded');
    });

    $http({
        method: 'GET',
        url: 'resources/ecourse/version'
    }).then(function (response) {
        $scope.productVersion = response.data.version;
        console.log('version ' + $scope.productVersion);
    });

});

angular.module("ECourseApp").controller('ECourseLessonController', function ($scope, $http, $rootScope, $routeParams) {
    $scope.lessonId = $routeParams.lessonId;
    console.log('started Happy Brain Science ECourse lesson controller', $scope);
    delete $scope.errorMessage;
    let userLocale = 'en-US';
    if (navigator.language) {
        userLocale = navigator.language;
        console.log('detected user preferred locale in browser ' + userLocale);
    }
    $http({
        method: 'GET',
        url: 'resources/ecourse/captions'
    }).then(function (response) {
        $scope.captions = response.data;
    });

    $http({
        method: 'GET',
        url: 'resources/ecourse/videos'
    }).then(function (response) {
        $scope.videos = response.data;
        $scope.videoURL = 'videos/' + $scope.videos[$scope.lessonId] + "/720p/index.m3u8";
        console.log('video URL ', $scope.videoURL);
        let player = videojs(document.getElementById('training-video-720p'));
        player.src({
            src: $scope.videoURL
        });
        console.log('updated player src');
    });

    $http({
        method: 'GET',
        url: 'resources/ecourse/version'
    }).then(function (response) {
        $scope.productVersion = response.data.version;
        console.log('version ' + $scope.productVersion);
    });

});
