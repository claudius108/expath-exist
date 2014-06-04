xquery version "3.0";

import module "http://expath.org/ns/ft-client";
import module namespace config = "http://kuberam.ro/ns/config" at "../config.xqm";

let $connection := ft-client:connect($config:ftp-server-connection-url)
let $expected-result := <expected-result/>
let $file-to-store := util:binary-to-string(util:binary-doc(concat($config:resources-collection, "test.txt")))
let $file-to-store-path := concat("/dir-with-rights/test", util:uuid(), ".txt")
let $store-file := ft-client:store-resource($connection, $file-to-store-path, $file-to-store)
let $actual-result :=
	<actual-result>
		{
		ft-client:delete-resource($connection, $file-to-store-path)
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