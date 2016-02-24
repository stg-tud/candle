<html>
<head>
<title>Evaluation Study</title>
<link rel="stylesheet" type="text/css" href="css/question.css" />
<link rel="stylesheet" type="text/css" href="css/growl/jquery.growl.css" />
<link rel="stylesheet" type="text/css" href="css/sh/shCore.css" />
<link rel="stylesheet" type="text/css" href="css/sh/shThemeDefault.css" />
<script type="text/javascript" src="js/jquery-2.0.3.js"></script>
<script type="text/javascript" src="js/require-2.1.10.js"
	data-main="js/index.js"></script>
<script type="text/javascript" src="js/growl/jquery.growl.js"></script>
<script type="text/javascript" src="js/syntax-highlighter.js"></script>
</head>
<body>
	<div id="container">
		<!-- ------------------------------------------------- -->
		<div id="intro">
			<h1>Software Engineering: Design &amp; Construction<br />In-class Exercise</h1>
			<noscript>WARNING: Javascript is currently disabled, you
				need to enabled it in order to use the platform.</noscript>
			<ul>
				<li>Do not reload the page</li>
				<li>Do not use the forward/backward buttons</li>
				<li>Do not close the browser window</li>
			</ul>
			<p>All questions have two pages: A start page and a question page.</p>
			<p>You have to press start on the first page. Once you started, you will see a question, a list of possible answers and a countdown.
			Select the right answer from the list and submit it by pressing the button, before the countdown expires.</p>
			<p>If you do not press the button no answer is registered for you!</p>
			<p>Some tasks affect your grading. Those task are marked with a red message on the first page.</p>
			<p>In order to participate, enter your matriculation number and
				start the questionnaire.</p>
			<form>
				<div class="error">error</div>
				<input type="text" name="name" />
				<button id="start">Start</button>
			</form>
			</p>
		</div>
		<!-- ------------------------------------------------- -->
		<div id="countdown"></div>
		<div id="tasks">
			<h1 id="title"></h1>
			<div id="content"></div>
			<div id="foot"></div>
		</div>
		<!-- ------------------------------------------------- -->
		<div id="end">
			<h1>Thank you for participating!</h1>
			<p>You successfully finished the questionnaire.</p>
		</div>
		<!-- ------------------------------------------------- -->
		<div id="error">
			<h1>Ooops, there seems to be an issue&hellip;</h1>
			<p class="cause" />
			<h3>Most likely, this is related to your network connection, please fix it and then refresh this page.</h3>
		</div>
		<!-- ------------------------------------------------- -->
	</div>
</body>
</html>