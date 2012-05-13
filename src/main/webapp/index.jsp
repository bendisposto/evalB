<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>ProB Logic Calculator</title>
<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="js/examples.js"></script>
<script type="text/javascript" src="js/prob.js"></script>
<script type="text/javascript" src="js/lib/codemirror.js"></script>
<script type="text/javascript" src="js/lib/hover.js"></script>
<link rel="stylesheet" href="js/lib/themes/green.css" />
<link rel="stylesheet" href="js/lib/themes/red.css" />
<link rel="stylesheet" href="js/lib/codemirror.css" />

<style>
.CodeMirror {
	float: left;
	width: 59%;
	border: 1px solid black;
	background-color: #FFFFFF
}

.CodeMirror-wrap {
	float: left;
	width: 39%;
	border: 1px solid black;
	background-color: #EEFFEE
}

.activeline {
	background: #FFC1C1 !important;
}

#version {
	font-size: xx-small;
	font-family: Consolas, Monaco, Lucida Console, Liberation Mono,
		DejaVu Sans Mono, Bitstream Vera Sans Mono, Courier New, monospace;
}
</style>

</head>
<body onload="initialize()">

	<textarea name="input" id="input" cols="80" rows="20"
		onkeyup="probeval()"></textarea>
	<textarea name="output" id="output" cols="80" rows="20"></textarea>
	<div id="output" class="output"></div>
	<form>
		Examples: <select size="1" id="examples" name="examples"
			onchange="load_example()">
			<option value="" selected="selected" />
		</select>
	</form>
	<div id="selectioneval"
		style="display: none; position: absolute; border: 1px solid #999999; background-color: #FFFFEE; padding: 4px;"></div>
	<div id="version"><%=de.prob.web.VersionInfo.getVersion()%></div>
</body>
</html>
