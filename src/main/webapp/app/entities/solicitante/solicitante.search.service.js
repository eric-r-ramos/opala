(function() {
    'use strict';

    angular
        .module('opalaApp')
        .factory('SolicitanteSearch', SolicitanteSearch);

    SolicitanteSearch.$inject = ['$resource'];

    function SolicitanteSearch($resource) {
        var resourceUrl =  'api/_search/solicitantes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
