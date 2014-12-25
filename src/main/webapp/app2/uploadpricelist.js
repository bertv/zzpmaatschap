function UploadPriceListContrl($scope, $pricelistservice) {
    $scope.alerts = [

    ];
    $scope.uploadButtonDisabled = false;
    $scope.progressVisible = false;
    $scope.addAlert = function (message, type) {
        //$scope.alerts.length=0;
        $scope.alerts.push({type: type, msg: message});
    };

    $scope.closeAlert = function (index) {
        $scope.alerts.splice(index, 1);
    };
    $scope.uploadedFile = {};
    $scope.setFile = function (file) {
        $scope.uploadedFile = file.files[0];
    }
    $scope.uploadFile = function (file) {
        $scope.uploadButtonDisabled = true;
        var fd = new FormData();
        fd.append("uploadedFile", file);

        var xhr = new XMLHttpRequest();


        xhr.upload.addEventListener("progress", uploadProgress, false);
        xhr.addEventListener("load", uploadComplete, false);
        xhr.addEventListener("error", uploadFailed, false);
        xhr.addEventListener("abort", uploadCanceled, false);
        xhr.open("POST", "../resteasy/upload/pricelist");

        xhr.send(fd)
    }
    $scope.returnActions = function () {

        $scope.$apply(function () {
            $scope.progressVisible = false;
            $scope.uploadButtonDisabled = false;
        });
    }
    function uploadProgress(evt) {
        $scope.$apply(function () {
            if (evt.lengthComputable) {
                $scope.progressVisible = true;
                $scope.progress = Math.round(evt.loaded * 100 / evt.total);
            } else {
                $scope.progress = 'unable to compute';
                $scope.progressVisible = false;
            }

        })
    }


    function uploadComplete(evt) {
        $scope.returnActions();
        /* This event is raised when the server send back a response */

        $scope.addAlert("Het bestand is succesvol geupload.", 'success');
    }

    function uploadFailed(evt) {
        $scope.returnActions();
        $scope.addAlert('Er is een fout opgetreden tijdens het uploaden van het bestand.', 'error');
    }

    function uploadCanceled(evt) {
        $scope.$apply(function () {
            $scope.progressVisible = false;
        });
        $scope.returnActions();
        $scope.addAlert("The upload has been canceled by the user or the browser dropped the connection.", 'error')
    }
}


UploadPriceListContrl.$inject = ['$scope', '$pricelistservice'];






