(function() {
    'use strict';

    angular
        .module('opalaApp')
        .controller('ItinerarioDeleteController',ItinerarioDeleteController);

    ItinerarioDeleteController.$inject = ['$uibModalInstance', 'entity', 'Itinerario'];

    function ItinerarioDeleteController($uibModalInstance, entity, Itinerario) {
        var vm = this;

        vm.itinerario = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Itinerario.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
