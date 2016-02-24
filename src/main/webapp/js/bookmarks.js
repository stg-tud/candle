define([], function() {
	var subscriber = []
	var enabled = true
	$(window).bind('hashchange', function() {
		var hash = window.location.hash.slice(1);
		if (enabled) {
			for (var i in subscriber) {
				subscriber[i](hash)
			}
		} else {
			enabled = true
		}
	})
	return {
		clear : function() {
			this.setHash("")
		},
		setHash : function(hashName) {
			enabled = false
			window.location.hash = hashName
		},
		subscribeChanges : function(callback) {
			subscriber.push(callback)
		},
		unsubscribeChanges : function(callback) {
			subscriber = []
		}
	}
})