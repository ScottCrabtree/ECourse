/* global angular */

angular.module("ECourseApp", []);

angular.module("ECourseApp").controller('ECourseController', function ($scope, $http, $rootScope) {
    console.log('started Happy Brain Science ECourse controller');
    delete $scope.errorMessage;
    let userLocale = 'en-US';
    if (navigator.language) {
        userLocale = navigator.language;
        console.log('detected user preferred locale in browser ' + userLocale);
    }
//    $http({
//        method: 'GET',
//        url: 'resources/ecourse/captions'
//    }).then(function (response) {
//        $scope.captions = response.data;
//        console.log('captions loaded');
//    });

    $scope.userAnswers = [];
    for (let i = 0; i < 4; i++) {
        $scope.userAnswers.push(undefined);
    }

    $http({
        method: 'GET',
        url: 'resources/ecourse/version'
    }).then(function (response) {
        $scope.productVersion = response.data.version;
        console.log('version ' + $scope.productVersion);
    });

});
