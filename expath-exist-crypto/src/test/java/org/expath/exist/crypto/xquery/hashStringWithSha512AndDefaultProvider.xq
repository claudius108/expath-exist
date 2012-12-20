xquery version "3.0";

import module "http://expath.org/ns/crypto";

let $expected-result :=
	<expected-result>+YpeZRBrctlL1xr6plZOScp/6ArUw3GihjtKys1e3qQ6/aWLFjoOFEfuiUJA3uLIkebH1OG+rDdM
	FZ0+/JFK2g==</expected-result>
, $actual-result :=
	<actual-result>
		{crypto:hash("Short string for tests.", "SHA-512", ())}
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