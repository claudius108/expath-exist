xquery version "3.0";

import module "http://expath.org/ns/ft-client";
import module namespace config = "http://kuberam.ro/ns/config" at "../config.xqm";

let $connection := ft-client:connect($config:ftp-server-connection-url)
let $expected-result := <expected-result/>
let $resource := util:binary-doc($config:resources-collection || "bg.gif")
let $actual-result := 
	<actual-result>
		{
		ft-client:store-resource($connection, concat("/dir-with-rights/tmp/bg", util:uuid(), ".gif"), $resource)
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