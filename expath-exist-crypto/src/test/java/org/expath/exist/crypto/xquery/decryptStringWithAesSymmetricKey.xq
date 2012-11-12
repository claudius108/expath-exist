xquery version "1.0";

let $expected-result :=
	<expected-result><a>Test!</a></expected-result>
, $actual-result :=
	<actual-result>
	  {
	    crypto:decrypt("195-222-168-136-219-30-67-109-140-47-163-70-57-42-195-86", "symmetric", "1234567890123456", "AES")
	  }
	</actual-result>
, $condition := util:parse-html($expected-result) = util:parse-html($actual-result)
	

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