(function() {
    'use strict';

    angular
        .module('opalaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('agendamento', {
            parent: 'entity',
            url: '/agendamento?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'opalaApp.agendamento.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/agendamento/agendamentos.html',
                    controller: 'AgendamentoController',
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
                    $translatePartialLoader.addPart('agendamento');
                    $translatePartialLoader.addPart('status');
                    $translatePartialLoader.addPart('sentido');
                    $translatePartialLoader.addPart('pagamento');
                    $translatePartialLoader.addPart('categoria');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('agendamento-detail', {
            parent: 'entity',
            url: '/agendamento/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'opalaApp.agendamento.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/agendamento/agendamento-detail.html',
                    controller: 'AgendamentoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('agendamento');
                    $translatePartialLoader.addPart('status');
                    $translatePartialLoader.addPart('sentido');
                    $translatePartialLoader.addPart('pagamento');
                    $translatePartialLoader.addPart('categoria');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Agendamento', function($stateParams, Agendamento) {
                    return Agendamento.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'agendamento',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('agendamento-detail.edit', {
            parent: 'agendamento-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agendamento/agendamento-dialog.html',
                    controller: 'AgendamentoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Agendamento', function(Agendamento) {
                            return Agendamento.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('agendamento.new', {
            parent: 'agendamento',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agendamento/agendamento-dialog.html',
                    controller: 'AgendamentoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                data: null,
                                numeroRequisicao: null,
                                centroCusto: null,
                                referencia: null,
                                valor: null,
                                observacao: null,
                                status: null,
                                sentido: null,
                                pagamento: null,
                                categoria: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('agendamento', null, { reload: 'agendamento' });
                }, function() {
                    $state.go('agendamento');
                });
            }]
        })
        .state('agendamento.edit', {
            parent: 'agendamento',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agendamento/agendamento-dialog.html',
                    controller: 'AgendamentoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Agendamento', function(Agendamento) {
                            return Agendamento.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('agendamento', null, { reload: 'agendamento' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('agendamento.delete', {
            parent: 'agendamento',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agendamento/agendamento-delete-dialog.html',
                    controller: 'AgendamentoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Agendamento', function(Agendamento) {
                            return Agendamento.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('agendamento', null, { reload: 'agendamento' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
