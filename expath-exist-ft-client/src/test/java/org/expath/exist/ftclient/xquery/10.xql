xquery version "3.0";

import module "http://expath.org/ns/ft-client";

let $private-key := util:binary-to-string(util:binary-doc(concat('xmldb:', resolve-uri('../resources/Open-Private-Key', concat(substring-after(system:get-module-load-path(), 'xmldb:'), '/')))))
let $connection := ft-client:connect(xs:anyURI('sftp://ftp-user:ftp-pass@127.0.0.1'), $private-key)
let $expected-result :=
	<expected-result/>
let $resource := util:binary-to-string(util:binary-doc(concat('xmldb:', resolve-uri('../resources/test.txt', concat(substring-after(system:get-module-load-path(), 'xmldb:'), '/')))))
let $actual-result := 
	<actual-result>
		{
		ft-client:store-resource($connection, concat("/home/ftp-user/dir-with-rights/tmp/test", util:uuid(), ".txt"), $resource)
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