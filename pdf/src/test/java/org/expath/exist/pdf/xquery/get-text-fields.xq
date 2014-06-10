xquery version "3.0";

import module "http://expath.org/ns/pdf";

let $expected-result := <expected-result>492</expected-result>

let $actual-result :=
    <actual-result>
        {
        count(map:keys(pdf:get-text-fields(util:binary-doc("SF.pdf"))))
        }
    </actual-result>
        
let $condition := normalize-space($expected-result/text()) = normalize-space($actual-result/text())
    

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
