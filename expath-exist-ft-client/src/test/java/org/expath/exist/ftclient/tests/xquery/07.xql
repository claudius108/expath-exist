xquery version "1.0";

let $script-collection := concat(replace(replace(request:get-effective-uri(), "tests/(\w)+.xql$", ""), "/rest//db", ""), 'data/')
, $connection := ft-client:connect(xs:anyURI('ftp://ftp-user:ftp-pass@127.0.0.1'))
, $expected-result :=
	<expected-result/>
, $file-to-store := util:binary-to-string(util:binary-doc(concat($script-collection, "test.txt")))
, $actual-result := 
	<actual-result>
		{
		ft-client:store-resource($connection, concat("/dir-with-rights/tmp/test", util:uuid(), ".txt"), $file-to-store)
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