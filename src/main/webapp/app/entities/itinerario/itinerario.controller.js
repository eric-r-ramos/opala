(function() {
    'use strict';

    angular
        .module('opalaApp')
        .controller('ItinerarioController', ItinerarioController);

    ItinerarioController.$inject = ['$scope', '$state', 'Itinerario', 'ItinerarioSearch'];

    function ItinerarioController ($scope, $state, Itinerario, ItinerarioSearch) {
        var vm = this;
        
        vm.itinerarios = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Itinerario.query(function(result) {
                vm.itinerarios = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ItinerarioSearch.query({query: vm.searchQuery}, function(result) {
                vm.itinerarios = result;
            });
        }    }
})();
