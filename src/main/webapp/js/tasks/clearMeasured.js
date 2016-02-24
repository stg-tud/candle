define(["tasks/renderUtil"], function(util) {
	return {
		render: function(task) {

			var expl = "<p>Select and submit your answer before the countdown expires. " +
					"If you haven't submitted your answer before the countdown has expired " +
					"no answer is registered for you.</p>"

			if(task.taskType.isGraded) {
				expl = expl + '<p class="warning">Your answer to this question is graded. Try to submit the correct answer <u>as quickly as possible</u>.'
			}
			
			$("#title").html(task.title)
			util.showExplanation(expl, function() {
				util.showCode(task)
				util.showQuestion(task)
				util.setTimeout(task.taskType.questionTime, function() {
					util.onTimeout(task)
				})
			})
		}
	}
})