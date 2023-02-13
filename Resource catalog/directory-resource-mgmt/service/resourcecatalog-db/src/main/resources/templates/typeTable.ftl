<w:tbl>
    <w:tblPr>
        <w:tblW w:w="5000" w:type="pct"/>
        <w:jc w:val="center"/>
        <w:tblBorders>
            <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
            <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
            <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
            <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
            <w:insideH w:val="single" w:sz="4" w:space="0" w:color="auto"/>
            <w:insideV w:val="single" w:sz="4" w:space="0" w:color="auto"/>
        </w:tblBorders>
        <w:tblLook w:val="01E0" w:firstRow="1" w:lastRow="1" w:firstColumn="1" w:lastColumn="1" w:noHBand="0" w:noVBand="0"/>
    </w:tblPr>
    <w:tblGrid>
        <w:gridCol w:w="587"/>
        <w:gridCol w:w="2107"/>
        <w:gridCol w:w="1699"/>
        <w:gridCol w:w="3909"/>
    </w:tblGrid>
    <w:tr w:rsidR="00721184" w:rsidRPr="00734D50" w:rsidTr="00730AB7">
        <w:trPr>
            <w:trHeight w:val="684"/>
            <w:tblHeader/>
            <w:jc w:val="center"/>
        </w:trPr>
        <w:tc>
            <w:tcPr>
                <w:tcW w:w="354" w:type="pct"/>
                <w:shd w:val="clear" w:color="auto" w:fill="D0CECE"/>
                <w:vAlign w:val="center"/>
            </w:tcPr>
            <w:p w:rsidR="00721184" w:rsidRPr="00734D50" w:rsidRDefault="00721184" w:rsidP="00730AB7">
                <w:pPr>
                    <w:pStyle w:val="affff0"/>
                    <w:rPr>
                        <w:szCs w:val="21"/>
                    </w:rPr>
                </w:pPr>
                <w:r w:rsidRPr="00734D50">
                    <w:rPr>
                        <w:rFonts w:hint="eastAsia"/>
                        <w:szCs w:val="21"/>
                    </w:rPr>
                    <w:t>序号</w:t>
                </w:r>
            </w:p>
        </w:tc>
        <w:tc>
            <w:tcPr>
                <w:tcW w:w="1269" w:type="pct"/>
                <w:shd w:val="clear" w:color="auto" w:fill="D0CECE"/>
                <w:vAlign w:val="center"/>
            </w:tcPr>
            <w:p w:rsidR="00721184" w:rsidRPr="00734D50" w:rsidRDefault="00721184" w:rsidP="00730AB7">
                <w:pPr>
                    <w:pStyle w:val="affff0"/>
                    <w:rPr>
                        <w:szCs w:val="21"/>
                    </w:rPr>
                </w:pPr>
                <w:r>
                    <w:rPr>
                        <w:rFonts w:hint="eastAsia"/>
                        <w:szCs w:val="21"/>
                    </w:rPr>
                    <w:t>资源分类代码</w:t>
                </w:r>
            </w:p>
        </w:tc>
        <w:tc>
            <w:tcPr>
                <w:tcW w:w="1023" w:type="pct"/>
                <w:shd w:val="clear" w:color="auto" w:fill="D0CECE"/>
                <w:vAlign w:val="center"/>
            </w:tcPr>
            <w:p w:rsidR="00721184" w:rsidRPr="00734D50" w:rsidRDefault="00721184" w:rsidP="00730AB7">
                <w:pPr>
                    <w:pStyle w:val="affff0"/>
                    <w:rPr>
                        <w:szCs w:val="21"/>
                    </w:rPr>
                </w:pPr>
                <w:r w:rsidRPr="00734D50">
                    <w:rPr>
                        <w:szCs w:val="21"/>
                    </w:rPr>
                    <w:t>资源</w:t>
                </w:r>
                <w:r>
                    <w:rPr>
                        <w:rFonts w:hint="eastAsia"/>
                        <w:szCs w:val="21"/>
                    </w:rPr>
                    <w:t>分类</w:t>
                </w:r>
                <w:r w:rsidRPr="00734D50">
                    <w:rPr>
                        <w:rFonts w:hint="eastAsia"/>
                        <w:szCs w:val="21"/>
                    </w:rPr>
                    <w:t>名称</w:t>
                </w:r>
            </w:p>
        </w:tc>
        <w:tc>
            <w:tcPr>
                <w:tcW w:w="2354" w:type="pct"/>
                <w:shd w:val="clear" w:color="auto" w:fill="D0CECE"/>
                <w:vAlign w:val="center"/>
            </w:tcPr>
            <w:p w:rsidR="00721184" w:rsidRPr="00734D50" w:rsidRDefault="00721184" w:rsidP="00730AB7">
                <w:pPr>
                    <w:pStyle w:val="affff0"/>
                    <w:rPr>
                        <w:szCs w:val="21"/>
                    </w:rPr>
                </w:pPr>
                <w:r>
                    <w:rPr>
                        <w:rFonts w:hint="eastAsia"/>
                        <w:szCs w:val="21"/>
                    </w:rPr>
                    <w:t>信息资源名称</w:t>
                </w:r>
            </w:p>
        </w:tc>
    </w:tr>
    <#if typeList??>
	<#list typeList as types >
	    <w:tr w:rsidR="00D50112">
	        <w:trPr>
	            <w:trHeight w:val="420"/>
	            <w:jc w:val="center"/>
	        </w:trPr>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="0" w:type="auto"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00D50112" w:rsidRDefault="00730AB7">
	                <w:pPr>
	                    <w:pStyle w:val="affff1"/>
	                </w:pPr>
	                <w:r>
	                    <w:t>${types.number!''}</w:t>
	                </w:r>
	            </w:p>
	        </w:tc>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="0" w:type="auto"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00D50112" w:rsidRDefault="00730AB7">
	                <w:pPr>
	                    <w:pStyle w:val="affff1"/>
	                </w:pPr>
	                <w:r>
	                    <w:t>${types.typcd!''}</w:t>
	                </w:r>
	            </w:p>
	        </w:tc>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="0" w:type="auto"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00D50112" w:rsidRDefault="00730AB7">
	                <w:pPr>
	                    <w:pStyle w:val="affff1"/>
	                </w:pPr>
	                <w:r>
	                    <w:t>${types.typnm!''}</w:t>
	                </w:r>
	            </w:p>
	        </w:tc>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="0" w:type="auto"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00D50112" w:rsidRDefault="00730AB7">
	                <w:pPr>
	                    <w:pStyle w:val="affff1"/>
	                </w:pPr>
	                <w:r>
	                    <w:t>${types.uviewnm!''}</w:t>
	                </w:r>
	            </w:p>
	        </w:tc>
	    </w:tr>
	</#list>
	</#if>
</w:tbl>