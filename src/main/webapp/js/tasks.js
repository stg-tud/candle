define([ "auth"], function(auth) {

	var currentTaskId = ""

	return {
		getCurrentTaskId : function() {
			return currentTaskId
		},
		hasNext : function() {
			var res = false
			$.ajax({
				"url" : "hasNext",
				"contentType" : "application/json",
				"type" : "post",
				"async" : false,
				"data" : auth.getName()
			}).done(function(r, textStatus, jqXHR) {
				if (r.status == "OK") {
					res = r.data
				} else {
					require("workflow").error(r.message, "server exception")
				}
			}).fail(function(jqXHR, textStatus, errorThrown) {
				require("workflow").error(textStatus, errorThrown)
			})
			return res
		},
		getNext : function() {
			var res = {}
			$.ajax({
				"url" : "getNext",
				"contentType" : "application/json",
				"type" : "post",
				"async" : false,
				"data" : auth.getName()
			}).done(function(r, textStatus, jqXHR) {
				if (r.status == "OK") {
					res = r.data
					currentTaskId = res.id
				} else {
					require("workflow").error(r.message, "server exception")
				}
			}).fail(function(jqXHR, textStatus, errorThrown) {
				require("workflow").error(textStatus, errorThrown)
			})
			return res
		}
	}
})