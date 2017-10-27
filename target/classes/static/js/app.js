
var app = angular.module('mainApp', ['ngRoute']);

app.config(function($routeProvider){
	$routeProvider
	.when('/home',{
		templateUrl: '/views/home.html',
		controller: 'homeController'
	})
	.when('/user',{
		templateUrl: '/views/home.html',
		controller: 'editTicketController'
	})
	.when('/login',{
		templateUrl: '/views/login.html',
		controller: 'loginController'
	})
	.when('/ticket_creation',{
		templateUrl: '/views/ticket_creation.html',
		controller: 'ticketCreationController'
	}) 
	.when('/user_creation',{
		templateUrl: '/views/userCreation.html',
		controller: 'userCreationController'
	})
	.when('/users_list',{
		templateUrl: '/views/allUserList.html',
		controller: 'dispayAllUsersController'
	})
	.when('/tickets_list',{
		templateUrl: '/views/allTicketList.html',
		controller: 'dispayAllTicketsController'
	})
	.when('/ticket',{
		templateUrl: '/views/ticketList.html',
		controller: 'ticketController'
	})  
	.when('/change_password',{
		templateUrl: '/views/changePassword.html',
		controller: 'ChangePassword'
	}) 
	.when('/reset_password',{
		templateUrl: '/views/resetPassword.html',
		controller: 'ResetPassword'
	}) 
	.when('/sign_out',{
		templateUrl: '/views/login.html',
		controller: 'Signout'
	}) 
	
	.otherwise(
			{ redirectTo: '/login'}
	);
});

app.controller("loginController", ['$scope','$location', '$http', function($scope, $location, $http) {

	
	$scope.login = function () {

		var username = $scope.postCtrl.inputData.username; 
		var password = $scope.postCtrl.inputData.password;

		alert(JSON.stringify({username, password}));
		$http({
			method: 'POST',
			url: '/login/',
			data: JSON.stringify({username, password}),
			headers: {'Content-Type': 'application/json'}
		})
		.success(function(data, status, headers, config) {
			/*console.log(data.object);
			console.log(data.object.userRole);
			console.log(data.object.name);
			*/
			if ( data.status === 'SUCCESS') {
				//$window.location.href = '/home';
				//$scope.loggedInUser = data.object.name;
				//$scope.userRoleOfLoggedInUser = 'abc';
				$location.path('/home');
			} else {
				$scope.errorMsg = "Invalid Login";
			}
		})
		.error(function(data, status, headers, config) {
			$scope.errorMsg = 'Unable to submit form';
		}) 
	}
	
	$scope.forgetPassword = function(){
		alert("Please contact your manager to reset your password.");
	}

}]);

app.controller("ticketController", ['$scope','$location', '$http', '$route', function($scope, $location, $http, $route) {

	// Get the modal for 'Delete' action 
	var deleteModal = document.getElementById('myDeleteModal');
	
	// Get the modal for 'Edit' action 
	var editModal = document.getElementById('myEditModal');

	// Get the button that opens the modal 
	var btn = document.getElementsByClassName("myDeleteBtn");

	$scope.getMyTickets = function() {

		$http({
			method: 'GET',
			url: '/ticket',
			data: '',
			headers: {'Content-Type': 'application/json'}
		})

		.success(function(data, status, headers, config) {
			console.log(data);
			$scope.myTicketList = data.listObject;			
			$scope.userRole = data.userRole;
			$scope.loggedInUser = data.tempUser;
			//console.log('**'+data.tempUser);
			//console.log('loggedInUser= '+data.username);
		})
		.error(function(data, status, headers, config) {
			console.log(data);
			$scope.errorMsg = 'Unable to Get result...!!!!!';
		}) 
	}
	
	
	$scope.updateThisTicket = function(ticketId) {
		
		var ticketId = document.getElementById('ticketId').value;
		var ticketForEdit = document.getElementById('ticketForEdit').value;
		
		var subject = document.getElementById('subject').value; 
		var description = document.getElementById('description').value; 
		//var state = document.getElementById('state').value; 
		//var state1 = document.getElementById('state').text; 
		var state = $scope.selectedState;
		var priority = $scope.radioValue;
		var assignedTo;
		alert(' '+$scope.selectedUser);
		if ($scope.selectedUser === undefined){
			assignedTo = document.getElementById('currentLoggedInUser').value;
			//console.log('@@@'+assignedTo);
		} else {
			assignedTo = $scope.selectedUser.username; 
		}
		
		alert(assignedTo);

		$http({
			method: 'PUT',
			url: '/ticket',
			data: JSON.stringify({ticketId, subject, description, priority, state, assignedTo}),
			params: {ticketId},
			headers: {'Content-Type': 'application/json'}
		})

		.success(function(data, status, headers, config) {
			editModal.style.display = "none";
			alert('Ticket updated successfully..!');
			$route.reload();//this will refresh the ticket list after update operation
		})
		.error(function(data, status, headers, config) {
			console.log(data);
			$scope.errorMsg = 'Unable to Update this Ticket...!!!!!';
		}) 				
	}	

	$scope.deleteThisTicket = function(ticketId) {

		$http({
			method: 'DELETE',
			url: '/ticket',
			data: '',
			params: {ticketId},
			headers: {'Content-Type': 'application/json'}
		})

		.success(function(data, status, headers, config) {
			deleteModal.style.display = "none";
			alert('Ticket deleted successfully..!');
			$route.reload();//this will refresh the ticket list after delete operation
		})
		.error(function(data, status, headers, config) {
			console.log(data);
			$scope.errorMsg = 'Unable to Delete this Ticket...!!!!!';
		}) 
	}
	$scope.openDeleteModal = function(ticketId) {
		$scope.ticketToDelete = ticketId;
		// When the user clicks the button, open the modal 
		deleteModal.style.display = "block";
	}
	$scope.closeDeleteModal = function() {		
		// When the user clicks on 'Close' button, Close the modal 
		deleteModal.style.display = "none";		
	}

}]);

app.controller("dispayAllUsersController", ['$scope','$location', '$http', '$route', function($scope, $location, $http, $route) {
	
	// Get the modal for 'Delete' action 
	var deleteModal = document.getElementById('myDeleteModal');
	
	// Get the modal for 'Edit' action 
	var editModal = document.getElementById('myEditModal');

	// Get the button that opens the modal 
	var btn = document.getElementsByClassName("myDeleteBtn");
	
	$scope.getAllUsers = function() {
		//window.location.href = 'ticketList.html';
		$http({
			method: 'GET',
			url: '/user',
			data: '',
			headers: {'Content-Type': 'application/json'}
		})

		.success(function(data, status, headers, config) {
			console.log(data);
			$scope.myUserList = data.listObject;

		})
		.error(function(data, status, headers, config) {
			console.log(data);
			$scope.errorMsg = 'Unable to Get users...!!!!!';
		}) 
	}
	
	$scope.getThisUserToEdit = function(username) {
		
		//window.location.href = 'ticketList.html';
		$http({
			method: 'GET',
			url: '/editUser',
			params: {username},
			data : '',
			headers: {'Content-Type': 'application/json'}
		})

		.success(function(data, status, headers, config) {
			console.log('In success '+data);
			$scope.userForEdit = data.object;
			$scope.radioValue =  data.object.userRole;
			console.log(data.object.userRole);

			// When the user clicks the button, open the modal 
			editModal.style.display = "block";
		})
		.error(function(data, status, headers, config) {
			console.log(data);
			$scope.errorMsg = 'Unable to Get result...!!!!!';
		}) 
	}
	
	$scope.deleteThisUser = function(username) {

		$http({
			method: 'DELETE',
			url: '/user',
			data: '',
			params: {username},
			headers: {'Content-Type': 'application/json'}
		})

		.success(function(data, status, headers, config) {
			deleteModal.style.display = "none";
			alert('User deleted successfully..!');
			$route.reload();//this will refresh the user list after delete operation
		})
		.error(function(data, status, headers, config) {
			console.log(data);
			$scope.errorMsg = 'Unable to Delete this User...!!!!!';
		}) 
	}
	$scope.openDeleteModal = function(username) {
		$scope.userToDelete = username;
		// When the user clicks the button, open the modal 
		deleteModal.style.display = "block";
	}
	$scope.closeDeleteModal = function() {		
		// When the user clicks on 'Close' button, Close the modal 
		deleteModal.style.display = "none";		
	}
	$scope.closeEditModal = function() {		
		// When the user clicks on 'Cancle' button, Close the modal 
		editModal.style.display = "none";		
	}
	
	$scope.updateThisUser = function(username) {

		var userForEdit = document.getElementById('userForEdit');
		var name = document.getElementById('name').value; 
		var email = document.getElementById('email').value; 
		var username = document.getElementById('username').value; 
		var userRole = $scope.radioValue;

		$http({
			method: 'PUT',
			url: '/user',
			data: JSON.stringify({name, email, username, userRole}),
			params: {username},
			headers: {'Content-Type': 'application/json'}
		})

		.success(function(data, status, headers, config) {
			editModal.style.display = "none";
			alert('User updated successfully..!');
			$route.reload();//this will refresh the user list after update operation
		})
		.error(function(data, status, headers, config) {
			console.log(data);
			$scope.errorMsg = 'Unable to Update this User...!!!!!';
		}) 		
	}
	$scope.cross = function() {		
		// When the user clicks on 'Cross' button, Close the modal 
		editModal.style.display = "none";		
		}

}]);

app.controller('homeController',['$scope','$location', '$http', function($scope, $location, $http)  {
	$scope.headingTitle = "Home cc";
	$scope.usersList = [];
	$scope.radioValue = "Low";
	
	$scope.states = "New,Open,Fixed,Verified Fixed,Connot Reproduce,As Designed,Documented,In Progress";

	$http({
		method: 'GET',
		url: 'http://localhost:8080/user/',
		data: {},
		headers: {'Content-Type': 'application/json'}
	}).success(function (result) {
		$scope.usersList = result.listObject;//This will bring user's list in 'Assigned To' drop down		
		/*console.log('$scope.usersList = '+$scope.usersList);
		for(var i in $scope.usersList){
			console.log($scope.usersList[i]);
		}*/	
	});

	// Get the modal for 'Edit' action 
	var editModal = document.getElementById('myEditModal');

	// Get the button that opens the modal 
	var btn = document.getElementsByClassName("myEditBtn");
	console.log(btn);
	// Get the <span> element that closes the modal
	var span = document.getElementsByClassName("close")[0];

	$scope.getThisTicketToEdit = function(ticketId) {

		//window.location.href = 'ticketList.html';
		$http({
			method: 'GET',
			url: '/editTicket',
			params: {ticketId},
			data : '',
			headers: {'Content-Type': 'application/json'}
		})

		.success(function(data, status, headers, config) {
			console.log(data);
			$scope.ticketForEdit = data.object;
			$scope.radioValue = data.object.priority;
			/*console.log('Radio value='+data.object.priority);*/
			$scope.selectedPriority = data.object.priority;

			// When the user clicks the button, open the modal 
			editModal.style.display = "block";

		})
		.error(function(data, status, headers, config) {
			console.log(data);
			$scope.errorMsg = 'Unable to Get result...!!!!!';
		}) 
	}

	$scope.closeEditModal = function() {		
		// When the user clicks on 'Close' button, Close the modal 
		editModal.style.display = "none";			
	}

	// When the user clicks on <span> (x), close the modal
	/*span.onclick = function() {
		editModal.style.display = "none";
	} */ 
	$scope.cross = function() {		
		// When the user clicks on 'Cross' button, Close the modal 
		editModal.style.display = "none";		
	}

}]);

	app.controller('ticketCreationController',  ['$scope','$location', '$http', function($scope, $location, $http) {
		$scope.selectedUsers = null;
		$scope.usersListOnCreateTicket = [];
		$scope.radioValue = "Low";
		$http({
			method: 'GET',
			url: 'http://localhost:8080/user/',
			data: {},
			headers: {'Content-Type': 'application/json'}
		}).success(function (result) {
			$scope.usersListOnCreateTicket = result.listObject;
		});

		$scope.createTicket = function () {
			var subject = $scope.postCtrl.inputData.subject; 
			var description = $scope.postCtrl.inputData.description;
			var priority = $scope.radioValue;
			var assignedTo = $scope.selectedUser.username;
			
			$http({
				method: 'POST',
				url: '/ticket/',
				data: JSON.stringify({subject, description, priority, assignedTo}),
				headers: {'Content-Type': 'application/json'}
			})
			.success(function(data, status, headers, config) {
				if ( data.status === 'SUCCESS') {
					$location.path('/home');
				} else {
					$scope.errorMsg = "errr";
				}
			})
			.error(function(data, status, headers, config) {
				$scope.errorMsg = 'Unable to submit form';
			}) 
		}    
	}]);

	
	app.controller('userCreationController',  ['$scope','$location', '$http', function($scope, $location, $http) {

		$scope.radioValue = "Worker";
		
		$scope.createUser = function () {
			var name = $scope.userCtrl.inputData.name; 
			var email = $scope.userCtrl.inputData.email;
			var username = $scope.userCtrl.inputData.username;
			
			var userRole = $scope.radioValue;
			//alert('name= '+name+'email= '+email+'userRole= '+userRole);
			$http({
				method: 'POST',
				url: '/user/',
				data: JSON.stringify({name, email, username, userRole}),
				headers: {'Content-Type': 'application/json'}
			})
			.success(function(data, status, headers, config) {
				console.log('data.status='+data.status);
				if ( data.status === 'SUCCESS') {
					$location.path('/users_list');
					alert('User has been created successfully..!!');
				} else {
					$scope.errorMsg = data.errorMsg;
				}
			})
			.error(function(data, status, headers, config) {
				$scope.errorMsg = 'Unable to submit form';
			}) 
		}    
	}]);
		
	app.controller('ChangePassword',  ['$scope','$location', '$http', function($scope, $location, $http) {
		
		$scope.changePassword = function(){
			var username = $scope.changePw.inputData.username; 
			var oldPassword = $scope.changePw.inputData.oldPassword;
			var newPassword = $scope.changePw.inputData.newPassword;
			var confirmPassword = $scope.changePw.inputData.confirmPassword;
			
			alert(' '+username+' '+oldPassword+' '+newPassword+' '+confirmPassword+' ');
			
			$http({
				method: 'PUT',
				url: '/changePassword/',
				data: JSON.stringify({username, oldPassword, newPassword, confirmPassword}),
				headers: {'Content-Type': 'application/json'}
			})
			.success(function (data, status, headers, config) {
				console.log('data= '+data.status);
				if ( data.status === 'SUCCESS') {					
					alert(data.errorMsg);
					$location.path('/login');
				} else {
					$scope.errorMsg = data.errorMsg;
				}				
			})
			.error(function(data, status, headers, config) {
				$scope.errorMsg = 'Unable to Change the password';
			})
		}
	}]);
	
app.controller('ResetPassword',  ['$scope','$location', '$http', function($scope, $location, $http) {
		
		$scope.resetPassword = function(){
			var username = $scope.resetPw.inputData.username; 
			var email = $scope.resetPw.inputData.email;
			
			alert(' '+username+' '+email+' ');
			
			$http({
				method: 'PUT',
				url: '/resetPassword/',
				data: JSON.stringify({username, email}),
				headers: {'Content-Type': 'application/json'}
			})
			.success(function (data, status, headers, config) {
				//$scope.usersListOnCreateTicket = result.listObject;
				console.log('data= '+data.status);
				if ( data.status === 'SUCCESS') {					
					alert(data.errorMsg);
					$location.path('/home');
				} else {
					$scope.errorMsg = data.errorMsg;
				}				
			})
			.error(function(data, status, headers, config) {
				$scope.errorMsg = 'Unable to Reset the password';
			})
		}
	}]);

app.controller('Signout',  ['$scope','$location', '$http', function($scope, $location, $http) {
	
	$scope.signout = function(){
		
		$http({
			method: 'GET',
			url: '/login/',
			data: '',
			headers: {'Content-Type': 'application/json'}
		})
		.success(function (data, status, headers, config) {
			console.log('data= '+data.status);
			if ( data.status === 'SUCCESS') {					
				alert(data.errorMsg);
				$location.path('/login');
			} else {
				$scope.errorMsg = "Some Error happened";
			}				
		})
		.error(function(data, status, headers, config) {
			$scope.errorMsg = 'Unable to signout';
		})
	}
}]);

app.controller("dispayAllTicketsController", ['$scope','$location', '$http', '$route', function($scope, $location, $http, $route) {

	$scope.usersList = [];
	
	// Get the modal for 'Edit' action 
	var editModal = document.getElementById('myEditModal');
	
	// Get the modal for 'Delete' action 
	var deleteModal = document.getElementById('myDeleteModal');

	// Get the button that opens the modal 
	var btn = document.getElementsByClassName("myEditBtn");
	
	console.log(btn);
	// Get the <span> element that closes the modal
	var span = document.getElementsByClassName("close")[0];
	var crossBtn = document.getElementById("cross");
	
	$scope.getAllTickets = function() {

		$http({
			method: 'GET',
			url: '/allTickets',
			data: '',
			headers: {'Content-Type': 'application/json'}
		})

		.success(function(data, status, headers, config) {
			console.log(data);
			$scope.myTicketList = data.listObject;			
		})
		.error(function(data, status, headers, config) {
			console.log(data);
			$scope.errorMsg = 'Unable to Get result...!!!!!';
		}) 
	}
	
	$scope.deleteThisTicketFromList = function(ticketId) {

		$http({
			method: 'DELETE',
			url: '/ticket',
			data: '',
			params: {ticketId},
			headers: {'Content-Type': 'application/json'}
		})

		.success(function(data, status, headers, config) {
			deleteModal.style.display = "none";
			alert('Ticket deleted successfully..!');
			$route.reload();//this will refresh the ticket list after delete operation
		})
		.error(function(data, status, headers, config) {
			console.log(data);
			$scope.errorMsg = 'Unable to Delete this Ticket...!!!!!';
		}) 
	}
	
	$scope.getThisTicketForEdit = function(ticketId) {

		$scope.states = "New,Open,Fixed,Verified Fixed,Connot Reproduce,As Designed,Documented,In Progress";
		
		$http({
			method: 'GET',
			url: '/editTicket',
			params: {ticketId},
			data : '',
			headers: {'Content-Type': 'application/json'}
		})

		.success(function(data, status, headers, config) {
			console.log(data);
			$scope.ticketForEdit = data.object;
			$scope.radioValue = data.object.priority;
			$scope.selectedPriority = data.object.priority;

			// When the user clicks the button, open the modal 
			editModal.style.display = "block";

		})
		.error(function(data, status, headers, config) {
			console.log(data);
			$scope.errorMsg = 'Unable to Get result...!!!!!';
		}) 
	}
	
	$scope.updateThisTicketToDB = function(ticketId) {
		
		var ticketId = document.getElementById('ticketId').value;
		var ticketForEdit = document.getElementById('ticketForEdit').value;		
		var subject = document.getElementById('subject').value; 
		var description = document.getElementById('description').value;  
		var state = $scope.selectedState;
		var priority = $scope.radioValue;
		var assignedTo;
		
		alert(' '+$scope.selectedUser);
		if ($scope.selectedUser === undefined){
			assignedTo = document.getElementById('currentLoggedInUser').value;
			console.log('@@@'+assignedTo);
		} else {
			assignedTo = $scope.selectedUser.username; 
		}		
		alert(assignedTo);

		$http({
			method: 'PUT',
			url: '/ticket',
			data: JSON.stringify({ticketId, subject, description, priority, state, assignedTo}),
			params: {ticketId},
			headers: {'Content-Type': 'application/json'}
		})

		.success(function(data, status, headers, config) {
			editModal.style.display = "none";
			alert('Ticket updated successfully..!');
			$route.reload();//this will refresh the ticket list after update operation
		})
		.error(function(data, status, headers, config) {
			console.log(data);
			$scope.errorMsg = 'Unable to Update this Ticket...!!!!!';
		}) 				
	}
	
	$scope.getUsersListInDropDown = function() {
		$http({
			method: 'GET',
			url: 'http://localhost:8080/user/',
			data: {},
			headers: {'Content-Type': 'application/json'}
		}).success(function (result) {
			$scope.usersList = result.listObject;//This will bring user's list in 'Assigned To' drop down		
			/*console.log('$scope.usersList = '+$scope.usersList);
			for(var i in $scope.usersList){
				console.log($scope.usersList[i]);
			}*/	
		});	
	}

	$scope.openDeleteModal = function(ticketId) {
		$scope.ticketToDelete = ticketId;
		// When the user clicks the button, open the modal 
		deleteModal.style.display = "block";

	}
	$scope.closeDeleteModal = function() {		
		// When the user clicks on 'Close' button, Close the modal 
		deleteModal.style.display = "none";		
	}
	$scope.closeEditModal = function() {		
		// When the user clicks on 'Cancle' button, Close the modal 
		editModal.style.display = "none";		
	}
	$scope.cross = function() {		
		// When the user clicks on 'Cross' button, Close the modal 
		editModal.style.display = "none";		
	}	

}]);
