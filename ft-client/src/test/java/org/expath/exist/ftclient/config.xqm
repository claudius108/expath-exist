xquery version "3.0";

module namespace config = "http://kuberam.ro/ns/config";

declare variable $config:server-home-folder := "/home/claudius/ftp";

declare variable $config:ftp-server-connection-url := xs:anyURI('ftp://ftp-user:ftp-pass@127.0.0.1');

declare variable $config:sftp-server-connection-url := xs:anyURI('sftp://ftp-user:ftp-pass@127.0.0.1');

declare variable $config:private-key := util:binary-to-string(util:binary-doc(concat('xmldb:', resolve-uri('resources/sftp-private.key', concat(substring-after(system:get-module-load-path(), 'xmldb:'), '/')))));

declare variable $config:tests-collection := replace(replace(request:get-effective-uri(), "tests/(\w)+.xql$", ""), "/rest//db", "");

declare variable $config:resources-collection := concat(replace(replace(request:get-effective-uri(), "tests/(\w)+.xql$", ""), "/rest//db", ""), 'resources/');
