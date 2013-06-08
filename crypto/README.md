<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
    	<title />
	</head>
	<body>
		<h2>eXist implementation for EXPath Cryptographic Module</h2>
		<h3>Currently implemented functions</h3>
		<ul>
			<li>crypto:hash()</li>
			<li>crypto:hmac() (only for xs:string data for now)</li>
			<li>crypto:encrypt() (only for xs:string data and symmetric encryption for now)</li>
			<li>crypto:decrypt() (only for xs:string data and symmetric decryption for now)</li>
			<li>crypto:generate-signature() (only for XML data for now)</li>
			<li>crypto:validate-signature() (only for XML data for now)</li>
		</ul>
		<h3>Documentation</h3>
		<p>For the latest version of the specification for this module see <a href="http://extxsltforms.sourceforge.net/expath-specs/crypto/crypto.html">here</a>.</p>
		<p>The implementation follows this specification.</p>
		<h3>Examples of usage</h3>
		<p>For examples of usage, see section Unit Tests.</p>
		<h3>Unit Tests</h3>
		<p>Unit Tests can be found in /apps/expath-crypto/tests/unit-tests' collection (when this library is installed in eXist).</p>
		<p>For a simple test runner, showing description and status (passed / failed) for each unit test, go <a href="tests/test-plan.xq">here</a>.</p>
	</body>
</html>