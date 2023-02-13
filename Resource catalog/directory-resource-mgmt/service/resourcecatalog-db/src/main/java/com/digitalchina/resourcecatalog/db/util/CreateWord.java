package com.digitalchina.resourcecatalog.db.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

import org.w3c.dom.Document;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
    
public class CreateWord {    
        
    private Configuration configuration = null;    
        
    public CreateWord(){    
        configuration = new Configuration();    
        configuration.setDefaultEncoding("UTF-8");    
    }    
        
    public static void main(String[] args) throws Exception {    
    	CreateWord test = new CreateWord();    
//        test.createWord();    
    }  
    
    public void createWord(Map<String,Object> dataMap,String type,String templatepath,String outPath) throws Exception{    
        configuration.setClassForTemplateLoading(this.getClass(), "/templates");  //FTL文件所存在的位置 
        Template t=null;    
        try {    
        	if("info".equals(type)){
        		t = configuration.getTemplate("infoMain.ftl");     
        	}else if("require".equals(type)){
        		t = configuration.getTemplate("requireMain.ftl");
        	}
        } catch (IOException e) {    
            e.printStackTrace();    
        }    
        String xmlName="temp"+Math.random()*10000+".xml";
        File outFile = new File(xmlName);  //生成临时文件 
        BufferedWriter out = null;    
        try {    
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));    
        } catch (FileNotFoundException e1) {    
            e1.printStackTrace();    
        }    
        try {    
            t.process(dataMap, out);    
            XmlWord test = new XmlWord();
            test.outputWord(templatepath+type+".docx", xmlName, outPath);
        } catch (TemplateException e) {    
            e.printStackTrace();    
        } catch (IOException e) {    
            e.printStackTrace();    
        }finally {
			if(out!=null){
				out.close();
			}
			if(outFile!=null){
				outFile.delete();
			}
		}
    }    
    //这里赋值的时候需要注意,xml中需要的数据你必须提供给它,不然会报找不到某元素错的.  
    private void getDataForInfo(Map<String, Object> dataMap) {    
        dataMap.put("deptName", "");
    }    
}