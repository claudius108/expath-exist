xquery version "3.0";

import module "http://expath.org/ns/ft-client";

let $script-collection := concat(replace(replace(request:get-effective-uri(), "tests/(\w)+.xql$", ""), "/rest//db", ""), 'data/')
let $private-key := util:binary-doc(concat($script-collection, 'Open-Private-Key'))
let $connection := ft-client:connect(xs:anyURI('sftp://ftp-user:ftp-pass@127.0.0.1'), util:binary-to-string($private-key))
let $expected-result :=
	<expected-result>0</expected-result>
let $actual-result := 
	<actual-result>
		{
		ft-client:retrieve-resource($connection, "/home/ftp-user/dir-with-rights/image-with-rights.gif")
		}
	</actual-result>
let $close-connection := ft-client:disconnect($connection)		
	

return
	<result>
		{
		(
		$actual-result,
		if (normalize-space($expected-result/text()) ne normalize-space($actual-result/text()))
			then <result-token>passed</result-token>
			else <result-token>failed</result-token>
		)
		}
	</result>