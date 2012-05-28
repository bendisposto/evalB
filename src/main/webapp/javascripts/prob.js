function createXMLHttpRequest() {
	var ua;
	if (window.XMLHttpRequest) {
		try {
			ua = new XMLHttpRequest();
		} catch (e) {
			ua = false;
		}
	} else if (window.ActiveXObject) {
		try {
			ua = new ActiveXObject("Microsoft.XMLHTTP");
		} catch (e) {
			ua = false;
		}
	}
	return ua;
}

var req = createXMLHttpRequest();
var delay;
var editor;
var output;
var lasthighlight = -1;
var formalism = "classicalb";

function urlencode(str) {
	str = (str + '').toString();
	return encodeURIComponent(str).replace(/!/g, '%21').replace(/'/g, '%27')
			.replace(/\(/g, '%28').replace(/\)/g, '%29').replace(/\*/g, '%2A')
			.replace(/%20/g, '+');
}

function sendRequest(target) {
	req.onreadystatechange = target;
	req.send();
}

function version() {
	req.open('GET', 'version', true);
	sendRequest(toOutput);
}

function probeval() {
	text = editor.getValue();
	input = urlencode(text);
	m = document.getElementById('mode').value;
	mode = urlencode(m);
	f = document.getElementById('formalism').value;
	formalism = urlencode(f);
	req.open('GET', 'evaluate?formalism='+formalism+'&input=' + input + '&mode=' + mode, true);
	sendRequest(toOutput);
}
function probevalselection() {
	text = editor.getSelection();
	input = urlencode(text);
	m = document.getElementById('mode').value;
	mode = urlencode(m);
	f = document.getElementById('formalism').value;
	formalism = urlencode(f);
	req.open('GET', 'evaluate?formalism='+formalism+'&input=' + input + '&mode=' + mode, true);
	sendRequest(selectionHover);
}

function selectionHover() {
	if (req.readyState == 4) {
		var obj = jQuery.parseJSON(req.responseText);
		document.getElementById('selectioneval').innerHTML = obj.output;
	} else {
		alert("loading" + ajax.readyState);
	}
}

function toOutput() {
	if (req.readyState == 4) {
		var obj = jQuery.parseJSON(req.responseText);
		output.setValue(obj.output);
		if (obj.highlight > 0) {
			lasthighlight = obj.highlight - 1;
			editor.setLineClass(lasthighlight, null, "activeline");
			output.setOption("theme", "red");
		} else {
			if (lasthighlight > -1) {
				editor.setLineClass(lasthighlight, null, null);
				output.setOption("theme", "green");
			}
		}
	} else {
		alert("loading" + ajax.readyState);
	}
}

function toInput() {
	if (req.readyState == 4) {
		editor.setValue(req.responseText);
		setTimeout(probeval, 300);
	} else {
		alert("loading" + ajax.readyState);
	}
}

function load_example() {
	r = document.getElementById('examples').value;
	editor.setValue(example_list[formalism][r]);
}

function load_example_list() {
	s = document.getElementById('examples');
	while (s.length> 0) {
	    s.remove(0);
	} 
	 s.add(new Option("", ""), null);
	for ( var e in example_list[formalism]) {
		 s.add(new Option(e, e), null);
    }
}

function switch_formalism() {
	formalism = document.getElementById('formalism').value;
	$("#syntax").load("formalism/" + formalism + "/syntax.html", function() {
		MathJax.Hub.Queue([ "Typeset", MathJax.Hub ]);
	});

	load_example_list();
	editor.setValue('1<2');
}

function initialize() {
	editor = CodeMirror.fromTextArea(document.getElementById("input"), {
		lineNumbers : true,
		onCursorActivity : function() {
			var sel = editor.getSelection();
			if (sel && sel != '') {
				ShowContent('selectioneval');
				clearTimeout(delay);
				delay = setTimeout(probevalselection, 300);
			} else {
				HideContent('selectioneval');
			}
		},
		onChange : function() {
			clearTimeout(delay);
			delay = setTimeout(probeval, 300);
		}
	});
	output = CodeMirror.fromTextArea(document.getElementById("output"), {
		lineWrapping : true,
		readOnly : "nocursor"
	});
	setTimeout(probeval, 300);
	switch_formalism();
}
