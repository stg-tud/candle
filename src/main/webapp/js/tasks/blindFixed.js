define(["tasks/renderUtil"], function(util) {

	var expl = "<p>The next question has two pages:</p>" +
		"<p>First, you will see some code and a countdown." +
		"After the count-down has expired, the code is removed, a question will appear, and the countdown is restarted.</p>" +
		"<p>Your answer to this question will be auto-submitted when the countdown expires again.</p>"

	return {
		render: function(task) {
			$("#title").html(task.title)
			util.showExplanation(expl, function() {
				util.showCode(task)
				util.setTimeout(task.taskType.codeTime, function() {
					util.hideCode()
					util.showQuestion(task)
					util.setTimeout(task.taskType.questionTime, function() {
						util.onTimeout(task)
					})
				})
			})
		}
	}
})