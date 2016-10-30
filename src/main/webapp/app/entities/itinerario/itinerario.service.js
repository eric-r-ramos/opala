(function() {
    'use strict';
    angular
        .module('opalaApp')
        .factory('Itinerario', Itinerario);

    Itinerario.$inject = ['$resource'];

    function Itinerario ($resource) {
        var resourceUrl =  'api/itinerarios/:id';

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
