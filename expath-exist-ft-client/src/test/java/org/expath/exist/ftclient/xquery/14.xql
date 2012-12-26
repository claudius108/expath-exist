xquery version "3.0";

import module "http://expath.org/ns/ft-client";

let $script-collection := concat(replace(replace(request:get-effective-uri(), "tests/(\w)+.xql$", ""), "/rest//db", ""), 'data/')
let $private-key := util:binary-to-string(util:binary-doc(concat($script-collection, 'Open-Private-Key')))
let $connection := ft-client:connect(xs:anyURI('sftp://ftp-user:ftp-pass@127.0.0.1'), $private-key)
let $expected-result :=
	<expected-result>0</expected-result>
let $actual-result := 
	<actual-result>
		{
		util:catch(
		"java.lang.Exception",
		ft-client:list-resources($connection, "/non-existing-directory"),
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