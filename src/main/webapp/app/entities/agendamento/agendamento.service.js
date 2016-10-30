(function() {
    'use strict';
    angular
        .module('opalaApp')
        .factory('Agendamento', Agendamento);

    Agendamento.$inject = ['$resource', 'DateUtils'];

    function Agendamento ($resource, DateUtils) {
        var resourceUrl =  'api/agendamentos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.data = DateUtils.convertDateTimeFromServer(data.data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
