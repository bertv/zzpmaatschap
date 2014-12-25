function GenerateContrl($scope, $reportservice) {
    $scope.alerts = [

    ];

    $scope.addAlert = function (message, type) {
        //$scope.alerts.length=0;
        $scope.alerts.push({type: type, msg: message});
    };

    $scope.closeAlert = function (index) {
        $scope.alerts.splice(index, 1);
    };
    $scope.offer = $scope.getSelectedOffer();

    $scope.downloadLink = function (reportfile) {
        return ('../resteasy/report/generate?reportid=' + reportfile.id);
    }
    $scope.deleteReport = function (report) {

        $reportservice.deleteReport({reportid: report.id}, function () {
            $scope.addAlert('Succesvol verwijderd. ', 'success');
            $scope.setAllReports();
        }, function () {
            $scope.addAlert('Er is een probleem met het verwijderen. ', 'error')
        });
    }
    $scope.saveReport = function (report) {

        $reportservice.saveReport(report, function () {
            $scope.addAlert('Succesvol opgeslagen. ', 'success')
        }, function () {
            $scope.addAlert('Er is een probleem met het opslaan van parameters. ', 'error')
        });
    }
    $scope.checkValueParameters = function (report) {
        var valid = false;
        report.parameters.forEach(function (par) {
            if (par.name != undefined && par.name.length > 0) {
                valid = true;
            }
        });
        return valid;
    }
    $scope.downloadReport = function (reportfile) {
        window.open('../resteasy/report/generate?reportid=' + reportfile.id, '_blank');
    }
    $scope.getAllReports = function () {
        var offer = $scope.$parent.getSelectedOffer();
        var reports = $reportservice.query({offerid: offer.id}, function () {
        }, function () {
            $scope.addAlert('Er is een probleem met het ophalen van de offertes. ', 'error')
        });

        return reports;
    }
    $scope.reports = $scope.getAllReports();
    $scope.save = function (filename) {


    }
    $scope.setAllReports = function(){
        $scope.reports = $scope.getAllReports();
    }
}

GenerateContrl.$inject = ['$scope', '$reportservice'];






