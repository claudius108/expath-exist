xquery version "1.0";

let $script-collection := concat(replace(replace(request:get-effective-uri(), "/(\w)+.xql$", ""), "/rest/db", ""), '/')
, $expected-result :=
	<expected-result>bac7b5a00a1ef2f2209c5832833d8e2b</expected-result>
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