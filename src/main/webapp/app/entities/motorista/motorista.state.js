(function() {
    'use strict';

    angular
        .module('opalaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('motorista', {
            parent: 'entity',
            url: '/motorista',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'opalaApp.motorista.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/motorista/motoristas.html',
                    controller: 'MotoristaController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('motorista');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('motorista-detail', {
            parent: 'motorista',
            url: '/motorista/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'opalaApp.motorista.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/motorista/motorista-detail.html',
                    controller: 'MotoristaDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('motorista');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Motorista', function($stateParams, Motorista) {
                    return Motorista.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'motorista',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('motorista-detail.edit', {
            parent: 'motorista-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/motorista/motorista-dialog.html',
                    controller: 'MotoristaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Motorista', function(Motorista) {
                            return Motorista.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('motorista.new', {
            parent: 'motorista',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/motorista/motorista-dialog.html',
                    controller: 'MotoristaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nome: null,
                                habilitacao: null,
                                vencimentoHabilitacao: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('motorista', null, { reload: 'motorista' });
                }, function() {
                    $state.go('motorista');
                });
            }]
        })
        .state('motorista.edit', {
            parent: 'motorista',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/motorista/motorista-dialog.html',
                    controller: 'MotoristaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Motorista', function(Motorista) {
                            return Motorista.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('motorista', null, { reload: 'motorista' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('motorista.delete', {
            parent: 'motorista',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/motorista/motorista-delete-dialog.html',
                    controller: 'MotoristaDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Motorista', function(Motorista) {
                            return Motorista.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('motorista', null, { reload: 'motorista' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
