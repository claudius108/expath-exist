xquery version "3.0";

import module "http://expath.org/ns/crypto";

let $input := doc('../resources/doc-1.xml')
let $expected-result :=
	<expected-result>xMpCOKC5I4INzFCab3WEmw==</expected-result>
let $actual-result :=
	<actual-result>
		{crypto:hash($input/*/*[1], "MD5", "SUN")}
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