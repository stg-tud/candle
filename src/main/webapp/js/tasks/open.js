define(["tasks/renderUtil"], function(util) {

	var expl = "<p>The next question is not timed.</p>"
	
	return {
		render: function(task) {
			$("#title").html(task.title)
			util.showExplanation(expl, function() {
				util.showCode(task)
				util.showQuestion(task)
			})
		}
	}
})