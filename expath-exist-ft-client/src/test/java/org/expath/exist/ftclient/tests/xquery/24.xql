xquery version "1.0";

let $script-collection := concat(replace(replace(request:get-effective-uri(), "tests/(\w)+.xql$", ""), "/rest//db", ""), 'data/')
, $connection := ft-client:connect(xs:anyURI('ftp://ftp-user:ftp-pass@127.0.0.1'))
, $expected-result :=
	<expected-result/>
, $file-to-store := util:binary-to-string(util:binary-doc(concat($script-collection, "test.txt")))
, $file-to-store-path := concat("/dir-with-rights/test", util:uuid(), ".txt")
, $store-file := ft-client:store-resource($connection, $file-to-store-path, $file-to-store)
, $actual-result :=
	<actual-result>
		{
		ft-client:delete-resource($connection, $file-to-store-path)
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