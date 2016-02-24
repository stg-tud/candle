define([ "countdown", "events", "tasks/answerRenderer" ], function(countdown,
		events, answerRenderer) {

	var $root = $("#content")
	var $code
	var timeQuestion

	return {
		showExplanation : function(text, callback) {
			$root.empty()
			$root.append(text)
			var $p = $("<p></p>")
			var $button = $("<button>Start</button>")
			$p.append($button)
			$root.append($p)

			$button.click(function() {
				$root.empty()
				callback()
			})
		},
		showCode : function(task) {
			events.fireStarted()
			if (task.code != "") {
				$code = $("<div />")
				$code.append("<p>" + task.description + "</p>")
				$code.append("<pre class='brush: scala'>" + task.code
						+ "</pre>")
				$root.empty()
				$root.append($code)
				SyntaxHighlighter.highlight();
			}
		},
		hideCode : function() {
			$code.hide()
			events.fireHidden()
		},
		showQuestion : function(task) {
			$root.append("<h4>" + task.question + "</h4>")

			answerRenderer.render(task, $root)

			if (task.taskType.isOpen || task.taskType.isMeasured) {
				var $button = $("<button>Submit</button>")
				$root.append($button)
				$button.click(this.onSubmit)
			}
		},
		onTimeout : function(task) {
			countdown.clearCountdown()
			var answer = answerRenderer.getAnswer()

			if (answer == undefined) {
				$.growl.warning({
					"title" : "You did not submit an answer.",
					"message" : ""
				})
				events.fireTimeout()
				require("workflow").nextTask()
			} else {
				if (task.taskType.isMeasured) {
					$.growl.warning({
						"title" : "You did not submit an answer.",
						"message" : ""
					})
					events.fireTimeout()
				} else {
					$.growl.notice({
						"title" : answer,
						"message" : ""
					})
					events.fireSubmission(answer)
				}
				require("workflow").nextTask()
			}
		},
		onSubmit : function() {
			var answer = answerRenderer.getAnswer()

			if (answer == undefined) {
				$.growl.warning({
					"title" : "You did not submit an answer.",
					"message" : ""
				})
			} else {
				countdown.clearCountdown()
				window.clearTimeout(timeQuestion)
				$.growl.notice({
					"title" : "Submission: " + answer,
					"message" : ""
				})
				events.fireSubmission(answer)
				require("workflow").nextTask()
			}
		},
		setTimeout : function(duration, cb) {
			countdown.startCountdown(duration)
			timeQuestion = window.setTimeout(cb, duration * 1000)
		}
	}
})