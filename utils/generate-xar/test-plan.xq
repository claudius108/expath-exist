xquery version "3.0";

declare namespace kert = "http://kuberam.ro/ns/kert";

let $unit-tests-collection-path := concat('xmldb:', resolve-uri('unit-tests', concat(substring-after(system:get-module-load-path(), 'xmldb:'), '/')))

let $unit-test-names := xmldb:get-child-resources($unit-tests-collection-path)

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
          background-color: #fbfbef;
          }
          html {
          width: 100%;
          padding: 5px;
          }
          .title {
        margin-left: 3%;
          }
          .test-summary {
          width: 98%;
          }
          .logo {
            position: relative;
            top: 2px;
            width: 3%;
            heigh: 100%;
            float: left;
            margin-top: 10px;
          } 
          .test-description {
        float: left;
            width: 95%;   
            margin-top: 10px;            
          }
          .test-summary-title {
          margin: 0 0 10 22;
          font-weight: bold;
          width: 90%;
          }
          .test-summary-description {
          width: 90%;          
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
        <h2 class="title">{$test-plan/kert:description/text()}</h2>
        {
        for $unit-test-name in $unit-test-names
        let $unit-test := $test-plan//kert:test[ends-with(kert:test-url, $unit-test-name)]
        let $unit-test-status := util:eval(xs:anyURI(concat($unit-tests-collection-path, '/', $unit-test-name)))//element()[local-name() = 'result-token']
        order by $unit-test-name
        return
             <div class="test-summary">
                <div class="logo">
                    <img src="../icon.png" width="24" height="24"/>
                </div>
                <div class="test-description">
                    <div class="test-summary-title">
                        {
                        $unit-test/kert:title/text()
                        }
                        (status: <span class="test-status-{$unit-test-status}">{$unit-test-status/text()}</span>)
                        <span class="test-summary-operations"><a href="{$unit-test/kert:test-url}">Run</a></span>
                     </div>
                     <div class="test-summary-description">{$unit-test/kert:description/text()}</div>
                 </div>
              </div>
          }
     </body>
</html>