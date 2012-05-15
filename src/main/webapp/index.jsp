

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

<script type="text/x-mathjax-config">
  MathJax.Hub.Config({tex2jax: {inlineMath: [['$','$'], ['\\(','\\)']]}});
</script>

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
			<select size="1" id="mode" name="mode" class="styled-select" onchange="probeval()">
				<option value="" selected="selected">Existential
					Quantification</option>
				<option value="tautology">Universal
					Quantification</option>
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


		<div class="sixteen columns">
			<hr style="margin-top: 20px;" />
			<h3>About evalB</h3>
			<p>evalB is an online calculator for logic formulas. It can
				evaluate predicates and formulas given in the B notation. Under the
				hood, evalB uses the ProB animator and model checker.</p>
		</div>

		<div class="sixteen columns">
			<h3>B Syntax in a Nutshell</h3>
		</div>

		<div class="cheat-box-container eight columns">
			<div class="cheat-box">
				<h4>Values</h4>
				<table>
					<tr class="row-one">
						<td class="row-label">Boolean</td>
						<td>TRUE, FALSE</td>
					</tr>
					<tr>
						<td class="row-label">Integer</td>
						<td>1,2,3, ...</td>
					</tr>
					<tr class="row-one">
						<td class="row-label">greatest implementable Integer</td>
						<td>MAXINT</td>
					</tr>
					<tr>
						<td class="row-label">least implementable Integer</td>
						<td>MININT</td>
					</tr>
					<tr class="row-one">
						<td class="row-label">Strings</td>
						<td>"..."</td>
					</tr>
				</table>
			</div>
			<div class="cheat-box">
				<h4>Predefined Sets</h4>
				<table>
					<tr class="row-one">
						<td class="row-label">Boolean</td>
						<td>BOOL</td>
					</tr>
					<tr>
						<td class="row-label">Integer</td>
						<td>INT, INTEGER [<a href="#2">1</a>]
						</td>
					</tr>
					<tr class="row-one">
						<td class="row-label">Natual Numbers</td>
						<td>NAT, NATURAL [<a href="#2">1</a>]
						</td>
					</tr>
					<tr>
						<td class="row-label">Strings</td>
						<td>STRING</td>
					</tr>
					<tr class="row-one">
						<td class="row-label">Empty set $\emptyset$</td>
						<td>{}</td>
					</tr>
				</table>
			</div>
			<div class="cheat-box">
				<h4>Logical connectives</h4>
				<table>
					<tr class="row-one">
						<td class="row-label">$P \wedge Q$</td>
						<td>P & Q</td>
					</tr>
					<tr>
						<td class="row-label">$P \vee Q$</td>
						<td>P or Q</td>
					</tr>
					<tr class="row-one">
						<td class="row-label">$P \implies Q$</td>
						<td>P => Q</td>
					</tr>
					<tr>
						<td class="row-label">$P \Leftrightarrow Q$</td>
						<td>P <=> Q</td>
					</tr>
					<tr class="row-one">
						<td class="row-label">$\neg P$</td>
						<td>not(P)</td>
					</tr>
				</table>
			</div>
			<div class="cheat-box">
				<h4>Quantifiers</h4>
				<table>
					<tr class="row-one">
						<td class="row-label">$\exists x \mid P$</td>
						<td>#x.(P)</td>
					</tr>
					<tr>
						<td class="row-label">$\forall x \mid P \implies Q$</td>
						<td>!x.(P=>Q)</td>
					</tr>
				</table>
			</div>
			<div class="cheat-box">
				<h4>Conversion</h4>
				<table>
					<tr class="row-one">
						<td class="row-label">Predicate to Boolean Value</td>
						<td>bool(P)</td>
					</tr>
					<tr>
						<td class="row-label">Cardinality of a Set</td>
						<td>card(S)</td>
					</tr>
				</table>
			</div>
			<div class="cheat-box">
				<h4>Equality</h4>
				<table>
					<tr class="row-one">
						<td class="row-label">$x = y$</td>
						<td>x = y</td>
					</tr>
					<tr>
						<td class="row-label">$x \not = y$</td>
						<td>x /= y</td>
					</tr>
				</table>
			</div>
			<div class="cheat-box">
				<h4>Set Predicates</h4>
				<table>
					<tr class="row-one">
						<td class="row-label">$x \in S$</td>
						<td>x : S</td>
					</tr>
					<tr>
						<td class="row-label">$x \not \in S$</td>
						<td>x /: y</td>
					</tr>
					<tr class="row-one">
						<td class="row-label">$ S \subseteq T$</td>
						<td>S <: T</td>
					</tr>
					<tr>
						<td class="row-label">$S \subset T$</td>
						<td>S <<: T</td>
					</tr>
					<tr>
				</table>
			</div>
			<div class="cheat-box">
				<h4>Set Construction</h4>
				<table>
					<tr class="row-one">
						<td class="row-label">Set enumeration</td>
						<td>{a,b,c}</td>
					</tr>
					<tr>
						<td class="row-label">Set comprehension $ \{x \mid P\} $</td>
						<td>{x | P}</td>
					</tr>
					<tr class="row-one">
						<td class="row-label">Lambda Abstraction</td>
						<td>%x.(P|E)</td>
					</tr>
					<tr>
						<td class="row-label">Intervall $ \{x \mid x \in \mathbb{Z}
							\wedge n \leq x \wedge x \leq m\} $</td>
						<td>n..m</td>
					</tr>
					<tr class="row-one">
						<td class="row-label">Powerset $\mathbb{P}(S)$</td>
						<td>POW(S)</td>
					</tr>
					<tr>
						<td class="row-label">Cartesian Product $ S \times T $</td>
						<td>S*T</td>
					</tr>
					<tr class="row-one">
						<td class="row-label">Set of relations $\mathbb{P}(S \times
							T)$</td>
						<td>S <-> T</td>
					</tr>
					<tr>
						<td class="row-label">Set of partial functions</td>
						<td>S +-> T</td>
					</tr>
					<tr class="row-one">
						<td class="row-label">Set of total functions</td>
						<td>S --> T</td>
					</tr>
				</table>
			</div>
		</div>
		<div class="cheat-box-container eight columns">
			<div class="cheat-box">
				<h4>Arithmetic</h4>
				<table>
					<tr class="row-one">
						<td class="row-label">Comparison</td>
						<td>>, <, >=, <=</td>
					</tr>
					<tr>
						<td class="row-label">Operators</td>
						<td>+, -, *, /</td>
					</tr>
					<tr class="row-one">
						<td class="row-label">$ x ^ y $</td>
						<td>x ** y</td>
					</tr>
					<tr>
						<td class="row-label">$ x\ \text{mod}\ y$</td>
						<td>x mod y</td>
					</tr>
					<tr class="row-one">
						<td class="row-label">Sucessor</td>
						<td>succ(x)</td>
					</tr>
					<tr>
						<td class="row-label">Predecessor</td>
						<td>pred(x)</td>
					</tr>
				</table>
			</div>
			<div class="cheat-box">
				<h4>Set Operations</h4>
				<table>
					<tr class="row-one">
						<td class="row-label">$S \cup T$</td>
						<td>S \/ T</td>
					</tr>
					<tr>
						<td class="row-label">$S \cap T$</td>
						<td>S /\ T</td>
					</tr>
					<tr class="row-one">
						<td class="row-label">$S \smallsetminus T$</td>
						<td>S - T</td>
					</tr>
				</table>
			</div>
			<div class="cheat-box">
				<h4>
					Relation Operations [<a href="#2">2</a>]
				</h4>
				<table>
					<tr class="row-one">
						<td class="row-label">Relational image $\{t \mid \exists s
							\cdot s \in S \wedge (s \mapsto t) \in r\}$</td>
						<td>r[S]</td>
					</tr>
					<tr>
						<td class="row-label">Relational composition $r2 \circ r1$</td>
						<td>(r1;r2)</td>
					</tr>
					<tr class="row-one">
						<td class="row-label">transitive closure</td>
						<td>closure1(r)</td>
					</tr>
					<tr>
						<td class="row-label">transitive reflexive closure</td>
						<td>closure(r)</td>
					</tr>
					<tr class="row-one">
						<td class="row-label">Domain</td>
						<td>dom(r)</td>
					</tr>
					<tr>
						<td class="row-label">Range</td>
						<td>range(r)</td>
					</tr>
					<tr class="row-one">
						<td class="row-label">Inverse $r^{-1}$</td>
						<td>r~</td>
					</tr>
					<tr>
						<td class="row-label">Identity relation</td>
						<td>id(S)</td>
					</tr>
					<tr class="row-one">
						<td class="row-label">Relational override</td>
						<td>r1 <+ r2</td>
					</tr>
					<tr>
						<td class="row-label">Domain restriction</td>
						<td>S <| r</td>
					</tr>
					<tr class="row-one">
						<td class="row-label">Domain subtraction</td>
						<td>S <<| r</td>
					</tr>
					<tr>
						<td class="row-label">Range restriction</td>
						<td>r |> S</td>
					</tr>
					<tr class="row-one">
						<td class="row-label">Range antirestriction</td>
						<td>r |>> S</td>
					</tr>
				</table>
			</div>
			<div class="cheat-box">
				<h4>
					Sequence operations [<a href="#3">3</a>]
				</h4>
				<table>
					<tr class="row-one">
						<td class="row-label">concatenation</td>
						<td>s1^s2</td>
					</tr>
					<tr>
						<td class="row-label">first element</td>
						<td>first(s)</td>
					</tr>
					<tr class="row-one">
						<td class="row-label">all but first element</td>
						<td>tail(s)</td>
					</tr>
					<tr>
						<td class="row-label">prepend an element</td>
						<td>E->s</td>
					</tr>
					<tr class="row-one">
						<td class="row-label">Size of a sequence</td>
						<td>size(s)</td>
					</tr>
				</table>
			</div>
			<!-- /cheat-box -->
			<div class="cheat-box">
				<h4>Sequence Construction</h4>
				<table>
					<tr class="row-one">
						<td class="row-label">Empty sequence</td>
						<td>[]</td>
					</tr>
					<tr>
						<td class="row-label">explicit sequence</td>
						<td>[a,b,c,...]</td>
					</tr>
					<tr class="row-one">
						<td class="row-label">Set of sequences over a set</td>
						<td>seq(S)</td>
					</tr>
					<tr>
						<td class="row-label">Set of injective sequences over a set</td>
						<td>iseq(S)</td>
					</tr>
					<tr class="row-one">
						<td class="row-label">Set of permutations</td>
						<td>perm(S)</td>
					</tr>
				</table>
			</div>

		</div>
		<!-- /cheat-box-container -->

		<div class="sixteen columns space">
			<p>
				<a name="1">[1]</a> INT is the set of integers between MININT and
				MAXINT, INTEGER are the mathematical integers $\mathbb{Z}$. NAT and
				NATURAL are similar.
			</p>
			<p>
				<a name="2">[2]</a> In B a relaton (or function) is a set of tuples.
				Thus any operation that is allowed on sets can also be applied to
				relations.
			</p>
			<p>
				<a name="3">[3]</a> A sequences of $\Sigma$ values $s=<\alpha \ldots
				\omega>$ is a total function $1..card(s) \rightarrow \Sigma$. Thus
				all operations on relations and sets are also allowed on sequences.
				However, in general they do not yield a sequence.
			</p>
		</div>

		<div class="sixteen columns space">
			<hr style="margin-top: 0px; margin-bottom: 0px;" />
			<div id="footer-logo">
				(C) 2012, <a href="http://www.stups.uni-duesseldorf.de">STUPS
					Group</a>, HHU Duesseldorf
			</div>
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
	<script src="examples.js"></script>
	<script src="javascripts/prob.js"></script>
	<script type="text/javascript"
		src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML"></script>

	<!-- End Document
  ================================================== -->
</body>
</html>