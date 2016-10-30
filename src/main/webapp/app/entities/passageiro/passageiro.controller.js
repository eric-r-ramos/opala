(function() {
    'use strict';

    angular
        .module('opalaApp')
        .controller('PassageiroController', PassageiroController);

    PassageiroController.$inject = ['$scope', '$state', 'Passageiro', 'PassageiroSearch'];

    function PassageiroController ($scope, $state, Passageiro, PassageiroSearch) {
        var vm = this;
        
        vm.passageiros = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Passageiro.query(function(result) {
                vm.passageiros = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            PassageiroSearch.query({query: vm.searchQuery}, function(result) {
                vm.passageiros = result;
            });
        }    }
})();
