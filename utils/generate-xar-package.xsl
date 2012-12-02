<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:spec="http://expath.org/ns/xmlspec" version="2.0">

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
			<package xmlns="http://expath.org/ns/pkg" name="http://expath.org/lib/{$module-prefix}" abbrev="{concat('expath-', $module-prefix)}"
				version="{$package-version}" spec="1.0">
				<title>
					<xsl:value-of select="$spec-title" />
				</title>
				<dependency processor="http://exist-db.org/" />
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
			</meta>
		</xsl:result-document>

		<xsl:result-document href="{concat($target-dir, '/exist.xml')}">
			<package xmlns="http://exist-db.org/ns/expath-pkg">
				<jar>
					<xsl:value-of select="$jar-name" />
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

		<xsl:result-document href="{concat($target-dir, '/expath-', $module-prefix, '-xqdoc.xml')}">
			<xqdoc xmlns="http://www.xqdoc.org/1.0">
				<control>
					<date>2012-11-26T11:59:12.124+02:00</date>
					<version>
						<xsl:value-of select="$package-version" />
					</version>
				</control>
				<module type="library">
					<uri>
						<xsl:value-of select="$module-namespace" />
					</uri>
					<name>
						<xsl:value-of select="$spec-title" />
					</name>
					<comment>
						<description>
							<xsl:value-of select="concat('A ', //element()[@id = 'module-description'])" />
						</description>
						<since>eXist-1.5</since>
					</comment>
				</module>
				<functions>
					<xsl:for-each select="//spec:function">
						<function>
							<comment>
								<description><xsl:value-of select="parent::*/preceding-sibling::*[local-name() = 'p']" /></description>
								<param>$data The data to GZip</param>
							</comment>
							<name><xsl:value-of select="substring-after(., ':')" /></name>
							<signature><xsl:value-of select="parent::*" /></signature>
						</function>
					</xsl:for-each>
				</functions>
			</xqdoc>
		</xsl:result-document>

	</xsl:template>
</xsl:stylesheet>