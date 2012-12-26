xquery version "3.0";

import module "http://expath.org/ns/ft-client";

let $script-collection := concat(replace(replace(request:get-effective-uri(), "tests/(\w)+.xql$", ""), "/rest//db", ""), 'data/')
let $private-key := util:binary-doc(concat($script-collection, 'Open-Private-Key'))
let $connection := ft-client:connect(xs:anyURI('sftp://ftp-user:ftp-pass@127.0.0.1'), util:binary-to-string($private-key))
let $expected-result :=
	<expected-result/>
	, $resource := util:binary-doc(concat($script-collection, "bg.gif"))
let $actual-result := 
	<actual-result>
		{
		ft-client:store-resource($connection, concat("/home/ftp-user/dir-with-rights/tmp/bg", util:uuid(), ".gif"), $resource)
		}
	</actual-result>
let $close-connection := ft-client:disconnect($connection)		
	

return
	<result>
		{
		(
		$actual-result,
		if ($actual-result/text() = 'true')
			then <result-token>passed</result-token>
			else <result-token>failed</result-token>
		)
		}
	</result>