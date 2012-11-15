xquery version "1.0";

let $script-collection := concat(replace(replace(request:get-effective-uri(), "tests/(\w)+.xql$", ""), "/rest//db", ""), 'data/')
, $private-key := util:binary-to-string(util:binary-doc(concat($script-collection, 'Open-Private-Key')))
, $connection := ft-client:connect(xs:anyURI('sftp://ftp-user:ftp-pass@127.0.0.1'), $private-key)
, $expected-result :=
	<expected-result>0</expected-result>
, $actual-result := 
	<actual-result>
		{
		ft-client:list-resources($connection, "/home/ftp-user/dir-with-rights")
		}
	</actual-result>
, $close-connection := ft-client:disconnect($connection)		
	

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