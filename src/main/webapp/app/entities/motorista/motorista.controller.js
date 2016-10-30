(function() {
    'use strict';

    angular
        .module('opalaApp')
        .controller('MotoristaController', MotoristaController);

    MotoristaController.$inject = ['$scope', '$state', 'Motorista', 'MotoristaSearch'];

    function MotoristaController ($scope, $state, Motorista, MotoristaSearch) {
        var vm = this;
        
        vm.motoristas = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Motorista.query(function(result) {
                vm.motoristas = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            MotoristaSearch.query({query: vm.searchQuery}, function(result) {
                vm.motoristas = result;
            });
        }    }
})();
