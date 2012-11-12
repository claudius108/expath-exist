xquery version "1.0";

let $connection := ft-client:connect(xs:anyURI('ftp://ftp-user:ftp-pass@127.0.0.1'))
, $expected-result :=
	<expected-result>0</expected-result>
, $actual-result := 
	<actual-result>
		{
		util:binary-to-string(ft-client:retrieve-resource($connection, "/dir-with-rights/test.txt"))
		}
	</actual-result>
, $close-connection := ft-client:disconnect($connection)		
	

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