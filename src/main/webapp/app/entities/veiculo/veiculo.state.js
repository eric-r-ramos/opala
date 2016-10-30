(function() {
    'use strict';

    angular
        .module('opalaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('veiculo', {
            parent: 'entity',
            url: '/veiculo',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'opalaApp.veiculo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/veiculo/veiculos.html',
                    controller: 'VeiculoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('veiculo');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('veiculo-detail', {
            parent: 'entity',
            url: '/veiculo/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'opalaApp.veiculo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/veiculo/veiculo-detail.html',
                    controller: 'VeiculoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('veiculo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Veiculo', function($stateParams, Veiculo) {
                    return Veiculo.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'veiculo',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('veiculo-detail.edit', {
            parent: 'veiculo-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/veiculo/veiculo-dialog.html',
                    controller: 'VeiculoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Veiculo', function(Veiculo) {
                            return Veiculo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('veiculo.new', {
            parent: 'veiculo',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/veiculo/veiculo-dialog.html',
                    controller: 'VeiculoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                marca: null,
                                modelo: null,
                                cor: null,
                                placa: null,
                                ano: null,
                                ativo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('veiculo', null, { reload: 'veiculo' });
                }, function() {
                    $state.go('veiculo');
                });
            }]
        })
        .state('veiculo.edit', {
            parent: 'veiculo',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/veiculo/veiculo-dialog.html',
                    controller: 'VeiculoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Veiculo', function(Veiculo) {
                            return Veiculo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('veiculo', null, { reload: 'veiculo' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('veiculo.delete', {
            parent: 'veiculo',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/veiculo/veiculo-delete-dialog.html',
                    controller: 'VeiculoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Veiculo', function(Veiculo) {
                            return Veiculo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('veiculo', null, { reload: 'veiculo' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
