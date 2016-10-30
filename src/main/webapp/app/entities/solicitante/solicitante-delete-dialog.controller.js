(function() {
    'use strict';

    angular
        .module('opalaApp')
        .controller('SolicitanteDeleteController',SolicitanteDeleteController);

    SolicitanteDeleteController.$inject = ['$uibModalInstance', 'entity', 'Solicitante'];

    function SolicitanteDeleteController($uibModalInstance, entity, Solicitante) {
        var vm = this;

        vm.solicitante = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Solicitante.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
