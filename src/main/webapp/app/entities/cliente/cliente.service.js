(function() {
    'use strict';
    angular
        .module('opalaApp')
        .factory('Cliente', Cliente);

    Cliente.$inject = ['$resource'];

    function Cliente ($resource) {
        var resourceUrl =  'api/clientes/:id';

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
