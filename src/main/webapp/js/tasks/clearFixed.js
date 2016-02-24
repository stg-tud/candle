define(["tasks/renderUtil"], function(util) {

	var expl = "<p>The next question is of type \"<b>Fixed</b>\".</p>" +
		"<p>The time you need to answer will NOT affect the grade.</p>" +
		"<p>However, to get points you MUST select your answer before the count-down expires.</p>"
	
	return {
		render: function(task) {
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