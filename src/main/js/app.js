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
            .when('/unauthorized', {
                templateUrl: 'pages/unauthorized.html',
                controller: 'ECourseUnauthorizedController'
            })
            .when('/lesson/:lessonId', {
                templateUrl: 'pages/lesson.html',
                controller: 'ECourseLessonController'
            })
            ;
});

angular.module("ECourseApp").controller('GoogleSignonController', function ($scope, $http, $rootScope, $cookies) {
    console.log('google signon controller started');
    let sessionToken = $cookies.get('happybrainscience-thrive9to5');
    if(sessionToken) {
        $cookies.remove('happybrainscience-thrive9to5');
    }
    $scope.googleSignon = function (credentials) {
        console.log('google onSignIn', credentials);        
        $http({
            method: 'POST',
            data: credentials,
            url: 'resources/ecourse/credential'
        }).then(function (response) {            
            console.log('posted google credentials OK', response);
            let sessionToken = response.data.sessionToken;
            if(sessionToken) {
                $cookies.put('happybrainscience-thrive9to5', sessionToken, {'path': '/'});
                window.location.href = '/';
            } else {
                window.location.href = '/unauthorized';
            }
        }, function(errorResponse) {
            window.location.href = '/unauthorized';
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

angular.module("ECourseApp").controller('ECourseUnauthorizedController', function ($scope, $http, $rootScope) {

    $http({
        method: 'GET',
        url: 'resources/ecourse/version'
    }).then(function (response) {
        $scope.productVersion = response.data.version;
        console.log('version ' + $scope.productVersion);
    });
    
    $http({
        method: 'GET',
        url: 'resources/ecourse/captions'
    }).then(function (response) {
        $scope.captions = response.data;
        console.log('captions loaded');
    });
    

});

angular.module("ECourseApp").controller('ECourseHomeController', function ($scope, $http, $rootScope, $cookies) {
    console.log('started Happy Brain Science ECourse home controller');
    // no cookie? go to login
    let sessionToken = $cookies.get('happybrainscience-thrive9to5');
    console.log('session token', sessionToken);
    if(!sessionToken) {
        window.location.href = '/#!/login';
    } else {
        document.getElementById("google-signon-block").style.display = 'none';
    }
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

angular.module("ECourseApp").controller('ECourseLessonController', function ($scope, $http, $rootScope, $routeParams, $cookies) {
    
    let sessionToken = $cookies.get('happybrainscience-thrive9to5');
    console.log('session token', sessionToken);
    if(!sessionToken) {
        window.location.href = '/#!/login';
    } else {
        document.getElementById("google-signon-block").style.display = 'none';
    }
    
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
