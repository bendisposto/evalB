function createXMLHttpRequest() {
        var ua;
        if(window.XMLHttpRequest) {
                try {
                        ua = new XMLHttpRequest();
                } catch(e) {
                        ua = false;
                }
        } else if(window.ActiveXObject) {
                try {
                        ua = new ActiveXObject("Microsoft.XMLHTTP");
                } catch(e) {
                        ua = false;
                }
        }
        return ua;
}
 
var req = createXMLHttpRequest();
var delay;
var editor;
var lasthighlight = -1;
 
function urlencode (str) {
    str = (str + '').toString();
    return encodeURIComponent(str).replace(/!/g, '%21').replace(/'/g, '%27').replace(/\(/g, '%28').
    replace(/\)/g, '%29').replace(/\*/g, '%2A').replace(/%20/g, '+');
}

function sendRequest(target)  {
        req.onreadystatechange = target;
        req.send();
}

function version() {
        req.open('GET', 'version',true);
        sendRequest(toOutput);
}

function probeval() {
        text = editor.getValue();
        input = urlencode(text);
        req.open('GET', 'evaluate?input='+input,true);
        sendRequest(toOutput);
}

function probval() {
        text = document.getElementById('input').value;
        input = urlencode(text);
        req.open('GET', 'validate.php?input='+input,true);
        sendRequest(toOutput);
}
 
function toOutput() {
        if(req.readyState == 4){
                var obj = jQuery.parseJSON(req.responseText);
                document.getElementById('output').innerHTML = obj.output;
                if (obj.highlight > 0) {
                  lasthighlight = obj.highlight-1;
                  editor.setLineClass(lasthighlight, null, "activeline");
                }
                else {
                 if (lasthighlight>-1) editor.setLineClass(lasthighlight, null, null);
                }
        } else {
                alert("loading" + ajax.readyState);
        }
}

function toInput() {
        if(req.readyState == 4){
                editor.setValue(req.responseText);
                setTimeout(probeval, 300);
        } else {
                alert("loading" + ajax.readyState);
        }
}

function toExamples() {
        if(req.readyState == 4){
                document.getElementById('examples').innerHTML = req.responseText;
        } else {
                alert("loading" + ajax.readyState);
        }
}


function load_example() {
 r = document.getElementById('examples').value;
 req.open('GET', 'examples?example='+r,true);  
 sendRequest(toInput);
}

function load_example_list() {
 req.open('GET', 'examples',true);  
 sendRequest(toExamples);
}

function initialize() {
  editor = CodeMirror.fromTextArea(document.getElementById("input"), { 
     lineNumbers: true,
     onChange: function() {
          clearTimeout(delay);
          delay = setTimeout(probeval, 300);
        }
     });
  editor.setValue('2**100');
  setTimeout(probeval, 300);
  load_example_list();
}