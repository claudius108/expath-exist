xquery version "1.0";

let $script-collection := concat(replace(replace(request:get-effective-uri(), "/(\w)+.xql$", ""), "/rest/db", ""), '/')
let $input := util:binary-doc(concat(substring-before($script-collection, 'unit-tests/'), 'resources/keystore'))
let $expected-result :=
	<expected-result>5efa1946cbd4cd89b6da13ee8303682eea4363d864beedbe1e8057090cccd04854221a016d4e1fe60fad6d6b8a43bdcfa3a87165a8ec892c78ab91139df3a33</expected-result>
let $actual-result :=
	<actual-result>
		{crypto:hash($input, "SHA-512", "SUN")}
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