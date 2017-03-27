(function() {
    'use strict';

    angular
        .module('opalaApp')
        .controller('VeiculoDeleteController',VeiculoDeleteController);

    VeiculoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Veiculo'];

    function VeiculoDeleteController($uibModalInstance, entity, Veiculo) {
        var vm = this;

        vm.veiculo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Veiculo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
