xquery version "1.0";

let $script-collection := concat(replace(replace(request:get-effective-uri(), "/(\w)+.xql$", ""), "/rest/db", ""), '/')
, $expected-result :=
	<expected-result>cfd32d129057c4ee5b2a6b172567cab19e2ff9176e294f3d63de47d87306404c0719e7d699ec0d350eeead956e58453969e45174eec6ee3d1095c2d8bf5a64ae</expected-result>
, $private-key :=
	util:binary-to-string(util:binary-doc(concat(substring-before($script-collection, 'unit-tests/'), 'resources/private-key.pem')))
, $actual-result :=
	<actual-result>
		{crypto:hmac("Short string for tests.", $private-key, "HmacSha512", "SunJCE")}
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