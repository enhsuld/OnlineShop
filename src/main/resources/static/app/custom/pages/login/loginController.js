angular
    .module('altairApp')
    .controller('loginCtrl', [
        '$scope',
        '$rootScope',
        '$http',
        '$state',
        'utils',
        'mainService',
        '$resource',
        '$cookies',
        '$httpParamSerializer',
        'jwtHelper',
        '$timeout',
        function ($scope,$rootScope,$http,$state,utils, mainService, $resource,$cookies,$httpParamSerializer,jwtHelper,$timeout) {
       	
				

    	   var $formValidate = $('#form_validation');

           $formValidate
               .parsley()
               .on('form:validated',function() {
                   $scope.$apply();
               })
               .on('field:validated',function(parsleyField) {
                   if($(parsleyField.$element).hasClass('md-input')) {
                       $scope.$apply();
                   }
               });
         
      	   var $respassword = $('#reset_password');

      	   $respassword
               .parsley()
               .on('form:validated',function() {
                   $scope.$apply();
               })
               .on('field:validated',function(parsleyField) {
                   if($(parsleyField.$element).hasClass('md-input')) {
                       $scope.$apply();
                   }
               });
              
               
            $scope.registerFormActive = false;

            var $login_card = $('#login_card'),
                $login_form = $('#login_form'),
                $login_help = $('#login_help'),
                $register_form = $('#register_form'),
                $login_password_reset = $('#login_password_reset');

            // show login form (hide other forms)
            var login_form_show = function() {
                $login_form
                    .show()
                    .siblings()
                    .hide();
            };

            // show register form (hide other forms)
            var register_form_show = function() {
                $register_form
                    .show()
                    .siblings()
                    .hide();
            };

            // show login help (hide other forms)
            var login_help_show = function() {
                $login_help
                    .show()
                    .siblings()
                    .hide();
            };

            // show password reset form (hide other forms)
            var password_reset_show = function() {
                $login_password_reset
                    .show()
                    .siblings()
                    .hide();
            };

            $scope.loginHelp = function($event) {
                $event.preventDefault();
                utils.card_show_hide($login_card,undefined,login_help_show,undefined);
            };

            $scope.backToLogin = function($event) {
                $event.preventDefault();
                $scope.registerFormActive = false;
                utils.card_show_hide($login_card,undefined,login_form_show,undefined);
            };

            $scope.registerForm = function($event) {
                $event.preventDefault();
                $scope.registerFormActive = true;
                utils.card_show_hide($login_card,undefined,register_form_show,undefined);
            };

            $scope.passwordReset = function($event) {
                $event.preventDefault();
                utils.card_show_hide($login_card,undefined,password_reset_show,undefined);
            };

            $scope.foo = {id:1 , name:"sample foo"};
            $scope.foos = $resource("http://localhost:8082/spring-security-oauth-resource/foos/:fooId",{fooId:'@id'});

            $scope.organiztion = "";
            $scope.isLoggedIn = false;

            $scope.getFoo = function(){
                $scope.foo = $scope.foos.get({fooId:$scope.foo.id});
            }

            $scope.loginData = {grant_type:"password", username: "", password: "", client_id: "fooClientIdPassword"};
            $scope.refreshData = {grant_type:"refresh_token"};

            var isLoginPage = window.location.href.indexOf("login") != -1;
            if(isLoginPage){
                if($cookies.get("access_token")){
                    $state.go('restricted.dashboard');
                }
            }else{
                if($cookies.get("access_token")){
                    $http.defaults.headers.common.Authorization= 'Bearer ' + $cookies.get("access_token");
                    getOrganization();
                    $scope.isLoggedIn = true;
                }else{
                    //obtainAccessToken($scope.refreshData);
                    $scope.isLoggedIn = false;
                }
            }

            $scope.login = function() {
                obtainAccessToken($scope.loginData);
            }

            $scope.refreshAccessToken = function(){
                obtainAccessToken($scope.refreshData);
            }

            $scope.logout = function() {
                logout($scope.loginData);
            }

            if ($cookies.get("remember")=="yes"){
                var validity = $cookies.get("validity");
                if (validity >10) validity -= 10;
                $timeout( function(){;$scope.refreshAccessToken();}, validity * 1000);
            }

            function obtainAccessToken(params){
                if (params.username != null){
                    if (params.remember != null){
                        $cookies.put("remember","yes");
                    }
                    else {
                        $cookies.remove("remember");
                    }
                }

               /* var req = {
                    method: 'POST',
                    url: "oauth/token",
                    headers: {"Content-type": "application/x-www-form-urlencoded; charset=utf-8"},
                    data: $httpParamSerializer(params)
                }*/

                $scope.encoded = btoa("fooClientIdPassword:secret");
                var req = {
                    method: 'POST',
                    url: "http://localhost:8080/oauth/token?grant_type=password&username="+$scope.loginData.username+"&password="+$scope.loginData.password+"",
                    headers: {
                        "Authorization": "Basic " + $scope.encoded,
                        "Content-type": "application/x-www-form-urlencoded; charset=utf-8"
                    }
                }


                $http(req).then(
                    function(data){
                        $http.defaults.headers.common.Authorization= 'Bearer ' + data.data.access_token;
                        var expireDate = new Date (new Date().getTime() + (1000 * data.data.expires_in));
                        $cookies.put("access_token", data.data.access_token, {'expires': expireDate});
                        $cookies.put("validity", data.data.expires_in);

                        $state.go('restricted.dashboard');
                    },function(){
                        console.log("error");
                        $state.go('login');
                    }
                );
            }

            function getOrganization(){
                var token = $cookies.get("access_token");
                //JWT
                /* var payload = jwtHelper.decodeToken(token);
                 console.log(payload);
                 $scope.organization = payload.organization; */

                //JDBC
                $http.get("http://localhost:8082/spring-security-oauth-resource/users/extra")
                    .then(function(response) {
                        console.log(response);
                        $scope.organization = response.data.organization;
                    });
            }

            function logout(params) {
                var req = {
                    method: 'DELETE',
                    url: "oauth/token"
                }
                $http(req).then(
                    function(data){
                        $cookies.remove("access_token");
                        $cookies.remove("remember");
                        window.location.href="login";
                    },function(){
                        console.log("error");
                    }
                );
            }


            /*mainService.withdomain('delete','http://localhost:8080/oauth/token/revokeById/:tokenId',{tokenId:'@tokenId'})
            .then(function(data){
                if(data){
                    sweet.show('Мэдээлэл', 'Амжилттай хадгаллаа.', 'success');
                }

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

            $scope.data = {
                grant_type:"password",
                username: "",
                password: "",
                client_id: "clientIdPassword"
            };
            $scope.encoded = btoa("fooClientIdPassword:secret");

            $scope.login = function() {

                var req = {
                    method: 'POST',
                    url: "http://localhost:8080/oauth/token?grant_type=password&username="+$scope.data.username+"&password="+$scope.data.password+"",
                    headers: {
                        "Authorization": "Basic " + $scope.encoded,
                        "Content-type": "application/x-www-form-urlencoded; charset=utf-8"
                    }
                }
                $http(req).then(function(data){
                    $http.defaults.headers.common.Authorization =
                        'Bearer ' + data.data.access_token;
                    localStorage.setItem("access_token", data.data.access_token);
                    $state.go('restricted.dashboard');
                });
            }*/

            $scope.res = {};
			$scope.resetPassword = function() {			
				 mainService.withdata('put','/service/send-mail', $scope.res)
		   			.then(function(data){
		   				if(data){
			   				sweet.show('Мэдээлэл', 'Амжилттай хадгаллаа.', 'success');
			   				init();
		   				}
			   				
	   			});
			};
        }
    ]);