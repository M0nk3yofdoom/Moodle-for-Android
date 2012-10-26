<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:json="http://json.org/">
	
	<xsl:output indent="no" omit-xml-declaration="yes" method="text" encoding="utf-8"/>
	<xsl:strip-space elements="*"/>

	<xsl:template match="node()">
		<xsl:copy>
			<xsl:text> { "courses": [</xsl:text>
			<xsl:for-each select="MULTIPLE/SINGLE">
        <xsl:text>{ </xsl:text>
        <xsl:for-each select="KEY">
				<xsl:if test="@name != ''">
					<xsl:text>"</xsl:text>
					<xsl:value-of select="@name"/>
					<xsl:text>": </xsl:text>
				</xsl:if>				
				<xsl:if test="VALUE/node() != ''">
					<xsl:text>"</xsl:text>
					<xsl:value-of select="VALUE/node()"/>
					<xsl:choose>
						<xsl:when test="position() = last()">
							<xsl:text>" </xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>", </xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:if>
        <xsl:if test="not(VALUE/node()) or VALUE/node() = ''">
          <xsl:choose>
            <xsl:when test="position() = last()">
              <xsl:text>" " </xsl:text>
            </xsl:when>
            <xsl:otherwise>
              <xsl:text>" ", </xsl:text>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:if>
        </xsl:for-each>
          <xsl:choose>
            <xsl:when test="position() = last()">
              <xsl:text>} </xsl:text>
            </xsl:when>
            <xsl:otherwise>
              <xsl:text>}, </xsl:text>
            </xsl:otherwise>
          </xsl:choose>
			</xsl:for-each>
      <xsl:text>]} </xsl:text>
		</xsl:copy>
	</xsl:template>	
</xsl:stylesheet>
