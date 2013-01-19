<?xml version="1.0" encoding="UTF-8"?>
<!-- kert, web test runner By Claudius Teodorescu Licensed under LGPL. -->
<xsl:stylesheet xmlns="http://www.w3.org/1999/xhtml" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:kert="http://kuberam.ro/ns/kert" version="2.0">
    <xsl:output method="xml"/>
    <xsl:param name="request-url"/>
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="//*[@class = 'test-status-']">
        <xsl:variable name="unitTestStatus" select="doc(resolve-uri(parent::*//*[local-name() = 'a']/@href, $request-url))//*[local-name() = 'result-token']/text()"/>
        <xsl:element name="span">
            <xsl:attribute name="class">
                <xsl:value-of select="concat('test-status-', $unitTestStatus)"/>
            </xsl:attribute>
        </xsl:element>
    </xsl:template>
</xsl:stylesheet>