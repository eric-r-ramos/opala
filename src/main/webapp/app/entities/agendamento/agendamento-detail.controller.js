(function() {
    'use strict';

    angular
        .module('opalaApp')
        .controller('AgendamentoDetailController', AgendamentoDetailController);

    AgendamentoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Agendamento', 'Cliente', 'Motorista', 'Endereco', 'Solicitante', 'Itinerario', 'Passageiro'];

    function AgendamentoDetailController($scope, $rootScope, $stateParams, previousState, entity, Agendamento, Cliente, Motorista, Endereco, Solicitante, Itinerario, Passageiro) {
        var vm = this;

        vm.agendamento = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('opalaApp:agendamentoUpdate', function(event, result) {
            vm.agendamento = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
