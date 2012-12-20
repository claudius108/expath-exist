xquery version "3.0";

import module "http://expath.org/ns/crypto";

let $string-to-hash := 'PUT
c8fdb181845a4ca6b8fec737b3581d76
text/html
Thu, 17 Nov 2005 18:49:58 GMT
x-amz-magic:abracadabra
x-amz-meta-author:foo@bar.com
/quotes/nelson'
let $expected-result :=
	<expected-result>jZNOcbfWmD/A/f3hSvVzXZjM2HU=</expected-result>
let $private-key :='OtxrzxIsfpFjA7SwPzILwy8Bw21TLhquhboDYROV'
let $actual-result :=
	<actual-result>
		{crypto:hmac($string-to-hash, $private-key, "HMAC-SHA-1", "SunJCE")}
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