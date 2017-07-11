angular
    .module('altairApp')
    	.controller("majorCtrl",['$rootScope','$timeout','$scope','cfpLoadingBar','user_data','mainService','sweet','$state','$stateParams','mainobj','Upload','fileUpload',
	        function ($rootScope,$timeout,$scope,cfpLoadingBar,user_data,mainService,sweet,$state,$stateParams,mainobj,Upload,fileUpload) {       	
    		
    			var modal = UIkit.modal("#modal_header_footer_print");
    			$scope.mainobj=mainobj;
    			$scope.domain="com.netgloo.models.MainAuditRegistration.";
    			
    		
    			
    			$scope.domain1="com.nbb.models.fn.LnkAuditForm.";

    		    $scope.mainGridOptions = {
		          dataSource: {
		        	  autoSync: true,
		        	  transport: {
		        		   read:  {
	                            url: "/core/list/LnkAuditForm",
	                            contentType:"application/json; charset=UTF-8",     
	                            data: { "custom":"where appid="+$stateParams.issueId+" and parentid is null","sort":[{field: 'id', dir: 'asc'}]},
	                            type:"POST"
	                        },
	                        update: {
	                            url: "/core/update/"+$scope.domain1+"",
	                            contentType:"application/json; charset=UTF-8",                                    
	                            type:"POST",
	                            complete: function(e) {
		                         	if(e.responseText=="false"){			 		                            		
	                            		UIkit.notify("Алдаа үүслээ.", {status:'warning'});
	                            	}else{
	                            		UIkit.notify("Амжилттай хадгаллаа.", {status:'success'});
	                            	}
		                    	}
	                        },
	                        parameterMap: function(options) {
	                        	return JSON.stringify(options);
	                        }
		              },
		              schema: {
                     	data:"data",
                     	total:"total",
                     	 model: {                                	
                             id: "id",
                             fields: {   
                         	    id:  { nullable: false, type: "number"},
                                 parentid: { nullable: true, type: "number" },
                                 formid: { type: "number" },
                                 data1: { type: "string",editable:false },
                                 data2: { type: "string",editable:false },
                                 data3: { type: "string" ,editable:false},
                                 data4: { type: "string" },
                                 data5: { type: "string" },
                                 data6: { type: "string" },
                                 data7: { type: "string" },
                                 data8: { type: "string" ,editable:false},
                              }		                    
                         }
                     },
		            pageSize: 50,
		            serverPaging: true,
		            serverSorting: true
		          },
		          sortable: true,
		          pageable: true,
		          dataBound: function () {
		        	  
		        	  var grid =this;
		        	  grid.element.delegate("tbody>tr", "dblclick", function () {
		        	      grid.expandRow($(this));
		        	  });
  	              },
		        /*  dataBound: function() {
		            this.expandRow(this.tbody.find("tr.k-master-row").first());
		          },*/
		          columns: [
	                    { field: "data1", title: "Дугаар", width:90 },
	                    { field: "data2", title:"Чанарын баталгаажуулалтын асуулга" },
	                ],
		          editable: true
		        };
		
    		    var aj=[{"text":"Тийм","value":1},{"text":"Үгүй","value":0},{"text":"Үл хамаарах","value":2}];
    		    
		        $scope.ordersGridOptions = function(dataItem) {
		          return {
		            dataSource: {
		              autoSync: true,
		              transport: {
		            		read:  {
	            			    url: "/core/list/LnkAuditForm",
	                            contentType:"application/json; charset=UTF-8",     
	                            data: { "custom":"where appid="+$stateParams.issueId+" and data8=1","sort":[{field: 'id', dir: 'asc'}]},
	                            type:"POST"
	                        },
	                        update: {
	                            url: "/core/update/"+$scope.domain1+"",
	                            contentType:"application/json; charset=UTF-8",                                    
	                            type:"POST",
	                            complete: function(e) {
		                         	if(e.responseText=="false"){			 		                            		
	                            		UIkit.notify("Алдаа үүслээ.", {status:'warning'});
	                            	}else{
	                            		UIkit.notify("Амжилттай хадгаллаа.", {status:'success'});
	                            	}
		                    	}
	                        },
	                        parameterMap: function(options) {
	                        	return JSON.stringify(options);
	                        }
		              },
		              schema: {
                     	data:"data",
                     	total:"total",
                     	model: {                                	
                             id: "id",
                             fields: {   
                        	    id:  { nullable: false, type: "number"},
                                parentid: { nullable: true, type: "number" },
                                formid: { type: "number" },
                                data1: { type: "string",editable:false },
                                data2: { type: "string",editable:false },
                                data3: { type: "string" ,editable:false},
                                data4: { type: "number" },
                                data5: { type: "boolean" },
                                data6: { type: "boolean" },
                                data7: { type: "string" ,editable:false},
                                data8: { type: "string" ,editable:false},
                                data9: { type: "string"},
                             }	                    
                         }
                      },
		              serverPaging: true,
		              serverSorting: true,
		              serverFiltering: true,
		              pageSize: 50,
		              filter: { field: "parentid", operator: "eq", value: dataItem.formid }
		            },
		            scrollable: true,
		            sortable: true,
		            pageable: {
	                    refresh: true,
	                    pageSizes: true,
	                    buttonCount: 5
	                },/*
		            dataBound: function() {
		              this.expandRow(this.tbody.find("tr.k-master-row").first());
		            },*/
		            columns: [
	                    { field: "data1", title: "№", width:60 },
	                    { field: "data2", title:"Чанарын баталгаажуулалтын асуулга", width:250},
	                    { field: "data3", title:"АОУС /АДБОУС (Холбогдох дүрэм, журам, гарын авлага)", width:250},
	                    { field: "data4", title:"Шийдвэр", values:aj, width:120},
	                    { field: "data9", title:"Тайлбар", editor: $scope.categoryDropDownEditor, width:250},
	                    { field: "data7", title:"Загвар татах", template: kendo.template($("#downtemp").html()), width: 150},
	                    { field: "data8", title:"Маягт хавсрах", template: kendo.template($("#uptemp").html()), width: 150}
	                ],
		            editable: true
		          };
		        };
		        
		        $scope.planid=$stateParams.issueId;
    			
		        $scope.exportExcel = function(dataItem){
		        	 $rootScope.content_preloader_show();
		          	 mainService.withdomain('get','/api/excel/verify/nbb/'+dataItem.appid+'/'+dataItem.formid).then(function(response){
		           		 if(response!=false){
		           			 var link = document.createElement('a');
    	 					 link.href = '/api/excel/export/nbb/'+dataItem.appid+'/'+dataItem.formid;
    	 					 link.download = "Filename";
    	 					 link.click();	
    	 					 if(dataItem.data7=='АТ'){
    	 						 setTimeout(function(){
            	 					 $rootScope.content_preloader_hide();
    	                         }, 5000);
    	 					 }
    	 					 else{
    	 						 setTimeout(function(){
            	 					 $rootScope.content_preloader_hide();
    	                         }, 1000);
    	 					 }
    	 					
		           		 }
		           		 else{
		           			 sweet.show('Анхаар!', 'Excel тайлан оруулаагүй байна !!!', 'error');
		           			 $rootScope.content_preloader_hide();
		           		 }
		           		
		           	 });
	            }
		        
		    	
		        
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

	            $scope.selectize_val_options = [
	                { value: 'Нийт орлого', label: 'Нийт орлого' },
	                { value: 'Нийт зардал', label: 'Нийт зардал' },
	                { value: 'Нийт хөрөнгө', label: 'Нийт хөрөнгө' },
	                { value: 'Цэвэр хөрөнгө', label: 'Цэвэр хөрөнгө' }
	            ];

	            $scope.selectize_val_config = {
	                maxItems: 1,
	                valueField: 'value',
	                labelField: 'label',
	                create: false,
	                placeholder: 'Данс сонгох...',
	                onChange: function() {
	                    $timeout(function() {
	                        $formValidate.parsley().validate();
	                    })
	                }
	            };
	            
	            var modal_a_4 = UIkit.modal("#modal_a_4");
		        
		        $scope.selectA4 = function(x,y){
		        	cfpLoadingBar.start();
		        	$scope.sendBtn=true;
		        	$rootScope.content_preloader_show();           
			       	mainService.withdomain('get','/api/excel/a4/'+$scope.planid).then(function(response){
		           		modal_a_4.show();
		           		$rootScope.content_preloader_hide();           
		           		$scope.a4=response;
		           	});
	        	}
		        
		        
		        $scope.selectB31 = function(x,y){
		        	cfpLoadingBar.start();
		        	$scope.sendBtn=true;
		        //	$rootScope.content_preloader_show(); 
		        	
		        	$state.go('restricted.work.accSurvey',{planid:$scope.planid,formid:y});
		        	
			       /*	mainService.withdomain('get','/api/excel/a4/'+$scope.planid).then(function(response){
		           		$rootScope.content_preloader_hide();           
		           		$scope.a4=response;
		           	});*/
	        	}
		        
		        
	            $scope.submitA4 = function() {
			       $scope.sendBtn=false;
			       console.log($scope.a4data);
			       mainService.withdata('post','/api/excel/a4/'+$scope.planid, $scope.a4data).then(function(response){
			    	   console.log("asd");
		           });
		        };
		        
		        
		        var modal_upload = UIkit.modal("#modal_excel_upload");
		        $scope.uploadExcel = function(x,y){
		        	 cfpLoadingBar.start();
		        	 $scope.sendBtn=true;
		        	 progressbar.addClass("uk-hidden");
		        	 modal_upload.show();
		        	 $scope.noteid=y;
	        	}
		        
		    	
			    $scope.submitUpload = function() {
			       $scope.sendBtn=false;
			       if ($scope.formUpload.uploadfile.$valid && $scope.uploadfile) {
			    	   bar.css("width", "0%").text("0%");
                       progressbar.removeClass("uk-hidden");
			           $scope.upload($scope.uploadfile, $scope.noteid);
			       }
		        };
		     
    		    
    			var select = UIkit.uploadSelect($("#file_upload-select"), settings),
                drop   = UIkit.uploadDrop($("#file_upload-drop"), settings);
 
			    $('.dropify').dropify();

			    $('.dropify-mn').dropify({
	                messages: {
	                    default: 'Excel тайлан оруулна',
	                    replace: 'Excel тайлан шинээр оруулах бол энд дарна уу',
	                    remove:  'Солих',
	                    error:   'Алдаа үүслээ'
	                }
	            });

				$scope.modalExcel =function(){
    				modal.show();
    				$scope.sendBtn=true;
    				$scope.ars=[];
    				progressbar.addClass("uk-hidden");
    			}
				
			   $scope.submit = function() {
				   $scope.sendBtn=false;
			       if ($scope.form.file.$valid && $scope.file) {
			    	   bar.css("width", "0%").text("0%");
                       progressbar.removeClass("uk-hidden");
			           $scope.upload($scope.file,0);
			       }
		       };

		      // upload on file select or drop
		       $scope.upload = function (file,i) {
		    	   var xurl="";
		    	   if(i==0){
		    		   xurl ='/api/excel/upload/zagwarExcel/'+$stateParams.issueId;
		    	   }
		    	   else{
		    		   xurl ='/api/excel/upload/form/'+$stateParams.issueId+'/'+i;
		    	   }
		    	   
		          Upload.upload({
		              url: xurl,
		              data: {file: file, 'username': $scope.username}
		          }).then(function (resp) {
		        	  progressbar.removeClass("uk-hidden");
		              console.log('Success ' + resp.config.data.file.name + 'uploaded. Response: ' + resp.data);
		              if(resp.data.excel){
	                      UIkit.notify("Амжилттай хадгаллаа.", {status:'success'});
	                      modal.hide();
		              }
		              else if(resp.data==false){
		            	  UIkit.notify("Excel загвар тохирохгүй байна.", {status:'error'});
		              }
		              else{
		            	  $scope.errorAccount=true;
		            	  if(resp.data.support){
		            		  UIkit.notify("Алдаа үүслээ.", {status:'warning'});
		            		  progressbar.addClass("uk-hidden");
		            	  }
		            	  else{
		            		  console.log(resp);
		            		  if(resp.data!=null){
		            			  $scope.forms=resp.data;
		            			  angular.forEach($scope.forms, function(value, key){
		            				  UIkit.notify(value.fname+ " маягтыг амжилттай хавсарлаа.", {status:'success'});
            			          });
		            			//  console.log($scope.ordersGridOptions.return.dataSource.filter);
		            			 /* $scope.ordersGridOptions.return.dataSource.transport.read.data={
		      	  	    				"custom":"where appid= '"+$stateParams.issueId+"' " 
		      	  	    		  }*/
		      						
		            			  
		            			  $(".dgrid").data("kendoGrid").dataSource.read();
		            		  }
		            		  else{
		            			  UIkit.notify("Excel загвар тохирохгүй байна.", {status:'error'});
		            		  }		            		 
		            		  progressbar.addClass("uk-hidden");
		            	  }
		            	  $scope.ars=resp.data.error;
		            	//  modal.hide();
			            //  modal_upload.hide();
		              }
		            
		          }, function (resp) {
		              console.log('Error status: ' + resp.status);
		          }, function (evt) {
		              var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
		             
		              percent = progressPercentage;
                      bar.css("width", percent+"%").text(percent+"%");                    
		              console.log('progress: ' + progressPercentage + '% ' + evt.config.data.file.name);
		          });
		       };

		       
		        $scope.categoryDropDownEditor = function(container, options) {
			        var editor = $('<textarea cols="30" rows="2" class="k-textbox md-bg-red-100" style="float:left;height:100%;" data-bind="value: ' + options.field + '"></textarea>')
			        .appendTo(container);
			    }
   			        
   			$scope.errorAccount=false;        
   			//$scope.ars=[];
   		    var progressbar = $(".file_upload-progressbar"),
               bar         = progressbar.find('.uk-progress-bar'),
               settings    = {

                   action: '/api/excel/upload/zagwarExcel/'+$stateParams.issueId, // upload url

                   allow : '*.(xlsx,xls)', // allow only images

                   loadstart: function() {
                       bar.css("width", "0%").text("0%");
                       progressbar.removeClass("uk-hidden");
                   },

                   progress: function(percent) {
                       percent = Math.ceil(percent);
                       bar.css("width", percent+"%").text(percent+"%");
                   },

                   allcomplete: function(data) {

                       bar.css("width", "100%").text("100%");

                       setTimeout(function(){
                           progressbar.addClass("uk-hidden");
                       }, 250);
                       if(JSON.parse(data).excel){
                       	modal.hide();
                       }
                       else{
                           $scope.errorAccount=true;
                           UIkit.notify("Алдаа үүслээ.", {status:'warning'});
                           $scope.ars=JSON.parse(data).error;
                           modal.hide();
                       
                       }
                   }
               };
    		
   		    
    	 }
    ]);
