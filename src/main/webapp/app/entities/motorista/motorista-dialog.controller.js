(function() {
    'use strict';

    angular
        .module('opalaApp')
        .controller('MotoristaDialogController', MotoristaDialogController);

    MotoristaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Motorista'];

    function MotoristaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Motorista) {
        var vm = this;

        vm.motorista = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.motorista.id !== null) {
                Motorista.update(vm.motorista, onSaveSuccess, onSaveError);
            } else {
                Motorista.save(vm.motorista, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('opalaApp:motoristaUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.vencimentoHabilitacao = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
