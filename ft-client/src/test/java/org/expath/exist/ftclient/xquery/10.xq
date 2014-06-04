xquery version "3.0";

import module "http://expath.org/ns/ft-client";
import module namespace config = "http://kuberam.ro/ns/config" at "../config.xqm";

let $connection := ft-client:connect($config:sftp-server-connection-url, $config:private-key)
let $expected-result := <expected-result/>
let $resource := util:binary-to-string(util:binary-doc(concat('xmldb:', resolve-uri('../resources/test.txt', concat(substring-after(system:get-module-load-path(), 'xmldb:'), '/')))))
let $actual-result := 
	<actual-result>
		{
		ft-client:store-resource($connection, $config:server-home-folder || "/dir-with-rights/tmp/test" || util:uuid() || ".txt", $resource)
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