(function() {
    'use strict';

    angular
        .module('opalaApp')
        .controller('SolicitanteDialogController', SolicitanteDialogController);

    SolicitanteDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Solicitante', 'User', 'Cliente', 'Agendamento'];

    function SolicitanteDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Solicitante, User, Cliente, Agendamento) {
        var vm = this;

        vm.solicitante = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();
        vm.clientes = Cliente.query();
        vm.agendamentos = Agendamento.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.solicitante.id !== null) {
                Solicitante.update(vm.solicitante, onSaveSuccess, onSaveError);
            } else {
                Solicitante.save(vm.solicitante, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('opalaApp:solicitanteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
