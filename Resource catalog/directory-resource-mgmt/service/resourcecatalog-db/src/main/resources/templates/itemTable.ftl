<#if info.itemList??>
    <#list info.itemList as item>
    <w:tr w:rsidR="00A83780" w:rsidTr="00A83780">
        <w:trPr>
            <w:trHeight w:val="359"/>
            <w:jc w:val="center"/>
        </w:trPr>
        <w:tc>
            <w:tcPr>
                <w:tcW w:w="1620" w:type="pct"/>
                <w:vAlign w:val="center"/>
            </w:tcPr>
            <w:p w:rsidR="00A83780" w:rsidRDefault="00A83780" w:rsidP="00A83780">
	            <w:r>
			        <w:t>${item.srcField!''}</w:t>
			    </w:r>
            </w:p>
        </w:tc>
        <w:tc>
            <w:tcPr>
                <w:tcW w:w="1543" w:type="pct"/>
                <w:vAlign w:val="center"/>
            </w:tcPr>
            <w:p w:rsidR="00A83780" w:rsidRDefault="00A83780" w:rsidP="00A83780">
            <w:r>
		        <w:t>${item.engCd!''}</w:t>
		    </w:r>
		    </w:p>
        </w:tc>
        <w:tc>
            <w:tcPr>
                <w:tcW w:w="892" w:type="pct"/>
                <w:vAlign w:val="center"/>
            </w:tcPr>
            <w:p w:rsidR="00A83780" w:rsidRDefault="00A83780" w:rsidP="00A83780">
            <w:r>
		        <w:t>${item.srcDataTyp!''}</w:t>
		    </w:r>
		    </w:p>
        </w:tc>
        <w:tc>
            <w:tcPr>
                <w:tcW w:w="945" w:type="pct"/>
                <w:vAlign w:val="center"/>
            </w:tcPr>
            <w:p w:rsidR="00A83780" w:rsidRDefault="00A83780" w:rsidP="00A83780">
            <w:r>
		        <w:t>${item.dataLen!''}</w:t>
		    </w:r>
		    </w:p>
        </w:tc>
    </w:tr>
	</#list>
</#if>