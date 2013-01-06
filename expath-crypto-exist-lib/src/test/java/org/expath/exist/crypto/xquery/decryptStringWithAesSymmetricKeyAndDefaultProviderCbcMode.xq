xquery version "3.0";

import module "http://expath.org/ns/crypto";

let $expected-result := <expected-result>Short string for tests.</expected-result>
let $iv := crypto:hash("initialization vector", "MD5", "")
let $actual-result :=
	<actual-result>
	  {
	    crypto:decrypt("51-143-171-200-187-20-34-252-231-243-254-42-36-13-9-123-191-251-243-42-3-238-193-13-155-168-139-67-135-3-143-54", "symmetric", "1234567890123456", "AES/CBC/PKCS5Padding", $iv, ())
	  }
	</actual-result>
let $condition := util:parse-html($expected-result) = util:parse-html($actual-result)
	

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