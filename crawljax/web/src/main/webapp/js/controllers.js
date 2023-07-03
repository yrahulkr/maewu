app.controller('ConfigIndexController', ['$rootScope', 'configs', function($rootScope, configs){
	$rootScope.configurations = configs;
}]);

app.controller('ConfigController', ['$scope', '$rootScope', '$state', 'configAdd', 'configHttp', 'historyHttp', 'restService', 'config', 
                                    function($scope, $rootScope, $state, configAdd, configHttp, historyHttp, restService, config){
	$scope.config = config;
	
	restService.changeRest(function(link){
		switch(link.target){
			case 'run':
				historyHttp.addCrawl(config);
				break;
			case 'save':
				if(validateForm('config_form')) configHttp.updateConfiguration($scope.config, $rootScope.$stateParams.configId);
				break;
			case 'delete':
				if(confirm('Are you sure you want to delete this configuration?')){
					configHttp.deleteConfiguration(config, $rootScope.$stateParams.configId).then(function(){
						$state.go('config');
					});
				}
				break;
			default:
				break;
		}
	});
	
	$scope.configAdd = configAdd;
}]);

app.controller('ConfigNewController', ['$scope', '$rootScope', '$state', 'restService', 'configHttp', 'config', 
                                       function($scope, $rootScope, $state, restService, configHttp, config){
	$scope.config = config;
	
	restService.changeRest(function(link){
		switch(link.target){
			case 'save':
				if(validateForm('config_form')){
					var idSource = configHttp.postConfiguration($scope.config);
					idSource.then(function(result){
						$state.go('configDetail.main', {configId: result.data.id});
					})
				}
				break;
			default:
				break;
		}
	});
}]);

app.controller('ConfigCopyController', ['$scope', '$rootScope', '$state', 'restService', 'configHttp', 'config', 
                                        function($scope, $rootScope, $state, restService, configHttp, config){
	$scope.config = config;
	
	restService.changeRest(function(link){
		switch(link.target){
			case 'save':
				if(validateForm('config_form')){
					var idSource = configHttp.postConfiguration($scope.config);
					idSource.then(function(result){
						$state.go('configDetail.main', {configId: result.data.id});
					});
				}
				break;
			default:
				break;
		}
	});
}]);

app.controller('TestIndexController', ['$rootScope', '$scope', '$state', 'testHttp', 'restService', 'testRecords', 
                                          function($rootScope, $scope, $state, testHttp, restService, testRecords){
	$rootScope.testRecords = testRecords;

	$scope.goToTest = function(id){
		$state.go('test', {testId: id});	
	};

	restService.changeRest(function(link){
		switch(link.target){
			case 'runTest':
				testHttp.addTestRecord();
				break;
		}
	});
	
}]);

app.controller('TestRecordController', ['$rootScope', '$scope', 'testHttp', 'restService', 'test',
																				function($rootScope, $scope, testHttp, restService, test) {
	$scope.test = test;
	$scope.getImageData = "";
	$scope.showImage = false;
	$scope.getImage = function(imageName, testId){
    testHttp.getTestImageData(imageName, testId).then(function(data){
    	$scope.getImageData = data;
    	$scope.showImage = true;
    	$scope.$apply();
    });
    $scope.hideImage = function(){
    	$scope.showImage = false;
    	$scope.$apply();
    }

 };
		//return imageName;
}]);

app.controller('TestDiffsController', ['$rootScope', '$scope', 'testHttp', 'restService', 'test', 'testImages',
																				function($rootScope, $scope, testHttp, restService, test, testImages) {
	$scope.test = test;
	$scope.testImages = testImages
	$scope.oldImage = "";
	$scope.newImage = "";
	$scope.showImage = false;
	$scope.getImage = function(stateName){
    	angular.forEach($scope.testImages.diffImages, function(value, index){
    		if(value.stateName == stateName ){
    			$scope.oldImage = value.oldImage;
    			$scope.newImage = value.newImage;
    			$scope.showImage = true;
    		}
    	});
    };
	$scope.hideImage = function(){
    	$scope.showImage = false;
    }
		//return imageName;
}]);

app.controller('HistoryIndexController', ['$rootScope', '$scope', '$filter', '$state', 'crawlRecords', 
                                          function($rootScope, $scope, $filter, $state, crawlRecords){
	$rootScope.crawlRecords = crawlRecords;
	
	$scope.goToCrawl = function(status, id){
		if(status == 'success'){
			$state.go('crawl.pluginOutput', {crawlId: id, pluginId: 'crawl-overview'});
		} else{
			$state.go('crawl.log', {crawlId: id});
		}
	};
}]);
app.controller('CrawlRecordController', ['$scope', '$rootScope', '$sce', 'historyHttp', 'socket', 'crawl', 
                                         function($scope, $rootScope, $sce, historyHttp, socket, crawl){
	$scope.crawl = crawl;
	$scope.log = '';
	
	$scope.$on('log-update', function(event, args){
		$scope.log = $sce.trustAsHtml(args.newLog);
		console.log('log-update');
		console.log(args.newLog);
		console.log($scope.log);
	});
	
	if ($scope.isLogging) {
		socket.sendMsg('stoplog');
	}
	setTimeout(function(){ 
		$('#logPanel').empty();
		socket.log = '';
		socket.sendMsg('startlog-' + $scope.crawl.id);
		$scope.isLogging = true;
	}, 0);
	
	$scope.$on('$destroy', function(){
		$scope.isLogging = false;
		socket.sendMsg('stoplog');
	});
	
	angular.element("#sideNav").scope().configId = crawl.configurationId;
}]);

app.controller('CrawlRecordPluginController', ['$scope', '$rootScope', '$sce', function($scope, $rootScope, $sce){
	$scope.trustedUrl = $sce.trustAsResourceUrl('/output/crawl-records/' + $rootScope.$stateParams.crawlId + '/plugins/' + $rootScope.$stateParams.pluginId)
}]);

app.controller('BreadcrumbController', ['$scope', function($scope){
	$scope.links = [];
}]);

app.controller('SideNavController', ['$scope', '$rootScope', 'restService', function($scope, $rootScope, restService){
	$scope.links = [];
	$scope.$on('restChanged', function(){
		$scope.rest = restService.rest;
	})
}]);

app.controller('CrawlQueueController', ['$scope', '$state', 'socket', function($scope, $state, socket){
	$scope.queue = angular.copy(socket.executionQueue);
	$scope.$on('queue-update', function(event, args){
		$scope.queue = args.newQueue;
		$scope.$apply();
	});
	
	$scope.goToCrawl = function(status, id){
		if(status == 'success'){
			$state.go('crawl.pluginOutput', {crawlId: id, pluginId: 'crawl-overview'});
		} else{
			$state.go('crawl.log', {crawlId: id});
		}
	};
}]);
