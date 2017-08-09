package zust.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import zust.entity.Course;

public class parseHtml {
    
    /** 
     * 在HTML文档中提取课表 
     * @param html HTML文档 
     * @return 返回课表 
     */  
    public static ArrayList<Course> getCourseList(String html){  
          
        String courseTableHtml=findCourseTableHtml(html);//找到课表部分的HTML  
        ArrayList<Course> courses=new ArrayList<Course>();  
  
        String[] rows=courseTableHtml.split("</tr><tr>");//按上课时间分隔HTML  
        System.out.println(rows.length);
        for(int i=2;i<rows.length;i+=1){  
              
            String r=rows[i];  
            String[] cols=r.split("</td><td([\\S\\s]*?)>");//按星期几分隔HTML  
              for(int j = 1;j<cols.length;j++){
            	  String cell=cols[j]; 
            	  if(cell.contains("<br>")){
            		  System.out.println("===============start===================");
            		  String[] info=cell.split("<br>"); 
            		  for(int k=0;k<info.length;k++){  
                    	System.out.println(info[k].toString());
                    }  
                    System.out.println("===============end===================");
                    System.out.println("");
            	  }
              }
            
              
        }  
                  
        return courses;  
    }     
      
    private static String findCourseTableHtml(String html){  
        String res="";  
        String tar="<table id=\"Table1\" class=\"blacktab\" bordercolor=\"Black\" border=\"0\" width=\"100%\">";  
        String pattern="<table id=\"Table1\" class=\"blacktab\" bordercolor=\"Black\" border=\"0\" width=\"100%\">([\\S\\s]+?)</table>";          
        Pattern p=Pattern.compile(pattern);  
        Matcher m=p.matcher(html);        
        if(m.find()){  
            res=m.group(0);  
            res=res.substring(res.indexOf(tar)+tar.length(),res.lastIndexOf("</table>")).trim();
        }else{  
            System.out.println(html);  
            System.out.println("什么都没有");  
        }  
        return res;  
    } 
}
