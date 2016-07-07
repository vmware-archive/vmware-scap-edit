<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" version="1.0" xmlns:req="http://scap.nist.gov/schema/validation/requirements/0.2">
    <xsl:param name="additional_reqs" />
    <xsl:template match="/">
        <xsl:variable name="addtDoc" select="document($additional_reqs)" />
        <html>
            <head>
                <title>SCAP Version 1.1 Requirements Matrix</title>
            </head>
            <body>
                <p>This table provides a matrix of SCAP content requirements is provided implemented in the SCAP Content Validation tool v1.1. The matrix indicates which requirements are checked by SCAP Content Validation tool. The section numbers in the matrix refer to SP 800-126 which is available 
                <a xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xls="http://schemas.openxmlformats.org/spreadsheetml/2006/main" xmlns:mse="ms:office:2007:excel" href="http://csrc.nist.gov/publications/PubsSPs.html#SP-800-126">here</a>
                .</p>
                <table border='1' cellpadding="5">
                    <xsl:variable name="cat" select="req:scap-validation-requirements/req:requirement" />
                    <xsl:for-each select="$cat">
                        <xsl:if test="generate-id(.) eq generate-id($cat[@usecase eq current()/@usecase][1])">
                            <tr>
                                <th colspan="7">
                                    <h2>
                                        <xsl:value-of select="@usecase" />
                                    </h2>
                                </th>
                            </tr>
                            <tr>
                                <th>Requirement ID</th>
                                <th>800-126 Section</th>
                                <th>800-126 Statement</th>
                                <th>Note</th>
                                <th>800-126 Derived Requirement</th>
                                <th>Requirement Type</th>
                                <th>Error Level</th>
                                <th>Requirement Category</th>
                            </tr>
                            <xsl:for-each select="$cat[@usecase eq current()/@usecase]">
                                <xsl:sort select="number(@id)" />
                                <tr>
                                <xsl:variable name="dreqcount" select="count(req:derived-requirements/req:derived-requirement)"/>
                                    <xsl:element name="td">
                                        <xsl:attribute name="rowspan" select="$dreqcount" />
                                        <xsl:value-of select="@id" />
                                    </xsl:element>
                                    <xsl:element name="td">
                                        <xsl:attribute name="rowspan" select="$dreqcount" />
                                        <xsl:value-of select="req:statement" />
                                    </xsl:element>
                                    <xsl:element name="td">
                                        <xsl:attribute name="rowspan" select="$dreqcount" />
                                        <xsl:value-of select="@section" />
                                    </xsl:element>
                                    <xsl:element name="td">
                                        <xsl:attribute name="rowspan" select="$dreqcount" />
                                        <xsl:value-of select="@note" />
                                    </xsl:element>
                                    <xsl:variable name="req" select="req:derived-requirements/req:derived-requirement" />
                                    <xsl:for-each select="$req">
                                        <xsl:choose>
                                            <xsl:when test="generate-id(.) eq generate-id($req[1])">
                                                <td>
                                                    <xsl:value-of select="req:statement" />
                                                </td>
                                                <td>
                                                    <xsl:value-of select="@check_type" />
                                                </td>
                                                <td>
                                                    <xsl:value-of select="@error_level" />
                                                </td>
                                                <td>
                                                    <xsl:value-of select="@category" />
                                                </td>
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <tr>
                                                    <td>
                                                        <xsl:value-of select="req:statement" />
                                                    </td>
                                                    <td>
                                                        <xsl:value-of select="@check_type" />
                                                    </td>
                                                    <td>
                                                        <xsl:value-of select="@error_level" />
                                                    </td>
                                                    <td>
                                                        <xsl:value-of select="@category" />
                                                    </td>
                                                </tr>
                                            </xsl:otherwise>
                                        </xsl:choose>
                                    </xsl:for-each>
                                </tr>
                            </xsl:for-each>
                            <xsl:if test="@usecase eq 'GENERAL'">
                                <xsl:for-each select="$addtDoc/req:scap-validation-requirements/req:requirement">
                                    <tr>
                                        <td>
                                            <xsl:value-of select="@id" />
                                        </td>
                                        <td>
                                            <xsl:value-of select="req:statement" />
                                        </td>
                                        <td>
                                            <xsl:value-of select="@section" />
                                        </td>
                                        <td>
                                            <xsl:value-of select="@note" />
                                        </td>
                                        <td>
                                            <xsl:value-of select="req:derived-requirements/req:derived-requirement/req:statement" />
                                        </td>
                                        <td>
                                            <xsl:value-of select="req:derived-requirements/req:derived-requirement/@check_type" />
                                        </td>
                                        <td>
                                            <xsl:value-of select="req:derived-requirements/req:derived-requirement/@error_level" />
                                        </td>
                                        <td>
                                            <xsl:value-of select="req:derived-requirements/req:derived-requirement/@category" />
                                        </td>
                                    </tr>
                                </xsl:for-each>
                            </xsl:if>
                        </xsl:if>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>
