xquery version "1.0";

let $script-collection := concat(replace(replace(request:get-effective-uri(), "/(\w)+.xql$", ""), "/rest/db", ""), '/')
, $string-to-hash := 'PUT
c8fdb181845a4ca6b8fec737b3581d76
text/html
Thu, 17 Nov 2005 18:49:58 GMT
x-amz-magic:abracadabra
x-amz-meta-author:foo@bar.com
/quotes/nelson'
, $expected-result :=
	<expected-result>jZNOcbfWmD/A/f3hSvVzXZjM2HU=</expected-result>
, $private-key :='OtxrzxIsfpFjA7SwPzILwy8Bw21TLhquhboDYROV'
, $actual-result :=
	<actual-result>
		{crypto:hmac($string-to-hash, $private-key, "HmacSha1", "SunJCE")}
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