(function() {
    'use strict';

    angular
        .module('opalaApp')
        .controller('ItinerarioDialogController', ItinerarioDialogController);

    ItinerarioDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Itinerario'];

    function ItinerarioDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Itinerario) {
        var vm = this;

        vm.itinerario = entity;
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
            if (vm.itinerario.id !== null) {
                Itinerario.update(vm.itinerario, onSaveSuccess, onSaveError);
            } else {
                Itinerario.save(vm.itinerario, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('opalaApp:itinerarioUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
