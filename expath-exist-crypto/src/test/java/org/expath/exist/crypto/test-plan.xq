xquery version "3.0";

declare namespace kert = "http://kuberam.ro/ns/kert";

let $unit-test-names := xmldb:get-child-resources("/web/tests/expath-crypto/unit-tests/")
   
let $test-plan := doc('test-plan.xml')/*
    

return 
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <style>
          <![CDATA[
          body {
          font-family: 'Verdana', sans-serif;
          width: 100%;
          margin: 0;
          padding: 0;
          background-color: #EFF5FB;
          }
          html {
          width: 100%;
          margin: 5 0 0 0;
          padding: 0;
          }
          .test-summary {
          width: 98%;
          border: 1px solid black;
          border-radius: 15px;
          margin: 5 5 15 5;
          }
          .test-summary-title {
          margin: 0 0 10 22;
          width: 100%;
          font-weight: bold;
          }
          .test-summary-description {
          margin-left:
          12px;
          }
          .test-summary-operations {
          margin-left: 12px;
          }
          .test-status-passed {
          color: green;
          }
          .test-status-failed {
          color: red;
          }
          ]]>
        </style>
    </head>
    <body>
        <h2>{$test-plan/kert:description}</h2>
        {
        for $unit-test-name in $unit-test-names
        let $unit-test := $test-plan//kert:test[ends-with(kert:test-url, $unit-test-name)]
        let $unit-test-status := util:eval(xs:anyURI(concat('/web/tests/expath-crypto/unit-tests/', $unit-test-name)))//element()[local-name() = 'result-token']
        order by $unit-test-name
        return
             <div class="test-summary">
                <div class="test-summary-title">
                    {$unit-test/kert:title} (status: <span class="test-status-{$unit-test-status}">{$unit-test-status/text()}</span>)
                    <span class="test-summary-operations"><a href="{$unit-test/kert:test-url}">Run test</a></span>
                </div>
            <div class="test-summary-description">{$unit-test/kert:description}</div>             
             </div>
 
        }
    </body>
</html>