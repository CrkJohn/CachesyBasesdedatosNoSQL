app=(function(){
	
	return{
		getCinemaByName:function(cinemaname){
			
			return apimock.getCinemaByName(cinemaname, function(cinema){
				$("#tableCinema > tr").remove(); //limpia la tabla para volver a llenar los datos
				var table = $("#tableCinema");
				
				for(let c of cinema[0].functions){
					table.append("<tr><td>"+c.movie.name+"</td><td>"+c.seats.length+"</td><td>"+c.date+"</td><tr>");
				}
				
				$("#cinemaName").text(cinema.name);
			})
			
		}
	}
	
})();