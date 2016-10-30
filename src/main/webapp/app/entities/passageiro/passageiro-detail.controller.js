(function() {
    'use strict';

    angular
        .module('opalaApp')
        .controller('PassageiroDetailController', PassageiroDetailController);

    PassageiroDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Passageiro'];

    function PassageiroDetailController($scope, $rootScope, $stateParams, previousState, entity, Passageiro) {
        var vm = this;

        vm.passageiro = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('opalaApp:passageiroUpdate', function(event, result) {
            vm.passageiro = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
