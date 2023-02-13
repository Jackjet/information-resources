<#if order.detailsList??>
<#list order.detailsList as type >
<w:p w:rsidR="00A24A2F" w:rsidRPr="00A24A2F" w:rsidRDefault="00730AB7" w:rsidP="00A24A2F">
    <w:pPr>
        <w:pStyle w:val="TITLE11111"/>
        <w:numPr>
            <w:ilvl w:val="4"/>
            <w:numId w:val="14"/>
        </w:numPr>
        <w:rPr>
            <w:rFonts w:hint="eastAsia"/>
        </w:rPr>
    </w:pPr>
    <w:r>
        <w:rPr>
            <w:rFonts w:hint="eastAsia"/>
        </w:rPr>
        <w:t> ${type.typNm!''}</w:t>
    </w:r>
    <w:r w:rsidR="00A24A2F">
        <w:t xml:space="preserve"></w:t>
    </w:r>
</w:p>
	<#list type.infoList as info >
	<w:p w:rsidR="00730AB7" w:rsidRPr="00CD3867" w:rsidRDefault="00730AB7" w:rsidP="00730AB7">
	    <w:pPr>
	        <w:pStyle w:val="TITLE111111"/>
	        <w:numPr>
	            <w:ilvl w:val="5"/>
	            <w:numId w:val="14"/>
	        </w:numPr>
	        <w:rPr>
	            <w:szCs w:val="21"/>
	        </w:rPr>
	    </w:pPr>
	    <w:r>
	        <w:t> ${info.uviewNm!''}</w:t>
	    </w:r>
	</w:p>
	<w:tbl>
	    <w:tblPr>
	        <w:tblW w:w="5250" w:type="pct"/>
	        <w:jc w:val="center"/>
	        <w:tblBorders>
	            <w:top w:val="single" w:sz="8" w:space="0" w:color="auto"/>
	            <w:left w:val="single" w:sz="8" w:space="0" w:color="auto"/>
	            <w:bottom w:val="single" w:sz="8" w:space="0" w:color="auto"/>
	            <w:right w:val="single" w:sz="8" w:space="0" w:color="auto"/>
	            <w:insideH w:val="single" w:sz="8" w:space="0" w:color="auto"/>
	            <w:insideV w:val="single" w:sz="8" w:space="0" w:color="auto"/>
	        </w:tblBorders>
	        <w:tblLook w:val="0000" w:firstRow="0" w:lastRow="0" w:firstColumn="0" w:lastColumn="0" w:noHBand="0" w:noVBand="0"/>
	    </w:tblPr>
	    <w:tblGrid>
	        <w:gridCol w:w="1497"/>
	        <w:gridCol w:w="2654"/>
	        <w:gridCol w:w="1592"/>
	        <w:gridCol w:w="2964"/>
	    </w:tblGrid>
	    <w:tr w:rsidR="00730AB7" w:rsidRPr="00CD3867" w:rsidTr="00730AB7">
	        <w:trPr>
	            <w:trHeight w:val="359"/>
	            <w:jc w:val="center"/>
	        </w:trPr>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="860" w:type="pct"/>
	                <w:shd w:val="clear" w:color="auto" w:fill="D0CECE" w:themeFill="background2" w:themeFillShade="E6"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00730AB7" w:rsidRPr="00CD3867" w:rsidRDefault="00730AB7" w:rsidP="00730AB7">
	                <w:pPr>
	                    <w:widowControl/>
	                    <w:jc w:val="left"/>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                </w:pPr>
	                <w:r w:rsidRPr="00CD3867">
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体" w:hint="eastAsia"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                    <w:t>信息资源</w:t>
	                </w:r>
	                <w:r>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体" w:hint="eastAsia"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                    <w:t>分类</w:t>
	                </w:r>
	            </w:p>
	        </w:tc>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="4140" w:type="pct"/>
	                <w:gridSpan w:val="3"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00730AB7" w:rsidRPr="00CD3867" w:rsidRDefault="00730AB7" w:rsidP="00730AB7">
	                <w:pPr>
	                    <w:widowControl/>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
	                        <w:color w:val="FF0000"/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                    <w:r>
					        <w:t>${info.deptTypeName!''}</w:t>
					    </w:r>
	                </w:pPr>
	            </w:p>
	        </w:tc>
	    </w:tr>
	    <w:tr w:rsidR="00730AB7" w:rsidRPr="00CD3867" w:rsidTr="00730AB7">
	        <w:trPr>
	            <w:trHeight w:val="359"/>
	            <w:jc w:val="center"/>
	        </w:trPr>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="860" w:type="pct"/>
	                <w:shd w:val="clear" w:color="auto" w:fill="D0CECE" w:themeFill="background2" w:themeFillShade="E6"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00730AB7" w:rsidRPr="00CD3867" w:rsidRDefault="00730AB7" w:rsidP="00730AB7">
	                <w:pPr>
	                    <w:widowControl/>
	                    <w:jc w:val="left"/>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                </w:pPr>
	                <w:r w:rsidRPr="00CD3867">
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体" w:hint="eastAsia"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                    <w:t>信息资源</w:t>
	                </w:r>
	                <w:r w:rsidRPr="00CD3867">
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                    <w:t>名称</w:t>
	                </w:r>
	            </w:p>
	        </w:tc>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="4140" w:type="pct"/>
	                <w:gridSpan w:val="3"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00730AB7" w:rsidRPr="00CD3867" w:rsidRDefault="00730AB7" w:rsidP="00730AB7">
	                <w:pPr>
	                    <w:widowControl/>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
	                        <w:color w:val="FF0000"/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                    <w:r>
					        <w:t>${info.uviewNm!''}</w:t>
					    </w:r>
	                </w:pPr>
	            </w:p>
	        </w:tc>
	    </w:tr>
	    <w:tr w:rsidR="00730AB7" w:rsidRPr="00CD3867" w:rsidTr="00730AB7">
	        <w:trPr>
	            <w:trHeight w:val="359"/>
	            <w:jc w:val="center"/>
	        </w:trPr>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="860" w:type="pct"/>
	                <w:shd w:val="clear" w:color="auto" w:fill="D0CECE" w:themeFill="background2" w:themeFillShade="E6"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00730AB7" w:rsidRPr="00924A5B" w:rsidRDefault="00730AB7" w:rsidP="00730AB7">
	                <w:pPr>
	                    <w:widowControl/>
	                    <w:jc w:val="left"/>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                </w:pPr>
	                <w:r w:rsidRPr="00924A5B">
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体" w:hint="eastAsia"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                    <w:t>信息资源</w:t>
	                </w:r>
	                <w:r w:rsidRPr="00924A5B">
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                    <w:t>摘要</w:t>
	                </w:r>
	            </w:p>
	        </w:tc>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="4140" w:type="pct"/>
	                <w:gridSpan w:val="3"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00730AB7" w:rsidRPr="00CD3867" w:rsidRDefault="00730AB7" w:rsidP="00730AB7">
	                <w:pPr>
	                    <w:widowControl/>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
	                        <w:color w:val="FF0000"/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                    <w:r>
					        <w:t>${info.uviewDesc!''}</w:t>
					    </w:r>
	                </w:pPr>
	            </w:p>
	        </w:tc>
	    </w:tr>
	    <w:tr w:rsidR="00730AB7" w:rsidRPr="00CD3867" w:rsidTr="00730AB7">
	        <w:trPr>
	            <w:trHeight w:val="359"/>
	            <w:jc w:val="center"/>
	        </w:trPr>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="860" w:type="pct"/>
	                <w:shd w:val="clear" w:color="auto" w:fill="D0CECE" w:themeFill="background2" w:themeFillShade="E6"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00730AB7" w:rsidRPr="00CD3867" w:rsidRDefault="00730AB7" w:rsidP="00730AB7">
	                <w:pPr>
	                    <w:widowControl/>
	                    <w:jc w:val="left"/>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                </w:pPr>
	                <w:r w:rsidRPr="00CD3867">
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                    <w:t>信息</w:t>
	                </w:r>
	                <w:r w:rsidRPr="00CD3867">
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                    <w:t>资源</w:t>
	                </w:r>
	                <w:r w:rsidRPr="007A7AE6">
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                    <w:t>编码</w:t>
	                </w:r>
	            </w:p>
	        </w:tc>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="4140" w:type="pct"/>
	                <w:gridSpan w:val="3"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00730AB7" w:rsidRPr="0044597F" w:rsidRDefault="00730AB7" w:rsidP="00730AB7">
	                <w:pPr>
	                    <w:widowControl/>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
	                        <w:color w:val="FF0000"/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                    <w:r>
					        <w:t>${info.uviewNo!''}</w:t>
					    </w:r>
	                </w:pPr>
	            </w:p>
	        </w:tc>
	    </w:tr>
	    <w:tr w:rsidR="00730AB7" w:rsidRPr="00CD3867" w:rsidTr="00730AB7">
	        <w:trPr>
	            <w:trHeight w:val="359"/>
	            <w:jc w:val="center"/>
	        </w:trPr>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="860" w:type="pct"/>
	                <w:shd w:val="clear" w:color="auto" w:fill="D0CECE" w:themeFill="background2" w:themeFillShade="E6"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00730AB7" w:rsidRDefault="00730AB7" w:rsidP="00730AB7">
	                <w:pPr>
	                    <w:widowControl/>
	                    <w:jc w:val="left"/>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                </w:pPr>
	                <w:r>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                    <w:t>信息资源提供方</w:t>
	                </w:r>
	                <w:r>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                    <w:br/>
	                </w:r>
	                <w:r>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                    <w:t>（</w:t>
	                </w:r>
	                <w:r w:rsidRPr="00CD3867">
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                    <w:t>下</w:t>
	                </w:r>
	                <w:r w:rsidRPr="00CD3867">
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                    <w:t>设机构</w:t>
	                </w:r>
	                <w:r>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                    <w:t>）</w:t>
	                </w:r>
	            </w:p>
	        </w:tc>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="1524" w:type="pct"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00730AB7" w:rsidRPr="00853DC1" w:rsidRDefault="00730AB7" w:rsidP="00730AB7">
	                <w:pPr>
	                    <w:widowControl/>
	                    <w:rPr>
	                        <w:color w:val="FF0000"/>
	                    </w:rPr>
	                </w:pPr>
	                <w:r>
				        <w:t>${info.belongToName!''}</w:t>
				    </w:r>
	            </w:p>
	        </w:tc>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="914" w:type="pct"/>
	                <w:shd w:val="clear" w:color="auto" w:fill="BFBFBF" w:themeFill="background1" w:themeFillShade="BF"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00730AB7" w:rsidRPr="00223314" w:rsidRDefault="00730AB7" w:rsidP="00730AB7">
	                <w:pPr>
	                    <w:widowControl/>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                </w:pPr>
	                <w:r>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                    <w:t>资源提供方代码</w:t>
	                </w:r>
	            </w:p>
	        </w:tc>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="4140" w:type="pct"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00730AB7" w:rsidRPr="00223314" w:rsidRDefault="00730AB7" w:rsidP="00730AB7">
	                <w:pPr>
	                    <w:widowControl/>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                </w:pPr>
	                <w:r>
				        <w:t>${info.provOrgCode!''}</w:t>
				    </w:r>
	            </w:p>
	        </w:tc>
	    </w:tr>
	    <w:tr w:rsidR="00730AB7" w:rsidRPr="00853DC1" w:rsidTr="00730AB7">
	        <w:trPr>
	            <w:trHeight w:val="359"/>
	            <w:jc w:val="center"/>
	        </w:trPr>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="860" w:type="pct"/>
	                <w:shd w:val="clear" w:color="auto" w:fill="D0CECE" w:themeFill="background2" w:themeFillShade="E6"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00730AB7" w:rsidRPr="00924A5B" w:rsidRDefault="00730AB7" w:rsidP="00730AB7">
	                <w:pPr>
	                    <w:widowControl/>
	                    <w:jc w:val="left"/>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                </w:pPr>
	                <w:r w:rsidRPr="00924A5B">
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                    <w:t>信息资源格式分类</w:t>
	                </w:r>
	            </w:p>
	        </w:tc>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="1524" w:type="pct"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00730AB7" w:rsidRPr="00853DC1" w:rsidRDefault="00730AB7" w:rsidP="00730AB7">
	                <w:pPr>
	                    <w:widowControl/>
	                    <w:rPr>
	                        <w:color w:val="FF0000"/>
	                    </w:rPr>
	                </w:pPr>
	                <w:r>
				        <w:t>${info.mediaFmtName!''}</w:t>
				    </w:r>
	            </w:p>
	        </w:tc>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="914" w:type="pct"/>
	                <w:shd w:val="clear" w:color="auto" w:fill="BFBFBF" w:themeFill="background1" w:themeFillShade="BF"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00730AB7" w:rsidRPr="00924A5B" w:rsidRDefault="00730AB7" w:rsidP="00730AB7">
	                <w:pPr>
	                    <w:widowControl/>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                </w:pPr>
	                <w:r w:rsidRPr="00924A5B">
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                    <w:t>信息资源格式类型</w:t>
	                </w:r>
	            </w:p>
	        </w:tc>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="4140" w:type="pct"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00730AB7" w:rsidRPr="00223314" w:rsidRDefault="00730AB7" w:rsidP="00730AB7">
	                <w:pPr>
	                    <w:widowControl/>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                </w:pPr>
	                <w:r>
				        <w:t>${info.mediaFmtTypeName!''}</w:t>
				    </w:r>
	            </w:p>
	        </w:tc>
	    </w:tr>
	    <w:tr w:rsidR="00730AB7" w:rsidRPr="00CD3867" w:rsidTr="00730AB7">
	        <w:trPr>
	            <w:trHeight w:val="359"/>
	            <w:jc w:val="center"/>
	        </w:trPr>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="860" w:type="pct"/>
	                <w:shd w:val="clear" w:color="auto" w:fill="D0CECE" w:themeFill="background2" w:themeFillShade="E6"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00730AB7" w:rsidRPr="00CD3867" w:rsidRDefault="00730AB7" w:rsidP="00730AB7">
	                <w:pPr>
	                    <w:widowControl/>
	                    <w:jc w:val="left"/>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                </w:pPr>
	                <w:r w:rsidRPr="00CD3867">
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                    <w:t>更新周期</w:t>
	                </w:r>
	            </w:p>
	        </w:tc>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="1524" w:type="pct"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00730AB7" w:rsidRPr="00CD3867" w:rsidRDefault="00730AB7" w:rsidP="00730AB7">
	                <w:pPr>
	                    <w:widowControl/>
	                    <w:rPr>
	                        <w:color w:val="FF0000"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                </w:pPr>
	                <w:r>
				        <w:t>${info.updateCycName!''}</w:t>
				    </w:r>
	            </w:p>
	        </w:tc>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="914" w:type="pct"/>
	                <w:shd w:val="clear" w:color="auto" w:fill="BFBFBF" w:themeFill="background1" w:themeFillShade="BF"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00730AB7" w:rsidRPr="00CD3867" w:rsidRDefault="00730AB7" w:rsidP="00730AB7">
	                <w:pPr>
	                    <w:widowControl/>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                </w:pPr>
	                <w:r>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                    <w:t>发布日期</w:t>
	                </w:r>
	            </w:p>
	        </w:tc>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="4140" w:type="pct"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00730AB7" w:rsidRPr="00CD3867" w:rsidRDefault="00730AB7" w:rsidP="00730AB7">
	                <w:pPr>
	                    <w:widowControl/>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                </w:pPr>
	                <w:r>
				        <w:t>${(info.pubDt?string("yyyy-MM-dd"))!}</w:t>
				    </w:r>
	            </w:p>
	        </w:tc>
	    </w:tr>
	    <w:tr w:rsidR="00730AB7" w:rsidRPr="00CD3867" w:rsidTr="00730AB7">
	        <w:trPr>
	            <w:trHeight w:val="359"/>
	            <w:jc w:val="center"/>
	        </w:trPr>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="860" w:type="pct"/>
	                <w:shd w:val="clear" w:color="auto" w:fill="D0CECE" w:themeFill="background2" w:themeFillShade="E6"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00730AB7" w:rsidRPr="00CD3867" w:rsidRDefault="00730AB7" w:rsidP="00730AB7">
	                <w:pPr>
	                    <w:widowControl/>
	                    <w:jc w:val="left"/>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                </w:pPr>
	                <w:r>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                    <w:t>共享类型</w:t>
	                </w:r>
	            </w:p>
	        </w:tc>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="1524" w:type="pct"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00730AB7" w:rsidRPr="00CD3867" w:rsidRDefault="00730AB7" w:rsidP="00730AB7">
	                <w:pPr>
	                    <w:widowControl/>
	                    <w:rPr>
	                        <w:color w:val="FF0000"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                </w:pPr>
	                <w:r>
				        <w:t>${info.shareLvName!''}</w:t>
				    </w:r>
	            </w:p>
	        </w:tc>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="914" w:type="pct"/>
	                <w:shd w:val="clear" w:color="auto" w:fill="BFBFBF" w:themeFill="background1" w:themeFillShade="BF"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00730AB7" w:rsidRPr="00CD3867" w:rsidRDefault="00730AB7" w:rsidP="00730AB7">
	                <w:pPr>
	                    <w:widowControl/>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                </w:pPr>
	                <w:r w:rsidRPr="00C57102">
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                    <w:t>共享条件/不予共享依据</w:t>
	                </w:r>
	            </w:p>
	        </w:tc>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="4140" w:type="pct"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00730AB7" w:rsidRPr="00CD3867" w:rsidRDefault="00730AB7" w:rsidP="00730AB7">
	                <w:pPr>
	                    <w:widowControl/>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                </w:pPr>
	                <w:r>
				        <w:t>${info.shareCondition!''}</w:t>
				    </w:r>
	            </w:p>
	        </w:tc>
	    </w:tr>
	    <w:tr w:rsidR="00730AB7" w:rsidRPr="00CD3867" w:rsidTr="00730AB7">
	        <w:trPr>
	            <w:trHeight w:val="359"/>
	            <w:jc w:val="center"/>
	        </w:trPr>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="860" w:type="pct"/>
	                <w:shd w:val="clear" w:color="auto" w:fill="D0CECE" w:themeFill="background2" w:themeFillShade="E6"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00730AB7" w:rsidRDefault="00730AB7" w:rsidP="00730AB7">
	                <w:pPr>
	                    <w:widowControl/>
	                    <w:jc w:val="left"/>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                </w:pPr>
	                <w:r>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                    <w:t>开放类型</w:t>
	                </w:r>
	            </w:p>
	        </w:tc>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="1524" w:type="pct"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00730AB7" w:rsidRPr="00CD3867" w:rsidRDefault="00730AB7" w:rsidP="00730AB7">
	                <w:pPr>
	                    <w:widowControl/>
	                    <w:rPr>
	                        <w:color w:val="FF0000"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                </w:pPr>
	                <w:r>
				        <w:t>${info.pubSts!''}</w:t>
				    </w:r>
	            </w:p>
	        </w:tc>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="914" w:type="pct"/>
	                <w:shd w:val="clear" w:color="auto" w:fill="BFBFBF" w:themeFill="background1" w:themeFillShade="BF"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00730AB7" w:rsidRPr="00C57102" w:rsidRDefault="00730AB7" w:rsidP="00730AB7">
	                <w:pPr>
	                    <w:widowControl/>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                </w:pPr>
	                <w:r w:rsidRPr="00282612">
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                    <w:t>开放条件/不开放依据</w:t>
	                </w:r>
	            </w:p>
	        </w:tc>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="4140" w:type="pct"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00730AB7" w:rsidRPr="00CD3867" w:rsidRDefault="00730AB7" w:rsidP="00730AB7">
	                <w:pPr>
	                    <w:widowControl/>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                </w:pPr>
	                <w:r>
				        <w:t>${info.pubCondition!''}</w:t>
				    </w:r>
	            </w:p>
	        </w:tc>
	    </w:tr>
	    <w:tr w:rsidR="00730AB7" w:rsidRPr="00CD3867" w:rsidTr="00730AB7">
	        <w:trPr>
	            <w:trHeight w:val="359"/>
	            <w:jc w:val="center"/>
	        </w:trPr>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="860" w:type="pct"/>
	                <w:shd w:val="clear" w:color="auto" w:fill="D0CECE" w:themeFill="background2" w:themeFillShade="E6"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00730AB7" w:rsidRDefault="00730AB7" w:rsidP="00730AB7">
	                <w:pPr>
	                    <w:widowControl/>
	                    <w:jc w:val="left"/>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                </w:pPr>
	                <w:r>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                    <w:t>关联资源分类</w:t>
	                </w:r>
	            </w:p>
	        </w:tc>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="1" w:type="pct"/>
	                <w:gridSpan w:val="3"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00730AB7" w:rsidRPr="00CD3867" w:rsidRDefault="00730AB7" w:rsidP="00730AB7">
	                <w:pPr>
	                    <w:widowControl/>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                </w:pPr>
	                <w:r>
				        <w:t>${info.typeName!''}</w:t>
				    </w:r>
	            </w:p>
	        </w:tc>
	    </w:tr>
	</w:tbl>
	<w:p w:rsidR="00730AB7" w:rsidRDefault="00730AB7"/>
	<w:tbl>
	    <w:tblPr>
	        <w:tblW w:w="5260" w:type="pct"/>
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
	        <w:gridCol w:w="2830"/>
	        <w:gridCol w:w="2695"/>
	        <w:gridCol w:w="1558"/>
	        <w:gridCol w:w="1651"/>
	    </w:tblGrid>
	    <w:tr w:rsidR="00A83780" w:rsidRPr="005F0CCF" w:rsidTr="00A83780">
	        <w:trPr>
	            <w:trHeight w:val="359"/>
	            <w:tblHeader/>
	            <w:jc w:val="center"/>
	        </w:trPr>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="1620" w:type="pct"/>
	                <w:shd w:val="clear" w:color="auto" w:fill="D0CECE" w:themeFill="background2" w:themeFillShade="E6"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00A83780" w:rsidRPr="005F0CCF" w:rsidRDefault="00A83780" w:rsidP="00A83780">
	                <w:pPr>
	                    <w:widowControl/>
	                    <w:jc w:val="left"/>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                </w:pPr>
	                <w:r>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                    <w:t>信息</w:t>
	                </w:r>
	                <w:r>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                    <w:t>项名称</w:t>
	                </w:r>
	            </w:p>
	        </w:tc>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="1543" w:type="pct"/>
	                <w:shd w:val="clear" w:color="auto" w:fill="D0CECE" w:themeFill="background2" w:themeFillShade="E6"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00A83780" w:rsidRPr="005F0CCF" w:rsidRDefault="00A83780" w:rsidP="00A83780">
	                <w:pPr>
	                    <w:widowControl/>
	                    <w:jc w:val="left"/>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                </w:pPr>
	                <w:r>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                    <w:t>标识</w:t>
	                </w:r>
	            </w:p>
	        </w:tc>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="892" w:type="pct"/>
	                <w:shd w:val="clear" w:color="auto" w:fill="D0CECE" w:themeFill="background2" w:themeFillShade="E6"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00A83780" w:rsidRPr="005F0CCF" w:rsidRDefault="00A83780" w:rsidP="00A83780">
	                <w:pPr>
	                    <w:widowControl/>
	                    <w:jc w:val="left"/>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                </w:pPr>
	                <w:r>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                    <w:t>数据类型</w:t>
	                </w:r>
	            </w:p>
	        </w:tc>
	        <w:tc>
	            <w:tcPr>
	                <w:tcW w:w="945" w:type="pct"/>
	                <w:shd w:val="clear" w:color="auto" w:fill="D0CECE" w:themeFill="background2" w:themeFillShade="E6"/>
	                <w:vAlign w:val="center"/>
	            </w:tcPr>
	            <w:p w:rsidR="00A83780" w:rsidRPr="005F0CCF" w:rsidRDefault="00A83780" w:rsidP="00A83780">
	                <w:pPr>
	                    <w:widowControl/>
	                    <w:jc w:val="center"/>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                </w:pPr>
	                <w:r>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                    <w:t>数据</w:t>
	                </w:r>
	                <w:r>
	                    <w:rPr>
	                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
	                        <w:b/>
	                        <w:bCs/>
	                        <w:kern w:val="0"/>
	                        <w:szCs w:val="21"/>
	                    </w:rPr>
	                    <w:t>长度</w:t>
	                </w:r>
	            </w:p>
	        </w:tc>
	    </w:tr>
		<!-- 信息项表格 -->
	    <#include "itemTable.ftl" />
	</w:tbl>
	<w:p w:rsidR="00A222CD" w:rsidRPr="00945264" w:rsidRDefault="00A222CD" w:rsidP="00730AB7"/>
	</#list>
</#list>
</#if>