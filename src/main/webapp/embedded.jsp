

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
<title>evalB - ProB Logic calculator</title>
<meta name="description" content="">
<meta name="author" content="Jens Bendisposto">

<!-- Mobile Specific Metas
    ================================================== -->
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">

<!-- CSS
    ================================================== -->
<link rel="stylesheet" href="stylesheets/plain-base.css">
<link rel="stylesheet" href="stylesheets/skeleton.css">
<link rel="stylesheet" href="stylesheets/layout.css">
<link rel="stylesheet" href="stylesheets/plain-evalb.css">
<link rel="stylesheet" href="stylesheets/plain-codemirror.css">
<link rel="stylesheet" href="stylesheets/themes/red.css">
<link rel="stylesheet" href="stylesheets/themes/green.css">


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
		<div class="two-thirds column">
			<textarea name="input" id="input" rows="15" onkeyup="probeval()"></textarea>
		</div>
		<div class="one-third column omega" style="vertical-align: text-top;">
			<b>Quantification Mode</b>
			<select size="1" id="mode" name="mode" class="styled-select" onchange="probeval()">
				<option value="" selected="selected">Existential
					(Solving)</option>
				<option value="tautology">Universal
					(Checking)</option>
			</select>
			<b>Examples</b>
			<select size="1" id="examples" name="examples" class="styled-select"
				onchange="load_example()">
				<option value="" selected="selected" />
			</select>
		</div>

		<div class="two-thirds column">
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
	<script src="javascripts/tabs.js"></script>
	<script src="javascripts/jquery-1.7.2.min.js"></script>
	<script src="javascripts/codemirror.js"></script>
	<script src="javascripts/hover.js"></script>
	<script src="examples.js"></script>
	<script src="javascripts/prob.js"></script>

	<!-- End Document
  ================================================== -->
</body>
</html>