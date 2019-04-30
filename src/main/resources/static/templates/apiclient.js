apiclient=(function(){
	return {
		getCinemaByName:function(name,callback){
			console.log(name);
			$.get("/cinemas/" + name, function(data) {
				callback(data);
			});
		}
	}

})();