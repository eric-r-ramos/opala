(function() {
    'use strict';

    angular
        .module('opalaApp')
        .controller('VeiculoController', VeiculoController);

    VeiculoController.$inject = ['$scope', '$state', 'Veiculo', 'VeiculoSearch'];

    function VeiculoController ($scope, $state, Veiculo, VeiculoSearch) {
        var vm = this;
        
        vm.veiculos = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Veiculo.query(function(result) {
                vm.veiculos = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            VeiculoSearch.query({query: vm.searchQuery}, function(result) {
                vm.veiculos = result;
            });
        }    }
})();
