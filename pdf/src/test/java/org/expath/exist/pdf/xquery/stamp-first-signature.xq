xquery version "3.0";

import module "http://expath.org/ns/pdf";

xmldb:store("/apps", "stamped.pdf", pdf:stamp(util:binary-doc("/apps/SF.pdf"), "Stamped!", "left: 70pt; top: 70pt; font-family: Helvetica; font-size: 22pt; color: rgb(144,144,0);"))