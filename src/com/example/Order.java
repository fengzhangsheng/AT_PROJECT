package com.example;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.logging.Level;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Order {

    private static final Logger logger = LoggerFactory.getLogger(Order.class);

    // 业务字段
    String crm_no;
    String flow_id;
    String num;
    String product;
    short current_state;
    String act_def_name;
    String SEND_TIME;
    String operation;

    // 静态资源（应由外部初始化，如 ServletContextListener）
    public static String SESSION_COOKIE;
    public static Connection connection;
    public static String sessionToken; // 可从配置或登录获取

    // ==================== 入口方法：供 ExecuteServlet 调用 ====================

    /**
     * 处理 OM 类型 Flow ID
     */
    public static void updateOM(String flowId) {
        if (flowId == null || flowId.trim().isEmpty()) {
            logger.warn("updateOM: flowId is null or empty");
            return;
        }
        logger.info("Executing updateOM for flow_id: {}", flowId);
        MSV(flowId);
    }

    /**
     * 处理 IM 类型 Flow ID
     */
    public static void updateAM(String flowId) {
        // 参数校验
        if (flowId == null || flowId.trim().isEmpty()) {
            logger.warn("updateIM: flowId is null or empty");
            return;
        }
        flowId = flowId.trim();
        log("updateAM", "开始处理, flowId=" + flowId);
        logger.info("Executing updateIM for flow_id: " + flowId);

        final String sql = "SELECT a.order_id AS flowid FROM am_ces.t_am_sub_flow_info a WHERE a.id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, flowId);
            logSql("updateAM", sql, "id=" + flowId);
            logger.info("Executing SQL: " + sql + " with parameter: " + flowId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String orderFlowId = rs.getString("flowid");
                    String requestBody = "ids=" + orderFlowId;

                    log("updateAM", "调用API: http://10.199.167.160:15203/am/plugins/activeFlow/cmdinstance/redoBatch.ilf, requestBody=" + requestBody);
                    logger.info("About to POST, requestBody=" + requestBody);

                    String response = HttpsUtils.post(
                            "http://10.199.167.160:15203/am/plugins/activeFlow/cmdinstance/redoBatch.ilf",
                            requestBody,
                            sessionToken
                    );

                    log("updateAM", "API响应: " + response);
                    logger.info("API response: " + response);
                } else {
                    log("updateAM", "⚠️ 未找到数据, flowId=" + flowId);
                    logger.warn("No data found for flowId: " + flowId);
                }
            }
        } catch (SQLException e) {
            logger.error("SQL error while processing flowId: " + flowId, e);
            throw new RuntimeException("Database query failed for flow: " + flowId, e);
        } catch (Exception e) {
            logger.error("Unexpected error during updateIM for flowId: " + flowId, e);
            throw new RuntimeException("Execution failed for flow: " + flowId, e);
        }
    }

    /**
     * 处理 AM 类型 Flow ID
     */
    public static void updateIM(String flowId) {
        // 1. 参数校验
        if (flowId == null || flowId.trim().isEmpty()) {
            logger.warn("updateIM: flowId is null or empty");
            return;
        }
        flowId = flowId.trim();
        log("updateIM", "开始处理, flowId=" + flowId);
        logger.info("Executing updateIM for flow_id: {}", flowId);

        // 2. 定义 SQL 查询（获取最新一条任务数据）
        final String sql =
                "SELECT flow_id, operate_type, serviceId, activityId " +
                        "FROM ( " +
                        "    SELECT a.flow_id, b.operate_type, a.uuid AS serviceId, c.uuid AS activityId, c.CREATE_TIME " +
                        "    FROM im_home_service a " +
                        "    LEFT JOIN im_task_form     b ON a.flow_id = b.flow_id " +
                        "    LEFT JOIN im_task_activity c ON a.flow_id = c.flow_id " +
                        "    WHERE a.flow_id = ? " +
                        "    ORDER BY c.CREATE_TIME DESC " +
                        ") WHERE rownum <= 1";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, flowId);
            logSql("updateIM", sql, "flowId=" + flowId);
            logger.info("Executing SQL: {} with parameter: {}", sql, flowId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String activityId  = rs.getString("activityId");
                    String flow_id     = rs.getString("flow_id");
                    String operateType = rs.getString("operate_type");
                    String serviceId   = rs.getString("serviceId");

                    String requestBody = "activityId=" + activityId +
                            "&flow_id=" + flow_id +
                            "&operate_type=" + operateType +
                            "&serviceId=" + serviceId;

                    log("updateIM", "调用API: http://10.199.167.141:15202/imService/plugins/family/taskinfo/submit.ilf, requestBody=" + requestBody);
                    logger.info("About to POST to API, requestBody={}", requestBody);

                    String response = HttpsUtils.post(
                            "http://10.199.167.141:15202/imService/plugins/family/taskinfo/submit.ilf",
                            requestBody,
                            sessionToken
                    );

                    log("updateIM", "API响应: " + response);
                    logger.info("API response: {}", response);
                } else {
                    log("updateIM", "⚠️ 未找到数据, flowId=" + flowId);
                    logger.warn("No data found for flowId: {}", flowId);
                }
            }
        } catch (SQLException e) {
            logger.error("SQL error while processing flowId: {}, error: {}", flowId, e.getMessage(), e);
            throw new RuntimeException("Database query failed for flow: " + flowId, e);
        } catch (Exception e) {
            logger.error("Unexpected error during updateIM for flowId: {}, error: {}", flowId, e.getMessage(), e);
            throw new RuntimeException("Execution failed for flow: " + flowId, e);
        }
    }

    public static void updatesn(String flowId, String oldVal, String newVal) {
        String sql =
                "UPDATE am_ces.t_am_cmd_instance t " +
                        "SET execute_cmd = REPLACE(execute_cmd, ?, ?) " +
                        "WHERE t.flow_id = ? " +
                        "  AND execute_cmd LIKE '%' || ? || '%'";

        log("updatesn", "开始处理, flowId=" + flowId + ", oldVal=" + oldVal + ", newVal=" + newVal);
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, oldVal);
            ps.setString(2, newVal);
            ps.setString(3, flowId);
            ps.setString(4, oldVal);

            logSql("updatesn", sql, "oldVal=" + oldVal + ", newVal=" + newVal + ", flowId=" + flowId);
            int rows = ps.executeUpdate();
            log("updatesn", "✅ 更新结果: " + rows + " 行");
            logger.info("Replaced '{}' with '{}' in {} row(s) for flow_id={}", oldVal, newVal, rows, flowId);

        } catch (SQLException e) {
            logger.error("Update execute_cmd failed for flow_id={}", flowId, e);
            throw new RuntimeException("Update failed: " + e.getMessage(), e);
        }
    }


    public static void finishamorder(String flowId) {
        // 1. 参数校验
        if (flowId == null || flowId.trim().isEmpty()) {
            logger.warn("finishamorder: flowId is null or empty");
            return;
        }
        flowId = flowId.trim();
        log("finishamorder", "开始处理, flowId=" + flowId);
        logger.info("Executing finishamorder for flow_id: {}", flowId);

        // 2. 定义三条 SQL 更新语句（使用 ? 参数化）
        final String updateCmdInstanceSql =
                "UPDATE am_ces.t_am_cmd_instance t1 " +
                        "SET t1.cmd_state = 1, " +
                        "    t1.result = 1 " +
                        "WHERE t1.flow_id = ?";

        final String updateFlowInfoSql =
                "UPDATE am_ces.t_am_flow_info t2 " +
                        "SET t2.flow_state = '3', " +
                        "    t2.result = '1', " +
                        "    t2.run_status = '3', " +
                        "    t2.excp_flag = '1' " +
                        "WHERE t2.flow_id = ?";

        final String updateSubFlowInfoSql =
                "UPDATE am_ces.t_am_sub_flow_info t3 " +
                        "SET t3.flow_state = '3', " +
                        "    t3.result = '1', " +
                        "    t3.run_status = '3' " +
                        "WHERE t3.flow_id = ?";

        try {
            // 3. 分别执行三条更新语句
            int update1 = executeUpdate(updateCmdInstanceSql, flowId);
            int update2 = executeUpdate(updateFlowInfoSql, flowId);
            int update3 = executeUpdate(updateSubFlowInfoSql, flowId);

            // 4. 日志记录影响行数
            logger.info("✅ updatesn completed for flow_id: {}. " +
                            "t_am_cmd_instance: {} rows, t_am_flow_info: {} rows, t_am_sub_flow_info: {} rows",
                    flowId, update1, update2, update3);

            if (update1 == 0 && update2 == 0 && update3 == 0) {
                logger.warn("⚠️ No rows were updated for flow_id: {}. Check if flow_id exists.", flowId);
            }

        } catch (SQLException e) {
            logger.error("❌ SQL error during updatesn for flowId: {}, error: {}", flowId, e.getMessage(), e);
            throw new RuntimeException("Database update failed for flow: " + flowId, e);
        } catch (Exception e) {
            logger.error("❌ Unexpected error during updatesn for flowId: {}, error: {}", flowId, e.getMessage(), e);
            throw new RuntimeException("Execution failed for flow: " + flowId, e);
        }
    }

    private static int executeUpdate(String sql, String flowId) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, flowId);
            logSql("finishamorder", sql, "flowId=" + flowId);
            int rows = ps.executeUpdate();
            log("finishamorder", "影响行数: " + rows);
            return rows;
        }
    }
    /**
     * 处理 AM SN（序列号）
     */
    public static void skipwfm(String flowId) {
        if (flowId == null || flowId.trim().isEmpty()) {
            logger.warn("updateIM: flowId is null or empty");
            return;
        }
        flowId = flowId.trim();
        log("skipwfm", "开始处理, flowId=" + flowId);
        logger.info("Executing updateIM for flow_id: {}", flowId);

        // Step 1: 查询匹配的 flow_id 列表
        List<String> flowIds = new ArrayList<>();
        String query = "SELECT t.flow_id, t.crm_no FROM om.T_BPM_FORM_INFO t " +
                "WHERE (t.crm_no = ? OR t.flow_id = ?) ";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, flowId);
            ps.setString(2, flowId);
            logSql("skipwfm", query, "flowId=" + flowId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String fid = rs.getString("flow_id");
                    log("skipwfm", "查询到: CRM=" + rs.getString("crm_no") + ", FlowID=" + fid);
                    flowIds.add(fid);
                }
            }
        } catch (SQLException e) {
            log("skipwfm", "❌ 数据库查询失败: " + e.getMessage());
            logger.error("Database query failed", e);
            return;
        }

        if (flowIds.isEmpty()) {
            log("skipwfm", "⚠️ 未找到 flowId: " + flowId);
            return;
        }

        // Step 2: 批量更新 task_status = '3'
        StringBuilder updateSql = new StringBuilder("UPDATE om.t_bpm_form_info SET task_status = '3' WHERE flow_id IN (");
        for (int i = 0; i < flowIds.size(); i++) {
            updateSql.append("?");
            if (i < flowIds.size() - 1) updateSql.append(",");
        }
        updateSql.append(")");

        int updateCount = 0;
        String updateSqlStr = updateSql.toString();
        try (PreparedStatement ps = connection.prepareStatement(updateSqlStr)) {
            for (int i = 0; i < flowIds.size(); i++) {
                ps.setString(i + 1, flowIds.get(i));
            }
            logSql("skipwfm", updateSqlStr, "flowIds=" + flowIds);
            updateCount = ps.executeUpdate();
            log("skipwfm", "批量更新结果: " + updateCount + " 行 (期望 " + flowIds.size() + ")");
        } catch (SQLException e) {
            log("skipwfm", "❌ 批量更新失败: " + e.getMessage());
            logger.error("Order update failed", e);
            return;
        }

        if (updateCount != flowIds.size()) {
            log("skipwfm", "⚠️ 更新数量不匹配: expected=" + flowIds.size() + ", actual=" + updateCount);
            return;
        }

        log("skipwfm", "订单已标记为异常状态");

        // Step 3: 遍历每个 flowId，查询任务信息并调用 API
        String selectQuery = "SELECT a.FLOW_ID AS flowId, t.ID AS workitemid, " +
                "a.proc_ins_id AS processinstid, a.FLOW_MODEL AS processId, " +
                "t.act_def_unique_id AS activityDefId, t.act_def_id AS activityId, " +
                "a.CURRENT_STATE AS orderstate " +
                "FROM om.T_BPM_FORM_INFO a " +
                "LEFT JOIN om.wf_dai_ban_task t ON a.proc_ins_id = t.PROCESS_ID " +
                "WHERE a.FLOW_ID = ?";

        try (PreparedStatement ps = connection.prepareStatement(selectQuery)) {
            for (String fid : flowIds) {
                ps.setString(1, fid);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String activityId = rs.getString("activityId");
                        if (activityId == null) continue;

                        if (!(activityId.contains("CoWork") ||
                                activityId.contains("FieldWork") ||
                                activityId.contains("COWork") ||
                                activityId.contains("Acti"))) {
                            log("skipwfm", "订单不在 WFM/AM 节点: " + activityId);
                            continue;
                        }

                        String params = "flowId=" + rs.getString("flowId") +
                                "&workitemid=" + rs.getString("workitemid") +
                                "&processinstid=" + rs.getString("processinstid") +
                                "&processId=" + rs.getString("processId") +
                                "&activityDefId=" + rs.getString("activityDefId") +
                                "&activityId=" + rs.getString("activityId") +
                                "&orderstate=" + rs.getString("orderstate") +
                                "&comment=jar";

                        try {
                            log("skipwfm", "调用API: http://10.199.167.160:15201/om/service/om/wotask/goToNext.ilf, flowId=" + fid);
                            String response = HttpsUtils.post(
                                    "http://10.199.167.160:15201/om/service/om/wotask/goToNext.ilf",
                                    params, sessionToken
                            );

                            if (response == null || response.trim().isEmpty()) {
                                log("skipwfm", "⚠️ API 返回空响应, flowId=" + fid);
                            } else {
                                log("skipwfm", "API响应 [flowId=" + fid + "]: " + response);
                            }

                        } catch (Exception httpEx) {
                            log("skipwfm", "❌ API调用失败, flowId=" + fid + ": " + httpEx.getClass().getSimpleName() + " - " + httpEx.getMessage());
                            logger.error("Failed to call API for flowId=" + fid, httpEx);
                            continue;
                        }
                    }
                } catch (SQLException e) {
                    log("skipwfm", "❌ 查询任务信息失败, flowId=" + fid + ": " + e.getMessage());
                    logger.error("Failed to query task info for flowId=" + fid, e);
                    continue;
                }
            }
        } catch (SQLException e) {
            log("skipwfm", "❌ 准备查询语句失败: " + e.getMessage());
            logger.error("Prepare statement failed for task query", e);
        }
        log("skipwfm", "✅ 处理完成");
    }

    public static void updateAMSN(String flowId, String operation, String worksheetAction) {
        // 参数校验
        if (flowId == null || flowId.trim().isEmpty()) {
            logger.warn("updateAMSN: flowId is null or empty");
            return;
        }
        flowId = flowId.trim();

        if (operation == null || operation.trim().isEmpty()) {
            logger.warn("updateAMSN: operation is null or empty");
            return;
        }
        operation = operation.trim();

        if (!"finish".equals(worksheetAction) && !"cancel".equals(worksheetAction)) {
            logger.warn("updateAMSN: invalid worksheetAction '{}', must be 'finish' or 'cancel'", worksheetAction);
            return;
        }

        // 根据 worksheetAction 决定 SOAP 方法名和 Action
        String soapMethodName;
        String soapAction;
        if ("finish".equals(worksheetAction)) {
            soapMethodName = "finishWorkSheet";
            soapAction = "finishWorkSheet";
        } else { // cancel
            soapMethodName = "cancelWorkSheet"; // 注意：SOAP 方法名大小写敏感！
            soapAction = "cancelWorkSheet";
        }

        logger.info("Executing {} for flowId: {}, operation: {}", soapMethodName, flowId, operation);

        String soapUrl = "http://10.199.167.141:15202/imService/services/ResFamilyResourceService";

        // 安全转义 XML 特殊字符（防止注入或格式错误）
        String safeFlowId = escapeXml(flowId);
        String safeOperation = escapeXml(operation);

        // 构建 SOAP 请求体
        String soapRequestBody =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                        "xmlns:ser=\"http://service.ws.family.plugins.inspur.com\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <ser:" + soapMethodName + ">\n" +  // 动态方法名
                        "         <ser:in0>\n" +
                        "         <![CDATA[<xml>\n" +
                        "  <flowId>" + safeFlowId + "</flowId>\n" +
                        "  <opentype>" + safeOperation + "</opentype>\n" +
                        "  <relocate_active_mode></relocate_active_mode>\n" +
                        "  <snNo></snNo>\n" +
                        "  <returnCode>0</returnCode>\n" +
                        "</xml>]]>\n" +
                        "         </ser:in0>\n" +
                        "      </ser:" + soapMethodName + ">\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";

        try {
            String response = SoapClient.sendSoap(soapUrl, soapAction, soapRequestBody);

            if (response != null && !response.startsWith("ERROR")) {
                logger.info("SOAP request succeeded for flowId: {}, operation: {}, action: {}",
                        flowId, operation, worksheetAction);
            } else {
                logger.error("SOAP request failed for flowId: {}, operation: {}, action: {}, response: {}",
                        flowId, operation, worksheetAction, response);
            }
        } catch (Exception e) {
            logger.error("Unexpected error during SOAP call for flowId: {}, operation: {}, action: {}",
                    flowId, operation, worksheetAction, e);
        }
    }

    // 辅助方法：安全转义 XML 字符
    private static String escapeXml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
    public static void updateAMSNold(String flowId, String operation) {
        // 参数校验
        if (flowId == null || flowId.trim().isEmpty()) {
            logger.warn("updateAMSN: flowId is null or empty");
            return;
        }
        flowId = flowId.trim();

        if (operation == null || operation.trim().isEmpty()) {
            logger.warn("updateAMSN: operation is null or empty, defaulting to 'SKIP'");
            operation = operation; // 可根据业务需要设为其他默认值
        }
        operation = operation.trim();

        logger.info("Executing updateAMSN for flowId: {}, operation: {}", flowId, operation);

        // SOAP 服务地址和 Action（请根据实际环境替换）
        String soapUrl = "http://10.199.167.141:15202/imService/services/ResFamilyResourceService"; // ❗替换为真实地址
        String soapAction = "finishWorkSheet"; // 可选，看服务是否需要

        // 构建 SOAP 请求体（内嵌 XML 用 CDATA 包裹）
        String soapRequestBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "xmlns:ser=\"http://service.ws.family.plugins.inspur.com\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <ser:finishWorkSheet>\n" +
                "         <ser:in0>\n" +
                "         <![CDATA[<xml>\n" +
                "  <flowId>" + flowId.replace("&", "&amp;").replace("<", "&lt;") + "</flowId>\n" +
                "  <opentype>" + operation.replace("&", "&amp;").replace("<", "&lt;") + "</opentype>\n" +
                "  <relocate_active_mode></relocate_active_mode>\n" +
                "  <snNo></snNo>\n" +
                "  <returnCode>0</returnCode>\n" +
                "</xml>]]>\n" +
                "         </ser:in0>\n" +
                "      </ser:finishWorkSheet>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";

        try {
            // 调用 SoapClient 发送请求
            String response = SoapClient.sendSoap(soapUrl, soapAction, soapRequestBody);

            // 处理响应
            if (response != null && !response.startsWith("ERROR")) {
                logger.info("SOAP request succeeded for flowId: {}, operation: {}", flowId, operation);
            } else {
                logger.error("SOAP request failed for flowId: {}, operation: {}, response: {}",
                        flowId, operation, response);
            }
        } catch (Exception e) {
            logger.error("Unexpected error during SOAP call for flowId: {}, operation: {}", flowId, operation, e);
        }
    }
    public static void markamorderAsAbnormal(String flowId) throws SQLException {
        log("markamorderAsAbnormal", "开始处理, flowId=" + flowId);
        String sql = "UPDATE om.T_BPM_FORM_INFO p SET p.TASK_STATUS =3 WHERE p.FLOW_ID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, flowId);
            logSql("markamorderAsAbnormal", sql, "flowId=" + flowId);
            int rows = stmt.executeUpdate();
            log("markamorderAsAbnormal", "更新结果: " + rows + " 行");
            if (rows == 0) {
                throw new SQLException("No order found with flow_id: " + flowId);
            }
        }
    }
    // ==================== 核心逻辑：更新状态 + 发送 POST ====================

    /**
     * 主逻辑：根据 flow_id 查询信息，更新 task_status，并调用外部接口
     */
    private static void MSV(String flowId) {
        List<String> flowIds = new ArrayList<>();

        String query = "SELECT flow_id, crm_no FROM T_BPM_FORM_INFO WHERE flow_id = ? ";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, flowId);
            logSql("MSV", query, "flowId=" + flowId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    logger.info("Found: crm_no={}, flow_id={}", rs.getString("crm_no"), rs.getString("flow_id"));
                    flowIds.add(rs.getString("flow_id"));
                }
            }
        } catch (SQLException e) {
            logger.error("Error querying flow_id: {}", flowId, e);
            return;
        }

        if (flowIds.isEmpty()) {
            logger.warn("No active flow found for flow_id: {}", flowId);
            return;
        }

        executeUpdateAndSend(flowIds);
    }

    /**
     * 统一执行更新和发送接口
     */
    private static void executeUpdateAndSend(List<String> flowIds) {
        if (flowIds.isEmpty()) return;

        // 构建 IN 查询语句（防止 SQL 注入：这里 flowId 来自 DB 查询，相对安全）
        StringBuilder inClause = new StringBuilder();
        for (int i = 0; i < flowIds.size(); i++) {
            if (i > 0) inClause.append(",");
            inClause.append("'").append(flowIds.get(i)).append("'");
        }

        String updateSql = "UPDATE T_BPM_FORM_INFO SET task_status = '3' WHERE flow_id IN (" + inClause + ")";
        try (Statement stmt = connection.createStatement()) {
            logSql("executeUpdateAndSend", updateSql, "flowIds=" + flowIds);
            int rows = stmt.executeUpdate(updateSql);
            log("executeUpdateAndSend", "批量更新: " + rows + " 行 (期望 " + flowIds.size() + ")");
            logger.info("Updated {} rows to task_status=3", rows);

            if (rows == flowIds.size()) {
                logger.info("All records updated successfully, sending POST...");
                sendPost(flowIds);
            } else {
                logger.warn("Mismatch: updated {} rows, expected {}", rows, flowIds.size());
            }
        } catch (SQLException e) {
            logger.error("Error during update", e);
        }
    }

    // ==================== 调用外部接口 ====================
    public static void closeWFMComplaintOrder(Connection con, String flowId) throws SQLException {
        log("closeWFMComplaintOrder", "开始处理, flowId=" + flowId);
        // Step 1: 更新主表状态
        String sql1 = "UPDATE ALGERIA_WFM.t_wfm_complaint " +
                "SET PRO_NEXT_ACT_NAME = 'Close', PRO_CUR_ACT_NAME = 'SDM Approval', endtime = SYSDATE " +
                "WHERE GAIATTCODE = ?";
        try (PreparedStatement ps1 = con.prepareStatement(sql1)) {
            ps1.setString(1, flowId);
            logSql("closeWFMComplaintOrder", sql1, "GAIATTCODE=" + flowId);
            int r1 = ps1.executeUpdate();
            log("closeWFMComplaintOrder", "Step1-更新主表: " + r1 + " 行");
        }

        // Step 2: 插入活动记录
        String sql2 = "INSERT INTO ALGERIA_WFM.T_WFM_COMPLAINT_ACTIVITY (" +
                "ID, PRIMARY_KEY, TICKET_STEP, PRO_NEXT_ACT_NAME, PROCESSDEFNAME, HANDLE_TIME) " +
                "SELECT SYS_GUID(), ID, 'Close', 'Close', 'Complaint', SYSDATE " +
                "FROM ALGERIA_WFM.t_wfm_complaint WHERE GAIATTCODE = ?";
        try (PreparedStatement ps2 = con.prepareStatement(sql2)) {
            ps2.setString(1, flowId);
            logSql("closeWFMComplaintOrder", sql2, "GAIATTCODE=" + flowId);
            int r2 = ps2.executeUpdate();
            log("closeWFMComplaintOrder", "Step2-插入活动记录: " + r2 + " 行");
        }

        // Step 3: 删除待办任务
        String sql3 = "DELETE FROM ALGERIA_WFM.WF_DAI_BAN_TASK " +
                "WHERE WODATAID IN (SELECT ID FROM ALGERIA_WFM.t_wfm_complaint WHERE GAIATTCODE = ?)";
        try (PreparedStatement ps3 = con.prepareStatement(sql3)) {
            ps3.setString(1, flowId);
            logSql("closeWFMComplaintOrder", sql3, "GAIATTCODE=" + flowId);
            int r3 = ps3.executeUpdate();
            log("closeWFMComplaintOrder", "Step3-删除待办任务: " + r3 + " 行");
        }

        // Step 4: 删除 WF_ASSIGNMENT
        String sql4 = "DELETE FROM ALGERIA_WFM.WF_ASSIGNMENT " +
                "WHERE ACTIVITY_ID IN (" +
                "  SELECT ACTIVITY_ID FROM ALGERIA_WFM.WF_DAI_BAN_TASK " +
                "  WHERE WODATAID IN (SELECT ID FROM ALGERIA_WFM.T_WFM_complaint WHERE GAIATTCODE = ?)" +
                ")";
        try (PreparedStatement ps4 = con.prepareStatement(sql4)) {
            ps4.setString(1, flowId);
            logSql("closeWFMComplaintOrder", sql4, "GAIATTCODE=" + flowId);
            int r4 = ps4.executeUpdate();
            log("closeWFMComplaintOrder", "Step4-删除WF_ASSIGNMENT: " + r4 + " 行");
        }

        // Step 5: 删除 WF_ACTIVITY
        String sql5 = "DELETE FROM ALGERIA_WFM.WF_ACTIVITY " +
                "WHERE ACTIVITY_ID IN (" +
                "  SELECT ACTIVITY_ID FROM ALGERIA_WFM.WF_DAI_BAN_TASK " +
                "  WHERE WODATAID IN (SELECT ID FROM ALGERIA_WFM.T_WFM_complaint WHERE GAIATTCODE = ?)" +
                ")";
        try (PreparedStatement ps5 = con.prepareStatement(sql5)) {
            ps5.setString(1, flowId);
            logSql("closeWFMComplaintOrder", sql5, "GAIATTCODE=" + flowId);
            int r5 = ps5.executeUpdate();
            log("closeWFMComplaintOrder", "Step5-删除WF_ACTIVITY: " + r5 + " 行");
        }
        log("closeWFMComplaintOrder", "✅ 处理完成");
    }

    /**
     * 向外部系统发送 POST 请求（模拟任务重试）
     */
    private static void sendPost(List<String> flowIds) {
        for (String flowId : flowIds) {
            JSONObject record = new JSONObject();
            String processInstId = null;
            String workItemId = null;

            // 修改后的 SQL：使用 AS 别名，但仍使用 ? 占位符防止 SQL 注入
            String sql = "SELECT " +
                    "a.flow_id AS flowid, " +
                    "b.PROCESSINSTID AS processinstid, " +
                    "a.flow_name AS processdefname, " +
                    "b.ACTIVITYINSTID AS activityinstid, " +
                    "b.WORKITEMID AS workitemid " +
                    "FROM t_bpm_form_info a " +
                    "LEFT JOIN USER_WAITING_TASK b ON a.flow_id = b.flowid " +
                    "WHERE a.flow_id = ?";

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, flowId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        processInstId = rs.getString("processinstid");
                        workItemId = rs.getString("workitemid");

                        // 直接从 ResultSet 取别名字段
                        record.put("flowid", rs.getString("flowid"));
                        record.put("processdefname", rs.getString("processdefname"));
                        record.put("processinstid", processInstId);
                        record.put("activityinstid", rs.getString("activityinstid"));
                        record.put("workitemid", workItemId);

                        // 其他字段设为 NULL
                        record.put("flowno", JSONObject.NULL);
                        record.put("flowtype", JSONObject.NULL);
                        // ... 其他字段
                    } else {
                        logger.warn("No data found for flow_id during sendPost: {}", flowId);
                        continue;
                    }
                }
            } catch (SQLException e) {
                logger.error("Error querying detail for flow_id: {}", flowId, e);
                continue;
            }

            // 构造请求体
            JSONArray recordArray = new JSONArray().put(record);
            String requestBody;
            try {
                requestBody = "flowId=" + flowId +
                        "&recordArray=" + URLEncoder.encode(recordArray.toString(), "UTF-8") +
                        "&processinstid=" + (processInstId != null ? processInstId : "") +
                        "&workitemid=" + (workItemId != null ? workItemId : "");
            } catch (UnsupportedEncodingException e) {
                logger.error("Encoding error", e);
                continue;
            }

            // 发送请求
            String url = "http://10.199.167.160:15201/om/service/om/wotask/reexecute.ilf";
            try {
                log("sendPost", "调用API: " + url + ", flowId=" + flowId);
                String response = HttpsUtils.post(url, requestBody, sessionToken);
                log("sendPost", "API响应: " + response);
            } catch (Exception e) {
                logger.error("Failed to send POST for flow_id: {}", flowId, e);
            }
        }
    }

    // ==================== 工具方法（可选）====================

    /**
     * 初始化数据库连接（示例，应在 ServletContextListener 中调用）
     */
    public static void initConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            // 示例使用 HikariCP 或直接 DriverManager
            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@//host:port/service", "username", "password"
            );
            logger.info("Database connection initialized.");
        }
    }

    /**
     * 关闭连接
     */
    public static void closeConnection(Connection con) {
        if (con != null) {
            try {
                if (!con.isClosed()) {
                    con.close();
                    log("closeConnection", "✅ 数据库连接已关闭");
                }
            } catch (SQLException closeEx) {
                logger.error("❌ Error closing connection", closeEx);
            }
        }
    }

    // ==================== 日志辅助方法 ====================

    /**
     * 格式化当前时间戳
     */
    private static String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 记录操作日志到 catalina.out
     */
    private static void log(String method, String message) {
        System.out.println("[" + now() + "] [Order." + method + "] " + message);
    }

    /**
     * 记录 SQL 执行日志到 catalina.out
     */
    private static void logSql(String method, String sql, String params) {
        System.out.println("[" + now() + "] [Order." + method + "] 📝 执行SQL: " + sql);
        if (params != null && !params.isEmpty()) {
            System.out.println("[" + now() + "] [Order." + method + "]    参数: " + params);
        }
    }
}