xquery version "3.0";

declare default element namespace "http://www.w3.org/1999/xhtml";
declare namespace kert = "http://kuberam.ro/ns/kert";
declare namespace httpclient = "http://exist-db.org/xquery/httpclient";
declare option exist:serialize "method=html5 media-type=text/html";

let $unit-tests-collection := substring-before(request:get-url(), "test-plan.xq")
let $test-plan := doc('test-plan.xml')

return
        <html>
            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
                <style>
                    body {{
                    font-family: 'Verdana', sans-serif;
                    width: 100%;
                    margin: 0;
                    padding: 0;
                    background-color: #fbfbef;
                    }}
                    html {{
                    width: 100%;
                    padding: 5px;
                    }}
                    .title {{
                    margin-left: 3%;
                    }}
                    .test-summary {{
                    float: left;
                    min-height: 50px;
                    width: 98%;
                    margin-bottom:
                    15px;
                    }}
                    .logo
                    {{
                    width: 3%;
                    float: left;
                    margin-top: 10px;
                    text-align: center;
                    }}
                    .test-summary-content {{
                    float: left;
                    width:
                    94%;                    
                    }}
                    .test-description {{
                    float: left;
                    width: 95%;
                    margin-top: 10px;
                    }}
                    .test-summary-title {{
                    margin: 0 0 10
                    22;
                    font-weight:
                    bold;
                    width: 90%;
                    }}
                    .test-summary-description {{
                    width: 90%;
                    }}
                    .test-summary-operations {{
                    margin-left: 12px;
                    }}
                    .test-status-passed {{
                    color: green;
                    }}
                    .test-status-passed:after {{
                    content: " passed";
                    }}
                    .test-status-failed {{
                    color: red;
                    }}
                    .test-status-failed:after {{
                    content: " failed";
                    }}
                </style>
            </head>    
            <body>
                <h2 class="title">{$test-plan/kert:test-plan/kert:description/text()}</h2>
                <h6 class="title">(test plan version: {$test-plan/kert:test-plan/kert:version/text()})</h6>
                {
                    for $unit-test in $test-plan/kert:test-plan/kert:test
                    let $unit-test-id := $unit-test/@id
                    let $unit-test-url := xs:anyURI($unit-tests-collection || normalize-space($unit-test/kert:test-url))
                    let $unit-test-status := httpclient:get($unit-test-url, false(), ())//*[local-name() = 'result-token']
                    
                    return
                        <div id="{$unit-test-id}" class="test-summary">
                            <div class="logo">
                                <img src="../icon.png" width="24" height="24" alt="*"/>
                            </div>
                            <div class="test-summary-content">
                                <div class="test-summary-title">
                                    {$unit-test/kert:title}
                                    (status:
                                    <span class="test-status-{$unit-test-status}" />
                                    )
                                    <span class="test-summary-operations">
                                        <a href="{$unit-test-url}">Run test</a>
                                    </span>
                                </div>
                                <div class="test-summary-description">{$unit-test/kert:description}</div>
                            </div>
                        </div>
                }
            </body>
        </html>