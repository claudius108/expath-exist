xquery version "3.0";

import module "http://expath.org/ns/ft-client";

let $script-collection := concat(replace(replace(request:get-effective-uri(), "tests/(\w)+.xql$", ""), "/rest//db", ""), 'data/')
let $connection := ft-client:connect(xs:anyURI('ftp://ftp-user:ftp-pass@127.0.0.1'))
let $expected-result :=
	<expected-result/>
let $resource := util:binary-doc(concat($script-collection, "bg.gif"))
let $actual-result := 
	<actual-result>
		{
		util:catch(
		"java.lang.Exception",
		ft-client:store-resource($connection, concat("/wrong-path/bg", util:uuid(), ".gif"), $resource),
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