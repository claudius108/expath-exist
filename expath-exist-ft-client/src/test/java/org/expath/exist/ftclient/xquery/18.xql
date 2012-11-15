xquery version "1.0";

let $script-collection := concat(replace(replace(request:get-effective-uri(), "tests/(\w)+.xql$", ""), "/rest//db", ""), 'data/')
, $private-key := util:binary-doc(concat($script-collection, 'Open-Private-Key'))
, $connection := ft-client:connect(xs:anyURI('sftp://ftp-user:ftp-pass@127.0.0.1'), util:binary-to-string($private-key))
, $expected-result :=
	<expected-result>0</expected-result>
, $actual-result := 
	<actual-result>
		{
		util:catch(
		"java.lang.Exception",
		ft-client:retrieve-resource($connection, "/home/ftp-user/dir-with-rights/non-existing-image.gif"),
		<error>{$util:exception-message}</error>
		)				
		}		
	</actual-result>
, $close-connection := ft-client:disconnect($connection)		
	

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