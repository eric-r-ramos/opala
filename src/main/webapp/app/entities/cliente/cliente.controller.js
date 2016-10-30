(function() {
    'use strict';

    angular
        .module('opalaApp')
        .controller('ClienteController', ClienteController);

    ClienteController.$inject = ['$scope', '$state', 'Cliente', 'ClienteSearch'];

    function ClienteController ($scope, $state, Cliente, ClienteSearch) {
        var vm = this;
        
        vm.clientes = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Cliente.query(function(result) {
                vm.clientes = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ClienteSearch.query({query: vm.searchQuery}, function(result) {
                vm.clientes = result;
            });
        }    }
})();
