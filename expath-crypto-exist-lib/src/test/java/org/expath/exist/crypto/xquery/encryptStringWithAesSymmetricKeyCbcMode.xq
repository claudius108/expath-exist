xquery version "3.0";

import module "http://expath.org/ns/crypto";

let $expected-result :=
	<expected-result>51-143-171-200-187-20-34-252-231-243-254-42-36-13-9-123-191-251-243-42-3-238-193-13-155-168-139-67-135-3-143-54</expected-result>
let $iv := crypto:hash("initialization vector", "MD5", "")
let $actual-result :=
	<actual-result>
		{
			crypto:encrypt("Short string for tests.", "symmetric", "1234567890123456", "AES/CBC/PKCS5Padding", $iv, "SunJCE")				
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