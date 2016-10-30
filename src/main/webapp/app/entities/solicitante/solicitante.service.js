(function() {
    'use strict';
    angular
        .module('opalaApp')
        .factory('Solicitante', Solicitante);

    Solicitante.$inject = ['$resource'];

    function Solicitante ($resource) {
        var resourceUrl =  'api/solicitantes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
