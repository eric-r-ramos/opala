(function() {
    'use strict';

    angular
        .module('opalaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('solicitante', {
            parent: 'entity',
            url: '/solicitante?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'opalaApp.solicitante.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/solicitante/solicitantes.html',
                    controller: 'SolicitanteController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('solicitante');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('solicitante-detail', {
            parent: 'entity',
            url: '/solicitante/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'opalaApp.solicitante.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/solicitante/solicitante-detail.html',
                    controller: 'SolicitanteDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('solicitante');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Solicitante', function($stateParams, Solicitante) {
                    return Solicitante.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'solicitante',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('solicitante-detail.edit', {
            parent: 'solicitante-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/solicitante/solicitante-dialog.html',
                    controller: 'SolicitanteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Solicitante', function(Solicitante) {
                            return Solicitante.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('solicitante.new', {
            parent: 'solicitante',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/solicitante/solicitante-dialog.html',
                    controller: 'SolicitanteDialogController',
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
                    $state.go('solicitante', null, { reload: 'solicitante' });
                }, function() {
                    $state.go('solicitante');
                });
            }]
        })
        .state('solicitante.edit', {
            parent: 'solicitante',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/solicitante/solicitante-dialog.html',
                    controller: 'SolicitanteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Solicitante', function(Solicitante) {
                            return Solicitante.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('solicitante', null, { reload: 'solicitante' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('solicitante.delete', {
            parent: 'solicitante',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/solicitante/solicitante-delete-dialog.html',
                    controller: 'SolicitanteDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Solicitante', function(Solicitante) {
                            return Solicitante.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('solicitante', null, { reload: 'solicitante' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
