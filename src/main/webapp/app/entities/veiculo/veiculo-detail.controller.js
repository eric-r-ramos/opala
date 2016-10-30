(function() {
    'use strict';

    angular
        .module('opalaApp')
        .controller('VeiculoDetailController', VeiculoDetailController);

    VeiculoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Veiculo'];

    function VeiculoDetailController($scope, $rootScope, $stateParams, previousState, entity, Veiculo) {
        var vm = this;

        vm.veiculo = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('opalaApp:veiculoUpdate', function(event, result) {
            vm.veiculo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
