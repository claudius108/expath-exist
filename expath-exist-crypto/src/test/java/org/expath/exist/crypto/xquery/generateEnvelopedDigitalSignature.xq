xquery version "3.0";

import module "http://expath.org/ns/crypto";

let $expected-result :=
	<expected-result>/KaCzo4Syrom78z3EQ5SbbB4sF7ey80etKII864WF64B81uRpH5t9jQTxeEu0ImbzRMqzVDZkVG9
	xD7nN1kuFw==
	</expected-result>
let $sample-doc := doc('../resources/doc-1.xml')
, $certificate-details :=
	<digital-certificate>
		<keystore-type>JKS</keystore-type>
		<keystore-password>ab987c</keystore-password>
		<key-alias>eXist</key-alias>
		<private-key-password>kpi135</private-key-password>
		<keystore-uri>{concat('xmldb:', resolve-uri('../resources/keystore', concat(substring-after(system:get-module-load-path(), 'xmldb:'), '/')))}</keystore-uri>
	</digital-certificate>
, $actual-result :=
	<actual-result>
		{
		let $signed-doc := crypto:generate-signature($sample-doc, "inclusive", "SHA1", "DSA_SHA1", "dsig", "enveloped")
		return $signed-doc//*[local-name() = 'P']/text()
		}
	</actual-result>
, $condition := normalize-space($expected-result/text()) = normalize-space($actual-result/text())
	

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