xquery version "3.0";

import module "http://expath.org/ns/crypto";

let $expected-result := <expected-result>Short string for tests.</expected-result>
let $actual-result :=
    <actual-result>
      {
        crypto:decrypt("222-157-20-54-132-99-46-30-73-43-253-148-61-155-86-141-51-56-40-42-31-168-189-56-236-102-58-237-175-171-9-87", "symmetric", "1234567890123456", "AES", (), "SunJCE")
      }
    </actual-result>
let $condition := util:parse-html($expected-result) = util:parse-html($actual-result)


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