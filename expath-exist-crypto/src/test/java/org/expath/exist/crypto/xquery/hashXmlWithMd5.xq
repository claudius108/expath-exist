xquery version "1.0";

let $script-collection := concat(replace(replace(request:get-effective-uri(), "/(\w)+.xql$", ""), "/rest//db", ""), '/')
let $expected-result :=
	<expected-result>xMpCOKC5I4INzFCab3WEmw==</expected-result>
let $actual-result :=
	<actual-result>
		{crypto:hash(doc(concat(substring-before($script-collection, 'unit-tests/'), 'resources/doc-1.xml'))/*/*[1], "MD5", "SUN")}
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