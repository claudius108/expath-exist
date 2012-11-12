xquery version "1.0";

let $script-collection := concat(replace(replace(request:get-effective-uri(), "/(\w)+.xql$", ""), "/rest/db", ""), '/')
, $expected-result :=
	<expected-result>1780850d20479be066e34d1b3a01f6ab621b2148fc5d150159fd229f1ee5acdd13f081f3f6c706566268199dbeb352da</expected-result>
, $actual-result :=
	<actual-result>
		{crypto:hash("Short string for tests.", "SHA-384", "SUN")}
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