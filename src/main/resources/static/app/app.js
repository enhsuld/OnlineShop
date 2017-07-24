/*
*  Altair Admin AngularJS
*/
;"use strict";

var altairApp = angular.module('altairApp', [
    'ui.router',
    'oc.lazyLoad',
    'ngSanitize',
    'ngRetina',
    'hSweetAlert',
    'ngFileUpload',
    'chieffancypants.loadingBar',
    'ncy-angular-breadcrumb',
    'ConsoleLogger',
    'ngCookies',
    'ngResource',
    'angular-jwt'
]);

altairApp.constant('variables', {
    header_main_height: 48,
    easing_swiftOut: [ 0.4,0,0.2,1 ],
    bez_easing_swiftOut: $.bez([ 0.4,0,0.2,1 ])
});

altairApp.config(function($sceDelegateProvider) {
    $sceDelegateProvider.resourceUrlWhitelist([
        'self',
        'https://www.youtube.com/**',
        'https://w.soundcloud.com/**'
    ]);
});

altairApp.config(function(cfpLoadingBarProvider) {
    cfpLoadingBarProvider.includeSpinner = true;
  });

// breadcrumbs
altairApp.config(function($breadcrumbProvider) {
    $breadcrumbProvider.setOptions({
        prefixStateName: 'restricted.dashboard',
        templateUrl: 'app/templates/breadcrumbs.tpl.html'
    });
});

altairApp.config(['$httpProvider', function($httpProvider) {
    $httpProvider.interceptors.push('rememberMeInterceptor');
}]);

altairApp.factory('rememberMeInterceptor', ['$q','$injector','$httpParamSerializer', function($q, $injector,$httpParamSerializer) {
    var interceptor = {
        responseError: function(response) {
            console.log(response.status);
            if (response.status == 401){
                alert();
                var $http = $injector.get('$http');
                var $cookies = $injector.get('$cookies');
                var deferred = $q.defer();

                var refreshData = {grant_type:"refresh_token"};

                var req = {
                    method: 'POST',
                    url: "oauth/token",
                    headers: {"Content-type": "application/x-www-form-urlencoded; charset=utf-8"},
                    data: $httpParamSerializer(refreshData)
                }

                $http(req).then(
                    function(data){
                        $http.defaults.headers.common.Authorization= 'Bearer ' + data.data.access_token;
                        var expireDate = new Date (new Date().getTime() + (1000 * data.data.expires_in));
                        $cookies.put("access_token", data.data.access_token, {'expires': expireDate});
                        $cookies.put("validity", data.data.expires_in);
                        window.location.href="index";
                    },function(){
                        console.log("error");
                        $cookies.remove("access_token");
                        window.location.href = "login";
                    }
                );

                // make the backend call again and chain the request
                return deferred.promise.then(function() {
                    return $http(response.config);
                });
            }
            return $q.reject(response);
        }
    };
    return interceptor;
}]);


altairApp.config(['$httpProvider', function($httpProvider) {
    $httpProvider.interceptors.push('rememberMeInterceptor');
    $httpProvider.interceptors.push(['$q', '$location','$cookies','$rootScope', function($q, $location,$cookies,$rootScope) {

        return {
            'request': function (config) {
                config.headers = config.headers || {};
              //  console.log($cookies.get('access_token'));
                if ($cookies.get('access_token')) {
                    config.headers.Authorization = 'Bearer ' + $cookies.get('access_token');
                }
                else{
                    $rootScope.sessionExpired=true;
                    $location.path('/login');
               //     $state.go('login');
                }
                return config;
            },
            'responseError': function(response) {
                if(response.status === 401 || response.status === 403) {
                    $location.path('/login');
                }
                return $q.reject(response);
            }
        };
    }]);
}]);




/* detect IE */
function detectIE(){var a=window.navigator.userAgent,b=a.indexOf("MSIE ");if(0<b)return parseInt(a.substring(b+5,a.indexOf(".",b)),10);if(0<a.indexOf("Trident/"))return b=a.indexOf("rv:"),parseInt(a.substring(b+3,a.indexOf(".",b)),10);b=a.indexOf("Edge/");return 0<b?parseInt(a.substring(b+5,a.indexOf(".",b)),10):!1};

/* Run Block */
altairApp
    .run([
        '$rootScope',
        '$state',
        '$stateParams',
        '$http',
        '$window',
        '$timeout',
        'preloaders',
        'variables',
        function ($rootScope, $state, $stateParams,$http,$window, $timeout,variables) {

            $rootScope.$state = $state;
            $rootScope.$stateParams = $stateParams;



            $rootScope.$on('$stateChangeSuccess', function () {

                // scroll view to top
                $("html, body").animate({
                    scrollTop: 0
                }, 200);

                if(detectIE()) {
                    $('svg,canvas,video').each(function () {
                        $(this).css('height', 0);
                    });
                };

                $timeout(function() {
                    $rootScope.pageLoading = false;
                },300);

                $timeout(function() {
                    $rootScope.pageLoaded = true;
                    $rootScope.appInitialized = true;
                    // wave effects
                    $window.Waves.attach('.md-btn-wave,.md-fab-wave', ['waves-button']);
                    $window.Waves.attach('.md-btn-wave-light,.md-fab-wave-light', ['waves-button', 'waves-light']);
                    if(detectIE()) {
                        $('svg,canvas,video').each(function() {
                            var $this = $(this),
                                height = $(this).attr('height'),
                                width = $(this).attr('width');

                            if(height) {
                                $this.css('height', height);
                            }
                            if(width) {
                                $this.css('width', width);
                            }
                            var peity = $this.prev('.peity_data,.peity');
                            if(peity.length) {
                                peity.peity().change()
                            }
                        });
                    }
                },600);

            });

            $rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
                // main search
                $rootScope.mainSearchActive = false;
                // secondary sidebar
                $rootScope.sidebar_secondary = false;
                $rootScope.secondarySidebarHiddenLarge = false;

                if($($window).width() < 1220 ) {
                    // hide primary sidebar
                    $rootScope.primarySidebarActive = false;
                    $rootScope.hide_content_sidebar = false;
                }
                if(!toParams.hasOwnProperty('hidePreloader')) {
                    $rootScope.pageLoading = true;
                    $rootScope.pageLoaded = false;
                }

            });

            // fastclick (eliminate the 300ms delay between a physical tap and the firing of a click event on mobile browsers)
            FastClick.attach(document.body);

            // get version from package.json
            $http.get('./package.json').success(function(response) {
                $rootScope.appVer = response.version;
            });

            // modernizr
            $rootScope.Modernizr = Modernizr;

            // get window width
            var w = angular.element($window);
            $rootScope.largeScreen = w.width() >= 1220;

            w.on('resize', function() {
                return $rootScope.largeScreen = w.width() >= 1220;
            });

            // show/hide main menu on page load
            $rootScope.primarySidebarOpen = $rootScope.largeScreen;

            $rootScope.pageLoading = true;

            // wave effects
            $window.Waves.init();
            
            
            $rootScope.menuAccordionMode = true;

        }
    ])
    .run(['PrintToConsole', function(PrintToConsole) {
        PrintToConsole.active = false;
    }])
;
