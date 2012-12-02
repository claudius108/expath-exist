xquery version "1.0";

let $private-key := util:binary-doc(concat('xmldb:', resolve-uri('../resources/private-key.pem', concat(substring-after(system:get-module-load-path(), 'xmldb:'), '/'))))
let $expected-result :=
	<expected-result>l4MY6Yosjo7W60VJeXB/PQ==</expected-result>
let $actual-result :=
	<actual-result>
		{crypto:hmac("Short string for tests.", $private-key, "HmacMD5", "SunJCE")}
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