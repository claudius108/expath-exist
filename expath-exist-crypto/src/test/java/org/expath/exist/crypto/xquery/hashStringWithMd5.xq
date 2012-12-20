xquery version "3.0";

import module "http://expath.org/ns/crypto";

let $expected-result :=
	<expected-result>use1oAoe8vIgnFgygz2OKw==</expected-result>
, $actual-result :=
	<actual-result>
		{crypto:hash("Short string for tests.", "MD5", "SUN")}
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