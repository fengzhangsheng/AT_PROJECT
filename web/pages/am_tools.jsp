<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>AM Tools</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="sidebar">
    <h2>🔧 Order Update Tool</h2>
    <a href="${pageContext.request.contextPath}/pages/om_tools.jsp">OM Tools</a>
    <a href="${pageContext.request.contextPath}/pages/am_tools.jsp">AM Tools</a>
    <a href="${pageContext.request.contextPath}/pages/wfm_tools.jsp">WFM Tools</a>
    <a href="${pageContext.request.contextPath}/pages/sdm_tools.jsp">SDM Tools</a>
    <a href="${pageContext.request.contextPath}/pages/im_tools.jsp">IM Tools</a>
</div>

<div class="main-content">
    <div class="container">
        <h2 class="page-title">AM Tools</h2>

        <div class="session-section">
            <h3>🔐 Session Manage</h3>
            <form action="ExecuteServlet" method="post">
                <input type="hidden" name="action" value="setSession"/>
                <label for="jsessionid">Input JSESSIONID:</label>
                <input type="text" id="jsessionid" name="jsessionid" placeholder="例如: ABC123XYZ789" required />
                <input type="submit" value="Set Session (JSESSIONID)" />
            </form>
        </div>

        <div class="divider">AM ORDER MANUALLY REDO</div>
        <%-- ✅ Update AM: 支持多 Flow ID --%>
        <form action="ExecuteServlet" method="post">
            <input type="hidden" name="action" value="updateAM"/>
            <label for="flow_id_am">Order redo (AM) - Batch:</label>
            <textarea
                    id="flow_id_am"
                    name="flow_id"
                    rows="4"
                    placeholder="Enter one Flow ID per line&#10;e.g.&#10;FLOW123456&#10;FLOW789012"
                    required></textarea>
            <input type="submit" value="Update AM (Batch)"/>
        </form>
        <div class="divider">AM ORDER MANUALLY REPLACE SN</div>
        <form action="ExecuteServlet" method="post">
            <!-- 指定操作类型 -->
            <input type="hidden" name="action" value="updateExecuteCmd"/>

            <label for="flow_id_sn">Flow ID:</label>
            <input type="text"
                   id="flow_id_sn"
                   name="flow_id"
                   placeholder="Enter Flow ID (e.g. 43419328130816)"
                   required />
            <br/><br/>

            <label for="old_value">Old Value in execute_cmd:</label>
            <input type="text"
                   id="old_value"
                   name="old_value"
                   placeholder="Enter the old string to replace (e.g. ABC123)"
                   required />
            <!-- 移除了 value="" 和 readonly -->
            <br/><br/>

            <label for="new_value">New Value in execute_cmd:</label>
            <input type="text"
                   id="new_value"
                   name="new_value"
                   placeholder="Enter the new string (e.g. XYZ789)"
                   required />
            <!-- 移除了 value="" 和 readonly -->
            <br/><br/>

            <input type="submit" value="Update execute_cmd (Replace)" />
        </form>
        <div class="separator"></div>
        <div class="divider">AM ORDER MANUALLY COMPLETE</div>
        <%-- ✅ NEW: Complete AM Work Order (Batch) --%>
        <form action="ExecuteServlet" method="post">
            <input type="hidden" name="action" value="completeAMOrder"/>

            <label for="am_flow_id">Flow ID—Complete AM Work Order (Batch):</label>
            <textarea
                    id="am_flow_id"
                    name="flow_id"
                    rows="4"
                    placeholder="Enter one Flow ID per line&#10;e.g.&#10;43416201219970&#10;43416201219971"
                    required></textarea>
            <br/><br/>

            <input type="submit" value="Complete AM Work Order (Batch)" />
        </form>
        <div class="divider">SKIP ONE AM SUBFLOW (By ID)</div>

        <form action="ExecuteServlet" method="post">
            <input type="hidden" name="action" value="forceCloseSubFlow" />

            <label for="sub_flow_ids2">Sub Flow IDs (one per line):</label>
            <textarea id="sub_flow_ids2" name="flow_id" rows="6"
                      placeholder="Enter one ID per line&#10;e.g.&#10;1009893905102&#10;1009895065002"
                      required></textarea>

            <br /><br />

            <!-- 新增的下拉框开始 -->
            <label for="status_action">Action Type:</label>
            <select id="status_action" name="status_action" required>
                <option value="" disabled selected>Select an action...</option>
                <option value="changetosuccess">Change to Success</option>
                <option value="changetofail">Change to Fail</option>
            </select>
            <!-- 新增的下拉框结束 -->

            <br /><br />

            <input type="submit" value="SKIP ONE AM SUBFLOW" />
        </form>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/common.js"></script>
</body>
</html>