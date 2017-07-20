/*
 *  Altair Admin angularjs
 *  controller
 */

angular
    .module('altairApp')
    .controller('mainCtrl', ['$scope','$resource','$http','$rootScope',
        function ($scope,$resource,$http,$rootScope) {

            $scope.$on('oauth:login', function(event, token) {
                alert();
                $http.defaults.headers.common.Authorization= 'Bearer ' + token.access_token;
                console.log('Authorized third party app with token', token.access_token);
                $scope.token=token.access_token;
            });





            $scope.revokeToken = $resource("http://localhost:8080/oauth/token/revokeById/:tokenId",{tokenId:'@tokenId'});
            $scope.tokens = $resource("http://localhost:8080/tokens");

            $scope.getTokens = function(){
                $scope.tokenList = $scope.tokens.query();
            }

            $scope.revokeAccessToken = function(){
                if ($scope.tokenToRevoke && $scope.tokenToRevoke.length !=0){
                    $scope.revokeToken.save({tokenId:$scope.tokenToRevoke});
                    $rootScope.message="Token:"+$scope.tokenToRevoke+" was revoked!";
                    $scope.tokenToRevoke="";
                }
            }
        }
    ])
;
