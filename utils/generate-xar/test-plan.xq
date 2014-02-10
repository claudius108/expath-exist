xquery version "3.0";

declare default element namespace "http://www.w3.org/1999/xhtml";
declare namespace kert = "http://kuberam.ro/ns/kert";
declare option exist:serialize "method=html5 media-type=text/html";


let $test-plan-in-html-format := doc(xmldb:store("/apps/expath-crypto/tests", "test-plan.html",  transform:transform(doc('test-plan.xml'), doc('generate-tests-presentation.xsl'), ())))

  let $update-unit-tests-statuses :=
    for $test-status-span in $test-plan-in-html-format//*[@class = 'test-status-']
    let $status := httpclient:get(xs:anyURI(substring-before(request:get-url(), "test-plan.xq") || $test-status-span/following-sibling::span/a/@href), false(), ())/*[2]/*/*[1]/text()
    return update value $test-status-span/@class with concat('test-status-', $status)
    
return $test-plan-in-html-format
