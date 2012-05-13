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
<title>evalB - Logic calculator</title>
<meta name="description" content="">
<meta name="author" content="Jens Bendisposto">

<!-- Mobile Specific Metas
    ================================================== -->
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">

<!-- CSS
    ================================================== -->
<link rel="stylesheet" href="stylesheets/base.css">
<link rel="stylesheet" href="stylesheets/skeleton.css">
<link rel="stylesheet" href="stylesheets/layout.css">
<link rel="stylesheet" href="stylesheets/evalb.css">
<link rel="stylesheet" href="stylesheets/codemirror.css">
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
		<h1 class="capital remove-bottom" style="margin-top: 40px">evalB</h1>
		<h2>Logic Calculator</h2>
		<hr />
		<div class="two-thirds column">
			<textarea name="input" id="input" rows="15" onkeyup="probeval()"></textarea>
		</div>
		<div class="one-third column omega" style="vertical-align: text-top;">
			<h3>Mode</h3>
			<select size="1" id="mode" name="mode" class="styled-select">
				<option value="Existential Quantification" selected="selected">Existential Quantification</option>
				<option value="Universal Quantification" >Universal Quantification</option>
			</select>
			<h3>Examples</h3>
			<select size="1" id="examples" name="examples" class="styled-select"
				onchange="load_example()">
				<option value="" selected="selected" />
			</select>
		</div>

		<div class="two-thirds column">
			<textarea name="output" id="output" cols="80" rows="5"></textarea>
		</div>


		<div class="sizteen columns">
			<hr style="margin-top: 20px;" />
			<h3>About evalB</h3>
			<p>evalB is an online calculator for logic formulas. It can
				evaluate predicates and formulas given in the B notation. Under the
				hood, evalB uses the ProB animator and model checker.</p>
			<div class="version"><%=de.prob.web.VersionInfo.getVersion()%></div>
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
	<script src="javascripts/examples.js"></script>
	<script src="javascripts/prob.js"></script>

	<!-- End Document
  ================================================== -->
</body>
</html>