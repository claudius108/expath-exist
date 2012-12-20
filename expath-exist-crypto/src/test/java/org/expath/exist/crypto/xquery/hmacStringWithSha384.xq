xquery version "3.0";

import module "http://expath.org/ns/crypto";

let $private-key := util:binary-to-string(util:binary-doc(concat('xmldb:', resolve-uri('../resources/private-key.pem', concat(substring-after(system:get-module-load-path(), 'xmldb:'), '/')))))
let $expected-result :=
	<expected-result>RRirKZTmx+cG8EXvgrRnpYFPEPYXaZBirY+LFmiUBAK61LCryDsL4clFRG5/BcBr</expected-result>
let $actual-result :=
	<actual-result>
		{crypto:hmac("Short string for tests.", $private-key, "HMAC-SHA-384", "SunJCE")}
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