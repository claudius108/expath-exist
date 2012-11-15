xquery version "1.0";

let $script-collection := concat(replace(replace(request:get-effective-uri(), "/(\w)+.xql$", ""), "/rest//db", ""), '/')
, $expected-result :=
	<expected-result>55LyDq7GFnqijauK4CQWR4AqyZk=</expected-result>
, $private-key :=
	util:binary-to-string(util:binary-doc(concat(substring-before($script-collection, 'unit-tests/'), 'resources/private-key.pem')))
, $actual-result :=
	<actual-result>
		{crypto:hmac("Short string for tests.", $private-key, "HmacSha1", "SunJCE")}
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