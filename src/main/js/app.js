/* global angular */

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
            .when('/lesson/:lessonId', {
                templateUrl: 'pages/lesson.html',
                controller: 'ECourseLessonController'
            })
            ;    
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
    console.log('started Happy Brain Science ECourse lesson controller',$scope);
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
    });

    $http({
        method: 'GET',
        url: 'resources/ecourse/version'
    }).then(function (response) {
        $scope.productVersion = response.data.version;
        console.log('version ' + $scope.productVersion);
    });

});