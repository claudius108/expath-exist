xquery version "3.0";

import module "http://expath.org/ns/crypto";

let $input := util:binary-doc(concat('xmldb:', resolve-uri('../resources/keystore', concat(substring-after(system:get-module-load-path(), 'xmldb:'), '/'))))
let $expected-result :=
	<expected-result>GyscHvnJKxInsBLgSg/FRAmQXYU=</expected-result>
let $actual-result :=
	<actual-result>
		{crypto:hash($input, "SHA-1", ())}
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