(function() {
    'use strict';

    angular
        .module('opalaApp')
        .controller('SolicitanteController', SolicitanteController);

    SolicitanteController.$inject = ['$scope', '$state', 'Solicitante', 'SolicitanteSearch'];

    function SolicitanteController ($scope, $state, Solicitante, SolicitanteSearch) {
        var vm = this;
        
        vm.solicitantes = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Solicitante.query(function(result) {
                vm.solicitantes = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            SolicitanteSearch.query({query: vm.searchQuery}, function(result) {
                vm.solicitantes = result;
            });
        }    }
})();
