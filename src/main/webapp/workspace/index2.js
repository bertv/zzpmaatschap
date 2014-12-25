
 var plunker=angular.module('plunker', ['ui.bootstrap','ngResource']);

function ItemContrl($scope){
    $scope.items=[
        {id:1,description:'Ontwerp tekening maken',category:'constructie',workitems:[{id:70},{id:60}]}
        ,
        {id:2,description:'Elektrische installatie lampen',category:'woonkamer inrichting',workitems:[{id:20},{id:30}]}
        ,
        {id:3,description:'Douchecabine installeren',category:'badkamer reparaties',workitems:[{id:20},{id:30}]}
    ];
    $scope.myFunc = function(value){
    };

};





plunker.factory('Offer', ['$resource', function($resource) {

	return $resource( '/zzpmaatschap/resteasy/offers/:id',
		{ id: '@id' }, { 
			loan: { 
				method: 'GET', 
				params: { id: '@id' }, 
				isArray: false 
			} 
			/* , method2: { ... } */
		} );

}]);




plunker.factory('Translate', function($resource) {
  return $resource('https://www.googleapis.com/language/translate/v2?q=:q&target=EN&source=NL&key=AIzaSyBFR1jOk9Erkio750EHljHlades_Efjv9g',
    { q:'hoi' },
    { update: { method: 'GET' }}
  );
});

  


plunker.directive('ngBlur', ['$parse', function($parse) {
  return function(scope, element, attr) {
    var fn = $parse(attr['ngBlur']);
    element.bind('blur', function(event) {
      scope.$apply(function() {
        fn(scope, {$event:event});
      });
    });
  }
}]);

