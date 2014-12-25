
function AccountContrl($scope, $accountservice,$location) {
    $scope.showForgotPassword=false;

    $scope.alerts = [

    ];
    $scope.offernew=undefined;
    $scope.addAlert = function(message,type) {
        $scope.alerts.length=0;
        $scope.alerts.push({type:type,msg: message});
    };

    $scope.closeAlert = function(index) {
        $scope.alerts.splice(index, 1);
    };

    $scope.forgotMessage=null;

    $scope.forgotPassword=function(mail){
        var returnMessage=$accountservice.forgot({email:mail});
        $scope.forgotMessage=returnMessage;


    }

    $scope.getStatus = function(){
        if (($location.search().status)=='failed'){
            $scope.addAlert('Wachtwoord of gebruikersnaam is niet juist.','error');
        }

    }

}

AccountContrl.$inject = ['$scope', '$accountservice','$location'];






