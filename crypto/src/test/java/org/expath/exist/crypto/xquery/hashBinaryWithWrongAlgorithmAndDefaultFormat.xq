xquery version "3.0";

import module "http://expath.org/ns/crypto";

let $input := util:binary-doc(concat('xmldb:', resolve-uri('../resources/keystore', concat(substring-after(system:get-module-load-path(), 'xmldb:'), '/'))))
let $expected-result :=
	<expected-result>err:CX21: The algorithm is not supported.</expected-result>
let $actual-result :=
	<actual-result>
        {
	  try {
	    crypto:hash($input, "SHA-17", ())
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