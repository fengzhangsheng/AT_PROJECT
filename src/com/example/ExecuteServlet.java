package com.example;

import java.io.OutputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.sql.*;
import java.util.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;
import java.net.URLEncoder;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// 👈 导入目标包
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import static com.example.Order.closeConnection;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
@WebServlet("/pages/ExecuteServlet")
public class ExecuteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(ExecuteServlet.class);
    public ExecuteServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        request.getRequestDispatcher("index.jsp").forward(request, response);

    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        HttpSession session = request.getSession(false);
//        if (session == null || session.getAttribute("loggedIn") == null) {
//            response.sendRedirect("LoginServlet");
//            return;
//        }
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");
        String flowId = request.getParameter("flow_id");
        List<String> flowIdList = new ArrayList<>();
        if (flowId != null) {
            flowId = flowId.trim();
            if (!flowId.isEmpty()) {
                String[] lines = flowId.split("\\r?\\n|\\r");
                for (String line : lines) {
                    String trimmed = line.trim();
                    if (!trimmed.isEmpty() && !flowIdList.contains(trimmed)) {
                        flowIdList.add(trimmed);
                    }
                }
            }
        }
        String jsessionId = request.getParameter("jsessionid");
        try {
            boolean success = false;
            Connection con = null;
            if (action == null || action.trim().isEmpty()) {
                throw new IllegalArgumentException("Missing 'action' parameter.");
            }

            switch (action) {
                // ✅ 新增：处理设置 JSESSIONID
                case "setSession":
                    String trimmedId = jsessionId != null ? jsessionId.trim() : null;
                    if (trimmedId != null && !trimmedId.isEmpty()) {
                        Order.sessionToken = trimmedId;
                        System.out.println("✅ JSESSIONID 已设置: " + Order.sessionToken);
                        System.out.println("<div class='success'>");
                        System.out.println("<h3>🔐 JSESSIONID 设置成功: " + escapeHtml(Order.sessionToken) + "</h3>");
                        System.out.println("</div>");
                        success = true;
                    } else {
                        throw new IllegalArgumentException("JSESSIONID 不能为空");
                    }
                    break;

                case "updateOM":
                    try {
                        Class.forName("com.huawei.gauss.jdbc.ZenithDriver");
                        con = DriverManager.getConnection("jdbc:zenith:@10.199.167.185:1888", "om", "om#_123OSS");
                        Order.connection = con;
                        // 执行业务逻辑
                        for (String flowIdl : flowIdList) {
                            flowId = flowIdl.trim();
                            if (isValidParam(flowId)) {
                                try {
                                    Order.updateOM(flowId);
                                    success = true;
                                } catch (Exception e) {
                                    // 记录错误信息但继续处理下一个 flowId
                                    logger.error("Failed to process Flow ID: " + flowId, e);
                                }
                            } else {
                                logger.warn("Invalid Flow ID skipped: {}", flowId);
                            }
                        }
                        if (!success) {
                            throw new IllegalArgumentException("No valid Flow ID was processed.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException("Error during updateOM action: " + e.getMessage(), e);
                    }finally {
                        closeConnection(con); // ✅ 使用工具方法关闭
                    }
                    break;

                case "updateIM":
                    try {
                        Class.forName("com.huawei.gauss.jdbc.ZenithDriver");
                        con = DriverManager.getConnection("jdbc:zenith:@10.199.167.181:1888", "im_rmw", "im_rmw#_123OSS");
                        Order.connection = con;
                        // 执行业务逻辑
                        for (String flowIdl : flowIdList) {
                            flowId = flowIdl.trim();
                            if (isValidParam(flowId)) {
                                try {
                                    Order.updateIM(flowId);
                                    success = true;
                                } catch (Exception e) {
                                    // 记录错误信息但继续处理下一个 flowId
                                    logger.error("Failed to process Flow ID: " + flowId, e);
                                }
                            } else {
                                logger.warn("Invalid Flow ID skipped: {}", flowId);
                            }
                        }
                        if (!success) {
                            throw new IllegalArgumentException("No valid Flow ID was processed.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException("Error during updateIM action: " + e.getMessage(), e);
                    }finally {
                        closeConnection(con); // ✅ 使用工具方法关闭
                    }
                    break;

                case "updateAM":
                    try {
                        Class.forName("com.huawei.gauss.jdbc.ZenithDriver");
                        con = DriverManager.getConnection("jdbc:zenith:@10.199.167.185:1888", "am_ces", "am#_123OSS");
                        Order.connection = con;
                        // 执行业务逻辑
                        for (String flowIdl : flowIdList) {
                            flowId = flowIdl.trim();
                            if (isValidParam(flowId)) {
                                try {
                                    Order.updateAM(flowId);
                                    success = true;
                                } catch (Exception e) {
                                    // 记录错误信息但继续处理下一个 flowId
                                    logger.error("Failed to process Flow ID: " + flowId, e);
                                }
                            } else {
                                logger.warn("Invalid Flow ID skipped: {}", flowId);
                            }
                        }
                        if (!success) {
                            throw new IllegalArgumentException("No valid Flow ID was processed.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException("Error during updateAM action: " + e.getMessage(), e);
                    }finally {
                        closeConnection(con); // ✅ 使用工具方法关闭
                    }
                    break;
                case "skipwfm":
                    try {
                        Class.forName("com.huawei.gauss.jdbc.ZenithDriver");
                        con = DriverManager.getConnection("jdbc:zenith:@10.199.167.185:1888", "om", "om#_123OSS");
                        Order.connection = con;
                        // 执行业务逻辑
                        for (String flowIdl : flowIdList) {
                            flowId = flowIdl.trim();
                            if (isValidParam(flowId)) {
                                try {
                                    Order.skipwfm(flowId);
                                    success = true;
                                } catch (Exception e) {
                                    // 记录错误信息但继续处理下一个 flowId
                                    logger.error("Failed to process Flow ID: " + flowId, e);
                                }
                            } else {
                                logger.warn("Invalid Flow ID skipped: {}", flowId);
                            }
                        }
                        if (!success) {
                            throw new IllegalArgumentException("No valid Flow ID was processed.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException("Error during skipwfm action: " + e.getMessage(), e);
                    }finally {
                        closeConnection(con); // ✅ 使用工具方法关闭
                    }
                    break;

                case "changeimworksheet":
                    String operation = request.getParameter("operation");           // e.g. "install"
                    String worksheetAction = request.getParameter("worksheet_action"); // "finish" 或 "cancel"

                    if (operation == null || operation.trim().isEmpty()) {
                        operation = "install";
                    } else {
                        operation = operation.trim();
                    }
                    if (!"finish".equals(worksheetAction) && !"cancel".equals(worksheetAction)) {
                        throw new IllegalArgumentException("Worksheet action must be 'finish' or 'cancel'.");
                    }
                    for (String flowIdk : flowIdList) {
                        String flowIdnw = flowIdk.trim();
                        if (isValidParam(flowIdnw)) {
                            try {
                                // 传入三个参数：flowId, operation, worksheetAction
                                Order.updateAMSN(flowIdnw, operation, worksheetAction);
                                success = true;
                                logger.info("Successfully processed Flow ID: {}, operation: {}, action: {}",
                                        flowId, operation, worksheetAction);
                            } catch (Exception e) {
                                logger.error("Failed to process Flow ID: " + flowId, e);
                            }
                        } else {
                            logger.warn("Invalid Flow ID skipped: {}", flowId);
                        }
                    }

                    if (!success) {
                        throw new IllegalArgumentException("No valid Flow ID was processed.");
                    }
                    break;
                case "forceCloseSubFlow":
                    String flowIdsText3 = request.getParameter("flow_id");
                    // 【新增】获取用户选择的操作类型
                    String statusAction = request.getParameter("status_action");

                    if (flowIdsText3 == null || flowIdsText3.trim().isEmpty()) {
                        throw new IllegalArgumentException("No Flow ID provided.");
                    }

                    // 【新增】验证操作类型是否合法，默认为 fail 如果未提供或非法，或者也可以直接抛错，这里选择默认安全策略或严格策略
                    // 为了严谨，如果既不是 success 也不是 fail，我们可以默认按 fail 处理，或者抛出异常。
                    // 这里假设前端已做校验，后端做兜底：如果不是 success，则视为 fail。
                    boolean isSuccessMode = "changetosuccess".equalsIgnoreCase(statusAction);

                    logger.info("Received forceCloseSubFlow request. Action Type: {} (Success Mode: {})", statusAction, isSuccessMode);

                    // 1. 解析并验证输入 ID
                    String[] flowIdList3 = flowIdsText3.split("\\r?\\n|,|;");
                    List<Long> validInputIds = new ArrayList<>();

                    for (String id : flowIdList3) {
                        String trimmedIdStr = id.trim();
                        if (!trimmedIdStr.isEmpty()) {
                            try {
                                long parsedId = Long.parseLong(trimmedIdStr);
                                if (isValidParam(parsedId)) {
                                    validInputIds.add(parsedId);
                                } else {
                                    logger.warn("Invalid Flow ID skipped: {}", parsedId);
                                }
                            } catch (NumberFormatException e) {
                                logger.warn("ID is not a valid long integer: {}", trimmedIdStr);
                            }
                        }
                    }

                    if (validInputIds.isEmpty()) {
                        throw new IllegalArgumentException("No valid Flow ID was processed.");
                    }

                    Connection conn = null;
                    PreparedStatement psUpdateCurrent = null;
                    PreparedStatement psUpdateCmd = null;

                    final int WAIT_TIME_MS = 5000;
                    final int MAX_RETRY_COUNT = 2;

                    try {
                        Class.forName("com.huawei.gauss.jdbc.ZenithDriver");
                        conn = DriverManager.getConnection("jdbc:zenith:@10.199.167.185:1888", "am_ces", "am#_123OSS");
                        conn.setAutoCommit(false);

                        // ================= 第一步：根据操作类型动态构建 SQL =================

                        String sql1;
                        String sql2;
                        String logStep1Msg;

                        if (isSuccessMode) {
                            // --- 模式：Change to Success ---
                            // FLOW_STATE='3' (通常代表完成/成功), RESULT='1' (成功), RUN_STATUS='3' (已完成)
                            sql1 = "UPDATE am_ces.t_am_sub_flow_info " +
                                    "SET FLOW_STATE='3', RESULT='1', RUN_STATUS='3', COMPLETE_TIME=sysdate " +
                                    "WHERE ID IN (" + String.join(",", Collections.nCopies(validInputIds.size(), "?")) + ")";

                            // CMD_STATE='1' (通常代表完成), RESULT='1' (成功)
                            sql2 = "UPDATE am_ces.T_AM_CMD_INSTANCE " +
                                    "SET CMD_STATE='1', RESULT='1', EXECUTE_TIME=sysdate " +
                                    "WHERE SUB_ORDER_ID IN (" + String.join(",", Collections.nCopies(validInputIds.size(), "?")) + ")";

                            logStep1Msg = "Marked {} sub-flows as SUCCESS.";
                        } else {
                            // --- 模式：Change to Fail (默认或显式选择) ---
                            // FLOW_STATE='4' (通常代表异常/失败), RESULT='2' (失败), RUN_STATUS='3' (已完成/停止)
                            sql1 = "UPDATE am_ces.t_am_sub_flow_info " +
                                    "SET FLOW_STATE='4', RESULT='2', RUN_STATUS='3', COMPLETE_TIME=sysdate " +
                                    "WHERE ID IN (" + String.join(",", Collections.nCopies(validInputIds.size(), "?")) + ")";

                            // CMD_STATE='1' (完成), RESULT='2' (失败)
                            sql2 = "UPDATE am_ces.T_AM_CMD_INSTANCE " +
                                    "SET CMD_STATE='1', RESULT='2', EXECUTE_TIME=sysdate " +
                                    "WHERE SUB_ORDER_ID IN (" + String.join(",", Collections.nCopies(validInputIds.size(), "?")) + ")";

                            logStep1Msg = "Marked {} sub-flows as EXCEPTION/FAIL.";
                        }

                        // 执行更新
                        String placeholders = String.join(",", Collections.nCopies(validInputIds.size(), "?"));
                        // 注意：上面的 sql1/sql2 字符串拼接中已经使用了 placeholders 逻辑，
                        // 但为了代码清晰，我们重新生成一次带占位符的完整 SQL 或者复用上面的逻辑。
                        // 上面的写法中，sql1 和 sql2 定义时直接嵌入了 placeholders 字符串，这是安全的因为 ID 数量已知。

                        // 重新构建带 ? 的 SQL 以确保逻辑严密 (上面的定义中直接用了字符串拼接，这里直接使用定义好的 sql1, sql2)
                        // 实际上上面的定义已经是完整的 SQL 字符串了，只需要 prepareStatement 即可。

                        psUpdateCurrent = conn.prepareStatement(sql1);
                        for (int i = 0; i < validInputIds.size(); i++) {
                            psUpdateCurrent.setLong(i + 1, validInputIds.get(i));
                        }
                        int count1 = psUpdateCurrent.executeUpdate();
                        logger.info("Step 1: " + logStep1Msg, count1);

                        psUpdateCmd = conn.prepareStatement(sql2);
                        for (int i = 0; i < validInputIds.size(); i++) {
                            psUpdateCmd.setLong(i + 1, validInputIds.get(i));
                        }
                        int count2 = psUpdateCmd.executeUpdate();
                        logger.info("Step 1 (Cmd): Updated {} associated commands.", count2);

                        // ================= 第二步：按 FLOW_ID 分组，并计算每个组的 Max Cut Seq =================
                        // 注意：如果是“强制成功”，通常意味着我们认为这些节点已经成功跑完了，
                        // 后续的链式恢复逻辑（Step 3）是否还需要执行？
                        // 业务逻辑判断：
                        // 1. 如果是 changetofail：通常是为了修复卡死的流程，将其置为失败，然后触发重试/恢复逻辑（即后面的 Step 2 & 3）。
                        // 2. 如果是 changetosuccess：通常是为了跳过某些步骤直接标记成功。
                        //    - 如果标记成功后，还需要继续跑后面的流程吗？
                        //    - 原代码逻辑是：不管第一步改成什么状态，都会尝试找下一个节点并激活。
                        //    - 如果用户选的是“成功”，可能期望流程继续往下走。
                        //    - 如果用户选的是“失败”，可能期望流程从失败点重试或继续。
                        // 鉴于原代码逻辑是通用的“恢复/跳过”逻辑，我们保留 Step 2 和 Step 3 的执行，
                        // 因为它们依赖于 DB 中当前的状态来决定下一步。只要 Step 1 更新了状态，Step 3 的查找逻辑 (FLOW_SEQ > maxCutSeq) 依然有效。

                        class FlowRecoveryContext {
                            List<Long> inputIds = new ArrayList<>();
                            Long maxCutSeq = null;
                            boolean canResume = false;
                        }

                        Map<Long, FlowRecoveryContext> recoveryPlan = new HashMap<>();

                        String sqlGetInfo = "SELECT ID, FLOW_ID, FLOW_SEQ FROM am_ces.t_am_sub_flow_info WHERE ID = ?";
                        try (PreparedStatement psMap = conn.prepareStatement(sqlGetInfo)) {
                            for (Long id : validInputIds) {
                                psMap.setLong(1, id);
                                try (ResultSet rs = psMap.executeQuery()) {
                                    if (rs.next()) {
                                        long fId = rs.getLong("FLOW_ID");
                                        long seq = rs.getLong("FLOW_SEQ");

                                        FlowRecoveryContext ctx = recoveryPlan.computeIfAbsent(fId, k -> new FlowRecoveryContext());
                                        ctx.inputIds.add(id);

                                        if (ctx.maxCutSeq == null || seq > ctx.maxCutSeq) {
                                            ctx.maxCutSeq = seq;
                                        }
                                    } else {
                                        logger.warn("SubFlow ID {} not found in DB.", id);
                                    }
                                }
                            }
                        }

                        if (recoveryPlan.isEmpty()) {
                            throw new IllegalArgumentException("No valid Flow associations found.");
                        }

                        logger.info("Processing {} distinct Main Flows with calculated cut points.", recoveryPlan.size());

                        String sqlFindNext = "SELECT ID, FLOW_SEQ FROM (" +
                                "SELECT ID, FLOW_SEQ FROM am_ces.t_am_sub_flow_info " +
                                "WHERE FLOW_ID = ? AND FLOW_SEQ > ? " +
                                "ORDER BY FLOW_SEQ ASC) WHERE ROWNUM = 1";

                        String sqlActivate = "UPDATE am_ces.t_am_sub_flow_info " +
                                "SET RUN_STATUS = '1', FLOW_STATE = '1' " +
                                "WHERE ID = ?";

                        String sqlCheck = "SELECT RESULT, RUN_STATUS FROM am_ces.t_am_sub_flow_info WHERE ID = ?";

                        String sqlUpdateMainFlow = "UPDATE am_ces.t_am_flow_info " +
                                "SET FLOW_STATE='3', RESULT='1', RUN_STATUS=3 " +
                                "WHERE FLOW_ID = ?";

                        // ================= 第三步：遍历每个主流程执行链式激活 =================
                        for (Map.Entry<Long, FlowRecoveryContext> entry : recoveryPlan.entrySet()) {
                            long currentFlowId = entry.getKey();
                            FlowRecoveryContext ctx = entry.getValue();

                            if (ctx.maxCutSeq == null) {
                                logger.warn("Flow {} has no valid cut sequence. Skipping.", currentFlowId);
                                continue;
                            }

                            logger.info("Starting chain recovery for FlowID: {}. Cut off at SEQ: {}. Involved IDs: {}",
                                    currentFlowId, ctx.maxCutSeq, ctx.inputIds);

                            long currentSeq = ctx.maxCutSeq;
                            boolean lastTaskSuccess = false;
                            Long waitingTaskId = null;
                            int retryCount = 0;

                            while (true) {
                                if (waitingTaskId == null) {
                                    try (PreparedStatement psNext = conn.prepareStatement(sqlFindNext)) {
                                        psNext.setLong(1, currentFlowId);
                                        psNext.setLong(2, currentSeq);

                                        ResultSet rsNext = psNext.executeQuery();

                                        if (rsNext.next()) {
                                            waitingTaskId = rsNext.getLong("ID");
                                            long nextSeq = rsNext.getLong("FLOW_SEQ");

                                            try (PreparedStatement psAct = conn.prepareStatement(sqlActivate)) {
                                                psAct.setLong(1, waitingTaskId);
                                                int updated = psAct.executeUpdate();
                                                if (updated > 0) {
                                                    logger.info("Activated next task: ID={}, SEQ={}. Waiting for completion...", waitingTaskId, nextSeq);
                                                    currentSeq = nextSeq;
                                                    retryCount = 0;
                                                    lastTaskSuccess = false;
                                                } else {
                                                    logger.error("Failed to activate task {}", waitingTaskId);
                                                    break;
                                                }
                                            }
                                        } else {
                                            logger.info("End of chain reached for Flow {}. No tasks found after SEQ {}.", currentFlowId, currentSeq);
                                            try (PreparedStatement psMain = conn.prepareStatement(sqlUpdateMainFlow)) {
                                                psMain.setLong(1, currentFlowId);
                                                psMain.executeUpdate();
                                                logger.info("Main Flow {} updated to COMPLETED.", currentFlowId);
                                            }
                                            break;
                                        }
                                    } catch (SQLException e) {
                                        logger.error("Error finding next task for Flow {}", currentFlowId, e);
                                        break;
                                    }
                                }

                                if (waitingTaskId != null) {
                                    try {
                                        Thread.sleep(WAIT_TIME_MS);
                                    } catch (InterruptedException e) {
                                        Thread.currentThread().interrupt();
                                        logger.error("Wait interrupted", e);
                                        break;
                                    }

                                    boolean taskSuccess = false;
                                    try (PreparedStatement psChk = conn.prepareStatement(sqlCheck)) {
                                        psChk.setLong(1, waitingTaskId);
                                        ResultSet rsChk = psChk.executeQuery();
                                        if (rsChk.next()) {
                                            int res = rsChk.getInt("RESULT");
                                            int status = rsChk.getInt("RUN_STATUS");
                                            if (res == 1 && status == 3) {
                                                taskSuccess = true;
                                            }
                                        }
                                    }

                                    if (taskSuccess) {
                                        logger.info("Task {} completed successfully. Proceeding to next...", waitingTaskId);
                                        waitingTaskId = null;
                                        lastTaskSuccess = true;
                                    } else {
                                        retryCount++;
                                        if (retryCount >= MAX_RETRY_COUNT) {
                                            logger.error("Timeout! Task {} did not complete after {} retries. Chain broken for Flow {}.",
                                                    waitingTaskId, MAX_RETRY_COUNT, currentFlowId);
                                            break;
                                        } else {
                                            logger.info("Task {} not yet done (Retry {}/{}). Waiting...", waitingTaskId, retryCount, MAX_RETRY_COUNT);
                                        }
                                    }
                                }
                            }
                        }

                        conn.commit();
                        logger.info("Force close and chain processing finished successfully for all flows. Mode: {}", statusAction);

                    } catch (Exception e) {
                        if (conn != null) {
                            try { conn.rollback(); } catch (SQLException ex) { logger.error("Rollback failed", ex); }
                        }
                        logger.error("Process failed", e);
                        throw e;
                    } finally {
                        if (psUpdateCurrent != null) try { psUpdateCurrent.close(); } catch (SQLException e) {}
                        if (psUpdateCmd != null) try { psUpdateCmd.close(); } catch (SQLException e) {}
                        closeConnection(conn);
                    }

                    break;
                case "updateRegionByServNo":

                    String servNoText = request.getParameter("serv_no");

                    if (servNoText == null || servNoText.trim().isEmpty()) {
                        throw new IllegalArgumentException("No Serv No provided.");
                    }

                    String[] servArray = servNoText.split("\\r?\\n");
                    List<String> validServNos = new ArrayList<>();

                    for (String s : servArray) {
                        String trimmed = s.trim();
                        if (isValidParam(trimmed)) {
                            validServNos.add(trimmed);
                        }
                    }

                    if (validServNos.isEmpty()) {
                        throw new IllegalArgumentException("No valid Serv No found.");
                    }

                    Connection conn2 = null;
                    PreparedStatement psInsert = null;
                    PreparedStatement psMerge = null;

                    try {
                        conn2 = DriverManager.getConnection("jdbc:zenith:@10.199.167.183:1888", "AT_SDM", "sdm#_123OSS");
                        conn2.setAutoCommit(false);

                        // 1️⃣ 清空中间表
                        Statement st = conn2.createStatement();
                        st.executeUpdate("TRUNCATE TABLE AT_SDM.mid_repair_region");

                        // 2️⃣ 构造 IN (?, ?, ?)
                        String placeholders = String.join(",",
                                Collections.nCopies(validServNos.size(), "?"));

                        String insertSql =
                                "INSERT INTO AT_SDM.mid_repair_region " +
                                        "SELECT DISTINCT s.serv_nbr AS nd, " +
                                        "       rr_b.uuid AS Region, " +
                                        "       rr_b.name AS CMP " +
                                        "FROM im_rmw.im_home_service@SDM_TO_IM s " +
                                        "JOIN im_rmw.im_region@SDM_TO_IM r " +
                                        "  ON s.region = r.uuid " +
                                        "JOIN im_rmw.im_region@SDM_TO_IM rr_b " +
                                        "  ON r.related_b_region = rr_b.uuid " +
                                        "WHERE s.serv_nbr IN (" + placeholders + ")";

                        psInsert = conn2.prepareStatement(insertSql);

                        for (int i = 0; i < validServNos.size(); i++) {
                            psInsert.setString(i + 1, validServNos.get(i));
                        }

                        int inserted = psInsert.executeUpdate();

                        // 3️⃣ MERGE
                        String mergeSql =
                                "MERGE INTO at_sdm.bt_complainttt T " +
                                        "USING AT_SDM.mid_repair_region S " +
                                        "ON (T.column_8 = S.nd) " +
                                        "WHEN MATCHED THEN " +
                                        " UPDATE SET " +
                                        "   T.column_43 = S.Region, " +
                                        "   T.column_44 = S.CMP";

                        psMerge = conn2.prepareStatement(mergeSql);
                        int merged = psMerge.executeUpdate();

                        conn2.commit();

                        logger.info("Region update completed. Inserted: {}, Merged: {}",
                                inserted, merged);

                        success = true;

                    } catch (Exception e) {

                        if (conn2 != null) conn2.rollback();
                        logger.error("Update region failed", e);
                        throw e;

                    } finally {

                        if (psInsert != null) psInsert.close();
                        if (psMerge != null) psMerge.close();
                        if (conn2 != null) conn2.close();
                    }

                    break;
                case "updateExecuteCmd":
                    try {
                        Class.forName("com.huawei.gauss.jdbc.ZenithDriver");
                        con = DriverManager.getConnection("jdbc:zenith:@10.199.167.185:1888", "am_ces", "am#_123OSS");
                        Order.connection = con;

                        // 获取参数
//                        String flowId = request.getParameter("flow_id");
                        String oldVal = request.getParameter("old_value");
                        String newVal = request.getParameter("new_value");

                        // 参数校验
                        if (flowId == null || oldVal == null || newVal == null ||
                                flowId.trim().isEmpty() || oldVal.trim().isEmpty() || newVal.trim().isEmpty()) {
                            throw new IllegalArgumentException("All parameters (flow_id, old_value, new_value) are required.");
                        }

                        flowId = flowId.trim();
                        oldVal = oldVal.trim();
                        newVal = newVal.trim();

                        logger.info("Received updateExecuteCmd request: flow_id={}, replacing '{}' with '{}'", flowId, oldVal, newVal);

                        // 执行更新
                        try {
                            Order.updatesn(flowId, oldVal, newVal);
                            success = true;
                        } catch (Exception e) {
                            logger.error("Failed to update execute_cmd for flow_id={}: '{}' → '{}'", flowId, oldVal, newVal, e);
                            throw e;
                        }

                        if (!success) {
                            logger.warn("No rows were updated for flow_id={}, old_value='{}'", flowId, oldVal);
                            // 可选：抛出异常或继续
                        }

                    } catch (Exception e) {
                        logger.error("Error during updateExecuteCmd action", e);
                        throw new RuntimeException("Update failed: " + e.getMessage(), e);
                    }finally {
                        closeConnection(con); // ✅ 使用工具方法关闭
                    }
                    break;

                case "completeAMOrder":
                    try {
                        // 加载数据库驱动
                        Class.forName("com.huawei.gauss.jdbc.ZenithDriver");

                        // 建立数据库连接
                        con = DriverManager.getConnection("jdbc:zenith:@10.199.167.185:1888", "am_ces", "am#_123OSS");
                        Order.connection = con;  // 设置静态连接

                        // 获取 Flow ID 列表（多行）
                        String flowIdText = request.getParameter("flow_id");
                        if (flowIdText == null || flowIdText.trim().isEmpty()) {
                            throw new IllegalArgumentException("Flow ID list is required.");
                        }

                        String[] flowIdLines = flowIdText.split("\\r?\\n");


                        for (String line : flowIdLines) {
                            String flowIdl = line.trim();
                            if (flowIdl.isEmpty()) continue;

                            if (!isValidParam(flowIdl)) {
                                logger.warn("Invalid Flow ID skipped: {}", flowIdl);
                                continue;
                            }

                            try {
                                // ✅ 调用统一方法完成 AM 工单（默认视为成功）
                                Order.finishamorder(flowIdl);
                                success = true;
                                logger.info("✅ AM work order completed successfully: {}", flowIdl);
                            } catch (Exception e) {
                                logger.error("❌ Failed to complete AM order for flow_id: {}", flowIdl, e);
                            }
                        }

                        if (!success) {
                            throw new RuntimeException("No valid AM orders were completed.");
                        }

                    } catch (Exception e) {
                        logger.error("Error during completeAMOrder", e);
                        throw new RuntimeException("Failed to complete AM orders: " + e.getMessage(), e);
                    }finally {
                        closeConnection(con); // ✅ 使用工具方法关闭
                    }
                    break;
                case "markAMOrderAbnormal":
                    try {
                        String flowIdText = request.getParameter("flow_id");
                        String orderType = request.getParameter("order_type");
                        String targetStatus = request.getParameter("target_status");

                        if (flowIdText == null || flowIdText.trim().isEmpty()) {
                            throw new IllegalArgumentException("Flow ID list is required.");
                        }
                        if (!"OM".equals(orderType) && !"IM".equals(orderType)) {
                            throw new IllegalArgumentException("Order type must be 'OM' or 'IM'.");
                        }

                        Set<String> validFlowIds = new LinkedHashSet<>();
                        for (String line : flowIdText.split("\\r?\\n")) {
                            String id = line.trim();
                            if (!id.isEmpty() && isValidParam(id)) {
                                validFlowIds.add(id);
                            } else if (!id.isEmpty()) {
                                logger.warn("Skipped invalid Flow ID: {}", id);
                            }
                        }

                        if (validFlowIds.isEmpty()) {
                            throw new IllegalArgumentException("No valid Flow IDs provided.");
                        }

                        Class.forName("com.huawei.gauss.jdbc.ZenithDriver");

                        // 👇 关键：根据 orderType 手动给全局 con 赋值（不使用 try-with-resources 包裹 Connection）
                        if ("OM".equals(orderType)) {
                            con = DriverManager.getConnection("jdbc:zenith:@10.199.167.185:1888", "om", "om#_123OSS");
                        } else {
                            con = DriverManager.getConnection("jdbc:zenith:@10.199.167.181:1888", "im_rmw", "im_rmw#_123OSS");
                        }

                        String sql;
                        if ("OM".equals(orderType)) {
                            String placeholders = String.join(",", Collections.nCopies(validFlowIds.size(), "?"));
                            sql = "UPDATE om.T_BPM_FORM_INFO SET TASK_STATUS = 3 WHERE FLOW_ID IN (" + placeholders + ")";
                        } else {
                            if (!"success".equals(targetStatus)
                                    && !"failed".equals(targetStatus)
                                    && !"uninstallcomplete".equals(targetStatus)) {
                                throw new IllegalArgumentException(
                                        "For IM orders, target_status must be 'success', 'failed' or 'uninstallcomplete'.");
                            }

                            String placeholders = String.join(",", Collections.nCopies(validFlowIds.size(), "?"));

                            if ("uninstallcomplete".equals(targetStatus)) {
                                sql = "UPDATE IM_RMW.IM_HOME_SERVICE SET FINISH_STATUS = 15 " +
                                        "WHERE STATEFLAG = 0 AND FLOW_ID IN (" + placeholders + ")";
                            } else {
                                sql = "UPDATE IM_RMW.IM_HOME_SERVICE SET FINISH_STATUS = ? " +
                                        "WHERE FINISH_STATUS = ? AND STATEFLAG = 0 AND FLOW_ID IN (" + placeholders + ")";
                            }
                        }

                        // ✅ 只对 PreparedStatement 使用 try-with-resources
                        try (PreparedStatement ps = con.prepareStatement(sql)) {
                            int index = 1;

                            if ("IM".equals(orderType)
                                    && !"uninstallcomplete".equals(targetStatus)) {

                                ps.setInt(index++, "failed".equals(targetStatus) ? 12 : 11); // 新状态
                                ps.setInt(index++, "failed".equals(targetStatus) ? 11 : 12); // 原状态
                            }

                            for (String flowIdnew : validFlowIds) {
                                ps.setString(index++, flowIdnew);
                            }

                            int rowsAffected = ps.executeUpdate();

                            logger.info(
                                    "✅ Batch update completed for {} orders. Target status: {}, affected rows: {}",
                                    orderType, targetStatus, rowsAffected);

                            if (rowsAffected == 0) {
                                logger.warn(
                                        "⚠️ No rows were updated. Orders may not exist, already in target status, or STATEFLAG ≠ 0.");
                            }
                        } // ps 自动关闭

                    } catch (ClassNotFoundException e) {
                        logger.error("Database driver not found", e);
                        throw new RuntimeException("Database driver missing", e);
                    } catch (SQLException e) {
                        logger.error("Database error during batch update", e);
                        throw new RuntimeException("Database operation failed: " + e.getMessage(), e);
                    } catch (Exception e) {
                        logger.error("Unexpected error in markAMOrderAbnormal", e);
                        throw new RuntimeException("Request processing failed", e);
                    } finally {
                        // 注意：如果你希望外部统一关闭 con，这里不要 close！
                        // 否则，如果 con 是在此方法内创建的，建议在这里关闭：
                        closeConnection(con);
                    }
                    break;
                case "changeUserPwd": {

                    String systemType = request.getParameter("system_type");
                    if (systemType == null || systemType.trim().isEmpty()) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "System type is required");
                        return;
                    }

                    systemType = systemType.trim().toUpperCase();

                    String jdbcUrl;
                    String dbUser;
                    String dbPassword;
                    String tableName;
                    String userStatus;
                    String whereColumn;
                    String statusColumn;

                    switch (systemType) {

                        case "WFM":
                            jdbcUrl = "jdbc:zenith:@10.199.167.212:1888";
                            dbUser = "algeria_wfm";
                            dbPassword = "wfm#_123OSS";
                            tableName = "ALGERIA_WFM.pub_users";
                            userStatus = "11";
                            whereColumn = "USER_ID";
                            statusColumn = "ACCOUNT_STATUS";
                            break;

                        case "OM":
                            jdbcUrl = "jdbc:zenith:@10.199.167.185:1888";
                            dbUser = "om";
                            dbPassword = "om#_123OSS";
                            tableName = "om.pub_users";
                            userStatus = "11";
                            whereColumn = "USER_ID";
                            statusColumn = "ACCOUNT_STATUS";
                            break;

                        case "AM":
                            jdbcUrl = "jdbc:zenith:@10.199.167.185:1888";
                            dbUser = "am_ces";
                            dbPassword = "am#_123OSS";
                            tableName = "am_ces.SYS_USERINFO";
                            userStatus = "1";
                            whereColumn = "USER_ACCOUNT";
                            statusColumn = "STATUS";
                            break;

                        case "SDM":
                            jdbcUrl = "jdbc:zenith:@10.199.167.183:1888";
                            dbUser = "AT_SDM";
                            dbPassword = "sdm#_123OSS";
                            tableName = "AT_SDM.pub_users";
                            userStatus = "11";
                            whereColumn = "USER_ID";
                            statusColumn = "ACCOUNT_STATUS";
                            break;

                        case "IM":
                            jdbcUrl = "jdbc:zenith:@10.199.167.181:1888";
                            dbUser = "im_platform";
                            dbPassword = "im_platform#_123OSS";
                            tableName = "im_platform.SYS_USERINFO";
                            userStatus = "1";
                            whereColumn = "USER_ACCOUNT";
                            statusColumn = "STATUS";
                            break;

                        default:
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unsupported system type: " + systemType);
                            return;
                    }

                    executePasswordUpdate(request, jdbcUrl, dbUser, dbPassword,
                            tableName, userStatus, whereColumn, statusColumn, systemType);

                    success = true;
                    break;
                }
                case "closeWFMOrder":
                    try {
                        // 加载数据库驱动
                        Class.forName("com.huawei.gauss.jdbc.ZenithDriver");

                        // 建立数据库连接（开启自动提交为 false，以便手动控制事务）
                        con = DriverManager.getConnection("jdbc:zenith:@10.199.167.212:1888", "algeria_wfm", "wfm#_123OSS");
                        con.setAutoCommit(false); // 👈 关键：开启事务
//                        Order.connection = con;   // 如果其他方法依赖此静态连接

                        // 获取 Flow ID 列表（注意：前端 textarea 的 name 是 flow_id，保持一致）
                        String flowIdText = request.getParameter("flow_id");
                        if (flowIdText == null || flowIdText.trim().isEmpty()) {
                            throw new IllegalArgumentException("Flow ID list is required.");
                        }

                        String[] flowIdLines = flowIdText.split("\\r?\\n");
                        int processedCount = 0;

                        for (String line : flowIdLines) {
                            String flowIdwfc = line.trim();
                            if (flowId.isEmpty()) continue;

                            if (!isValidParam(flowIdwfc)) {
                                logger.warn("Invalid Flow ID skipped: {}", flowId);
                                continue;
                            }

                            try {
                                Order.closeWFMComplaintOrder(con, flowIdwfc); // ✅ 封装的核心方法
                                processedCount++;
                                logger.info("✅ Closed WFM complaint order: {}", flowIdwfc);
                            } catch (Exception e) {
                                logger.error("❌ Failed to close WFM order for flow_id: {}", flowIdwfc, e);
                                con.rollback(); // 回滚整个批次（可选：也可只跳过当前）
                                throw new RuntimeException("Failed at flow_id: " + flowIdwfc, e);
                            }
                        }

                        if (processedCount == 0) {
                            throw new RuntimeException("No valid WFM orders were closed.");
                        }

                        con.commit(); // 👈 提交事务
                        success = true;
                        logger.info("✅ Successfully closed {} WFM orders.", processedCount);

                    } catch (Exception e) {
                        logger.error("Error during closeWFMOrder", e);
                        if (con != null) {
                            try {
                                con.rollback(); // 确保出错时回滚
                            } catch (SQLException ex) {
                                logger.warn("Rollback failed", ex);
                            }
                        }
                        throw new RuntimeException("Failed to close WFM orders: " + e.getMessage(), e);
                    } finally {
                        if (con != null) {
                            try {
                                con.setAutoCommit(true); // 恢复默认
                            } catch (SQLException ignored) {}
                            closeConnection(con);
                        }
                    }
                    break;
                case "updateWFMRegionBatch":
                    try {
                        Class.forName("com.huawei.gauss.jdbc.ZenithDriver");
                        con = DriverManager.getConnection(
                                "jdbc:zenith:@10.199.167.212:1888",
                                "algeria_wfm",
                                "wfm#_123OSS");

                        String flowIdsText = request.getParameter("flow_id");
                        String oldRegion = request.getParameter("old_region");
                        String newRegion = request.getParameter("new_region");

                        // 参数校验
                        if (flowIdsText == null || flowIdsText.trim().isEmpty()
                                || oldRegion == null || oldRegion.trim().isEmpty()
                                || newRegion == null || newRegion.trim().isEmpty()) {
                            throw new IllegalArgumentException("All fields are required.");
                        }

                        if (flowIdList.isEmpty()) {
                            throw new IllegalArgumentException("No valid Flow IDs provided.");
                        }

                        // =========================
                        // 查询 REGIONID
                        // =========================
                        String regionId = null;

                        String queryRegionSql =
                                "SELECT CODE FROM PUB_REGION WHERE NAME = ?";

                        try (PreparedStatement ps = con.prepareStatement(queryRegionSql)) {
                            ps.setString(1, newRegion.trim());

                            try (ResultSet rs = ps.executeQuery()) {
                                if (rs.next()) {
                                    regionId = rs.getString("CODE");
                                }
                            }
                        }

                        // 未找到区域
                        if (regionId == null || regionId.trim().isEmpty()) {
                            throw new RuntimeException(
                                    "Region [" + newRegion + "] not found in PUB_REGION.");
                        }

                        // =========================
                        // 构建 IN 条件
                        // =========================
                        StringBuilder inClause = new StringBuilder();
                        for (int i = 0; i < flowIdList.size(); i++) {
                            if (i > 0) {
                                inClause.append(",");
                            }
                            inClause.append("?");
                        }

                        String sql =
                                "UPDATE ALGERIA_WFM.T_WFM_FULFILLMENT " +
                                        "SET REGION = ?, REGIONID = ? " +
                                        "WHERE CRM_ORDER_ID IN (" + inClause + ") " +
                                        "AND REGION = ?";

                        try (PreparedStatement ps = con.prepareStatement(sql)) {

                            int index = 1;

                            // REGION
                            ps.setString(index++, newRegion.trim());

                            // REGIONID
                            ps.setString(index++, regionId);

                            // CRM_ORDER_ID
                            for (String flowId5 : flowIdList) {
                                ps.setString(index++, flowId5);
                            }

                            // 原 REGION
                            ps.setString(index, oldRegion.trim());

                            int updatedRows = ps.executeUpdate();

                            logger.info(
                                    "Updated {} rows. Region: '{}' -> '{}', RegionId: '{}', Flow IDs: {}",
                                    updatedRows,
                                    oldRegion,
                                    newRegion,
                                    regionId,
                                    flowIdList);
                        }

                    } catch (Exception e) {
                        logger.error("Failed to batch update region", e);
                        throw new RuntimeException(
                                "Batch region update failed: " + e.getMessage(), e);
                    } finally {
                        closeConnection(con);
                    }
                    break;
                case "updateComplaintRegionBatch":
                    try {
                        Class.forName("com.huawei.gauss.jdbc.ZenithDriver");
                        con = DriverManager.getConnection(
                                "jdbc:zenith:@10.199.167.212:1888",
                                "algeria_wfm",
                                "wfm#_123OSS");

                        String flowIdsText = request.getParameter("flow_id");
                        String newRegion = request.getParameter("new_region");

                        // =========================
                        // 参数校验
                        // =========================
                        if (flowIdsText == null || flowIdsText.trim().isEmpty()
                                || newRegion == null || newRegion.trim().isEmpty()) {
                            throw new IllegalArgumentException("Flow IDs and New Region are required.");
                        }

                        // 如果你前面已经生成 flowIdList，可忽略这段
//                        List<String> flowIdList = Arrays.stream(flowIdsText.split(","))
//                                .map(String::trim)
//                                .filter(s -> !s.isEmpty())
//                                .collect(Collectors.toList());

                        if (flowIdList.isEmpty()) {
                            throw new IllegalArgumentException("No valid Flow IDs provided.");
                        }

                        // =========================
                        // 1. 查询 REGIONID
                        // =========================
                        String regionId = null;

                        String regionSql =
                                "SELECT CODE FROM algeria_WFM.PUB_REGION WHERE NAME = ?";

                        try (PreparedStatement ps = con.prepareStatement(regionSql)) {
                            ps.setString(1, newRegion.trim());

                            try (ResultSet rs = ps.executeQuery()) {
                                if (rs.next()) {
                                    regionId = rs.getString("CODE");
                                }
                            }
                        }

                        // 校验是否存在
                        if (regionId == null || regionId.trim().isEmpty()) {
                            throw new RuntimeException(
                                    "Region [" + newRegion + "] not found in PUB_REGION.");
                        }

                        // =========================
                        // 2. 动态 IN SQL
                        // =========================
                        StringBuilder inClause = new StringBuilder();

                        for (int i = 0; i < flowIdList.size(); i++) {
                            if (i > 0) {
                                inClause.append(",");
                            }
                            inClause.append("?");
                        }

                        String sql =
                                "UPDATE algeria_WFM.T_WFM_COMPLAINT " +
                                        "SET REGION = ?, REGIONID = ? " +
                                        "WHERE FLOW_ID IN (" + inClause + ")";

                        // =========================
                        // 3. 执行 UPDATE
                        // =========================
                        try (PreparedStatement ps = con.prepareStatement(sql)) {

                            int index = 1;

                            // REGION
                            ps.setString(index++, newRegion.trim());

                            // REGIONID
                            ps.setString(index++, regionId);

                            // FLOW_ID IN (...)
                            for (String flowId6 : flowIdList) {
                                ps.setString(index++, flowId6);
                            }

                            int updatedRows = ps.executeUpdate();

                            logger.info(
                                    "Updated {} complaint records. Region='{}', RegionId='{}', Flow IDs={}",
                                    updatedRows,
                                    newRegion,
                                    regionId,
                                    flowIdList);

                            request.setAttribute(
                                    "message",
                                    "Successfully updated " + updatedRows + " complaint(s).");
                        }

                    } catch (Exception e) {
                        logger.error(
                                "Failed to batch update complaint region",
                                e);

                        request.setAttribute(
                                "error",
                                "Update failed: " + e.getMessage());

                        throw new RuntimeException(
                                "Batch complaint region update failed",
                                e);

                    } finally {
                        closeConnection(con);
                    }
                    break;
                case "changeOntSn":
                    try {
                        Class.forName("com.huawei.gauss.jdbc.ZenithDriver");
                        con = DriverManager.getConnection("jdbc:zenith:@10.199.167.181:1888", "im_rmw", "im_rmw#_123OSS");
                        con.setAutoCommit(false); // 手动控制事务

                        // 获取前端传入的批量数据（格式：service_no,new_serial_num 每行一对）
                        String ontPairsText = request.getParameter("ont_pairs");
                        if (ontPairsText == null || ontPairsText.trim().isEmpty()) {
                            throw new IllegalArgumentException("Input data is required.");
                        }

                        // 解析输入
                        List<String[]> pairs = new ArrayList<>();
                        String[] lines = ontPairsText.trim().split("\\r?\\n");
                        for (String line : lines) {
                            line = line.trim();
                            if (line.isEmpty()) continue;
                            String[] parts = line.split(",", 2);
                            if (parts.length != 2) {
                                throw new IllegalArgumentException("Invalid format on line: " + line + ". Expected: service_no,new_serial_num");
                            }
                            String serviceNo = parts[0].trim();
                            String serialNum = parts[1].trim();
                            if (serviceNo.isEmpty() || serialNum.isEmpty()) {
                                throw new IllegalArgumentException("Empty value on line: " + line);
                            }
                            pairs.add(new String[]{serviceNo, serialNum});
                        }

                        if (pairs.isEmpty()) {
                            throw new IllegalArgumentException("No valid records to process.");
                        }

                        // Step 1: 清空中间表
                        try (PreparedStatement deleteStmt = con.prepareStatement("DELETE FROM IM_RMW.ONT_CHANGE_TEMP")) {
                            deleteStmt.executeUpdate();
                        }

                        // Step 2: 批量插入新数据到中间表
                        String insertSql = "INSERT INTO IM_RMW.ONT_CHANGE_TEMP (GROUPID, NEW_ONT_SN) VALUES (?, ?)";
                        try (PreparedStatement insertStmt = con.prepareStatement(insertSql)) {
                            for (String[] pair : pairs) {
                                insertStmt.setString(1, pair[0]); // SERVICE_NO
                                insertStmt.setString(2, pair[1]); // NEW_SERIAL_NUM
                                insertStmt.addBatch();
                            }
                            insertStmt.executeBatch();
                        }

                        // Step 3: 执行 PL/SQL 匿名块（完成 ONT 更新）
                        String plsql = "DECLARE\n" +
                                "    CURSOR cur_ont_temp IS \n" +
                                "        SELECT GROUPID, NEW_ONT_SN \n" +
                                "        FROM IM_RMW.ONT_CHANGE_TEMP \n" +
                                "        FOR UPDATE;\n" +
                                "\n" +
                                "    v_exists_same_ont NUMBER;\n" +
                                "    v_shared_count     NUMBER;\n" +
                                "    new_uuid           VARCHAR2(50);  -- 提到外层\n" +
                                "    u                  VARCHAR2(32);  -- 提到外层\n" +
                                "BEGIN\n" +
                                "    FOR rec IN cur_ont_temp LOOP\n" +
                                "        IF (rec.GROUPID IS NOT NULL AND rec.NEW_ONT_SN IS NOT NULL) THEN\n" +
                                "\n" +
                                "            -- 检查是否已存在相同 ONT\n" +
                                "            SELECT COUNT(1)\n" +
                                "            INTO v_exists_same_ont\n" +
                                "            FROM IM_RMW.IM_HOME_SERVICEINSTANCE \n" +
                                "            WHERE RELATED_RESOURCE IN (\n" +
                                "                SELECT UUID \n" +
                                "                FROM IM_RMW.IM_ONT \n" +
                                "                WHERE SERIALNUM = rec.NEW_ONT_SN AND STATEFLAG = 0\n" +
                                "            )\n" +
                                "            AND RELATED_SERVICE IN (\n" +
                                "                SELECT UUID \n" +
                                "                FROM IM_RMW.IM_HOME_SERVICE \n" +
                                "                WHERE GROUPID = rec.GROUPID\n" +
                                "            )\n" +
                                "            AND LAST_ACTIVITY = 1 AND STATEFLAG = 0;\n" +
                                "            IF (v_exists_same_ont = 0) THEN\n" +
                                "                -- 检查共享数量\n" +
                                "                SELECT COUNT(1)\n" +
                                "                INTO v_shared_count\n" +
                                "                FROM IM_RMW.IM_HOME_SERVICEINSTANCE \n" +
                                "                WHERE RELATED_RESOURCE IN (\n" +
                                "                    SELECT RELATED_RESOURCE \n" +
                                "                    FROM IM_RMW.IM_HOME_SERVICEINSTANCE \n" +
                                "                    WHERE RELATED_RESOURCE LIKE 'ONT%' \n" +
                                "                      AND LAST_ACTIVITY = 1 \n" +
                                "                      AND STATEFLAG = 0\n" +
                                "                      AND RELATED_SERVICE IN (\n" +
                                "                          SELECT uuid \n" +
                                "                          FROM IM_RMW.IM_HOME_SERVICE \n" +
                                "                          WHERE GROUPID = rec.GROUPID\n" +
                                "                      )\n" +
                                "                ) \n" +
                                "                AND LAST_ACTIVITY = 1 \n" +
                                "                AND STATEFLAG = 0;\n" +
                                "                IF (v_shared_count > 2 ) THEN\n" +
                                "                    -- 生成新 UUID\n" +
                                "                    SELECT RAWTOHEX(UUID()) INTO u FROM DUAL LIMIT 1;  -- ✅ 保留原样\n" +
                                "                    new_uuid := 'ONT:' || LOWER(\n" +
                                "                        SUBSTR(u, 1, 8)  || '-' ||\n" +
                                "                        SUBSTR(u, 9, 4)  || '-' ||\n" +
                                "                        SUBSTR(u, 13, 4) || '-' ||\n" +
                                "                        SUBSTR(u, 17, 4) || '-' ||\n" +
                                "                        SUBSTR(u, 21)\n" +
                                "                    );\n" +
                                "\n" +
                                "                    -- 更新服务实例\n" +
                                "                    UPDATE IM_RMW.IM_HOME_SERVICEINSTANCE\n" +
                                "                    SET RELATED_RESOURCE = new_uuid\n" +
                                "                    WHERE LAST_ACTIVITY = 1 \n" +
                                "                      AND STATEFLAG = 0 \n" +
                                "                      AND RELATED_SERVICE IN (\n" +
                                "                          SELECT UUID FROM IM_RMW.IM_HOME_SERVICE \n" +
                                "                          WHERE GROUPID = rec.GROUPID AND STATEFLAG = 0\n" +
                                "                      )\n" +
                                "                      AND RELATED_RESOURCE LIKE 'ONT%';\n" +
                                "\n" +
                                "                    -- 插入新 ONT\n" +
                                "                    INSERT INTO IM_RMW.IM_ONT(\n" +
                                "                        NAME, SERIALNUM, related_region, uuid, RELATED_EMS, VENDOR_ID,\n" +
                                "                        OLT_ID, MASTER_OLT_PORT, create_time, MODIFY_TIME, TIME_STAMP,\n" +
                                "                        STATEFLAG, creator, MODIFIER, auth\n" +
                                "                    )\n" +
                                "                    SELECT \n" +
                                "                        'FTTH_' || rec.NEW_ONT_SN AS NAME,\n" +
                                "                        rec.NEW_ONT_SN AS SERIALNUM,\n" +
                                "                        s.region AS related_region,\n" +
                                "                        new_uuid AS uuid,\n" +
                                "                        v.RELATED_EMS AS RELATED_EMS,\n" +
                                "                        v.VENDOR_ID AS VENDOR_ID,\n" +
                                "                        v.uuid AS OLT_ID,     \n" +
                                "                        s.ACCESS_DEVICE AS MASTER_OLT_PORT, \n" +
                                "                        SYSDATE AS create_time,\n" +
                                "                        SYSDATE AS MODIFY_TIME,\n" +
                                "                        SYSDATE AS TIME_STAMP,\n" +
                                "                        0 AS STATEFLAG,\n" +
                                "                        'system' AS creator,\n" +
                                "                        'system' AS MODIFIER,\n" +
                                "                        'SN' AS auth       \n" +
                                "                    FROM im_rmw.im_home_service s\n" +
                                "                    JOIN im_pos p\n" +
                                "                      ON s.access_device = p.uuid AND p.pos_level = 2\n" +
                                "                    JOIN im_pos pp\n" +
                                "                      ON p.up_pos = pp.uuid AND pp.pos_level = 1\n" +
                                "                    JOIN im_port i\n" +
                                "                      ON pp.related_oltptp = i.uuid\n" +
                                "                    JOIN im_card c\n" +
                                "                      ON i.related_card = c.uuid\n" +
                                "                    JOIN im_fbbdev v\n" +
                                "                      ON c.related_ne = v.uuid\n" +
                                "                    JOIN im_vendor vv\n" +
                                "                      ON v.vendor_id = vv.uuid  \n" +
                                "                    WHERE s.GROUPID = rec.GROUPID \n" +
                                "                      AND s.SERVICE_TYPE = 'Voice' LIMIT 1; -- ✅ 保留原样\n" +
                                "                ELSE\n" +
                                "                    -- 更新现有 ONT\n" +
                                "                    UPDATE IM_RMW.IM_ONT \n" +
                                "                    SET NAME = 'FTTH_' || rec.NEW_ONT_SN, \n" +
                                "                        SERIALNUM = rec.NEW_ONT_SN\n" +
                                "                    WHERE uuid IN (\n" +
                                "                        SELECT RELATED_RESOURCE \n" +
                                "                        FROM IM_RMW.IM_HOME_SERVICEINSTANCE\n" +
                                "                        WHERE LAST_ACTIVITY = 1 \n" +
                                "                          AND STATEFLAG = 0 \n" +
                                "                          AND RELATED_SERVICE IN (\n" +
                                "                              SELECT UUID \n" +
                                "                              FROM IM_RMW.IM_HOME_SERVICE \n" +
                                "                              WHERE GROUPID = rec.GROUPID AND STATEFLAG = 0\n" +
                                "                          )\n" +
                                "                    );\n" +
                                "                END IF;\n" +
                                "            END IF;\n" +
                                "        END IF;\n" +
                                "    END LOOP;\n" +
                                "    COMMIT;\n" +
                                "END;";

                        try (CallableStatement cs = con.prepareCall(plsql)) {
                            cs.execute();
                        }

                        con.commit();
                        response.getWriter().println("✅ Successfully processed " + pairs.size() + " ONT SN changes.");

                    } catch (Exception e) {
                        if (con != null) {
                            try {
                                con.rollback();
                            } catch (SQLException rollbackEx) {
                                logger.error("Rollback failed", rollbackEx);
                            }
                        }
                        logger.error("❌ Failed to change ONT SN", e);
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        response.getWriter().println("Error: " + e.getMessage());
                    } finally {
                        if (con != null) {
                            try {
                                con.setAutoCommit(true);
                                con.close();
                            } catch (SQLException e) {
                                logger.warn("Failed to close connection", e);
                            }
                        }
                    }
                    break;
                case "batchUpdateRepid":
                    try {
                        Class.forName("com.huawei.gauss.jdbc.ZenithDriver");
                        con = DriverManager.getConnection(
                                "jdbc:zenith:@10.199.167.181:1888",
                                "im_rmw",
                                "im_rmw#_123OSS"
                        );

                        con.setAutoCommit(false); // 手动事务

                        // 1️⃣ 获取前端数据
                        String text = request.getParameter("pairs");
                        if (text == null || text.trim().isEmpty()) {
                            throw new IllegalArgumentException("Input data is required.");
                        }

                        // 2️⃣ 解析数据（groupid,repid）
                        List<String[]> pairs = new ArrayList<>();
                        String[] lines = text.trim().split("\\r?\\n");

                        for (String line : lines) {
                            line = line.trim();
                            if (line.isEmpty()) continue;

                            String[] parts = line.split(",", 2);
                            if (parts.length != 2) {
                                throw new IllegalArgumentException("Invalid format: " + line);
                            }

                            String groupId = parts[0].trim();
                            String repId = parts[1].trim();

                            if (groupId.isEmpty() || repId.isEmpty()) {
                                throw new IllegalArgumentException("Empty value: " + line);
                            }

                            pairs.add(new String[]{groupId, repId});
                        }

                        if (pairs.isEmpty()) {
                            throw new IllegalArgumentException("No valid records.");
                        }

                        // 3️⃣ 清空临时表
                        try (PreparedStatement ps = con.prepareStatement(
                                "DELETE FROM IM_RMW.REPID_CHANGE_TEMP"
                        )) {
                            ps.executeUpdate();
                        }

                        // 4️⃣ 插入临时表
                        String insertSql = "INSERT INTO IM_RMW.REPID_CHANGE_TEMP (GROUPID, REPID) VALUES (?, ?)";
                        try (PreparedStatement ps = con.prepareStatement(insertSql)) {
                            for (String[] p : pairs) {
                                ps.setString(1, p[0]);
                                ps.setString(2, p[1]);
                                ps.addBatch();
                            }
                            ps.executeBatch();
                        }

                        // 5️⃣ 执行批量更新（PL/SQL）
                        String plsql =
                                "DECLARE\n" +
                                        "    CURSOR cur IS \n" +
                                        "        SELECT GROUPID, REPID FROM IM_RMW.REPID_CHANGE_TEMP;\n" +
                                        "BEGIN\n" +
                                        "    FOR rec IN cur LOOP\n" +
                                        "        IF rec.GROUPID IS NOT NULL AND rec.REPID IS NOT NULL THEN\n" +
                                        "            UPDATE IM_RMW.IM_HOME_SERVICE s\n" +
                                        "            SET s.REPID = rec.REPID\n" +
                                        "            WHERE s.GROUPID = rec.GROUPID\n" +
                                        "              AND s.STATEFLAG = 0;\n" +
                                        "        END IF;\n" +
                                        "    END LOOP;\n" +
                                        "    COMMIT;\n" +
                                        "END;";

                        try (CallableStatement cs = con.prepareCall(plsql)) {
                            cs.execute();
                        }

                        con.commit();

                        response.getWriter().println("✅ Successfully updated " + pairs.size() + " records.");

                    } catch (Exception e) {
                        if (con != null) {
                            try {
                                con.rollback();
                            } catch (SQLException ex) {
                                logger.error("Rollback failed", ex);
                            }
                        }

                        logger.error("❌ Batch update failed", e);
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        response.getWriter().println("Error: " + e.getMessage());

                    } finally {
                        if (con != null) {
                            try {
                                con.setAutoCommit(true);
                                con.close();
                            } catch (SQLException e) {
                                logger.warn("Close failed", e);
                            }
                        }
                    }
                    break;
                case "downloadSdmFastExcel": {
                    try {
                        String exportType = request.getParameter("export_type");
                        String flowIdsParam = request.getParameter("flow_id");

                        if (exportType == null ||
                                (!"running_to_crm".equals(exportType) && !"crm_only".equals(exportType))) {
                            sendDownloadError(response, "Invalid export type");
                            return;
                        }

                        if (flowIdsParam == null || flowIdsParam.trim().isEmpty()) {
                            sendDownloadError(response, "Flow IDs are required");
                            return;
                        }

                        List<String> validFlowIds = Arrays.stream(flowIdsParam.split("[\\r\\n,]+"))
                                .map(String::trim)
                                .filter(id -> !id.isEmpty())
                                .collect(Collectors.toList());

                        if (validFlowIds.isEmpty()) {
                            sendDownloadError(response, "No valid Flow ID found");
                            return;
                        }

                        // 1️⃣ 先在内存中生成 Excel（⚠️ 关键）
                        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                        String flowIdsForSql = String.join(",", validFlowIds);
                        SQLToExcel.exportSdmFastExcel(exportType, flowIdsForSql, buffer);

                        byte[] excelBytes = buffer.toByteArray();

                        if (excelBytes.length == 0) {
                            throw new RuntimeException("Generated Excel is empty");
                        }

                        // 2️⃣ 确认成功后，再写 HTTP 响应
                        response.reset();

                        String rawFilename = "running_to_crm".equals(exportType)
                                ? "CTT_Complete_CTT.xlsx"
                                : "CTT_Close_CTT.xlsx";

                        String encodedFilename = URLEncoder.encode(rawFilename, "UTF-8")
                                .replaceAll("\\+", "%20");

                        response.setContentType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        response.setContentLength(excelBytes.length);
                        response.setHeader(
                                "Content-Disposition",
                                "attachment; filename*=UTF-8''" + encodedFilename
                        );

                        try (OutputStream out = response.getOutputStream()) {
                            out.write(excelBytes);
                            out.flush();
                        }

                        logger.info("Exported {} flows to Excel", validFlowIds.size());
                        return;

                    } catch (Exception e) {
                        logger.error("Excel download failed", e);

                        response.reset();
                        response.setContentType("text/plain;charset=UTF-8");
                        response.getWriter().write("Export failed: " + e.getMessage());
                        return;
                    }
                }
                default:
                    throw new IllegalArgumentException("Unknown action: " + action);
            }

            PrintWriter out = response.getWriter();

            // 响应页面头部（简单 HTML）
            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>Execution Result</title>");
            out.println("<meta charset='UTF-8'>");
            out.println("<style>body{font-family:Arial,sans-serif;padding:20px;background:#f7f7f7;}");
            out.println(".success{color:green;font-weight:bold;}");
            out.println(".error{color:red;}");
            out.println(".back{margin-top:20px;}</style></head><body>");

            // ✅ 只有非 setSession 且成功时才显示通用成功消息
            if (!"setSession".equals(action) && success) {
                out.println("<div class='success'>");
                out.println("<h3>✅ Action '" + escapeHtml(action) + "' executed successfully!</h3>");
                out.println("</div>");
            }

            out.println("<div class='back'>");
            out.println("<a href='javascript:window.history.back();'>← Go Back</a>");
            out.println("</div>");
            out.println("</body></html>");

        } catch (Exception e) {
            // 错误时才输出 HTML 错误页
            String errorMsg = e.getMessage() != null ? e.getMessage() : "Unknown error";
            PrintWriter out = response.getWriter();
            response.setContentType("text/html;charset=UTF-8");
            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>Error</title><meta charset='UTF-8'>");
            out.println("<style>.error{color:red;}</style></head><body>");
            out.println("<div class='error'>");
            out.println("<h3>❌ Execution failed:</h3>");
            out.println("<p><strong>" + escapeHtml(errorMsg) + "</strong></p>");
            out.println("</div>");
            out.println("<div class='back'><a href='javascript:history.back()'>&larr; Go Back</a></div>");
            out.println("</body></html>");

            // 在服务器日志中打印完整异常
            e.printStackTrace(); // 或使用 logger.error("Execution error", e);
        }
    }

    /**
     * 检查参数是否有效（非空且非空白）
     */
    private boolean isValidParam(String param) {
        return param != null && !param.trim().isEmpty();
    }
    private boolean isValidParam(int param) {
        return param > 0;
    }

    private void sendDownloadError(HttpServletResponse response, String message) {
        try {
            // 👇 非常重要：重置响应，清除之前设置的 header 和缓冲区
            response.reset();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("text/plain;charset=UTF-8");

            byte[] msgBytes = ("Error: " + message).getBytes(StandardCharsets.UTF_8);
            response.setContentLength(msgBytes.length);

            try (OutputStream os = response.getOutputStream()) {
                os.write(msgBytes);
                os.flush();
            }
        } catch (IOException ioEx) {
            logger.error("Failed to send download error response", ioEx);
            // 无法再做更多，日志记录即可
        }
    }
    private boolean isValidParam(long id) {
        // 1. 基础检查：ID 必须大于 0
        if (id <= 0) {
            return false;
        }
        return true;
    }
    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return String.format("%032x", new BigInteger(1, digest));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void executePasswordUpdate(
            HttpServletRequest request,
            String jdbcUrl,
            String dbUser,
            String dbPassword,
            String tableName,
            String userStatus,
            String whereColumn,
            String statusColumn,
            String systemType) {

        Connection con = null;

        try {

            Class.forName("com.huawei.gauss.jdbc.ZenithDriver");
            con = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);

            String userIdsParam = request.getParameter("user_ids");
            String rawPassword = request.getParameter("password");

            if (userIdsParam == null || userIdsParam.trim().isEmpty()) {
                throw new IllegalArgumentException("User IDs are required.");
            }

            if (rawPassword == null || rawPassword.trim().isEmpty()) {
                throw new IllegalArgumentException("Password is required.");
            }

            // MD5加密
            Set<String> saltSystems = new HashSet<String>();
            saltSystems.add("OM");
            saltSystems.add("SDM");
//            saltSystems.add("IM");

            String passwordHash;

            if (saltSystems.contains(systemType)) {
                passwordHash = MD5Util.encodePwd2(rawPassword);
            } else {
                passwordHash = MD5Util.encodePwd(rawPassword);
            }
            String[] userIds = userIdsParam.split(",");

            List<String> validUserIds = new ArrayList<>();

            for (String id : userIds) {
                String trimmed = id.trim();
                if (isValidParam(trimmed)) {
                    validUserIds.add(trimmed.toLowerCase());
                }
            }

            if (validUserIds.isEmpty()) {
                throw new IllegalArgumentException("No valid user IDs provided.");
            }

            String inClause = String.join(",", Collections.nCopies(validUserIds.size(), "?"));

            String sql = "UPDATE " + tableName +
                    " SET PASSWORD = ?, " + statusColumn + " = ?" +
                    " WHERE LOWER(" + whereColumn + ") IN (" + inClause + ")";

            try (PreparedStatement pstmt = con.prepareStatement(sql)) {

                pstmt.setString(1, passwordHash);
                pstmt.setString(2, userStatus);

                for (int i = 0; i < validUserIds.size(); i++) {
                    pstmt.setString(i + 3, validUserIds.get(i));
                }

                int rowsAffected = pstmt.executeUpdate();

                logger.info("Updated {} users in {}", rowsAffected, tableName);

                request.setAttribute("message",
                        "Successfully updated " + rowsAffected + " user(s).");

            }

        } catch (Exception e) {

            logger.error("Password update failed", e);
            request.setAttribute("message", "Error: " + e.getMessage());

        } finally {

            closeConnection(con);

        }
    }
    /**
     * 简单的 HTML 转义，防止 XSS
     */

    private void sendErrorPage(HttpServletResponse response, String message) throws IOException {
        try {
            response.reset(); // 清除之前可能设置的 header
        } catch (Exception ignored) {}
        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><html><body style='font-family:Arial;'>");
        out.println("<h3 style='color:red;'>❌ Error: " + escapeHtml(message) + "</h3>");
        out.println("<a href='javascript:history.back()'>&larr; Go Back</a>");
        out.println("</body></html>");
    }
    private String escapeHtml(String input) {
        if (input == null) return "";
        return input
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }
}
