(function() {
    'use strict';

    angular
        .module('opalaApp')
        .controller('EnderecoController', EnderecoController);

    EnderecoController.$inject = ['$scope', '$state', 'Endereco', 'EnderecoSearch'];

    function EnderecoController ($scope, $state, Endereco, EnderecoSearch) {
        var vm = this;
        
        vm.enderecos = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Endereco.query(function(result) {
                vm.enderecos = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            EnderecoSearch.query({query: vm.searchQuery}, function(result) {
                vm.enderecos = result;
            });
        }    }
})();
