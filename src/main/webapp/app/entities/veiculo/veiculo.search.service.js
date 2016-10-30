(function() {
    'use strict';

    angular
        .module('opalaApp')
        .factory('VeiculoSearch', VeiculoSearch);

    VeiculoSearch.$inject = ['$resource'];

    function VeiculoSearch($resource) {
        var resourceUrl =  'api/_search/veiculos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
