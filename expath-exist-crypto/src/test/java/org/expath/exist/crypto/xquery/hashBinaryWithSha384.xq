xquery version "1.0";

let $script-collection := concat(replace(replace(request:get-effective-uri(), "/(\w)+.xql$", ""), "/rest/db", ""), '/')
let $input := util:binary-doc(concat(substring-before($script-collection, 'unit-tests/'), 'resources/keystore'))
let $expected-result :=
	<expected-result>dc43771a05fb624022109fde8fafc3c2dafcecd7b45ed2d67cfc26673a8b9cbbf37ef35f2ea805f147b97da31620a16</expected-result>
let $actual-result :=
	<actual-result>
		{crypto:hash($input, "SHA-384", "SUN")}
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