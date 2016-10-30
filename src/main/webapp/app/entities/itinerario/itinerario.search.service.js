(function() {
    'use strict';

    angular
        .module('opalaApp')
        .factory('ItinerarioSearch', ItinerarioSearch);

    ItinerarioSearch.$inject = ['$resource'];

    function ItinerarioSearch($resource) {
        var resourceUrl =  'api/_search/itinerarios/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
