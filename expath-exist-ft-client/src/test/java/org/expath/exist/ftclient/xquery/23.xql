xquery version "3.0";

import module "http://expath.org/ns/ft-client";

let $script-collection := concat(replace(replace(request:get-effective-uri(), "tests/(\w)+.xql$", ""), "/rest//db", ""), 'data/')
let $connection := ft-client:connect(xs:anyURI('ftp://ftp-user:ftp-pass@127.0.0.1'))
let $expected-result :=
	<expected-result/>
let $directory-to-create-path := "/dir-with-rights/test"
(:, $store-file := ft-client:store-resource($connection, $file-to-store-path, $file-to-store):)
let $actual-result :=
	<actual-result>
		{
		ft-client:delete-resource($connection, $directory-to-create-path)
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