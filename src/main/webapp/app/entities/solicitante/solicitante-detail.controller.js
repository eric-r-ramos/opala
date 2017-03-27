(function() {
    'use strict';

    angular
        .module('opalaApp')
        .controller('SolicitanteDetailController', SolicitanteDetailController);

    SolicitanteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Solicitante', 'User', 'Cliente', 'Agendamento'];

    function SolicitanteDetailController($scope, $rootScope, $stateParams, previousState, entity, Solicitante, User, Cliente, Agendamento) {
        var vm = this;

        vm.solicitante = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('opalaApp:solicitanteUpdate', function(event, result) {
            vm.solicitante = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
