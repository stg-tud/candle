define([ "events" ], function(events) {

	var _name

	function playerExists(auth, cbFail) {
		var res = true
		$.ajax({
			"url" : "exists",
			"contentType" : "application/json",
			"type" : "post",
			"async" : false,
			"data" : auth
		}).done(function(r) {
			if (r.status == "OK") {
				res = r.data
			} else {
				cbFail(r.message)
			}
		}).fail(function(r) {
			cbFail(r.message)
		})
		return res
	}

	function register(auth, cbFail) {
		$.ajax({
			"url" : "register",
			"contentType" : "application/json",
			"type" : "post",
			"async" : false,
			"data" : auth
		}).done(function(r) {
			if (r.status != "OK") {
				cbFail(r.message)
			}
		}).fail(function(r) {
			cbFail(r.message)
		})
	}

	return {
		register : function(nameRaw, cbDone, cbFail) {
			_name = (nameRaw + "").trim()
			if (_name.length == 0) {
				cbFail("This field must not be empty!")
			} else if (_name != parseInt(_name, 10)) {
				cbFail("You did not enter a matriculation number!")
			} else {
				if (playerExists(_name, cbFail)) {
					events.fireReload()
				} else {
					register(_name, cbDone, cbFail)
				}
				cbDone()
			}
		},
		getName : function() {
			return _name
		}
	}
})