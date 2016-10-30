(function() {
    'use strict';

    angular
        .module('opalaApp')
        .factory('MotoristaSearch', MotoristaSearch);

    MotoristaSearch.$inject = ['$resource'];

    function MotoristaSearch($resource) {
        var resourceUrl =  'api/_search/motoristas/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
