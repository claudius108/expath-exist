xquery version "3.0";

import module "http://expath.org/ns/ft-client";

let $connection := ft-client:connect(xs:anyURI('ftp://ftp-user:ftp-pass@127.0.0.1'))
let $expected-result :=
	<expected-result>0</expected-result>
let $actual-result := 
	<actual-result>
		{
		util:catch(
		"java.lang.Exception",
		ft-client:list-resources($connection, "/dir-with-rights/dir-without-rights"),
		<error>{$util:exception-message}</error>
		)				
		}		
	</actual-result>
let $close-connection := ft-client:disconnect($connection)		
	

return
	<result>
		{
		(
		$actual-result,
		if ($actual-result/error)
			then <result-token>passed</result-token>
			else <result-token>failed</result-token>
		)
		}
	</result>