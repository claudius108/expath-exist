<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
    	<title />
	</head>
	<body>
		<h2>eXist implementation for EXPath PDF Module</h2>
		<h3>Currently implemented functions</h3>
		<ul>
			<li>pdf:get-text-files()</li>
			<li>pdf:set-text-files()</li>
		</ul>
		<h3>Documentation</h3>
		<p>For the latest version of the specification for this module see <a href="http://http://kuberam.ro/specs/expath/pdf/pdf.html">here</a>.</p>
		<p>The implementation follows this specification.</p>
		<h3>Examples of usage</h3>
		<p>For examples of usage, see section Unit Tests.</p>
		<h3>Unit Tests</h3>
		<p>Unit Tests can be found in /apps/expath-pdf/tests/unit-tests' collection (when this library is installed in eXist).</p>
		<p>For a simple test runner, showing description and status (passed / failed) for each unit test, go <a href="tests/test-plan.xq">here</a> 
		(the tests have to be run as dba role, in order to have rights to write documents within the DB).</p>
	</body>
</html>
