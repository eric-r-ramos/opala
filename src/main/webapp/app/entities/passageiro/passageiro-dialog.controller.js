(function() {
    'use strict';

    angular
        .module('opalaApp')
        .controller('PassageiroDialogController', PassageiroDialogController);

    PassageiroDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Passageiro'];

    function PassageiroDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Passageiro) {
        var vm = this;

        vm.passageiro = entity;
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
            if (vm.passageiro.id !== null) {
                Passageiro.update(vm.passageiro, onSaveSuccess, onSaveError);
            } else {
                Passageiro.save(vm.passageiro, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('opalaApp:passageiroUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
