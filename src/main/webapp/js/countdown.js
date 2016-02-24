define([], function() {
	
	var intervalId
	var $title = $("#countdown")
	
	function formatTime(time) {
		var format
		var seconds = time % 60
		var minutes = (time - seconds) / 60
		if (minutes < 10) {
			format = "0" + minutes
		} else {
			format = minutes
		}
		format = format + ":"
		if (seconds < 10) {
			format = format + "0" + seconds
		} else {
			format = format + seconds
		}
		return format
	}
	
	function getClass(time) {
		var cssClass
		if (time <= 5) {
			cssClass = "countdown-immediate"
		} else if (time <= 15) {
			cssClass = "countdown-urgent"
		} else if (time <= 30) {
			cssClass = "countdown-moderate"
		} else {
			cssClass = "countdown-normal"
		}
		return cssClass
	}
	
	function hideDisplay() {
		$title.hide()
	}
	
	function setDisplay(time) {
		$title.removeClass()
		$title.addClass(getClass(time))
		$title.html(formatTime(time))
		$title.show()
	}
	
	return {
		clearCountdown : function() {
			window.clearInterval(intervalId)
			hideDisplay()
		},
		startCountdown : function(time) {
			this.clearCountdown()
			setDisplay(time)
			intervalId = window.setInterval(function() {
				setDisplay(--time)
			}, 1000)
		}
	}
})