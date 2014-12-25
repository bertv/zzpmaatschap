window._skel_config = {
    prefix: '../css/h'
};

function AdminContrl($scope, $companyservice, $accountservice) {
    $scope.alerts = [

    ];
    $scope.addAlert = function (message, type) {
        $scope.alerts.length = 0;
        $scope.alerts.push({type: type, msg: message});
    };

    $scope.closeAlert = function (index) {
        $scope.alerts.splice(index, 1);
    };

    $scope.getRoles = function () {
        return $accountservice.roles([], function () {
        }, function () {
        });
    }
    $scope.getCompanies = function (callBackFunction) {
        return $companyservice.query([], function () {
            callBackFunction();
        }, function () {
        });
    }

    $scope.getCAccounts = function (callBackFunction) {
        return $accountservice.allCAccounts([], function () {
            callBackFunction();
        }, function () {
            $scope.addAlert('Er is een probleem met het ophalen van de gebruikers. ', 'error')
        });
    }
    $scope.caccounts = {};
    $scope.setCAccounts = function (callBackFunction) {
        $scope.caccounts = $scope.getCAccounts(callBackFunction);
    }
    $scope.merge = function (user) {
        $accountservice.save(user, function () {
        }, function () {
        });
    }
    $scope.roles = {};
    $scope.companies = {};
    $scope.getAll = function () {
        $scope.roles = $scope.getRoles();
        var companiesLoadedFunction = function () {
            var accountLoadedFunction = function () {

            }
            $scope.caccounts = $scope.getCAccounts(accountLoadedFunction);
        }
        $scope.companies = $scope.getCompanies(companiesLoadedFunction);
    }
    $scope.reset = function (account) {
        $accountservice.forgot({email: account.email}, function () {
        }, function () {
        });
    }
    $scope.mergeCompany = function (company) {
        $companyservice.merge(company, function () {
            $scope.getAll();
        });
    }
    $scope.removeCompany = function (company) {
        $companyservice.remove({operation: 'remove'}, company, function (resp) {
            if (resp.status != "ok") {
                alert(resp.status);
            }
            $scope.getAll();
        }, function (resp) {
        });
    }
    $scope.addCompany = function () {
        $companyservice.save({name: 'bedrijfsnaam'}, function () {
            $scope.getAll();
        }, function () {
        });
    }
}

AdminContrl.$inject = ['$scope', '$companyservice', '$accountservice'];






