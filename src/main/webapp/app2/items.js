zzp.filter('getById', function () {
    return function (input, id) {

        var i = 0, len = input.length;
        for (; i < len; i++) {

            if (input[i].description == id.description) {

                return input[i];
            }
        }
        return null;
    }
});
zzp.filter('numberFixedLen', function () {
    return function (a, b) {
        return(1e4 + a + "").slice(-b)
    }
});

zzp.directive('checkEmailOnBlur', function () {
    var EMAIL_REGX = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}$/;
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function (scope, elm, attr, ctrl) {

            if (attr.type === 'radio' || attr.type === 'checkbox') return;
            elm.unbind('input').unbind('keydown').unbind('change');

            elm.bind('blur', function () {
                scope.$apply(function () {
                    if (EMAIL_REGX.test(elm.val())) {
                        ctrl.$setValidity('emails', true);
                    } else {
                        ctrl.$setValidity('emails', false);
                    }
                });
            });
        }
    };

});
var ModalPriceListInstanceCtrl = function ($scope, $modalInstance, items, materialtypes) {
    $scope.levenshteinDistance = function (a, b) {
        var cost;

        // get values

        var m = a.length;


        var n = b.length;

        // make sure a.length >= b.length to use O(min(n,m)) space, whatever that is
        if (m < n) {
            var c = a;
            a = b;
            b = c;
            var o = m;
            m = n;
            n = o;
        }

        var r = new Array();
        r[0] = new Array();
        for (var c = 0; c < n + 1; c++) {
            r[0][c] = c;
        }

        for (var i = 1; i < m + 1; i++) {
            r[i] = new Array();
            r[i][0] = i;
            for (var j = 1; j < n + 1; j++) {
                cost = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;
                r[i][j] = $scope.minimator(r[i - 1][j] + 1, r[i][j - 1] + 1, r[i - 1][j - 1] + cost);
            }
        }

        return r[m][n];
    };
    $scope.minimator = function (x, y, z) {
        if (x < y && x < z) return x;
        if (y < x && y < z) return y;
        return z;
    };


    $modalInstance.materialtypes = materialtypes;
    $modalInstance.items = items;
    $modalInstance.selected = {
        item: null
    };

    $scope.okPriceList = function (searchitem) {

        $modalInstance.offeritem.materials[0].name = searchitem.name + '(' + searchitem.itemNumber + ')';
        $modalInstance.offeritem.materials[0].price = searchitem.price;
        var oldDistance = 100;
        for (var i = 0; i < $modalInstance.materialtypes.length; i++) {
            var distance = $scope.levenshteinDistance($modalInstance.materialtypes[i].name, searchitem.unit);
            distance = distance - ($modalInstance.materialtypes[i].name.charAt(4).toLowerCase() == (searchitem.unit).charAt(0).toLowerCase() ? 4 : 0);
            distance = distance - ($modalInstance.materialtypes[i].name.charAt(5).toLowerCase() == (searchitem.unit).charAt(1).toLowerCase() ? 3 : 0);
            if (distance < oldDistance) {
                $modalInstance.offeritem.materials[0].type = $modalInstance.materialtypes[i];
                oldDistance = distance;
            }
        }

        $modalInstance.close();
    };

    $scope.cancelPriceList = function () {
        $modalInstance.dismiss('cancel');
    };
};
var ModalInstanceCtrl = function ($scope, $modalInstance, items) {
    $scope.items = items;
    $scope.selected = {
        item: $scope.items[0]
    };

    $scope.ok = function () {
        $modalInstance.close($scope.selected.item);
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
};

var ModalConfirmationInstanceCtrl = function ($scope, $modalInstance) {


    $scope.ok = function () {
        $modalInstance.close();
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
};
function ItemContrl($scope, $itemservice, $categoryservice, $filter, $modal, $log, $tariffservice,$modelConfirmation) {

    $scope.itemnew = {};
    $scope.worktariffs = {};
    $scope.items = {};
    $scope.allitems = {};
    $scope.waiting=false;
    $scope.changeWorkMinutes = function (item, hours, minutes) {
        if (item.works[0] != undefined && hours != undefined && minutes != undefined) {


            item.works[0].workMinutes = (hours * 60) + minutes; //$scope.$eval(""+$scope.workDate+" | date:'mm'");

        }
        return minutes;
    }


    $scope.initWorkMinutes = function (item) {
        if (item.works[0] != undefined) {
            return (item.works[0].workMinutes % 60);
        }
        return 0;
    }
    $scope.initWorkHours = function (item) {
        if (item.works[0] != undefined) {
            return Math.floor(item.works[0].workMinutes / 60);
        }
        return 0;
    }
    $scope.showComment=false;
    $scope.openConfirmation = function (callbackYes, callbackNo) {

        var modalInstance = $modal.open({
            templateUrl: 'itemDeleteConfirmation.html',
            controller: ModalConfirmationInstanceCtrl,
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

    $scope.advancedModus = false;
    $scope.advancedModusToggle = function () {
        $scope.advancedModus = !$scope.advancedModus;
    }
    $scope.openPriceListSearch = function (item) {
        var modalInstance = $modal.open({
            templateUrl: 'pricelistsearch',
            controller: ModalPriceListInstanceCtrl,

            resolve: {
                items: function () {
                    return $scope.items;
                },
                materialtypes: function () {
                    return $scope.materialtypes;
                }

            }
        });
        modalInstance.offeritem = item;
        modalInstance.result.then(function () {

        }, function () {

        });
    }
    $scope.open = function (callbackYes, callbackNo) {

        var modalInstance = $modal.open({
            templateUrl: 'myModalContent.html',
            controller: ModalInstanceCtrl,
            resolve: {
                items: function () {
                    return $scope.items;
                }

            }
        });
        modalInstance.callbackYes = callbackYes;
        modalInstance.callbackNo = callbackNo;
        modalInstance.result.then(function (selectedItem) {

            callbackYes();
        }, function () {
            callbackNo();

        });
    };

    $scope.load = function () {
        $scope.items = $scope.loadItems();
        $scope.allitems=$scope.loadAll();
        $scope.categories = $scope.allCategories(function () {
            $scope.worktariffs = $scope.getAllWorkTariffs(function () {
                $scope.itemnew = $scope.add();
            });

        });


    }
    $scope.allCategories = function (successFunction) {
        return $categoryservice.query({}, function (response) {

            successFunction();
        });
    }
    $scope.categories = $scope.allCategories(function () {
    });
//    $scope.checkCategory = function (value) {
//        var found = $filter('getById')($scope.categories, value.category);
//        $log.info(found);
//        if (found == null) {
//            $scope.open();
//        }
//    };


    $scope.loadItems = function () {
        var offer = $scope.getSelectedOffer();
        var items;
        if (offer != undefined) {

            items = $itemservice.query({offerid: offer.id}, function () {

            }, function () {
                $scope.$parent.selectTheOffer(undefined);

            });

        }
        return items;
    }

    $scope.materialtypes = [
        {id: 1, name: 'stuk'},
        {id: 2, name: 'm'},
        {id: 3, name: 'm2'},
        {id: 4, name: 'm3'},
        {id: 5, name: 'cm'},
        {id: 6, name: 'cm2'},
        {id: 7, name: 'cm3'},
        {id: 8, name: 'mm'},
        {id: 9, name: 'doos'},
        {id: 10, name: 'plaat'},
        {id: 11, name: 'emmer'},
        {id: 12, name: 'kan'},
        {id: 13, name: 'pak'},
        {id: 14, name: 'fles'},
        {id: 15, name: 'rol'},
        {id: 16, name: 'zak'},
        {id: 17, name: 'flacon'},
        {id: 18, name: 'bus'}
    ];
    $scope.loadAll = function () {
        return $itemservice.query([], function () {

        }, function () {

        });

    }
    $scope.getAllWorkTariffs = function (succesFunction) {
        return $tariffservice.query({}, function (response) {
            succesFunction();
        });

    }


    $scope.removeFromItems = function (item) {
        $scope.items.splice($scope.items.indexOf(item), 1);
    }
    $scope.add = function () {

        var newItem = new $itemservice({description: ''});
        newItem.offer = $scope.getSelectedOffer();
        var workTariff30 = {};
        if ($scope.worktariffs.length == 0) {
            $scope.worktariffs = $scope.getAllWorkTariffs();
        }
        for (var i = 0; i < $scope.worktariffs.length; i++) {
            if ($scope.worktariffs[i].value == "30") {
                workTariff30 = $scope.worktariffs[i];
            }
        }
        ;
        newItem.works = [
            {'workMinutes': 0.0, 'tariff': workTariff30}
        ];

        newItem.materials = [
            {quantity: 1}
        ];
        var itemnew = [];
        itemnew.push(newItem);
        return itemnew;
    }

    $scope.remove = function (item) {


        $scope.openConfirmation(function(){


            if (item.id != null) {
                item.$delete([], function () {
                    $scope.addAlert('Succesvol verwijderd.', 'success');
                    $scope.removeFromItems(item);
                }, function () {
                    $scope.addAlert('Er is een fout opgetreden.', 'error');
                    $scope.load();
                });
            } else {
                $scope.items.splice($scope.items.indexOf(item), 1);
            }

        },function(){})


    }
    $scope.alreadyExisting = function (newCat, searchitems) {
        if ($scope.alreadyExistingWithReturn(newCat, searchitems) != null) {
            return true;
        }
        return false;

    }

    $scope.alreadyExistingWithReturn = function (newCat, searchitems) {
        var cat;
        for (var i = 0; i < searchitems.length; i++) {
            cat = (searchitems[i]);

            if (cat.description == newCat.description) {
                return cat;
            }
        }

        return null;

    }
    $scope.mergeCat = function (item, continueFunction) {

        var callbackYes = function () {

            var attachedCat = $categoryservice.save(item.category, function () {
                continueFunction(attachedCat);
            });
        }
        var callbackNo = function () {

            item.category.description = '';
        }
        if (!$scope.alreadyExisting(item.category, $scope.categories)) {

            item.category.id = null;

            $scope.open(callbackYes, callbackNo);

        } else {

            var alreadyExisting = $scope.alreadyExistingWithReturn(item.category, $scope.categories);

            if (alreadyExisting!= item.category){
            angular.copy(alreadyExisting, item.category);
            }
            callbackYes();

        }
        $scope.categories = $scope.allCategories(function () {
        });

    }
    $scope.workminutes = [];
    $scope.workhours = [];
    $scope.calculateTotalsWorkHours = function (iitems) {
        var total = 0;
        for (var item = 0; item < iitems.length; item++) {
            var value = $scope.calculateTotalWorkHours(iitems[item]);

            if (value != null) {
                total = total + value;
            }
        }
        return total;
    }
    $scope.calculateTotalsWork = function (iitems) {
        var total = 0;
        for (var item = 0; item < iitems.length; item++) {
            var value = $scope.calculateTotalWork(iitems[item]);

            if (value != null) {
                total = total + value;
            }
        }
        return "" + total;
    }
    $scope.calculateTotalWorkHours = function (item) {
        var result=$scope.calculateTotalWork(item);
        if (result==0){
            return 0;
        }
        return Math.floor(result / 60);
    }
    $scope.calculateTotalWork = function (item) {
        var total = 0;
        for (var i = 0; i < item.works.length; i++) {
            total = total + item.works[i].workMinutes;
        }
        if (isNaN(window.Math.round(total * item.size))){
            return 0;
        }
        return window.Math.round(total * item.size);
    }
    $scope.calculateWorks = function (work) {
        return (work.tariff.value * (work.workMinutes / 60));
    }
    $scope.calculateMaterials = function (material) {
        return (material.quantity * material.price);
    }
    $scope.calculateTotal = function (item) {
        var totalWork = 0;
        var totalMaterial = 0;
        for (var i = 0; i < item.works.length; i++) {
            totalWork = totalWork + $scope.calculateWorks(item.works[i]);
        }
        for (i = 0; i < item.materials.length; i++) {
            totalMaterial = totalMaterial + $scope.calculateMaterials(item.materials[i]);
        }
        return (totalWork + totalMaterial) * item.size;
    }
    $scope.calculateTotals = function (iitems) {
        var total = 0;
        for (var item = 0; item < iitems.length; item++) {
            var value = $scope.calculateTotal(iitems[item]);

            if (value != null) {
                total = total + value;
            }
        }
        return "" + total;
    }
    $scope.getTariff = function (tariff) {
        return '€' + tariff.value + '';

    }

    $scope.merge = function (item) {
        item.offer = $scope.getSelectedOffer();

        var continueFunction = function (attachedCat) {
            item.category = attachedCat;
            if (item.id == undefined) {
                $scope.save(item);
            } else {
                item.$merge([], function () {
                    $scope.categories = $scope.allCategories(function () {
                        $scope.toggleEditMode(item);
                        $scope.unsetDirty(item);
                        $scope.addAlert("De wijzigingen zijn opgeslagen.", 'success');
                    });

                }, function () {
                    $scope.addAlert("Er is een fout opgetreden. De wijzigingen zijn niet opgeslagen.", 'error');
                });
            }

        }
        $scope.mergeCat(item, continueFunction);

    };
    $scope.terugrekenwaarde=0;
    $scope.copy = function (item) {
        var fromAll = $scope.alreadyExistingWithReturn(item, $scope.allitems);
        if (fromAll != null) {
            var oldId = item.id;
            var oldWorkId = item.works[0].id;
            var oldMaterialId = item.materials[0].id;

            angular.copy(fromAll, item);
            item.id = oldId;
            item.offer = $scope.getSelectedOffer();

            item.works[0].id = oldWorkId;
            item.materials[0].id = oldMaterialId;
            item.popularity = 0;
            $scope.workhours[item.id] = $scope.initWorkHours(item);
            $scope.workminutes[item.id] = $scope.initWorkMinutes(item);
            fromAll.popularity = fromAll.popularity + 1;
            fromAll.$save();

        }

    }
    $scope.changeSize=function(item){
        var totalNorm=$scope.calculateTotal(item)/item.size;

        var size=Math.round(item.terugrekenwaarde/totalNorm);
        if (!(size<=0)){
            item.size=size;
            $scope.setDirty(item);
        }
    }

    $scope.saveFromItems = function (item) {
        $scope.items.push(item);
    }
    $scope.save = function (newItem) {

        var returnedItem = newItem.$save([], function (res) {

            $scope.saveFromItems(res);
            $scope.itemnew = $scope.add();
            $scope.addAlert("Het item is toegevoegd.", 'success');
        }, function () {
            $scope.addAlert("Er is een fout opgetreden. De wijzigingen zijn niet opgeslagen.", 'error');
            $scope.load();
        });

    };
    $scope.up = function(item){
        $scope.waiting=true;
        var indexCurrent = $scope.items.indexOf(item);
        var prevItem;

        if (indexCurrent>0){
            prevItem=$scope.items[indexCurrent-1];
            var orderNumber=item.orderNumber;
            item.orderNumber=prevItem.orderNumber;
            prevItem.orderNumber=orderNumber;
            $scope.setDirty(prevItem);
            $scope.setDirty(item);

        }
        $scope.items.sort(function (a,b){return (parseInt(a.orderNumber)+parseInt(b.id))-(parseInt(b.orderNumber)+parseInt(b.id))});


            var returnedItem = item.$save([], function (res) {
                if(prevItem!=undefined){
                prevItem.$save([], function (res) {
                    $scope.waiting=false;
                }, function () {
                    $scope.addAlert("Er is een fout opgetreden. De wijzigingen zijn niet opgeslagen.", 'error');
                    $scope.load();
                });
                }else{
                $scope.waiting=false;}
            }, function () {

                $scope.addAlert("Er is een fout opgetreden. De wijzigingen zijn niet opgeslagen.", 'error');
                $scope.load();
            });



    }
    $scope.editmode = [];
    $scope.toggleEditMode = function (item) {

        if ($scope.editmode[item.id] == undefined) {
            $scope.editmode[item.id] = {edit: false, dirty: false};
        }
        var edit = !$scope.editmode[item.id].edit;
        $scope.editmode.forEach(function (entry) {

            entry.edit = false;

        });
        $scope.editmode[item.id].edit = edit;
    }
    $scope.getEditMode = function (item) {
        if ($scope.editmode[item.id] == undefined) {
            return false;
        }
        return $scope.editmode[item.id].edit;
    };
    $scope.isDirty = function (item) {
        if ($scope.editmode[item.id] == undefined) {
            return false;
        }
        return $scope.editmode[item.id].dirty;
    }
    $scope.setDirty = function (item) {
        if ($scope.editmode[item.id] == undefined) {
            $scope.editmode[item.id] = {dirty: true};
        } else {
            $scope.editmode[item.id].dirty = true;
        }
    };
    $scope.unsetDirty = function (item) {
        if ($scope.editmode[item.id] == undefined) {
            $scope.editmode[item.id] = {dirty: false};
        } else {
            $scope.editmode[item.id].dirty = false;
        }
    };
    $scope.isNull = function (obj) {
        if (obj == undefined || obj == null) {
            return true;
        } else {

            if (obj === "") {
                return true;
            }
            if (obj == 0) {
                return false;
            }
        }
        return false;
    }
    $scope.isValid = function (item) {
        if ($scope.isNull(item.description)) {
            return false;
        }
        if (item.category!=undefined){
            if ($scope.isNull(item.category.description)) {
                return false;
            }
        }

        if ($scope.isNull(item.size)) {
            return false;
        }
        if ($scope.isNull(item.works[0].workMinutes)) {
            return false;
        }
        if ($scope.isNull(item.works[0].tariff)) {
            return false;
        }
        if ($scope.isNull(item.materials[0].price)) {
            return false;
        }
        if ($scope.isNull(item.materials[0].type)) {
            return false;
        }
        if ($scope.isNull(item.materials[0].type.name)) {
            return false;
        }
        return true;
    };
    $scope.alerts = [

    ];

    $scope.addAlert = function (message, type) {
        //$scope.alerts.length=0;
        $scope.alerts = [
            {type: type, msg: message}
        ];
    };

    $scope.closeAlert = function (index) {
        $scope.alerts.splice(index, 1);
    };
    $scope.makenew =function(item){
        item.size=1;
    }
}
;

ItemContrl.$inject = ['$scope', '$itemservice', '$categoryservice', '$filter', '$modal', '$log', '$tariffservice'];

zzp.controller( 'ItemCtrl',ItemContrl);
