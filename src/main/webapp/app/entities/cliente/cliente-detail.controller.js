(function() {
    'use strict';

    angular
        .module('opalaApp')
        .controller('ClienteDetailController', ClienteDetailController);

    ClienteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Cliente', 'Solicitante'];

    function ClienteDetailController($scope, $rootScope, $stateParams, previousState, entity, Cliente, Solicitante) {
        var vm = this;

        vm.cliente = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('opalaApp:clienteUpdate', function(event, result) {
            vm.cliente = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
