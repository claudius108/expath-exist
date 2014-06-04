xquery version "3.0";

import module "http://expath.org/ns/ft-client";
import module namespace config = "http://kuberam.ro/ns/config" at "../config.xqm";

let $connection := ft-client:connect($config:ftp-server-connection-url)
let $expected-result := <expected-result>0</expected-result>
let $actual-result := 
	<actual-result>
		{
		ft-client:retrieve-resource($connection, "/dir-with-rights/image-with-rights.gif")
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