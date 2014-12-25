window._skel_config = {
    prefix: '../css/h'
};

function OfferAdminContrl($scope, $companyservice, $offeradminservice) {
    $scope.alerts = [

    ];
    $scope.addAlert = function (message, type) {
        $scope.alerts.length = 0;
        $scope.alerts.push({type: type, msg: message});
    };

    $scope.closeAlert = function (index) {
        $scope.alerts.splice(index, 1);
    };


    $scope.getCompanies = function (callBackFunction) {
        return $companyservice.query([], function () {

        }, function () {
        });
    }

    $scope.getAllOffers = function (callBackFunction) {
        var offers = $offeradminservice.tenantoffers({operation: 'tenant'}, function () {

        }, function () {
        });

        return offers;
    }
    $scope.roles = {};
    $scope.companies = {};
    $scope.getAll = function () {

        $scope.companies = $scope.getCompanies();
        $scope.offers = $scope.getAllOffers();
    }
    $scope.save = function(offer){
        $offeradminservice.saveTenant({operation:'saveTenant'},offer,function(){$scope.addAlert('Wijzigingen zijn opgeslagen.','success');},function(){$scope.addAlert('Er is een fout opgetreden.','error');});
    }
}

OfferAdminContrl.$inject = ['$scope', '$companyservice', '$offeradminservice'];





