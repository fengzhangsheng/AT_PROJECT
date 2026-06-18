<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>WFM Tools</title>
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
        <h2 class="page-title">WFM Tools</h2>

        <div class="session-section">
            <h3>🔐 Session Manage</h3>
            <form action="ExecuteServlet" method="post">
                <input type="hidden" name="action" value="setSession"/>
                <label for="jsessionid">Input JSESSIONID:</label>
                <input type="text" id="jsessionid" name="jsessionid" placeholder="例如: ABC123XYZ789" required />
                <input type="submit" value="Set Session (JSESSIONID)" />
            </form>
        </div>

        <div class="divider">Close WFM Orders (Complaint)</div>
        <form action="ExecuteServlet" method="post">
            <input type="hidden" name="action" value="closeWFMOrder" />

            <label for="flow_ids_to_close">Flow IDs to Close (one per line):</label>
            <textarea id="flow_ids_to_close" name="flow_id" rows="6"
                      placeholder="Enter one Flow ID per line&#10;e.g.&#10;251208111016737&#10;251209140376091"
                      required></textarea>
            <br /><br />

            <input type="submit" value="Close Selected WFM Orders (Batch)" />
        </form>
        <div class="divider">Update WFM Order Region (Batch)</div>
        <form action="ExecuteServlet" method="post">
            <input type="hidden" name="action" value="updateWFMRegionBatch" />

            <label for="flow_ids_region">Flow IDs (one per line):</label>
            <textarea
                    id="flow_ids_region"
                    name="flow_id"
                    rows="6"
                    placeholder="Enter one Flow ID (CRM Order ID) per line&#10;e.g.&#10;114838275109&#10;114838275108"
                    required></textarea>
            <br /><br />

            <label for="old_region">Old Region:</label>
            <input type="text"
                   id="old_region"
                   name="old_region"
                   placeholder="e.g. Cecli de Berrahal"
                   required />
            <br /><br />

            <label for="new_region">New Region:</label>
            <input type="text"
                   id="new_region"
                   name="new_region"
                   placeholder="e.g. Cecli de Boumerdes"
                   required />
            <br /><br />

            <input type="submit" value="Update Region (Batch)" />
        </form>
        <div class="divider">Update Complaint Order Region (Batch)</div>
        <form action="ExecuteServlet" method="post">
            <input type="hidden" name="action" value="updateComplaintRegionBatch" />

            <label for="complaint_flow_ids">Flow IDs (one per line):</label>
            <textarea
                    id="complaint_flow_ids"
                    name="flow_id"
                    rows="6"
                    placeholder="Enter one Flow ID (Complaint ID) per line&#10;e.g.&#10;2501121535459260&#10;2501121533459260"
                    required></textarea>
            <br /><br />

            <label for="complaint_new_region">New Region:</label>
            <input type="text"
                   id="complaint_new_region"
                   name="new_region"
                   placeholder="e.g. Cecli de Boumerdes"
                   required />
            <br /><br />

            <input type="submit" value="Update Complaint Region (Batch)" />
        </form>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/common.js"></script>
</body>
</html>