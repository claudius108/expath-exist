xquery version "3.0";

import module "http://expath.org/ns/crypto";

let $expected-result :=
	<expected-result><error>Invalid AES key length: 17 bytes</error></expected-result>
, $actual-result :=
	<actual-result>
		{
			util:catch(
				"java.lang.Exception",
				crypto:encrypt("&lt;a&gt;Test!&lt;/a&gt;", "symmetric", "12345678901234567", "AES", ()),
				<error>{substring-before($util:exception-message, ' [')}</error>
			)				
		}
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