xquery version "3.0";

import module "http://expath.org/ns/crypto";

let $expected-result := <expected-result>true</expected-result>
let $input := doc('../resources/doc-1.xml')
let $certificate-details :=
	<digital-certificate>
		<keystore-type>JKS</keystore-type>
		<keystore-password>ab987c</keystore-password>
		<key-alias>eXist</key-alias>
		<private-key-password>kpi135</private-key-password>
		<keystore-uri>{concat('xmldb:', resolve-uri('../resources/keystore', concat(substring-after(system:get-module-load-path(), 'xmldb:'), '/')))}</keystore-uri>
	</digital-certificate>
let $signed-doc := crypto:generate-signature($input, "inclusive", "SHA1", "DSA_SHA1", "dsig", "enveloped")	
let $actual-result :=
	<actual-result>
		{
		crypto:validate-signature($signed-doc)
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
	</result>