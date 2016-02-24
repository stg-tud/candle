define(["tasks/blindFixed", "tasks/blindMeasured", "tasks/clearFixed", "tasks/clearMeasured", "tasks/open"],
		function(blindFixed, blindMeasured, clearFixed, clearMeasured, open) {
	return {
		dispatch: function(style) {
			if(style.isOpen) {
				return open
			} else if (style.isBlind) {
				if (style.isMeasured) {
					return blindMeasured
				} else {
					return blindFixed
				}
			} else {
				if (style.isMeasured) {
					return clearMeasured
				} else {
					return clearFixed
				}
			}
		}
	}
})