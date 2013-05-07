xquery version "3.0";

declare default element namespace "http://www.w3.org/1999/xhtml";
declare namespace kert = "http://kuberam.ro/ns/kert";
declare option exist:serialize "method=html5 media-type=text/html";

let $collection-path := substring-before(request:get-servlet-path(), 'test-plan.xq')

let $test-plan-in-html-format := transform:transform(doc(concat($collection-path, 'test-plan.xml')), doc(concat($collection-path, 'generate-tests-presentation.xsl')), ())

return transform:transform($test-plan-in-html-format, doc(concat($collection-path, 'update-unit-tests-statuses.xsl')), <parameters><param name="request-url" value="{$collection-path}"/></parameters>)