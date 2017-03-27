(function() {
    'use strict';

    angular
        .module('opalaApp')
        .controller('MotoristaDeleteController',MotoristaDeleteController);

    MotoristaDeleteController.$inject = ['$uibModalInstance', 'entity', 'Motorista'];

    function MotoristaDeleteController($uibModalInstance, entity, Motorista) {
        var vm = this;

        vm.motorista = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Motorista.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
