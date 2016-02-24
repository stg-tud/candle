define([ "events" ], function(events) {

	var getAnswer_dispatched;

	function renderMultipleChoice(task, pane) {

		// TODO make labels clickable

		var group = $("<form action=\"\"></form>")
		for ( var i in task.options) {
			var option = task.options[i]
			group.append('<label><input type="radio" name="XY" value="' + option + '" />' + option + '</label>')
		}
		pane.append(group)

		$("input[name=XY]").change(function() {
			events.fireSelection(getAnswer_dispatched())
		})
	}

	function getAnswerMultipleChoice() {
		var answer = $('input[name="XY"]:checked').val()
		return answer
	}

	function renderFreeText(pane) {
		var form = $("<form action=\"\"></form>")
		form.append('<textarea id="YZ" />')
		pane.append(form)
	}

	function getAnswerFreeText() {
		var answer = $('#YZ').val()
		return answer
	}

	return {
		getAnswer : function() {
			return getAnswer_dispatched()
		},
		render : function(task, pane) {
			if (task.answerType == "MULTIPLE_CHOICE") {
				getAnswer_dispatched = getAnswerMultipleChoice
				renderMultipleChoice(task, pane)
			} else if (task.answerType == "FREE_TEXT") {
				getAnswer_dispatched = getAnswerFreeText
				renderFreeText(pane)
			} else {
				console.error("unknown answerType '" + task.answerType + "'")
				getAnswer_dispatched = function() {
					console.error("cannot get answer for unknown answerType '"
							+ task.answerType + "'")
				}
			}
		}
	}
})