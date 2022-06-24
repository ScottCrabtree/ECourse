/* global angular */

angular.module("TheHappinessTestApp", []);

angular.module("TheHappinessTestApp").controller('HappinessTestController', function ($scope, $http, $rootScope) {
    console.log('started TheHappinessTest user controller');
    delete $scope.errorMessage;
    let userLocale = 'en-US';
    if (navigator.language) {
        userLocale = navigator.language;
        console.log('detected user preferred locale in browser ' + userLocale);
    }
    $http({
        method: 'GET',
        url: 'resources/happinesstest/captions'
    }).then(function (response) {
        $scope.captions = response.data;
        console.log('captions loaded');
    });

    $scope.userAnswers = [];
    for (let i = 0; i < 4; i++) {
        $scope.userAnswers.push(undefined);
    }

    $http({
        method: 'GET',
        url: 'resources/happinesstest/version'
    }).then(function (response) {
        $scope.productVersion = response.data.version;
        console.log('version ' + $scope.productVersion);
    });

    $scope.images = [
        {imagePath: 'assets/istock_manhandsinpockets.png',
            imageAltText: 'Man hands in pockets',
            imageWidth: '200px'
        },
        {imagePath: 'assets/free_landscape.png',
            imageAltText: 'Landscape',
            imageWidth: '400px'
        },
        {imagePath: 'assets/woman_in_rain_with_dog.png',
            imageAltText: 'Boy with his dog in the rain',
            imageWidth: '400px'
        },
        {imagePath: 'assets/photo_128169777_lonely-woman.png',
            imageAltText: 'Lonely woman',
            imageWidth: '400px'
        }


    ];

    $scope.selectedUserAnswer = function() {
        console.log('change updated model answer');
    };

    $scope.updateQuestion = function (questionIndex) {
        $scope.testState = 'QUESTION';
        let questionNumber = questionIndex + 1;
        $scope.questionIndex = questionIndex;
        let indexName = 'question_' + questionNumber;
        $scope.questionText = $scope.captions[indexName];
        $scope.answers = [];
        let answerValueIndex = 1;
        let answerIndexName = 'answer_' + questionNumber + '_min';
        let answerObject = {
            answerIndex: answerValueIndex - 1,
            answerValue: answerValueIndex,
            answerText: answerValueIndex + ' : ' + $scope.captions[answerIndexName]            
        };
        $scope.answers.push(answerObject);
        for (let i = 2; i <= 6; i++) {
            answerValueIndex = i;
            answerObject = {
                answerIndex: answerValueIndex - 1,
                answerText: answerValueIndex,
                answerValue: answerValueIndex
            };
            $scope.answers.push(answerObject);
        }
        answerValueIndex = 7;
        answerIndexName = 'answer_' + questionNumber + '_max';
        answerObject = {
            answerIndex: answerValueIndex - 1,
            answerValue: answerValueIndex,
            answerText: answerValueIndex + ' : ' + $scope.captions[answerIndexName]
        };
        $scope.answers.push(answerObject);
        if ($scope.questionIndex === 4) {
            $scope.nextButtonCaption = 'Finish & Submit';
        } else {
            $scope.nextButtonCaption = 'Next';
        }
    };

    $scope.nextQuestionClick = function () {
        console.log('advance to next question');
        $scope.userAnswers[$scope.questionIndex] = $scope.selectedAnswer;
        if ($scope.questionIndex === 3) {
            $scope.questionIndex = $scope.questionIndex + 1;            
            $scope.testState = 'FINISH';
        } else {
            let questionIndex = $scope.questionIndex + 1;
            $scope.selectedAnswer = $scope.userAnswers[questionIndex];
            $scope.updateQuestion(questionIndex);
        }
    };

    $scope.previousQuestionClick = function () {        
        let questionIndex = $scope.questionIndex - 1;        
        console.log('back to previous question',questionIndex);
        $scope.selectedAnswer = $scope.userAnswers[questionIndex];
        $scope.updateQuestion(questionIndex);
    };

    $scope.submitResultsClick = function () {
        console.log('user submit results');
        $scope.testState = 'COMPLETE';
        var response = {
            emailAddress: $scope.emailAddressText,
            answers: $scope.userAnswers,
            subscribeChecked: $scope.subscribeCheck,
            emailResultsChecked: $scope.emailResults
        };
        $http({
            method: 'POST',
            url: 'resources/happinesstest/submitresults',
            data: response
        }).then(function (response) {            
            console.log('answers submitted');
        });
    };

    $scope.testState = 'START';

    $scope.startButtonClick = function () {
        console.log('user clicked start');
        $scope.updateQuestion(0);
    };

});
