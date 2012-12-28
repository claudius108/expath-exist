xquery version "3.0";

import module "http://expath.org/ns/ft-client";

let $private-key := util:binary-to-string(util:binary-doc(concat('xmldb:', resolve-uri('../resources/Open-Private-Key', concat(substring-after(system:get-module-load-path(), 'xmldb:'), '/')))))
let $connection := ft-client:connect(xs:anyURI('sftp://ftp-user:ftp-pass@127.0.0.1'), $private-key)
let $expected-result :=
	<expected-result>0</expected-result>
let $actual-result := 
	<actual-result>
		{
		ft-client:list-resources($connection, "/home/ftp-user/dir-with-rights")
		}
	</actual-result>
let $close-connection := ft-client:disconnect($connection)		
	

return
	<result>
		{
		(
		$actual-result,
		if (count($actual-result/*/*) > number($expected-result))
			then <result-token>passed</result-token>
			else <result-token>failed</result-token>
		)
		}
	</result>