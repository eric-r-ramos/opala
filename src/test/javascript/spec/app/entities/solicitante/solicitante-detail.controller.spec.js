'use strict';

describe('Controller Tests', function() {

    describe('Solicitante Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockSolicitante, MockUser, MockCliente, MockAgendamento;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockSolicitante = jasmine.createSpy('MockSolicitante');
            MockUser = jasmine.createSpy('MockUser');
            MockCliente = jasmine.createSpy('MockCliente');
            MockAgendamento = jasmine.createSpy('MockAgendamento');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Solicitante': MockSolicitante,
                'User': MockUser,
                'Cliente': MockCliente,
                'Agendamento': MockAgendamento
            };
            createController = function() {
                $injector.get('$controller')("SolicitanteDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'opalaApp:solicitanteUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
