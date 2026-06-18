<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>IM Tools</title>
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
        <h2 class="page-title">IM Tools</h2>

        <div class="session-section">
            <h3>🔐 Session Manage</h3>
            <form action="ExecuteServlet" method="post">
                <input type="hidden" name="action" value="setSession"/>
                <label for="jsessionid">Input JSESSIONID:</label>
                <input type="text" id="jsessionid" name="jsessionid" placeholder="例如: ABC123XYZ789" required />
                <input type="submit" value="Set Session (JSESSIONID)" />
            </form>
        </div>

        <div class="divider">IM MANUALLY SENDS THE ORDER TO OM</div>
        <%-- ✅ Update IM: 支持多 Flow ID --%>
        <form action="ExecuteServlet" method="post">
            <input type="hidden" name="action" value="updateIM"/>
            <label for="flow_id_im">Order Todo (IM) - Batch:</label>
            <textarea
                    id="flow_id_im"
                    name="flow_id"
                    rows="4"
                    placeholder="Enter one Flow ID per line&#10;e.g.&#10;FLOW123456&#10;FLOW789012"
                    required></textarea>
            <input type="submit" value="Update IM (Batch)"/>
        </form>
        <div class="divider">IM ORDER MANUALLY FINISH / CANCEL WORKSHEET</div>
        <form action="ExecuteServlet" method="post">
            <input type="hidden" name="action" value="changeimworksheet"/>

            <label for="flow_id">Flow ID (Batch):</label>
            <textarea
                    id="flow_id"
                    name="flow_id"
                    rows="5"
                    placeholder="Enter one Flow ID per line&#10;e.g.&#10;FLOW123456&#10;FLOW789012"
                    required></textarea>
            <br/><br/>

            <!-- 保留原有的 Operation 下拉框 -->
            <label for="operation">Operation Type:</label>
            <select id="operation" name="operation" required>
                <option value="">-- Select Operation --</option>
                <option value="install" selected>install</option>
                <%--            <option value="install">install</option>--%>
                <option value="uninstall">uninstall</option>
                <option value="relocate">relocate</option>
                <option value="change port">change port</option>
            </select>
            <br/><br/>

            <!-- 新增：Finish or Cancel -->
            <label for="worksheet_action">Worksheet Action:</label>
            <select id="worksheet_action" name="worksheet_action" required>
                <option value="">-- Select Action --</option>
                <option value="finish">Finish Worksheet</option>
                <option value="cancel">Cancel Worksheet</option>
            </select>
            <br/><br/>

            <input type="submit" value="Submit Worksheet Action (Batch)"/>
        </form>
        <div class="divider">Change IM Order Install status</div>
        <form action="ExecuteServlet" method="post">
            <input type="hidden" name="action" value="markAMOrderAbnormal" />
            <input type="hidden" name="order_type" value="IM" />

            <label for="iim_flow_id">IM Flow IDs (one per line):</label>
            <textarea
                    id="iim_flow_id"
                    name="flow_id"
                    rows="6"
                    placeholder="Enter one IM Flow ID per line&#10;e.g.&#10;172575039589958&#10;172575039589278"
                    required></textarea>
            <br /><br />

            <label for="target_status">Target Install Status:</label>
            <select id="target_status" name="target_status" required>
                <option value="">-- Select Status --</option>
                <option value="success">Mark as install Complete</option>
                <option value="failed">Mark as install failed</option>
                <option value="uninstallcomplete">Mark as Uninstall Complete</option>
            </select>
            <br /><br />

            <input type="submit" value="Update Install Status" />
        </form>
        <form action="ExecuteServlet" method="post">
            <h3>Change ONT Serial Number (Batch)</h3>
            <input type="hidden" name="action" value="changeOntSn" />

            <label for="ont_pairs">Service No and New Serial Number (one pair per line, comma-separated):</label><br />
            <textarea
                    id="ont_pairs"
                    name="ont_pairs"
                    rows="10"
                    placeholder="Format: service_no,new_serial_num&#10;e.g.:&#10;SVC123456789,HWTC12345678&#10;SVC987654321,HWTC87654321"
                    required></textarea>
            <br /><br />

            <input type="submit" value="Change ONT SN (Batch)" />
        </form>
        <form action="ExecuteServlet" method="post">
            <h3>Batch Update RepID by GroupID</h3>

            <input type="hidden" name="action" value="batchUpdateRepid" />

            <label for="pairs">GroupID and RepID (one pair per line):</label><br />
            <textarea
                    id="pairs"
                    name="pairs"
                    rows="10"
                    placeholder="Format: groupid,repid&#10;e.g.:&#10;G12345,R88888&#10;G67890,R99999"
                    required></textarea>
            <br /><br />

            <input type="submit" value="Batch Update" />
        </form>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/common.js"></script>
</body>
</html>