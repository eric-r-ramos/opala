(function() {
    'use strict';

    angular
        .module('opalaApp')
        .factory('Sessions', Sessions);

    Sessions.$inject = ['$resource'];

    function Sessions ($resource) {
        return $resource('api/account/sessions/:series', {}, {
            'getAll': { method: 'GET', isArray: true}
        });
    }
})();
