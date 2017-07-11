angular
    .module('altairApp')
    	.controller("fileCtrl",['$scope','$timeout','Upload','p_menu','user_data','mainService','$state','sweet',
	        function ($scope,$timeout,Upload,p_menu,user_data,mainService,$state,sweet) {       	
	    		var aj=p_menu;
	    		var init={"text":"ROOT","value":"null"};	    	
				aj.push(init);
				
	        	$scope.domain="com.nbb.models.FileUpload.";
	        	
	        	var progressbar = $("#file_upload-progressbar"),
	                bar         = progressbar.find('.uk-progress-bar'),
	                settings    = {

	                    action: '/api/file/upload/zagwarExcel', // upload url

	                    allow : '*.(xlsx)', // allow only images

	                    loadstart: function() {
	                        bar.css("width", "0%").text("0%");
	                        progressbar.removeClass("uk-hidden");
	                    },

	                    progress: function(percent) {
	                        percent = Math.ceil(percent);
	                        bar.css("width", percent+"%").text(percent+"%");
	                    },

	                    allcomplete: function(response) {

	                        bar.css("width", "100%").text("100%");

	                        setTimeout(function(){
	                            progressbar.addClass("uk-hidden");
	                        }, 250);
	                        $(".k-grid").data("kendoGrid").dataSource.read(); 
	                      //  alert("Upload Completed")
	                    }
	                };
	        	
					$scope.download = function(id){
					  mainService.withdomain('get', '/api/files/'+id).
						then(function(data){
							$scope.formdata=data[0];
							modalUpdate.show();
						});  
					}
					  
					$scope.deleteO = function(id){
					    	 
							sweet.show({
					        	   title: 'Баталгаажуулалт',
					    text: 'Та устгахдаа итгэлтэй байна уу?',
					    type: 'warning',
					    showCancelButton: true,
					    confirmButtonColor: '#DD6B55',
					    confirmButtonText: 'Тийм',
					    cancelButtonText: 'Үгүй',
					    closeOnConfirm: false,
					    closeOnCancel: false
					  
						}, function(inputvalue) {
						 if (inputvalue) {
							 $scope.formdata = {};
							 $scope.formdata.id = id;
							 mainService.withdata('POST', '/core/delete/'+$scope.domain,  $scope.formdata).
								then(function(data){
					    			$(".k-grid").data("kendoGrid").dataSource.read(); 
						   			sweet.show('Анхаар!', 'Амжилттай устлаа.', 'success');
								});
					    }else{
					        sweet.show('Анхаар!', 'Устгах үйлдэл хийгдсэнгүй!!!', 'error');
					            }    		
					        });
					  }

	        	  	var select = UIkit.uploadSelect($("#file_upload-select"), settings),
	                drop   = UIkit.uploadDrop($("#file_upload-drop"), settings);
	        	
	        	  	 $scope.selectize_val_options = [
	 	                { value: 1, label: 'ТШЗ' },
	 	                { value: 2, label: 'ААН' }
	 	            ];

	 	            $scope.selectize_val_config = {
	 	                maxItems: 1,
	 	                valueField: 'value',
	 	                labelField: 'label',
	 	                create: false,
	 	                placeholder: 'ААН төрөл сонгох...',
	 	                onChange: function() {
	 	                    $timeout(function() {
	 	                        $formValidate.parsley().validate();
	 	                    })
	 	                }
	 	            };
    	    
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
		            var modal = UIkit.modal("#modal_a_4");
	        	  	$scope.addnew = function(){
	        	  		$scope.sendBtn=true;
	        	  		modal.show();
	        	  	}
	        	  	
	        	  	
	        	    $scope.submitUpload = function() {
	 			       $scope.sendBtn=false;
	 			       if ($scope.formUpload.uploadfile.$valid && $scope.uploadfile) {
	 			    	   bar.css("width", "0%").text("0%");
	                        progressbar.removeClass("uk-hidden");
	 			           $scope.upload($scope.uploadfile, $scope.aan);
	 			       }
	 		        };
	 		        
	 		       // upload on file select or drop
	 		       $scope.upload = function (file,i) {
	 		    	   var xurl="";
	 		    	   console.log(i);
	 		    	   if(i!=0){
	 		    		   xurl ='/api/file/upload/zagwarExcel/'+$scope.aan;
	 		    	   }
	 		    	   
	 		          Upload.upload({
	 		              url: xurl,
	 		              data: {file: file, 'username': $scope.username}
	 		          }).then(function (resp) {
	 		        	  progressbar.removeClass("uk-hidden");
	 		              console.log('Success ' + resp.config.data.file.name + 'uploaded. Response: ' + resp.data);
	 		              $(".k-grid").data("kendoGrid").dataSource.read(); 
	 		              if(resp.data.excel){
	 	                      UIkit.notify("Амжилттай хадгаллаа.", {status:'success'});
	 	                      modal.hide();
	 		              }
	 		              else if(resp.data==false){
	 		            	  UIkit.notify("Excel загвар тохирохгүй байна.", {status:'error'});
	 		              }
	 		              modal.hide();
	 		          }, function (resp) {
	 		              console.log('Error status: ' + resp.status);
	 		          }, function (evt) {
	 		              var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
	 		             
	 		              percent = progressPercentage;
	                       bar.css("width", percent+"%").text(percent+"%");                    
	 		              console.log('progress: ' + progressPercentage + '% ' + evt.config.data.file.name);
	 		          });
	 		       };
	        	  	
	        	  	
	     			$scope.fileGrid = {
 		                dataSource: {
 		                   
 		                    transport: {
 		                    	read:  {
 		                            url: "/core/list/FileUpload",
 		                            contentType:"application/json; charset=UTF-8",     
 		                            data: {"sort":[{field: 'id', dir: 'desc'}]},
 		                            type:"POST"
 		                        },
 		                        update: {
 		                            url: "/core/update/"+$scope.domain+"",
 		                            contentType:"application/json; charset=UTF-8",                                    
 		                            type:"POST",
 		                            complete: function(e) {
 		                            	$(".k-grid").data("kendoGrid").dataSource.read(); 
 		                    		}
 		                        },
 		                        destroy: {
 		                            url: "/core/delete/"+$scope.domain+"",
 		                            contentType:"application/json; charset=UTF-8",                                    
 		                            type:"POST"
 		                        },
 		                        create: {
 		                        	url: "/core/create/"+$scope.domain+"",
 		                            contentType:"application/json; charset=UTF-8",                                    
 		                            type:"POST",
 		                            complete: function(e) {
 		                            	$(".k-grid").data("kendoGrid").dataSource.read(); 
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
 		                             	id: { editable: false,nullable: true},
 		                             	menuname: { type: "string", validation: { required: true } },
 		                             	stateurl: { type: "string", defaultValue:'#'},
 		                                uicon: { type: "string"},
 		                                parentid: { type: "number"},
 		                                orderid: { type: "number" }
 		                             }
 		                         }
 		                     },
 		                    pageSize: 10,
 		                    serverFiltering: true,
 		                    serverPaging: true,
 		                    serverSorting: true
 		                },
 		                //toolbar: ["create"],
 		                toolbar: kendo.template($("#add").html()),		
 		                filterable:{
 			                	 mode: "row"
 			                },
 		                sortable: true,
 		                resizable: true,
 		                pageable: {
 		                    refresh: true,
 		                    pageSizes: true,
 		                    buttonCount: 5
 		                },
 		                columns: [
 		                	 	  {title: "#",template: "<span class='row-number'></span>", width:"60px"},
 		                          { field:"filename", title: "Нэр /Mn/" },
 		                          { field: "name", title:"URL" },
 		                          { field: "filesize", title:"IKON"},
 		                          { field: "fileurl", title:"Эцэг цэс"},
 		                          { field: "mimetype", title:"Дараалал", width: "200px" },
 		                          {template: kendo.template($("#extend").html()), width: "200px"}
                        ],
                        dataBound: function () {
   		                var rows = this.items();
   		                  $(rows).each(function () {
   		                      var index = $(this).index() + 1 
   		                      + ($(".k-grid").data("kendoGrid").dataSource.pageSize() * ($(".k-grid").data("kendoGrid").dataSource.page() - 1));;
   		                      var rowLabel = $(this).find(".row-number");
   		                      $(rowLabel).html(index);
   		                  });
   		  	           },
 		                      
 		          };  	
	        
	        }
    ]);
