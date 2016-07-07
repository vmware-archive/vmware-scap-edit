<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:svr="http://scap.nist.gov/schema/validation/results/0.1" xmlns:xcf="nist:scap:xslt:function">
    <xsl:template match="/svr:scap-validation-results">
        <xsl:variable name="hash" select="svr:resources/svr:resource" />
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
				width: 10em;
				}

				h2 {
				margin: 1em -0.5em 0.5em;
				padding: 2px 3px 2px;
				font-size:1.25em;
				border-bottom: solid 3px #cccccc;
				background-color: #eeeeee;
				width: 60em;
				}

				h3 {
				margin: 1em -0.5em 0.5em 0.5em;
				padding: 2px 3px 2px;
				font-size:1em;
				border-bottom: solid 3px #cccccc;
				background-color: #eeeeee;
				width: 60em;
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
            <body>
                <h1>SCAP Content Validation Results</h1>
                <h3>
                    <xsl:value-of select="string('Submitted Resource: ')" />
                    <xsl:value-of select="@resource-id" />
                    <xsl:value-of select="string(' (')" />
                    <xsl:value-of select="$hash[@id = current()/@resource-id]/svr:hash/@algorithm" />
                    <xsl:value-of select="string(': ')" />
                    <xsl:value-of select="$hash[@id = current()/@resource-id]/svr:hash" />
                    <xsl:value-of select="string(')')" />
                    <br />
                    <xsl:value-of select="string('Use-case: ')" />
                    <xsl:value-of select="@use-case" />
                    <br />
                    <xsl:value-of select="string('Validation Time: ')" />
                    <xsl:value-of select="@validation-datetime" />
                    <br />
                    <xsl:value-of select="string('SCAP Version: ')" />
                    <xsl:value-of select="@scap-version" />
                    <br />
                    <xsl:value-of select="string('OVAL Version: ')" />
                    <xsl:value-of select="@oval-version" />
                    <br />
                    <xsl:value-of select="string('Tool Version: ')" />
                    <xsl:value-of select="@implementation-version" />
                    <br />
                </h3>
                <xsl:variable name="messages" select="svr:messages/svr:message" />
                <xsl:for-each select="$messages">
                    <xsl:sort select="@resource-id" />
                    <xsl:if test="generate-id(.) = generate-id($messages[@resource-id = current()/@resource-id][1])">
                        <h2>
                            <xsl:value-of select="@resource-id" />
                            <xsl:if test="exists($hash[@id = current()/@resource-id]/svr:hash)">
                                <br />
                                <xsl:value-of select="string('(')" />
                                <xsl:value-of select="$hash[@id = current()/@resource-id]/svr:hash/@algorithm" />
                                <xsl:value-of select="string(': ')" />
                                <xsl:value-of select="$hash[@id = current()/@resource-id]/svr:hash" />
                                <xsl:value-of select="string(')')" />
                            </xsl:if>
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
                            <xsl:variable name="resMessages" select="$messages[@resource-id = current()/@resource-id]" />
                            <xsl:for-each select="$resMessages">
                                <xsl:sort select="@type" />
                                <xsl:if test="generate-id(.) = generate-id($resMessages[@type = current()/@type][1])">
                                    <xsl:variable name="typeMessages" select="$resMessages[@type = current()/@type]" />
                                    <xsl:for-each select="$typeMessages">
                                        <xsl:sort select="xcf:get-requirement-number-numeric(@requirement-id)" data-type="number" />
                                        <xsl:choose>
                                            <xsl:when test="@type != 'SCHEMATRON'">
                                                <tr>
                                                    <th>
                                                        <xsl:value-of select="@requirement-id" />
                                                    </th>
						    <td align="center">
						        <xsl:value-of select="count($resMessages[@requirement-id = current()/@requirement-id])"/>
						    </td>
                                                    <td>
                                                        <xsl:value-of select="@level" />
                                                    </td>
                                                    <td>
                                                        <xsl:value-of select="@type" />
                                                    </td>
                                                    <td>
                                                        <xsl:value-of select="." />
                                                    </td>
                                                    <td>
                                                        <br />
                                                    </td>
                                                    <td>
                                                        <br />
                                                    </td>
                                                </tr>
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <xsl:if test="generate-id(.) = generate-id($typeMessages[@requirement-id = current()/@requirement-id][1])">
                                                    <tr>
                                                        <th>
                                                            <xsl:value-of select="@requirement-id" />
                                                        </th>
                                                        <td align="center">
 							    <xsl:value-of select="count($resMessages[@requirement-id = current()/@requirement-id])"/>
                                                        </td>
                                                        <td>
                                                            <xsl:value-of select="@level" />
                                                        </td>
                                                        <td>
                                                            <xsl:value-of select="@type" />
                                                        </td>
                                                        <td>
                                                            <xsl:value-of select="." />
                                                        </td>
                                                        <td>
                                                            <xsl:for-each select="$resMessages[@requirement-id = current()/@requirement-id]">
                                                                <xsl:if test="generate-id(.) != generate-id($resMessages[@requirement-id = current()/@requirement-id][1])">
                                                                    <br />
                                                                    <br />
                                                                </xsl:if>
                                                                <xsl:value-of select="@location" />
                                                            </xsl:for-each>
                                                        </td>
                                                        <td>
                                                            <xsl:value-of select="@test" />
                                                        </td>
                                                    </tr>
                                                </xsl:if>
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
        <xsl:param name="reqNum" />
        <xsl:choose>
            <xsl:when test="matches($reqNum,'^\d+$')">
                <xsl:value-of select="$reqNum" />
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="xcf:get-requirement-number-numeric(substring($reqNum,2))" />
            </xsl:otherwise>
        </xsl:choose>
    </xsl:function>
</xsl:stylesheet>
