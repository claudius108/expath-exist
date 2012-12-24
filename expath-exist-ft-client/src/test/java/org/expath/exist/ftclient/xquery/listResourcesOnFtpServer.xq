xquery version "3.0";

import module "http://expath.org/ns/ft-client";

let $input := util:binary-doc(concat('xmldb:', resolve-uri('../resources/keystore', concat(substring-after(system:get-module-load-path(), 'xmldb:'), '/'))))

let $connection := ft-client:connect(xs:anyURI('ftp://ftp-user:ftp-pass@127.0.0.1'))
, $expected-result :=
	<expected-result>0</expected-result>
, $actual-result := 
	<actual-result>
		{
		ft-client:list-resources($connection, "/dir-with-rights")
		}
	</actual-result>
, $close-connection := ft-client:disconnect($connection)
, $condition := count($actual-result/*/*) > number($expected-result)
	
return
	<result>
		{
		(
		if ($condition)
			then <result-token>passed</result-token>
			else <result-token>failed</result-token>
		, $actual-result
		)
		}
	</result>