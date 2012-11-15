xquery version "1.0";

let $script-collection := concat(replace(replace(request:get-effective-uri(), "/(\w)+.xql$", ""), "/rest/db", ""), '/')
let $private-key := util:binary-doc(concat(substring-before($script-collection, 'unit-tests/'), 'resources/Open-Private-Key'))
let $connection := ft-client:connect(xs:anyURI('sftp://ftp-user:ftp-pass@127.0.0.1'), util:binary-to-string($private-key))
let $expected-result := <expected-result/>
let $actual-result := 
	<actual-result>
		{
		ft-client:store-resource($connection, concat("/home/ftp-user/dir-with-rights/tmp/tempFolder", util:uuid(), "/"), ())
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