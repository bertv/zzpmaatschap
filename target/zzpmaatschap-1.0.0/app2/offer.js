function OfferContrl($scope, $offerservice,$modal) {
    $scope.alerts = [

    ];
    $scope.waiting=true;
    $scope.offernew = {};
    $scope.addAlert = function (message, type) {
        $scope.alerts.length = 0;
        $scope.alerts.push({type: type, msg: message});
    };

    $scope.closeAlert = function (index) {
        $scope.alerts.splice(index, 1);
    };
    var ModalInstanceCtrl = function ($scope, $modalInstance) {


        $scope.ok = function () {
            $modalInstance.close();
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    };

    $scope.open = function (callbackYes, callbackNo) {

        var modalInstance = $modal.open({
            templateUrl: 'offerDeleteConfirmation.html',
            controller: ModalInstanceCtrl,
            resolve: {

            }
        });
        modalInstance.callbackYes = callbackYes;
        modalInstance.callbackNo = callbackNo;
        modalInstance.result.then(function () {

            callbackYes();
        }, function () {
            callbackNo();

        });
    };

    $scope.clear = function (new_offer) {
        $scope.offernew={id:null,name:null};

    }
    $scope.save = function (offername) {
        var offer = new $offerservice();
        angular.copy($scope.offernew, offer);
        offer.id = null;
        $offerservice.save(offer, function () {
            $scope.addAlert("Succesvol toegevoegd.", 'success');

            $scope.load();

        }, function (info) {
            $scope.addAlert("Er is een fout opgetreden. Mogelijk bestaat de offertenaam al. De wijzigingen zijn niet opgeslagen.", 'error');
        });

    }
    $scope.selectOffer = function (offer) {
        $scope.$parent.selectTheOffer(offer);
        $scope.offernew = {};

        var newOffer = new $offerservice();
        angular.copy(offer, newOffer);
        $scope.offernew=newOffer;

    }
    $scope.disableMe = function (offer) {
        if ($scope.$parent.selectedoffer == undefined) {
            return false;
        }
        return $scope.$parent.selectedoffer.id == offer.id;
    }

    $scope.validate = function (value) {
        if (value == undefined) {
            return false;
        }
        return !(value.length > 200);
    }

    $scope.loading = function () {
        var offers = $offerservice.query([], function () {
            $scope.waiting=false;
            var selectedOffer = $scope.$parent.getSelectedOffer();
            if (selectedOffer != undefined) {
                for (var i = 0; i < offers.length; i++) {

                    if (selectedOffer.id == offers[i].id) {
                        $scope.selectOffer(offers[i]);

                        break;
                    }
                }
            }

        },function(){
            $scope.addAlert("Er is een fout opgetreden bij het laden van offers.", 'error');
            $scope.waiting=false;
        });


        return offers;
    }
    $scope.load = function () {
        $scope.offers = $scope.loading();

    }
    $scope.offers = $scope.loading();
    $scope.merge = function (offernew) {

        var theOffer=offernew;

        if (theOffer != undefined && theOffer.id != null) {
            $offerservice.save(theOffer, function () {

                $scope.load();
                $scope.addAlert("Succesvol offerte opgeslagen.", 'success');
            }, function () {
                $scope.addAlert("Er is een fout opgetreden tijdens het opslaan. Mogelijk bestaat de offertenaam al.", 'error');
            });
        }
    }
    $scope.copy = function (offer) {

        offer.$copy({operation: 'copy'}, function () {
            $scope.load();
            $scope.addAlert('De offerte is succesvol gekopiëerd.', 'success');
        }, function () {
            $scope.addAlert("Er is een fout opgetreden bij het kopieren. De wijzigingen zijn niet opgeslagen.", 'error');
        });
    }
    $scope.remove = function (offer) {
        $scope.open(function(){

            $offerservice.delete(offer, function () {
                $scope.addAlert('De offerte is verwijderd', 'success')+
                $scope.load();
            }, function (info) {
                $scope.addAlert("Er is een fout opgetreden. De wijzigingen zijn niet opgeslagen.", 'error');
            });

        },function(){});


    }
    $scope.checkSelectedOffer = function () {
        return $scope.$parent.selectedoffer == undefined;
    }
    $scope.checkUniquenessWhenCopy = function (offerToCheck) {

        if (offerToCheck == undefined) {
            return false;
        }
        for (var i = 0; i < $scope.offers.length; i++) {

            if (((offerToCheck.name+'(kopie)') == $scope.offers[i].name)) {

                return true;
            }
        }
        return false;
    }
    $scope.checkUniqueness = function (offerToCheck) {

        var offer = offerToCheck;
        if (offer == undefined) {
            return false;
        }
        for (var i = 0; i < $scope.offers.length; i++) {

            if ((offer.name == $scope.offers[i].name)) {

                return true;
            }
        }
        return false;
    }


}

OfferContrl.$inject = ['$scope', '$offerservice','$modal'];






