<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>OM Tools</title>
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
        <h2 class="page-title">OM Tools</h2>

        <div class="session-section">
            <h3>🔐 Session Manage</h3>
            <form action="ExecuteServlet" method="post">
                <input type="hidden" name="action" value="setSession"/>
                <label for="jsessionid">Input JSESSIONID:</label>
                <input type="text" id="jsessionid" name="jsessionid" placeholder="例如: ABC123XYZ789" required />
                <input type="submit" value="Set Session (JSESSIONID)" />
            </form>
        </div>
        <div class="separator"></div>
        <div class="divider">RE-EXECUTE THE CURRENT PROCESS</div>
        <%-- ✅ Update OM: 支持多 Flow ID --%>
        <form action="ExecuteServlet" method="post">
            <input type="hidden" name="action" value="updateOM"/>
            <label for="flow_id_om">Order reexcute (OM) - Batch:</label>
            <textarea
                    id="flow_id_om"
                    name="flow_id"
                    rows="4"
                    placeholder="Enter one Flow ID per line&#10;e.g.&#10;FLOW123456&#10;FLOW789012"
                    required></textarea>
            <input type="submit" value="Update OM (Batch)"/>
        </form>
        <div class="divider">OM ORDER SKIP WFM OR AM STEP</div>
        <%-- ✅ Skip WFM: 支持多 Flow ID --%>
        <form action="ExecuteServlet" method="post">
            <input type="hidden" name="action" value="skipwfm"/>
            <label for="flow_id_skipwfm">Flow ID (Skip WFM OR AM) - Batch:</label>
            <textarea
                    id="flow_id_skipwfm"
                    name="flow_id"
                    rows="4"
                    placeholder="Note: Enter CRM_NO to skip the WFM step, and enter FLOW_ID to skip the AM step.&#10;e.g.&#10;FLOW123456&#10;FLOW789012"
                    required></textarea>
            <input type="submit" value="Skip WFM OR AM step (Batch)" />
        </form>
        <div class="divider">Mark OM Orders as Abnormal (Batch)</div>
        <form action="ExecuteServlet" method="post">
            <input type="hidden" name="action" value="markAMOrderAbnormal" />
            <input type="hidden" name="order_type" value="OM" />

            <label for="oom_flow_id">OM Flow IDs (one per line):</label>
            <textarea
                    id="oom_flow_id"
                    name="flow_id"
                    rows="6"
                    placeholder="Enter one OM Flow ID per line&#10;e.g.&#10;174575039589958&#10;174575039589962"
                    required></textarea>
            <br /><br />

            <input type="submit" value="Changtoabnormal" />
        </form>

        <br /><br />
        <div class="divider">CHANGE USER PASSWORD</div>
        <form action="ExecuteServlet" method="post">
            <input type="hidden" name="action" value="changeUserPwd" />
            <label for="system_type">Select System:</label>
            <select id="system_type" name="system_type" required onchange="updatePlaceholder(this.value)">
                <option value="">-- Choose a system --</option>
                <option value="OM">OM</option>
                <option value="WFM">WFM</option>
                <option value="SDM">SDM</option>
                <option value="IM">IM</option>
                <option value="AM">AM</option>
            </select>
            <br/><br/>
            <label for="user_ids">Enter User IDs (comma separated):</label>
            <input type="text" id="user_ids" name="user_ids"
                   placeholder="e.g. user1,user2,user3" required />
            <br/><br/>
            <label for="password">New Password:</label>
            <div style="position: relative; display: inline-block;">
                <input type="password" id="password" name="password"
                       placeholder="Enter new password" required style="padding-right: 30px;" />
                <!-- 眼睛按钮 -->
                <span id="togglePassword"
                      style="position: absolute; right: 5px; top: 50%; transform: translateY(-50%);
                     cursor: pointer; user-select: none;">👁️</span>
            </div>
            <br/><br/>
            <input type="submit" value="Change Password" />
        </form>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/common.js"></script>
</body>
</html>