(function() {
    'use strict';

    angular
        .module('opalaApp')
        .factory('PassageiroSearch', PassageiroSearch);

    PassageiroSearch.$inject = ['$resource'];

    function PassageiroSearch($resource) {
        var resourceUrl =  'api/_search/passageiros/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
