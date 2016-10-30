(function() {
    'use strict';

    angular
        .module('opalaApp')
        .controller('EnderecoDialogController', EnderecoDialogController);

    EnderecoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Endereco'];

    function EnderecoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Endereco) {
        var vm = this;

        vm.endereco = entity;
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
            if (vm.endereco.id !== null) {
                Endereco.update(vm.endereco, onSaveSuccess, onSaveError);
            } else {
                Endereco.save(vm.endereco, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('opalaApp:enderecoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
