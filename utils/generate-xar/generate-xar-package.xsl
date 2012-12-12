<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:spec="http://expath.org/ns/xmlspec"
	exclude-result-prefixes="spec" version="2.0">

	<xsl:output method="xml" />

	<xsl:param name="cxan.org-id" />
	<xsl:param name="module-prefix" />
	<xsl:param name="module-namespace" />
	<xsl:param name="package-version" />
	<xsl:param name="target-dir" />
	<xsl:param name="package-main-class" />
	<xsl:param name="jar-name" />

	<xsl:variable name="spec-title">
		<xsl:copy-of select="concat('EXPath ', //element()[local-name() = 'title'])" />
	</xsl:variable>
	<xsl:variable name="author">
		<xsl:copy-of select="//element()[local-name() = 'author'][1]/element()[1]" />
	</xsl:variable>

	<xsl:template match="/">

		<xsl:result-document href="{concat($target-dir, '/expath-pkg.xml')}">
			<package xmlns="http://expath.org/ns/pkg" name="http://expath.org/lib/{$module-prefix}" abbrev="{concat('expath-', $module-prefix)}"
				version="{$package-version}" spec="1.0">
				<title>
					<xsl:value-of select="$spec-title" />
				</title>
				<dependency processor="http://exist-db.org/" />
			</package>
		</xsl:result-document>

		<xsl:call-template name="generate-repo-descriptor">
			<xsl:with-param name="package-type" select="'library'" />
		</xsl:call-template>

		<xsl:call-template name="generate-repo-descriptor">
			<xsl:with-param name="package-type" select="'application'" />
		</xsl:call-template>

		<xsl:result-document href="{concat($target-dir, '/exist.xml')}">
			<package xmlns="http://exist-db.org/ns/expath-pkg">
				<jar>
					<xsl:value-of select="concat('expath-exist-', $module-prefix, '-lib/', $jar-name)" />
				</jar>
				<java>
					<namespace>
						<xsl:value-of select="$module-namespace" />
					</namespace>
					<class>
						<xsl:value-of select="$package-main-class" />
					</class>
				</java>
			</package>
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

	<xsl:template name="generate-repo-descriptor">
		<xsl:param name="package-type" />
		<xsl:result-document href="{concat($target-dir, '/../repo-', $package-type, '.xml')}">
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
				<type>
					<xsl:value-of select="$package-type" />
				</type>
				<xsl:choose>
					<xsl:when test="$package-type = 'application'">
						<target>
							<xsl:value-of select="concat('expath-', $module-prefix)" />
						</target>

						<prepare>pre-install.xql</prepare>
					</xsl:when>
				</xsl:choose>
			</meta>
		</xsl:result-document>
	</xsl:template>

</xsl:stylesheet>