package com.example;

import java.io.OutputStream;
import java.sql.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SQLToExcel {

    private static final String JDBC_URL = "jdbc:zenith:@10.199.167.183:1888";
    private static final String USERNAME = "AT_SDM";
    private static final String PASSWORD = "sdm#_123OSS";

    public static void exportSdmFastExcel(String exportType, String flowIds, OutputStream out) throws Exception {
        // 1. 参数校验（可选但推荐）
        if (flowIds == null || flowIds.trim().isEmpty()) {
            throw new IllegalArgumentException("flowIds cannot be null or empty");
        }

        // 2. 构建 IN 条件（注意：此处仍有 SQL 注入风险，见下方说明）
        String[] ids = flowIds.split(",");
        StringBuilder inClause = new StringBuilder();
//        for (int i = 0; i < ids.length; i++) {
//            if (i > 0) inClause.append(",");
//            inClause.append("'").append(ids[i].trim()).append("'");
//        }
        for (int i = 0; i < ids.length; i++) {
            String cleanId = ids[i].trim().replace("'", ""); // 移除所有单引号
            if (cleanId.isEmpty()) continue; // 跳过空项
            if (i > 0 && inClause.length() > 0) inClause.append(",");
            inClause.append("'").append(cleanId).append("'");
        }

        // 3. 根据类型选择 SQL
        String sql;
        if ("running_to_crm".equals(exportType)) {
            sql = "SELECT " +
                    "ticketno AS \"Ticket No\", " +
                    "'Finish' AS OperationMode, " +
                    "'' AS MidWayType, " +
                    "'' AS MidWayOpinion, " +
                    "'' AS UpdateDescription, " +
                    "'' AS AssignTo, " +
                    "'' AS ToOthersDescription, " +
                    "SYSDATE AS finishTime, " +
                    "'' AS rootCause, " +
                    "'' AS subCause, " +
                    "'' AS FinishDescription, " +
                    "'' AS RejectDescription, " +
                    "'' AS CRMUpdateDescription, " +
                    "'submit' AS \"Templdate Operation\" " +
                    "FROM bt_complainttt " +
                    "WHERE ticketno IN (" + inClause + ")";
        } else if ("crm_only".equals(exportType)) {
            sql = "SELECT " +
                    "ticketno AS \"Ticket No\", " +
                    "'Accept' AS OperationMode, " +
                    "'0' AS MidWayType, " +
                    "'0' AS MidWayOpinion, " +
                    "'1' AS Description, " +
                    "'Approve' AS CRMUpdateDescription, " +
                    "'submit' AS \"Templdate Operation\" " +
                    "FROM bt_complainttt " +
                    "WHERE ticketno IN (" + inClause + ")";
        } else {
            throw new IllegalArgumentException("Invalid export_type");
        }

        // 4. 显式加载驱动并手动管理连接（按你的要求）
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        XSSFWorkbook workbook = null;

        try {
            // 加载 GaussDB 驱动
            Class.forName("com.huawei.gauss.jdbc.ZenithDriver");

            // 获取连接
            con = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

            // 创建语句
            stmt = con.createStatement();

            // 执行查询
            rs = stmt.executeQuery(sql);

            // 创建 Excel
            workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Data");

            // 写表头
            ResultSetMetaData meta = rs.getMetaData();
            Row header = sheet.createRow(0);
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                header.createCell(i - 1).setCellValue(meta.getColumnLabel(i));
            }

            // 写数据
            int rowNum = 1;
            while (rs.next()) {
                Row row = sheet.createRow(rowNum++);
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    String value = rs.getString(i);
                    row.createCell(i - 1).setCellValue(value != null ? value : "");
                }
            }

            // 写入输出流
            workbook.write(out);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("GaussDB JDBC Driver not found", e);
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        } finally {
            // 手动关闭资源（逆序关闭）
            if (workbook != null) {
                try { workbook.close(); } catch (Exception ignored) {}
            }
            if (rs != null) {
                try { rs.close(); } catch (SQLException ignored) {}
            }
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException ignored) {}
            }
            if (con != null) {
                try { con.close(); } catch (SQLException ignored) {}
            }
        }
    }
}