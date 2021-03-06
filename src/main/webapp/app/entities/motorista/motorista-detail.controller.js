(function() {
    'use strict';

    angular
        .module('opalaApp')
        .controller('MotoristaDetailController', MotoristaDetailController);

    MotoristaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Motorista', 'User'];

    function MotoristaDetailController($scope, $rootScope, $stateParams, previousState, entity, Motorista, User) {
        var vm = this;

        vm.motorista = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('opalaApp:motoristaUpdate', function(event, result) {
            vm.motorista = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
