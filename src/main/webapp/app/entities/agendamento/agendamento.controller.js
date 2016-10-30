(function() {
    'use strict';

    angular
        .module('opalaApp')
        .controller('AgendamentoController', AgendamentoController);

    AgendamentoController.$inject = ['$scope', '$state', 'Agendamento', 'AgendamentoSearch'];

    function AgendamentoController ($scope, $state, Agendamento, AgendamentoSearch) {
        var vm = this;
        
        vm.agendamentos = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Agendamento.query(function(result) {
                vm.agendamentos = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            AgendamentoSearch.query({query: vm.searchQuery}, function(result) {
                vm.agendamentos = result;
            });
        }    }
})();
