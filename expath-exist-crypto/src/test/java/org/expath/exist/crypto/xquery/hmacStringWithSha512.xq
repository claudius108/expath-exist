xquery version "1.0";

import module namespace xut = "http://kuberam.ro/ns/xquery-unit-tests" at "../../xquery-unit-tests.xqm";

let $private-key := util:binary-to-string(util:binary-doc(concat($xut:resources-collection, 'private-key.pem')))
let $expected-result :=
	<expected-result>z9MtEpBXxO5bKmsXJWfKsZ4v+RduKU89Y95H2HMGQEwHGefWmewNNQ7urZVuWEU5aeRRdO7G7j0Q
	lcLYv1pkrg==</expected-result>
let $actual-result :=
	<actual-result>
		{crypto:hmac("Short string for tests.", $private-key, "HMAC-SHA-512", ())}
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