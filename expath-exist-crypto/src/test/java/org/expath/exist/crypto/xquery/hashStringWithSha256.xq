xquery version "1.0";

let $script-collection := concat(replace(replace(request:get-effective-uri(), "/(\w)+.xql$", ""), "/rest/db", ""), '/')
, $expected-result :=
	<expected-result>13e0742732d18319b6fb5ac1f2a219a10d909fe24bc7024258e46fe3a7ca84c3</expected-result>
, $actual-result :=
	<actual-result>
		{crypto:hash("Short string for tests.", "SHA-256", "SUN")}
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