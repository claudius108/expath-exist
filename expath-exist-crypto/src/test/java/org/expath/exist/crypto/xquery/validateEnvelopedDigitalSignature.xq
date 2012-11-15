xquery version "1.0";

let $script-collection := concat(replace(replace(request:get-effective-uri(), "/(\w)+.xql$", ""), "/rest//db", ""), '/')
let $expected-result := <expected-result>true</expected-result>
let $resources-dir-path := concat(substring-before($script-collection, 'unit-tests/'), 'resources/')
let $input := doc(concat($resources-dir-path, 'doc-1.xml'))
let $certificate-details :=
	<digital-certificate>
		<keystore-type>JKS</keystore-type>
		<keystore-password>ab987c</keystore-password>
		<key-alias>eXist</key-alias>
		<private-key-password>kpi135</private-key-password>
		<keystore-uri>{concat($resources-dir-path, 'keystore')}</keystore-uri>
	</digital-certificate>
	
	
return $input
(:
	
let $actual-result :=
	<actual-result>
		{
		let $signed-doc := crypto:generate-signature($input, "inclusive", "SHA1", "DSA_SHA1", "dsig", "enveloped")
		return crypto:validate-signature($signed-doc)
		}
	</actual-result>
let $condition := normalize-space($expected-result/text()) = normalize-space($actual-result/text())
	

return
	<result>
		{
		(
		if ($condition)
			then <result-token>passed</result-token>
			else <result-token>failed</result-token>
		, $actual-result
		)
		}
	</result>	:)