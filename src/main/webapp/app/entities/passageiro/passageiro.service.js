(function() {
    'use strict';
    angular
        .module('opalaApp')
        .factory('Passageiro', Passageiro);

    Passageiro.$inject = ['$resource'];

    function Passageiro ($resource) {
        var resourceUrl =  'api/passageiros/:id';

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
