define(["tasks/renderUtil"], function(util) {

	var expl = "<p>The next question is of type \"<b>blind-measured</b>\".</p>" +
		"<p>First you will see some code.</p>" +
		"<p>After the count-down has expired the code disappears and you will see the corresponding question.</p>" +
		"<p>Select and submit your answer before the newly started count-down expires.</p>" +
		"<p>Try to submit as quick as possible, the time that you need for your answer is measured</p>" +
		"<p>If you haven't submitted your answer before the count-down has expired your answer isn't accepted.</p>"
	
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