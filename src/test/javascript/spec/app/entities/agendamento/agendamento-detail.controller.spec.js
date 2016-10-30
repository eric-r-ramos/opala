'use strict';

describe('Controller Tests', function() {

    describe('Agendamento Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAgendamento, MockCliente, MockMotorista, MockEndereco, MockSolicitante, MockVeiculo, MockItinerario, MockPassageiro;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAgendamento = jasmine.createSpy('MockAgendamento');
            MockCliente = jasmine.createSpy('MockCliente');
            MockMotorista = jasmine.createSpy('MockMotorista');
            MockEndereco = jasmine.createSpy('MockEndereco');
            MockSolicitante = jasmine.createSpy('MockSolicitante');
            MockVeiculo = jasmine.createSpy('MockVeiculo');
            MockItinerario = jasmine.createSpy('MockItinerario');
            MockPassageiro = jasmine.createSpy('MockPassageiro');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Agendamento': MockAgendamento,
                'Cliente': MockCliente,
                'Motorista': MockMotorista,
                'Endereco': MockEndereco,
                'Solicitante': MockSolicitante,
                'Veiculo': MockVeiculo,
                'Itinerario': MockItinerario,
                'Passageiro': MockPassageiro
            };
            createController = function() {
                $injector.get('$controller')("AgendamentoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'opalaApp:agendamentoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
