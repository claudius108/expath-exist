<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:spec="http://expath.org/ns/xmlspec"
	version="2.0">

	<xsl:output method="xml" />

	<xsl:param name="cxan.org-id" />
	<xsl:param name="module-prefix" />
	<xsl:param name="module-namespace" />
	<xsl:param name="package-version" />
	<xsl:param name="target-dir" />
	<xsl:param name="package-main-class" />
	<xsl:param name="jar-name" />

	<xsl:template match="/">
		<xsl:variable name="spec-title">
			<xsl:copy-of select="concat('EXPath ', //element()[local-name() = 'title'])" />
		</xsl:variable>
		<xsl:variable name="author">
			<xsl:copy-of select="//element()[local-name() = 'author'][1]/element()[1]" />
		</xsl:variable>

		<xsl:result-document href="{concat($target-dir, '/expath-pkg.xml')}">
			<package xmlns="http://expath.org/ns/pkg">
				<module name="http://expath.org/lib/{$module-prefix}" version="{$package-version}">
					<title>
						<xsl:value-of select="$spec-title" />
					</title>
					<exist>
						<java>
							<namespace>
								<xsl:value-of select="$module-namespace" />
							</namespace>
							<class>
								<xsl:value-of select="$package-main-class" />
							</class>
						</java>
					</exist>
				</module>
			</package>

		</xsl:result-document>

		<xsl:result-document href="{concat($target-dir, '/repo.xml')}">
			<meta xmlns="http://exist-db.org/xquery/repo">
				<description>
					<xsl:value-of select="$spec-title" />
				</description>
				<author>
					<xsl:value-of select="$author" />
				</author>
				<website />
				<status>stable</status>
				<license>GNU-LGPL</license>
				<copyright>true</copyright>
				<type>library</type>
				<target>
					<xsl:value-of select="concat('/db/apps/expath/expath-', $module-prefix)" />
				</target>
				<prepare />
				<finish />
				<permissions user="admin" password="" group="dba" mode="0775" />
			</meta>
		</xsl:result-document>

		<xsl:result-document href="{concat($target-dir, '/pre-install.xql')}" method="text">
			xquery version "1.0";

			import module namespace xdb="http://exist-db.org/xquery/xmldb";

			(: The following external
			variables are set by the repo:deploy function :)

			(: file path pointing to the exist installation directory :)
			declare
			variable $home external;
			(: path to the directory
			containing the unpacked .xar package :)
			declare variable $dir
			external;
			(: the target collection into which the app is
			deployed :)
			declare variable $target external;

			declare function
			local:mkcol-recursive($collection, $components) {
			if (exists($components)) then
			let $newColl := concat($collection,
			"/", $components[1])
			return (
			xdb:create-collection($collection, $components[1]),
			local:mkcol-recursive($newColl,
			subsequence($components, 2))
			)
			else
			()
			};

			(: Helper function to recursively create a collection hierarchy. :)
			declare
			function local:mkcol($collection, $path) {
			local:mkcol-recursive($collection, tokenize($path, "/"))
			};

			(: store the
			collection configuration :)
			local:mkcol("/db/system/config", $target),
			xdb:store-files-from-pattern(concat("/system/config", $target), $dir, "*.xconf")
		</xsl:result-document>

		<xsl:result-document href="{concat($target-dir, '/cxan.xml')}">
			<package xmlns="http://cxan.org/ns/package" id="{concat('expath-', $module-prefix, '-exist')}" name="http://expath.org/lib/{$module-prefix}"
				version="{$package-version}">
				<author id="{$cxan.org-id}">
					<xsl:value-of select="$author" />
				</author>
				<category id="libs">Libraries</category>
				<category id="exist">eXist extensions</category>
				<tag>
					<xsl:value-of select="$module-prefix" />
				</tag>
				<tag>expath</tag>
				<tag>library</tag>
				<tag>exist</tag>
			</package>
		</xsl:result-document>

	</xsl:template>
</xsl:stylesheet>