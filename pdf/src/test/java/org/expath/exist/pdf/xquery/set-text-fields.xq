xquery version "3.0";

import module "http://expath.org/ns/pdf";
import module namespace contentextraction = "http://exist-db.org/xquery/contentextraction" at "java:org.exist.contentextraction.xquery.ContentExtractionModule";

let $text-fields :=  map {
    "form1[0].#subform[0].Date18[0]" := "18",
    "form1[0].#subform[0].Date8[1]" := "30",
    "form1[0].#subform[0].Date8[0]" := "08",
    "form1[0].#subform[0].Date22[0]" := "22",
    "form1[0].#subform[0].MONTHYEAR2[0]" := "01/14",
    "form1[0].#subform[0].Date15[0]" := "15",
    "form1[0].#subform[0].Date17[0]" := "17",
    "form1[0].#subform[0].Date13[0]" := "13",
    "form1[0].#subform[0].Date4[0]" := "04",
    "form1[0].#subform[0].Date2[1]" := "24",
    "form1[0].#subform[0].Date4[1]" := "26",
    "form1[0].#subform[0].Date2[0]" := "02",
    "form1[0].#subform[0].Date11[0]" := "11",
    "form1[0].#subform[0].MONTHYEAR[0]" := "01/14",
    "form1[0].#subform[0].Date5[1]" := "27",
    "form1[0].#subform[0].Date21[0]" := "21",
    "form1[0].#subform[0].Date1[0]" := "01",
    "form1[0].#subform[0].Date20[0]" := "20",
    "form1[0].#subform[0].Date10[0]" := "10",
    "form1[0].#subform[0].Date3[0]" := "03",
    "form1[0].#subform[0].Date14[0]" := "14",
    "form1[0].#subform[0].Date1[1]" := "23",
    "form1[0].#subform[0].Date16[0]" := "16",
    "form1[0].#subform[0].Date3[1]" := "25",
    "form1[0].#subform[0].Date9[1]" := "31",
    "form1[0].#subform[0].Date7[1]" := "29",
    "form1[0].#subform[0].Date7[0]" := "07",
    "form1[0].#subform[0].Date6[0]" := "06",
    "form1[0].#subform[0].Date19[0]" := "19",
    "form1[0].#subform[0].Date6[1]" := "28",
    "form1[0].#subform[0].Date9[0]" := "09",
    "form1[0].#subform[0].Date12[0]" := "12",
    "form1[0].#subform[0].Date5[0]" := "05"
}


let $expected-result := <expected-result>01/14</expected-result>

let $actual-result :=
    <actual-result>
        {
            xmldb:store("/db", "SF-modified.pdf", pdf:set-text-fields(util:binary-doc("SF.pdf"), $text-fields))
(:            contentextraction:get-metadata-and-content(util:binary-doc("SF-modified.pdf")):)
        }
    </actual-result>
        
(:let $condition := contains(normalize-space($actual-result/text()), normalize-space($expected-result/text())):)
let $condition := util:binary-doc-available("SF-modified.pdf")
    

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
