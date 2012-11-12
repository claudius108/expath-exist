xquery version "1.0";

let $script-collection := concat(replace(replace(request:get-effective-uri(), "/(\w)+.xql$", ""), "/rest/db", ""), '/')
let $input := util:binary-doc(concat(substring-before($script-collection, 'unit-tests/'), 'resources/keystore'))
let $expected-result :=
	<expected-result>dfb262341ca6db9d327b7694274e1169983748548fd37a89f0547f23525c9152</expected-result>
let $actual-result :=
	<actual-result>
		{crypto:hash($input, "SHA-256", "SUN")}
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