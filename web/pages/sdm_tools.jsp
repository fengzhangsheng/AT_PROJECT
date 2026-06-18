<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>SDM Tools</title>
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
        <h2 class="page-title">SDM Tools</h2>

        <div class="session-section">
            <h3>🔐 Session Manage</h3>
            <form action="ExecuteServlet" method="post">
                <input type="hidden" name="action" value="setSession"/>
                <label for="jsessionid">Input JSESSIONID:</label>
                <input type="text" id="jsessionid" name="jsessionid" placeholder="例如: ABC123XYZ789" required />
                <input type="submit" value="Set Session (JSESSIONID)" />
            </form>
        </div>

        <div class="divider">UPDATE SDM UNKNOW REGION</div>

        <form action="ExecuteServlet" method="post">
            <input type="hidden" name="action" value="updateRegionByServNo" />

            <label for="serv_nos">Serv No (one per line):</label>
            <textarea id="serv_nos" name="flow_id" rows="6"
                      placeholder="Enter one Serv No per line&#10;e.g.&#10;251208111016737"
                      required></textarea>
            <br /><br />

            <input type="submit" value="UPDATE SDM UNKNOW REGION" />
        </form>
        <div class="divider">GET FAST EXCEL (Clos SDM Order)</div>
        <form action="ExecuteServlet" method="post">
            <input type="hidden" name="action" value="downloadSdmFastExcel" />

            <label for="export_type">Select Export Type:</label>
            <select id="export_type" name="export_type" required>
                <option value="">-- Choose an option --</option>
                <option value="running_to_crm">Running -> CRM Approve CTT</option>
                <option value="crm_only">CRM Approve CTT -> Close</option>
            </select>
            <br/><br/>

            <label for="sdm_flow_id">Enter Flow IDs (one per line):</label>
            <textarea
                    id="sdm_flow_id"
                    name="flow_id"
                    rows="5"
                    placeholder="e.g.
AT-CTT-20190126-02293
AT-CTT-20190126-02309
AT-CTT-20190126-02308"
                    required></textarea>
            <br/><br/>

            <input type="submit" value="Download Excel File" />
        </form>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/common.js"></script>
</body>
</html>