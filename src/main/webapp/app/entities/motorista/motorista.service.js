(function() {
    'use strict';
    angular
        .module('opalaApp')
        .factory('Motorista', Motorista);

    Motorista.$inject = ['$resource', 'DateUtils'];

    function Motorista ($resource, DateUtils) {
        var resourceUrl =  'api/motoristas/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.vencimentoHabilitacao = DateUtils.convertLocalDateFromServer(data.vencimentoHabilitacao);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.vencimentoHabilitacao = DateUtils.convertLocalDateToServer(copy.vencimentoHabilitacao);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.vencimentoHabilitacao = DateUtils.convertLocalDateToServer(copy.vencimentoHabilitacao);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
