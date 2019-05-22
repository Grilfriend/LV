package com.qhit.tests.ALpoi;


import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;



public class Poi {
    public static void main(String[] args)throws InstantiationException, IllegalAccessException,
            ClassNotFoundException,SQLException, IOException {
        Poi a=new Poi();
        a.jdbcex(true);
    }

    private void jdbcex(boolean isClose) throws InstantiationException, IllegalAccessException,
            ClassNotFoundException, SQLException, IOException {

        String xlsFile = "E:/test_export.xlsx";		//输出文件
        //内存中只创建100个对象，写临时文件，当超过100条，就将内存中不用的对象释放。
        Workbook wb = new SXSSFWorkbook(100);			//关键语句
        Sheet sheet = null;		//工作表对象
        Row nRow;		//行对象
        Cell nCell;		//列对象
        String[] name = {"userid","cname","username","password"};

        //使用jdbc链接数据库
        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        String url = "jdbc:mysql://localhost:3306/ems?useUnicode=true&characterEncoding=utf-8&useSSL=true&serverTimezone=UTC";
        String user = "root";
        String password = "123456";
        //获取数据库连接
        Connection conn = DriverManager.getConnection(url, user,password);
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
        String sql = "select * from produce_job limit 1000000";   //100万测试数据
        ResultSet rs = stmt.executeQuery(sql);



        ResultSetMetaData rsmd = rs.getMetaData();
        long  startTime = System.currentTimeMillis();	//开始时间


        int rowNo = 0;		//总行号
        int pageRowNo = 1;	//页行号

        while(rs.next()) {
            //打印300000条后切换到下个工作表，可根据需要自行拓展，2百万，3百万...数据一样操作，只要不超过1048576就可以
            if(rowNo%1000000==0){
                System.out.println("Current Sheet:" + rowNo/1000000);
                sheet = wb.createSheet("我的第"+(rowNo/1000000)+"个工作簿");//建立新的sheet对象
                sheet = wb.getSheetAt(rowNo/1000000);		//动态指定当前的工作表

                nRow = sheet.createRow(0);
                nRow.setHeightInPoints(20);
                for(int i=0; i<name.length; i++){
                    nCell = nRow.createCell(i);
                    nCell.setCellValue(name[i]);
                }

                pageRowNo = 1;		//每当新建了工作表就将当前工作表的行号重置为0
            }
            rowNo++;
            nRow = sheet.createRow(pageRowNo++);	//新建行对象
            // 打印每行，每行有6列数据   rsmd.getColumnCount()==6 --- 列属性的个数
            for(int j=0;j<rsmd.getColumnCount();j++){
                nCell = nRow.createCell(j);
                nCell.setCellValue(rs.getString(j+1));
            }

            if(rowNo%10000==0){
                System.out.println("row no: " + rowNo);
            }
//		Thread.sleep(1);	//休息一下，防止对CPU占用，其实影响不大
        }

        long finishedTime = System.currentTimeMillis();	//处理完成时间
        System.out.println("处理完成时间: " + (double)(finishedTime - startTime)/1000 + "s");

        FileOutputStream fOut = new FileOutputStream(xlsFile);
        wb.write(fOut);
        fOut.flush();		//刷新缓冲区
        fOut.close();

        long stopTime = System.currentTimeMillis();		//写文件时间
        System.out.println("写文件时间: " + (double)(stopTime - startTime)/1000 + "s");

        if(isClose){
            this.close(rs, stmt, conn);
        }
    }

    //执行关闭流的操作
    private void close(ResultSet rs, Statement stmt, Connection conn ) throws SQLException{
        rs.close();
        stmt.close();
        conn.close();
    }
}
