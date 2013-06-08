xquery version "3.0";

import module "http://expath.org/ns/ft-client";

let $connection := ft-client:connect(xs:anyURI('ftp://ftp-user:ftp-pass@127.0.0.1'))
let $expected-result :=
	<expected-result>0</expected-result>
let $close-connection := ft-client:disconnect($connection)
let $actual-result := 
	<actual-result>
		{
          try {
            ft-client:list-resources($connection, "/")
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
		$actual-result,
		if ($condition)
			then <result-token>passed</result-token>
			else <result-token>failed</result-token>
		)
		}
	</result>