xquery version "1.0";

let $title := 'Get Information about the File Transfer Client Module Status'
, $expected-result :=
	<expected-result>0</expected-result>
, $actual-result := 
	<actual-result>
		<util-is-module-mapped>{util:is-module-mapped('http://expath.org/ns/ft-client')}</util-is-module-mapped>
		<util-is-module-registered>{util:is-module-registered('http://expath.org/ns/ft-client')}</util-is-module-registered>
		<util-get-module-info>{util:get-module-info('http://expath.org/ns/ft-client')}</util-get-module-info>
	</actual-result>

(: util:registered-modules() util:mapped-modules() util:unmap-module() :)

return
	<result>
		{
		(
		$actual-result,
		if ($actual-result/util-is-module-registered/text() = 'true')
			then <result-token>passed</result-token>
			else <result-token>failed</result-token>
		)
		}
	</result>


