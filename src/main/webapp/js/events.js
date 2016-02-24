define([], function() {

	function publish(eventType, answer) {
		var event = {
			"auth" : require("auth").getName(),
			"taskId" : require("tasks").getCurrentTaskId(),
			"type" : eventType,
			"answer" : answer
		}
		
		$.ajax({
			url : "log",
			contentType : "application/json",
			type : "post",
			async: false,
			data : JSON.stringify(event)
		}).done(function(r) {
			if(r.status != "OK") {
				require("workflow").error(r.message, "server exception")
			}
		}).fail(function(jqXHR, textStatus, errorThrown) {
			require("workflow").error(textStatus, errorThrown)
		})
	}

	return {
		fireStarted : function() {
			publish("PAGE_START", "")
		},
		fireSelection : function(answer) {
			publish("SELECTION", answer)
		},
		fireSubmission : function(answer) {
			publish("SUBMISSION", answer)
		},
		fireTimeout : function() {
			publish("TIMEOUT", "")
		},
		fireHidden : function() {
			publish("CODE_HIDDEN", "")
		},
		fireReload : function() {
			$.growl.warning({
				"title":"You left your previous session by reloading or closing the browser window. The questionnaire continues with the next question.",
				"message":""
			})
			publish("RELOAD", "")
		}
	}
})