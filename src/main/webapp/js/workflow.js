define(["bookmarks", "tasks", "tasks/styleDispatcher"], function(bookmarks, loader, dispatcher) {
	var $container = $("#container")
	var $intro = $("#intro")
	var $content = $("#tasks")
	var $end = $("#end")
	var $error = $("#error")
	var $body = $("body")

	return {
		clear: function() {
			bookmarks.clear()
		},
		startTask: function() {

			window.onbeforeunload = function() {
				var warn = "WARNING: If you reload or leave the page now, you cannot get points for this question anymore!"
				$.growl.error({
					title: warn,
					message: ""
				})
				return warn
			}

			bookmarks.subscribeChanges(function() {
				$.growl.error({
					title: "Do not use navigation-buttons!",
					message: ""
				})
				bookmarks.setHash(loader.getCurrentTaskId())
			})

			$intro.hide();
			$content.show();
			this.nextTask();
		},
		nextTask: function() {
			if(loader.hasNext()) {
				var task = loader.getNext()
				bookmarks.setHash(task.id)
				var renderer = dispatcher.dispatch(task.taskType)
				renderer.render(task)
			} else {
				this.end()
			}
		},
		end: function() {
			window.onbeforeunload = undefined
			$content.hide();
			$end.show()
		},
		error : function(description, exception) {
			$container.toggleClass("error");
			$intro.hide()
			$content.hide()
			$end.hide()
			$error.find(".cause").text(exception)
			$error.show()
			
			bookmarks.unsubscribeChanges()
			window.onbeforeunload = function() {}
			
			throw new Error("execution should stop");
		}
	}
})