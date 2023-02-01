<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
                version="1.0">
    <xsl:output encoding="UTF-8" indent="yes" method="xml" standalone="no" omit-xml-declaration="no"/>
    <xsl:template match="OrderData">
        <fo:root language="EN">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="A4-invoice" page-height="297mm" page-width="210mm" margin-top="5mm"
                                       margin-bottom="5mm" margin-left="5mm" margin-right="5mm">
                    <fo:region-body margin-top="10mm" margin-bottom="10mm"/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <fo:page-sequence master-reference="A4-invoice">
                <fo:flow flow-name="xsl-region-body" border-collapse="collapse" reference-orientation="0">
                    <fo:block>Order No. : #
                        <xsl:value-of select="id"/>
                    </fo:block>
                    <fo:block>Date :
                        <xsl:value-of select="date"/>
                    </fo:block>
                    <fo:block>Time :
                        <xsl:value-of select="time"/>
                    </fo:block>


                    <fo:table table-layout="fixed" width="100%" font-size="10pt" border-color="black"
                              border-width="0.35mm" border-style="solid" text-align="center" display-align="center"
                              space-after="5mm" space-before="15mm">
                        <fo:table-column column-width="proportional-column-width(8)"/>
                        <fo:table-column column-width="proportional-column-width(7)"/>
                        <fo:table-column column-width="proportional-column-width(40)"/>
                        <fo:table-column column-width="proportional-column-width(15)"/>
                        <fo:table-column column-width="proportional-column-width(15)"/>
                        <fo:table-column column-width="proportional-column-width(15)"/>

                        <fo:table-header font-weight="bold">
                            <fo:table-row border-bottom="solid 1px black" height="10mm">
                                <fo:table-cell>
                                    <fo:block>#</fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block>Barcode</fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block>Name</fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block>Quantity</fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block>Selling Price</fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block>Total</fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-header>

                        <fo:table-body>
                            <xsl:for-each select="order-items/item">
                                <fo:table-row border-bottom="solid 1px black" height="8mm">
                                    <fo:table-cell>
                                        <fo:block>
                                            <xsl:value-of select="sn"/>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell>
                                        <fo:block>
                                            <xsl:value-of select="barcode"/>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell>
                                        <fo:block>
                                            <xsl:value-of select="name"/>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell>
                                        <fo:block>
                                            <xsl:value-of select="quantity"/>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell>
                                        <fo:block>
                                            <xsl:value-of select="sellingPrice"/>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell>
                                        <fo:block>
                                            <xsl:value-of select="total"/>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </xsl:for-each>

                            <fo:table-row border-bottom="solid 1px black" height="8mm" font-weight="bold">
                                <fo:table-cell>
                                    <fo:block>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block>
                                        Total
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block>
                                        <xsl:value-of select="items-count"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block>
                                        Rs:
                                        <xsl:value-of select="total-bill"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>


                        </fo:table-body>

                    </fo:table>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>