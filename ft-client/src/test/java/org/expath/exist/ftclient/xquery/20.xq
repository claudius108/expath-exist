xquery version "3.0";

import module "http://expath.org/ns/ft-client";
import module namespace config = "http://kuberam.ro/ns/config" at "../config.xqm";

let $connection := ft-client:connect($config:ftp-server-connection-url)
let $expected-result := <expected-result/>
let $resource := util:binary-doc(concat('xmldb:', resolve-uri('../resources/bg.gif', concat(substring-after(system:get-module-load-path(), 'xmldb:'), '/'))))
let $actual-result := 
	<actual-result>
		{
          try {
            ft-client:store-resource($connection, concat("/wrong-path/bg", util:uuid(), ".gif"), $resource)
          }
          catch * {
            <error>{$err:description}</error>
          }                                             
		}		
	</actual-result>
let $close-connection := ft-client:disconnect($connection)
let $condition := contains(normalize-space($actual-result/element()/text()), normalize-space($expected-result/element()/text()))
	
	
return
	<result>
		{
		(
		$actual-result,
		if ($condition)
			then <result-token>passed</result-token>
			else <result-token>failed</result-token>
		)
		}
	</result>