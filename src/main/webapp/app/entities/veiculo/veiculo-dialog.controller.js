(function() {
    'use strict';

    angular
        .module('opalaApp')
        .controller('VeiculoDialogController', VeiculoDialogController);

    VeiculoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Veiculo'];

    function VeiculoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Veiculo) {
        var vm = this;

        vm.veiculo = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.veiculo.id !== null) {
                Veiculo.update(vm.veiculo, onSaveSuccess, onSaveError);
            } else {
                Veiculo.save(vm.veiculo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('opalaApp:veiculoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
