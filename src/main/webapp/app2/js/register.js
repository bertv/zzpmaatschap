function RegisterContrl($scope, $accountservice) {
    $scope.alerts = [

    ];
    $scope.successfull=false;
    $scope.addAlert = function (message, type) {
        $scope.alerts.length=0;
        $scope.alerts.push({type: type, msg: message});
    };

    $scope.closeAlert = function (index) {
        $scope.alerts.splice(index, 1);
    };
    $scope.user = {};
    $scope.send = function () {
        $accountservice.register($scope.user, function (resp) {

            if (resp.status=="ok"){
                $scope.addAlert('Bedankt voor uw registratie. U ontvangt een email voor de bevestiging.','success');
                $scope.successfull=true;
            }
            if (resp.status=="not_valid"){
                $scope.addAlert('Foutreden: '+resp.message,'warning');
            }
        }, function () {
            $scope.addAlert('Er is een fout opgetreden. Excuses voor het ongemak.','error');
        });

    }

}

RegisterContrl.$inject = ['$scope', '$accountservice'];






