(function() {
    'use strict';

    angular
        .module('opalaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('itinerario', {
            parent: 'entity',
            url: '/itinerario',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'opalaApp.itinerario.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/itinerario/itinerarios.html',
                    controller: 'ItinerarioController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('itinerario');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('itinerario-detail', {
            parent: 'itinerario',
            url: '/itinerario/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'opalaApp.itinerario.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/itinerario/itinerario-detail.html',
                    controller: 'ItinerarioDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('itinerario');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Itinerario', function($stateParams, Itinerario) {
                    return Itinerario.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'itinerario',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('itinerario-detail.edit', {
            parent: 'itinerario-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/itinerario/itinerario-dialog.html',
                    controller: 'ItinerarioDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Itinerario', function(Itinerario) {
                            return Itinerario.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('itinerario.new', {
            parent: 'itinerario',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/itinerario/itinerario-dialog.html',
                    controller: 'ItinerarioDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                descricao: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('itinerario', null, { reload: 'itinerario' });
                }, function() {
                    $state.go('itinerario');
                });
            }]
        })
        .state('itinerario.edit', {
            parent: 'itinerario',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/itinerario/itinerario-dialog.html',
                    controller: 'ItinerarioDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Itinerario', function(Itinerario) {
                            return Itinerario.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('itinerario', null, { reload: 'itinerario' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('itinerario.delete', {
            parent: 'itinerario',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/itinerario/itinerario-delete-dialog.html',
                    controller: 'ItinerarioDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Itinerario', function(Itinerario) {
                            return Itinerario.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('itinerario', null, { reload: 'itinerario' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
