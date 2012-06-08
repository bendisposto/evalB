

<!DOCTYPE html>
<!--[if lt IE 7 ]><html class="ie ie6" lang="en"> <![endif]-->
<!--[if IE 7 ]><html class="ie ie7" lang="en"> <![endif]-->
<!--[if IE 8 ]><html class="ie ie8" lang="en"> <![endif]-->
<!--[if (gte IE 9)|!(IE)]><!-->
<html lang="en">
<!--<![endif]-->
<head>
<!-- Basic Page Needs
    ================================================== -->
<meta charset="utf-8">
<title>ProB Logic calculator</title>
<meta name="description" content="">
<meta name="author" content="Jens Bendisposto">

<!-- Mobile Specific Metas
    ================================================== -->
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">

<!-- CSS
    ================================================== -->
<link rel="stylesheet"
	href="css/stylesheets_<%=de.prob.web.TimeStamp.TIME%>/plain-base.css">
<link rel="stylesheet"
	href="css/stylesheets_<%=de.prob.web.TimeStamp.TIME%>/plain-skeleton.css">
<link rel="stylesheet"
	href="css/stylesheets_<%=de.prob.web.TimeStamp.TIME%>/layout.css">
<link rel="stylesheet"
	href="css/stylesheets_<%=de.prob.web.TimeStamp.TIME%>/plain-evalb.css">
<link rel="stylesheet"
	href="css/stylesheets_<%=de.prob.web.TimeStamp.TIME%>/plain-codemirror.css">

<!--[if lt IE 9]>
          <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
      <![endif]-->

<!-- Favicons
      ================================================== -->
<link rel="shortcut icon" href="images/favicon.ico">
<link rel="apple-touch-icon" href="images/apple-touch-icon.png">
<link rel="apple-touch-icon" sizes="72x72"
	href="images/apple-touch-icon-72x72.png">
<link rel="apple-touch-icon" sizes="114x114"
	href="images/apple-touch-icon-114x114.png">
</head>
<body onLoad="initialize()">

	<!-- Primary Page Layout
      ================================================== -->

	<!-- Delete everything in this .container and get started on your own site! -->

	<div class="container">
		<div class="left">
			<textarea name="input" id="input" rows="3" onkeyup="probeval()"></textarea>
		</div>
		<div class="left-noclear" style="vertical-align: text-top;">
			<b>Quantification Mode</b>
			<select size="1" id="mode" name="mode" class="styled-select" onchange="probeval()">
				<option value="" selected="selected">Existential
					(Solving)</option>
				<option value="tautology">Universal
					(Checking)</option>
			</select>
			<b>Formalism</b>
			<select size="1" id="formalism" name="formalism"
				class="styled-select" onchange="switch_formalism()">
				<option value="classicalb" selected="selected">B Method</option>
				<option value="tla">TLA+</option>
			</select>
			<b>Examples</b>
			<select size="1" id="examples" name="examples" class="styled-select"
				onchange="load_example()">
				<option value="" selected="selected" />
			</select>
		</div>
		<div class="left">
			<textarea name="output" id="output" cols="80" rows="5"></textarea>
		</div>

		<div class="sixteen columns space">
			<hr style="margin-top: 0px; margin-bottom: 0px;" />
			<div class="footer"><%=de.prob.web.VersionInfo.getVersion()%></div>
		</div>

		<div id="selectioneval"
			style="display: none; position: absolute; border: 1px solid #999999; background-color: #FFFFEE; padding: 4px;"></div>


	</div>
	<!-- container -->

	<!-- JS
      ================================================== -->
	<!-- <script src="http://code.jquery.com/jquery-1.7.1.min.js"></script> -->
	<script src="js/javascripts_<%=de.prob.web.TimeStamp.TIME%>/tabs.js"></script>
	<script
		src="js/javascripts_<%=de.prob.web.TimeStamp.TIME%>/jquery-1.7.2.min.js"></script>
	<script
		src="js/javascripts_<%=de.prob.web.TimeStamp.TIME%>/codemirror.js"></script>
	<script src="js/javascripts_<%=de.prob.web.TimeStamp.TIME%>/hover.js"></script>
	<script
		src="js/javascripts_<%=de.prob.web.TimeStamp.TIME%>/examples.js"></script>
	<script src="js/javascripts_<%=de.prob.web.TimeStamp.TIME%>/prob.js"></script>

	<!-- End Document
  ================================================== -->
</body>
</html>