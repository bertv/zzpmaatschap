
function AccountContrl($scope, $accountservice) {
    $scope.alerts = [

    ];
    $scope.addAlert = function(message,type) {
        $scope.alerts.length=0;
        $scope.alerts.push({type:type,msg: message});
    };

    $scope.closeAlert = function(index) {
        $scope.alerts.splice(index, 1);
    };

    $scope.getAccount = function(){
        return $accountservice.query();
    };
    $scope.account = $scope.getAccount();
    $scope.loadAll = function(){
        $scope.account=$scope.getAccount();
    }
    $scope.selectCompany = function(company){
        $accountservice.selectCompany(company,function(){$scope.loadAll();},function(){});
    }

}

AccountContrl.$inject = ['$scope', '$accountservice'];






