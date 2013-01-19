xquery version "3.0";

declare default element namespace "http://www.w3.org/1999/xhtml";
declare namespace kert = "http://kuberam.ro/ns/kert";

let $test-plan-in-html-format := transform:transform(doc('test-plan.xml'), doc('generate-tests-presentation.xsl'), ())
    
    
return transform:transform($test-plan-in-html-format, doc('update-unit-tests-statuses.xsl'), <parameters><param name="request-url" value="{request:get-url()}"/></parameters>)