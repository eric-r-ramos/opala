(function() {
    'use strict';

    angular
        .module('opalaApp')
        .controller('ItinerarioDetailController', ItinerarioDetailController);

    ItinerarioDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Itinerario'];

    function ItinerarioDetailController($scope, $rootScope, $stateParams, previousState, entity, Itinerario) {
        var vm = this;

        vm.itinerario = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('opalaApp:itinerarioUpdate', function(event, result) {
            vm.itinerario = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
