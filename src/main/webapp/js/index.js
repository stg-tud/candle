require(["workflow", "auth"], function(workflow, auth) {

//	$("body").click(function(e) {
//		e.preventDefault()
//	})
	
	workflow.clear()
	
	$("#intro button").click(function(e) {
		var name = $("#intro input").val().trim()
		
		var cbDone = function() {
			workflow.startTask()
		}
		
		var cbFail = function(errorMsg) {
			var $err = $("#intro .error")
			$err.html(errorMsg)
			$err.show()
		}
		
		auth.register(name, cbDone, cbFail)
		e.preventDefault()
	})
})