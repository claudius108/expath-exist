xquery version "1.0";

let $script-collection := concat(replace(replace(request:get-effective-uri(), "/(\w)+.xql$", ""), "/rest/db", ""), '/')
, $expected-result :=
	<expected-result>f98a5e65106b72d94bd71afaa6564e49ca7fe80ad4c371a2863b4acacd5edea43afda58b163a0e1447ee894240dee2c891e6c7d4e1beac374c159d3efc914ada</expected-result>
, $actual-result :=
	<actual-result>
		{crypto:hash("Short string for tests.", "SHA-512", "SUN")}
	</actual-result>
, $condition := normalize-space($expected-result/text()) = normalize-space($actual-result/text())
	

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