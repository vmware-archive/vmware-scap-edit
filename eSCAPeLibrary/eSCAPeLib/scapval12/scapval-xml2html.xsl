<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:svr="http://scap.nist.gov/schema/validation/results/0.1" xmlns:xcf="nist:scap:xslt:function">
  <xsl:template match="/svr:scap-validation-results">
    <xsl:variable name="hash" select="//svr:message[@type = 'HASH']"/>
    <xsl:variable name="submittedResource" select="//svr:message[@type = 'SUBMITTED_RESOURCE']"/>
    <html>
      <head>
        <title>SCAP Content Validation Results</title>
        <style type="text/css">
                	<xsl:text disable-output-escaping="yes">
				<!-- General Settings -->
				<!-- set-up the fonts -->
				body,div,p,blockquote,ol,ul,dl,li,dt,dd,td,th,pre {
				font-family: Verdana, Geneva, Arial, helvetica, sans-serif;
				}

				body {
				background-color: #ffffff;
				color: #000154;
				font-size: 0.85em;
				margin: 2em;
				width: 75%;
				}


				h3,h4,h5,h6 {
				font-size:1em;
				}

				<!-- reset padding and margin for all block-level elements -->
				html,form,div,ol,ul,dl,li,dt,dd,td,th,h1,h2,h3,h4,h5,h6,pre,p,blockquote,fieldset,img {
				margin:0;
				padding:0;
				}

				h1 {
				margin: 1em -0.5em 0.5em;
				font-size:1.5em;
				border-bottom: solid 3px #cccccc;
				}

				h2 {
				margin: 1em -0.5em 0.5em;
				padding: 2px 3px 2px;
				font-size:1.25em;
				border-bottom: solid 3px #cccccc;
				background-color: #eeeeee;
				}

				h3 {
				margin: 1em -0.5em 0.5em 0.5em;
				padding: 2px 3px 2px;
				font-size:1em;
				border-bottom: solid 3px #cccccc;
				background-color: #eeeeee;
				}

				pre {
				background-color: #dddddd;
				border: solid thin black;
				margin: 0.5em 5em;
				padding: 1em;
				overflow: auto;
				width: 100%;
				font-size: 1.25em;
				}

				code, samp {
				font-family: courier;
				}

				<!-- reset the border -->
				a img,:link img,:visited img, img, fieldset {
				border:none;
				}

				<!-- de-italicize address -->
				address {
				font-style:normal;
				}

				<!-- give paragraphs a margin -->
				p {
				margin: 0 0 0.3em 0;
				}

				<!-- provide feedback on abbreviations and acronyms -->
				abbr, acronym, .help {
				border-bottom: 1px dotted #333;
				cursor: help;
				}

				.command {
				font-size: 1em;
				font-weight: bold;
				}

				.flag {
				font-weight: bold;
				}

				.arg {
				font-weight: bold;
				font-style: italic;
				}

				div.section {
				margin: 2em 1.25em 0em;
				}

				div.topOfPage {
				font-size: 0.7em;
				margin: 1em -1.5em 0em;
				}

				#parameters dd {
				margin: 0.25em 1em;
				}

				#parameters dt {
				margin-top: 1em;
				}

				#parameters dd ul {
				list-style: none;
				}

				#parameters dd li {
				margin: 0.25em 1em;
				}

				#remarks ul {
				list-style: none;
				}

				#remarks li {
				margin: 1em 1em;
				}

				div.example {
				margin-left: 1em;
				}

				table {
					border-width: 2px;
					border-style: solid;
					border-color: #cccccc;

				}
				td, th { 
					border-width: 2px;
					border-style: solid;
					border-color: #cccccc;
					padding:10px;
				}
                	</xsl:text>
                </style>
      </head>
      <xsl:variable name="messages" select="svr:messages/svr:message"/>
      <body>
        <h1>SCAP Content Validation Results</h1>
        <h3>
          <xsl:value-of select="string('Validation Result: ')"/>
          <xsl:choose>
            <xsl:when test="exists($messages[@level eq 'ERROR'])">
              <font color="#7E2217" size="+1">
                <xsl:value-of select="string('FAIL')"/>
              </font>
            </xsl:when>
            <xsl:otherwise>
              <font color="#347235" size="+1">
                <xsl:value-of select="string('PASS')"/>
              </font>
            </xsl:otherwise>
          </xsl:choose>
          <br/>
          <xsl:for-each select="$submittedResource">
            <xsl:value-of select="string('Submitted Resource: ')"/>
            <xsl:value-of select="@resource-id"/>
            <xsl:value-of select="string(' (')"/>
            <xsl:value-of select="$hash[@resource-id = current()/@resource-id]/text()"/>
            <xsl:value-of select="string(')')"/>
          <br/>
          </xsl:for-each>
          <xsl:value-of select="string('Validation Time: ')"/>
          <xsl:value-of select="@validation-datetime"/>
          <br/>
          <xsl:value-of select="string('Tool Version: ')"/>
          <xsl:value-of select="@implementation-version"/>
          <br/>
        </h3>
        <xsl:variable name="messagesToIterate" select="$messages[exists(@resource-id) and exists(@requirement-id) and matches(@level,'^(WARN|ERROR|INFO)$')]"/>
        <xsl:variable name="sch_debug" select="$messages[@type = 'SCHEMATRON_DETAIL']"/>
        
        <xsl:for-each select="$messagesToIterate">
          <xsl:sort select="@resource-id"/>
          <xsl:if test="generate-id(.) = generate-id($messagesToIterate[@resource-id = current()/@resource-id][1])">
            <h2>
              <xsl:value-of select="@resource-id"/>
            </h2>
            <table>
              <tr>
                <th>Requirement</th>
                <th>Count</th>
                <th>Level</th>
                <th>Type</th>
                <th>Description</th>
                <th>Location</th>
                <th>Test</th>
              </tr>
              <xsl:variable name="resMessages" select="$messagesToIterate[@resource-id = current()/@resource-id]"/>
              
              <xsl:for-each select="$resMessages">
                <xsl:sort select="@type"/>
                <xsl:if test="generate-id(.) = generate-id($resMessages[@type = current()/@type][1])">
                  <xsl:variable name="typeMessages" select="$resMessages[@type = current()/@type]"/>
                  <xsl:for-each select="$typeMessages">
                    <xsl:sort select="xcf:get-requirement-number-numeric(replace(@requirement-id,'-','.'))" data-type="number"/>
                    <xsl:choose>
                      <xsl:when test="@type = 'SCHEMATRON'">
                        <xsl:if test="generate-id(.) = generate-id($typeMessages[. = current()][1])">
                          <tr>
                            <th>
                              <xsl:value-of select="@requirement-id"/>
                            </th>
                            <td align="center">
                              <xsl:value-of select="count($typeMessages[. = current()])"/>
                              <br/>
                              <xsl:value-of select="string('(of')"/>
                              <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
                              <xsl:value-of select="count($typeMessages[@requirement-id = current()/@requirement-id])"/>
                              <xsl:value-of select="string(')')"/>
                            </td>
                            <td>
                              <xsl:value-of select="@level"/>
                            </td>
                            <td>
                              <xsl:value-of select="@type"/>
                            </td>
                            <td>
                              <xsl:value-of select="."/>
                            </td>
                            <td>
                              <xsl:if test="count($sch_debug[@resource-id eq current()/@resource-id and @requirement-id eq current()/@requirement-id]) gt 0">
                                <xsl:for-each select="$sch_debug[@resource-id eq current()/@resource-id and @requirement-id eq current()/@requirement-id]">
                                  <xsl:if test="generate-id(.) != generate-id($sch_debug[@resource-id eq current()/@resource-id and @requirement-id eq current()/@requirement-id][1])">
                                    <br/>
                                    <br/>
                                  </xsl:if>
                                  <p><xsl:value-of select="current()/text()"/></p>
                                </xsl:for-each>
                                <br/>
                                <br/>
                                <xsl:value-of select="string('Schematron Context:')"/>
                                <br/>
                              </xsl:if>
                              <xsl:for-each select="$typeMessages[. = current()]">
                                <xsl:if test="generate-id(.) != generate-id($typeMessages[. = current()][1])">
                                  <br/>
                                  <br/>
                                </xsl:if>
                                <xsl:value-of select="@location"/>
                              </xsl:for-each>
                              
                            </td>
                            <td>
                              <xsl:value-of select="@test"/>
                            </td>
                          </tr>
                        </xsl:if>
                      </xsl:when>
                      <xsl:otherwise>
                        <tr>
                          <th>
                            <xsl:value-of select="@requirement-id"/>
                          </th>
                          <td align="center">
                            <xsl:value-of select="string('1')"/>
                            <br/>
                            <xsl:value-of select="string('(of')"/>
                            <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
                            <xsl:value-of select="count($typeMessages[@requirement-id = current()/@requirement-id])"/>
                            <xsl:value-of select="string(')')"/>
                          </td>
                          <td>
                            <xsl:value-of select="@level"/>
                          </td>
                          <td>
                            <xsl:value-of select="@type"/>
                          </td>
                          <td>
                            <xsl:value-of select="."/>
                          </td>
                          <td>
                            <xsl:choose>
                              <xsl:when test="exists(@location)">
                                <xsl:value-of select="@location"/>
                              </xsl:when>
                              <xsl:otherwise>
                                <br/>
                              </xsl:otherwise>
                            </xsl:choose>
                          </td>
                          <td>
                            <xsl:choose>
                              <xsl:when test="exists(@test)">
                                <xsl:value-of select="@test"/>
                              </xsl:when>
                              <xsl:otherwise>
                                <br/>
                              </xsl:otherwise>
                            </xsl:choose>
                          </td>
                        </tr>
                      </xsl:otherwise>
                    </xsl:choose>
                  </xsl:for-each>
                </xsl:if>
              </xsl:for-each>
            </table>
          </xsl:if>
        </xsl:for-each>
        
      </body>
    </html>
  </xsl:template>
  <xsl:function name="xcf:get-requirement-number-numeric">
    <xsl:param name="reqNum"/>
    <xsl:choose>
      <xsl:when test="matches($reqNum,'^\d+(\.\d+)?$')">
        <xsl:value-of select="$reqNum"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="xcf:get-requirement-number-numeric(substring($reqNum,2))"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:function>
</xsl:stylesheet>
