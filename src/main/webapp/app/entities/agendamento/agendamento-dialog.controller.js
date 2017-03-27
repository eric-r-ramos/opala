(function() {
    'use strict';

    angular
        .module('opalaApp')
        .controller('AgendamentoDialogController', AgendamentoDialogController);

    AgendamentoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Agendamento', 'Cliente', 'Motorista', 'Endereco', 'Solicitante', 'Itinerario', 'Passageiro'];

    function AgendamentoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Agendamento, Cliente, Motorista, Endereco, Solicitante, Itinerario, Passageiro) {
        var vm = this;

        vm.agendamento = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.clientes = Cliente.query();
        vm.motoristas = Motorista.query();
        vm.enderecos = Endereco.query();
        vm.solicitantes = Solicitante.query();
        vm.itinerarios = Itinerario.query();
        vm.passageiros = Passageiro.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.agendamento.id !== null) {
                Agendamento.update(vm.agendamento, onSaveSuccess, onSaveError);
            } else {
                Agendamento.save(vm.agendamento, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('opalaApp:agendamentoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.data = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
