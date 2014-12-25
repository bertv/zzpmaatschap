function PriceListContrl($scope, $pricelistservice) {
    $scope.alerts = [

    ];



    $scope.searchname = "";
    $scope.pricelistcounts=[];
    $scope.getAllPriceLists = function () {
        var pricelists= $pricelistservice.query([], function () {

            for (var i=0;i<pricelists.length;i++){
                $scope.pricelistcounts[pricelists[i].id]=$scope.count(pricelists[i]);
            }
        }, function () {

        });

        return pricelists;
    }
    $scope.pricelists = $scope.getAllPriceLists();
    $scope.remove = function (pricelist) {
        $pricelistservice.remove({pricelist: pricelist.id}, function () {
            $scope.pricelists = $scope.getAllPriceLists();
        }, function () {
        });
    }
    $scope.count = function (pricelist) {
        return $pricelistservice.count({pricelist: pricelist.id}, function () {

        }, function () {
        });
    }
    $scope.searchitems = [];
    $scope.search = function (name) {
        var name = '%' + name + '%';
        $scope.searchitems = $pricelistservice.search({name: name, pricelist: 1}, function () {
        }, function () {
        });
    }
    $scope.pricelistselect = [];

    $scope.selectAndReturn = function (searchitem) {

        if ($scope.$parent.okPriceList != undefined) {
            $scope.$parent.okPriceList(searchitem);
        }
    }

}

PriceListContrl.$inject = ['$scope', '$pricelistservice'];







