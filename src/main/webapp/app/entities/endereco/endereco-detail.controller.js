(function() {
    'use strict';

    angular
        .module('opalaApp')
        .controller('EnderecoDetailController', EnderecoDetailController);

    EnderecoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Endereco'];

    function EnderecoDetailController($scope, $rootScope, $stateParams, previousState, entity, Endereco) {
        var vm = this;

        vm.endereco = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('opalaApp:enderecoUpdate', function(event, result) {
            vm.endereco = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
