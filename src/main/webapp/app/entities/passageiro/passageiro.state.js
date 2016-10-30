(function() {
    'use strict';

    angular
        .module('opalaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('passageiro', {
            parent: 'entity',
            url: '/passageiro',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'opalaApp.passageiro.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/passageiro/passageiros.html',
                    controller: 'PassageiroController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('passageiro');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('passageiro-detail', {
            parent: 'entity',
            url: '/passageiro/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'opalaApp.passageiro.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/passageiro/passageiro-detail.html',
                    controller: 'PassageiroDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('passageiro');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Passageiro', function($stateParams, Passageiro) {
                    return Passageiro.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'passageiro',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('passageiro-detail.edit', {
            parent: 'passageiro-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/passageiro/passageiro-dialog.html',
                    controller: 'PassageiroDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Passageiro', function(Passageiro) {
                            return Passageiro.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('passageiro.new', {
            parent: 'passageiro',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/passageiro/passageiro-dialog.html',
                    controller: 'PassageiroDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nome: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('passageiro', null, { reload: 'passageiro' });
                }, function() {
                    $state.go('passageiro');
                });
            }]
        })
        .state('passageiro.edit', {
            parent: 'passageiro',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/passageiro/passageiro-dialog.html',
                    controller: 'PassageiroDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Passageiro', function(Passageiro) {
                            return Passageiro.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('passageiro', null, { reload: 'passageiro' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('passageiro.delete', {
            parent: 'passageiro',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/passageiro/passageiro-delete-dialog.html',
                    controller: 'PassageiroDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Passageiro', function(Passageiro) {
                            return Passageiro.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('passageiro', null, { reload: 'passageiro' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
