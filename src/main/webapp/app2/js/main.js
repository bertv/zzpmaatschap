var zzp = angular.module('zzp', ['ui.bootstrap', 'ngResource','ui.validate','ngCookies']);

function MainContrl($scope,$cookieStore,$location){



    $scope.selectedoffer={};
    $scope.selectTheOffer = function(offer){


    if (offer!=undefined && offer.id !=undefined){

        $cookieStore.put('offerid',offer.id);
        $cookieStore.put('offer',offer);
        $scope.selectedoffer=offer;
    }else{
        $cookieStore.remove('offerid');
        $cookieStore.remove('offer');
        $scope.selectedoffer=undefined;
    }

    }

    $scope.getSelectedOffer = function(){
        var offer =$cookieStore.get('offer');

        $scope.selectedoffer=offer;
        return offer;
    }
    $scope.goto = function(loc){
        location.href=loc;
    }
}

MainContrl.$inject = ['$scope', '$cookieStore','$location'];


zzp.factory('$itemservice', ['$resource', function ($resource) {

    return $resource('../resteasy/items/:operation/:offerid',
        { id: '@id' }, {
            delete: {
                method: 'DELETE',
                params: { operation: 'delete' },
                isArray: false
            },
            query: {
                method: 'GET',
                params: { operation: 'query',offerid: '@offerid' },
                isArray: true
            }
            ,
            save: {
                method: 'POST',
                params: {operation: 'merge' },
                isArray: false
            },
            merge: {
                method:'POST',
                params: { operation: 'merge' },
                isArray: false
            }


            /* , method2: { ... } */
        });

}]);
zzp.directive("intro", function(){
    return {
        restrict: "E",
        transclude: true,
        scope: {
            title: "@"
        },
        templateUrl: "intro.html",
        link: function(scope){
            scope.isContentVisible = false;
            scope.toggleContent = function(){
                scope.isContentVisible = !scope.isContentVisible;
            };
        }
}
});


zzp.factory('$offerservice', ['$resource', function ($resource) {


    return $resource('../resteasy/offers/:operation',
        { operation:'@operation' }, {
            delete: {

                method: 'POST',
                params: {operation:'delete' },
                isArray: false
            },
            copy:{
                method: 'POST',
                params: {  },
                isArray: false
            },
            save:{
                params: {operation:'save'},
                method: 'POST',
                isArray:false
            },
            tenantoffers:{
                method: 'GET',
                isArray:true
            },
            saveTenant:{
                method: 'POST',
                isArray:false
            },
            query:{
                params: { operation:'query' },
                method: 'GET',
                isArray:true
            }

            /* , method2: { ... } */
        });

}]);
zzp.factory('$offeradminservice', ['$resource', function ($resource) {

    return $resource('../resteasy/offers/:operation',
        { operation:'@operation' }, {

            tenantoffers:{
                method: 'GET',
                isArray:true
            },
            saveTenant:{
                method: 'POST',
                isArray:false
            },removeold:{
                method:'POST'

            }

        });

}]);
zzp.factory('$tariffservice', ['$resource', function ($resource) {

    return $resource('../resteasy/tariff/all',
        { id: '@id' }, {



            /* , method2: { ... } */
        });

}]);

zzp.factory('$categoryservice', ['$resource', function ($resource) {

    return $resource('../resteasy/category/:operation',
        { id: '@id' }, {
            save:{
                params:{operation:'save'},
                method: 'POST',
                isArray:false
            },
            query:{
                params:{operation:'query'},
                method: 'GET',
                isArray:true
            }
            /* , method2: { ... } */
        });

}]);



zzp.factory('$reportservice', ['$resource', function ($resource) {

    return $resource('../resteasy/report/:option',
        { option: '@id' }, {

            query: {
                method: 'GET',
                params: { option:'all',offerid: '@offerid' },
                isArray: true
            },
            saveReport: {
                method: 'POST',
                params: { option:'save',offerid: '@offerid' },
                isArray: false
            },
            deleteReport: {
                method: 'GET',
                params: { option:'delete',reportid: '@reportid' },
                isArray: false
            }

        });

}]);

zzp.factory('$uploadservice', ['$resource', function ($resource) {

    return $resource('../resteasy/upload/report',
        { }, {

            query: {
                method: 'POST',
                params: { filename: '@filename' },
                isArray: false
            }


        });

}]);

zzp.factory('$pricelistservice', ['$resource', function ($resource) {

    return $resource('../resteasy/pricelist/:option',
        { }, {
            count: {
                method: 'GET',
                params: { option: 'count',pricelist:'@pricelist' },
                isArray: false
            },
            query: {

                method: 'GET',
                params: { option: 'query' },
                isArray: true
            },
            search:{
                method: 'GET',
                params: { option: 'search',name:'@name',pricelist:'@pricelist' },
                isArray: true
            },
            delete:{

                method: 'DELETE',
                params: {option: 'delete', pricelist:'@pricelist' },
                isArray: false
            }


        });

}]);
zzp.factory('$companyservice', ['$resource', function ($resource) {

    return $resource('../resteasy/company/:operation',{ },
        {

            query: {
                method: 'GET',
                params:{operation:'all'},
                isArray: true
            },
            merge: {
                method: 'POST',
                params:{operation:'merge'},
                isArray: false
            },
            save: {
                method: 'POST',
                params:{operation:'save'},
                isArray: false
            },
            remove: {
                method: 'POST',
                params:{operation:'remove'},
                isArray: false

            }


        });

}]);
zzp.factory('$accountservice', ['$resource', function ($resource) {

    return $resource('../resteasy/account/:option',
        { }, {
            allCAccounts: {
                method: 'GET',
                params: { option: 'allcaccounts' },
                isArray: true
            },
            query: {
                method: 'GET',
                params: { option: 'user' },
                isArray: false
            },
            forgot: {
                method: 'GET',
                params: { option: 'forgot',email:'@email' },
                isArray: false
            },
            register: {
                method: 'POST',
                params: { option: 'register' },
                isArray: false
            },
            roles:{
                method: 'GET',
                params: { option: 'roles' },
                isArray: true
            },
            save:{
                method: 'POST',
                params: { option: 'save' },
                isArray: false
            },
            selectCompany:{
                method:'POST',
                params:{option:'selectCompany'},
                isArray:false
            }


        });

}]);
