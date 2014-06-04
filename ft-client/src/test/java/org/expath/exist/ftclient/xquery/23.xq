xquery version "3.0";

import module "http://expath.org/ns/ft-client";
import module namespace config = "http://kuberam.ro/ns/config" at "../config.xqm";

let $connection := ft-client:connect($config:ftp-server-connection-url)
let $expected-result := <expected-result/>
let $directory-to-create-path := concat('/dir-with-rights/tmp/test', util:uuid())
let $store-file := ft-client:store-resource($connection, $directory-to-create-path, ())
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