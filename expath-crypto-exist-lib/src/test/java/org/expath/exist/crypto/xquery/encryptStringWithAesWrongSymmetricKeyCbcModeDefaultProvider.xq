xquery version "3.0";

import module "http://expath.org/ns/crypto";

let $expected-result := <expected-result>err:CX19: The secret key is invalid</expected-result>
let $iv := crypto:hash("initialization vector", "MD5", "")	
let $actual-result :=
	<actual-result>
		{
          try {
            crypto:encrypt("Short string for tests.", "symmetric", "12345678901234567", "AES/CBC/PKCS5Padding", $iv, ())
          }
          catch * {
            <error>{$err:description}</error>
          }		
		}
			</actual-result>
let $condition := contains(normalize-space($actual-result/element()/text()), normalize-space($expected-result/element()/text()))
	

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
	