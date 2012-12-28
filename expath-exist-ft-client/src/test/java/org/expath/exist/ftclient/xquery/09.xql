xquery version "3.0";

import module "http://expath.org/ns/ft-client";

let $private-key := util:binary-to-string(util:binary-doc(concat('xmldb:', resolve-uri('../resources/Open-Private-Key', concat(substring-after(system:get-module-load-path(), 'xmldb:'), '/')))))
let $connection := ft-client:connect(xs:anyURI('sftp://ftp-user:ftp-pass@127.0.0.1'), $private-key)
let $expected-result := <expected-result>0</expected-result>
let $actual-result := 
	<actual-result>
		{
		util:binary-to-string(ft-client:retrieve-resource($connection, "/home/ftp-user/dir-with-rights/test.txt"))
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