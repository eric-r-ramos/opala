(function() {
    'use strict';

    angular
        .module('opalaApp')
        .controller('PassageiroDeleteController',PassageiroDeleteController);

    PassageiroDeleteController.$inject = ['$uibModalInstance', 'entity', 'Passageiro'];

    function PassageiroDeleteController($uibModalInstance, entity, Passageiro) {
        var vm = this;

        vm.passageiro = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Passageiro.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
