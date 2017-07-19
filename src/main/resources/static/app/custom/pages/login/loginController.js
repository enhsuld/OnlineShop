angular
    .module('altairApp')
    .controller('loginCtrl', [
        '$scope',
        '$rootScope',
        '$http',
        '$state',
        'utils',
        'mainService',
        '$cookies',
        function ($scope,$rootScope,$http,$state,utils, mainService, $cookies) {
       	
				

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
                    $cookies.put("access_token", data.data.access_token);
                    $state.go('restricted.dashboard');
                });
            }

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