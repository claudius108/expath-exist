xquery version "1.0";

let $script-collection := concat(replace(replace(request:get-effective-uri(), "tests/(\w)+.xql$", ""), "/rest//db", ""), 'data/')
, $private-key := util:binary-doc(concat($script-collection, 'Open-Private-Key'))
, $connection := ft-client:connect(xs:anyURI('sftp://ftp-user:ftp-pass@127.0.0.1'), util:binary-to-string($private-key))
, $expected-result :=
	<expected-result/>
	, $resource := util:binary-doc(concat($script-collection, "bg.gif"))
, $actual-result := 
	<actual-result>
		{
		ft-client:store-resource($connection, concat("/home/ftp-user/dir-with-rights/tmp/bg", util:uuid(), ".gif"), $resource)
		}
	</actual-result>
, $close-connection := ft-client:disconnect($connection)		
	

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