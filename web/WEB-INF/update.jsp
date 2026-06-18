<%-- /WEB-INF/update.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Order Update Tool</title>
    <link rel="icon" href="<%=request.getContextPath()%>/images/favicon.ico">
    <style>
        body { font-family: Arial, sans-serif; padding: 40px; background: #f4f6f9; }
        .container { max-width: 800px; margin: auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
        h2 { text-align: center; color: #333; }
        label { display: block; margin: 15px 0 5px; font-weight: bold; }
        input[type="text"], textarea {
            width: 100%;
            padding: 10px;
            margin-bottom: 20px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 16px;
        }
        textarea {
            resize: vertical;
            min-height: 80px;
        }
        input[type="submit"] {
            background: #007BFF;
            color: white;
            padding: 12px 30px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
        }
        input[type="submit"]:hover { background: #0056b3; }
        .btn-group { text-align: center; margin-top: 20px; }
        .separator { border-top: 1px solid #eee; margin: 20px 0; }
        .session-section {
            background: #f8f9fa;
            padding: 15px;
            border-radius: 8px;
            border: 1px solid #e9ecef;
            margin-bottom: 30px;
        }
        .session-section h3 {
            margin-top: 0;
            color: #495057;
            font-size: 18px;
        }
        .divider {
            display: flex;
            align-items: center;
            text-align: center;
            margin: 25px 0;
            color: #888;
            font-size: 14px;
        }

        .divider::before,
        .divider::after {
            content: '';
            flex: 1;
            border-bottom: 1px solid #ccc;
        }

        .divider::before {
            margin-right: 10px;
        }

        .divider::after {
            margin-left: 10px;
        }
        #flow_id_skipwfm::placeholder {
            color: red;
            opacity: 1;
        }
        #flow_id_skipwfm::-moz-placeholder {
            color: red;
            opacity: 1;
        }
        #flow_id_skipwfm::-webkit-input-placeholder {
            color: red;
            opacity: 1;
        }
    </style>
</head>
<!-- 空闲检测与自动登出 -->
<%--<script>--%>
<%--    const IDLE_TIMEOUT = 5 * 60 * 1000; // 15分钟--%>
<%--    let idleTimer = null;--%>

<%--    function resetIdleTimer() {--%>
<%--        if (idleTimer) {--%>
<%--            clearTimeout(idleTimer);--%>
<%--        }--%>
<%--        document.title = "Order Update Tool";--%>
<%--        idleTimer = setTimeout(() => {--%>
<%--            alert("You have been automatically logged out due to prolonged inactivity.");--%>
<%--            window.location.href = "logout";--%>
<%--        }, IDLE_TIMEOUT);--%>
<%--    }--%>

<%--    function setupIdleDetection() {--%>
<%--        const events = ['mousedown', 'mousemove', 'keypress', 'scroll', 'touchstart'];--%>
<%--        events.forEach(event => {--%>
<%--            document.addEventListener(event, resetIdleTimer, true);--%>
<%--        });--%>
<%--        resetIdleTimer(); // 启动--%>
<%--    }--%>

<%--    document.addEventListener('DOMContentLoaded', setupIdleDetection);--%>
<%--</script>--%>
<body>
<div class="container">
<%--        &lt;%&ndash; ✅ 登出按钮：放在顶部右侧 &ndash;%&gt;--%>
<%--            <div style="--%>
<%--    position: fixed;--%>
<%--    top: 20px;--%>
<%--    right: 20px;--%>
<%--    z-index: 1000;--%>
<%--">--%>
<%--                <form action="logout" method="post" style="--%>
<%--        background: #f8f9fa;--%>
<%--        border-radius: 8px;--%>
<%--        border: 1px solid #e9ecef;--%>
<%--        padding: 10px 15px;--%>
<%--        display: inline-block;--%>
<%--        text-align: right;--%>
<%--        box-shadow: 0 4px 6px rgba(0,0,0,0.1);--%>
<%--    ">--%>
<%--                    <small style="--%>
<%--            color: #555;--%>
<%--            font-size: 14px;--%>
<%--            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;--%>
<%--        ">--%>
<%--                        Account:--%>
<%--                        <strong style="--%>
<%--                font-weight: 600;--%>
<%--                color: #1a1a1a;--%>
<%--                background: #d0ebff;--%>
<%--                padding: 2px 8px;--%>
<%--                margin: 0 8px;--%>
<%--                border-radius: 6px;--%>
<%--                font-family: 'Courier New', monospace;--%>
<%--                letter-spacing: 0.3px;--%>
<%--            ">--%>
<%--                            <%= session.getAttribute("username") != null ? session.getAttribute("username") : "Guest" %>--%>
<%--                        </strong>--%>
<%--                        |--%>
<%--                    </small>--%>
<%--                    <button type="submit" style="--%>
<%--            background: #dc3545;--%>
<%--            color: white;--%>
<%--            padding: 8px 16px;--%>
<%--            border: none;--%>
<%--            border-radius: 6px;--%>
<%--            cursor: pointer;--%>
<%--            font-size: 14px;--%>
<%--            font-weight: 500;--%>
<%--            transition: background 0.3s ease, transform 0.1s ease;--%>
<%--            display: inline-flex;--%>
<%--            align-items: center;--%>
<%--            gap: 6px;--%>
<%--            box-shadow: 0 1px 3px rgba(0,0,0,0.1);--%>
<%--            margin-left: 10px;--%>
<%--        "--%>
<%--                            onmouseover="this.style.background='#c82333'"--%>
<%--                            onmouseout="this.style.background='#dc3545'"--%>
<%--                            onmousedown="this.style.transform='scale(0.96)'"--%>
<%--                            onmouseup="this.style.transform='scale(1)'">--%>
<%--                        🔐 Logout--%>
<%--                    </button>--%>
<%--                </form>--%>
<%--            </div>--%>
    <h2>🔧 Order Update Tool (Batch Mode)</h2>

    <%-- ✅ 会话管理：保持单个输入即可 --%>
    <div class="session-section">
        <h3>🔐 Session Manage</h3>
        <form action="ExecuteServlet" method="post">
            <input type="hidden" name="action" value="setSession"/>
            <label for="jsessionid">Input JSESSIONID:</label>
            <input type="text" id="jsessionid" name="jsessionid" placeholder="例如: ABC123XYZ789" />
            <input type="submit" value="SettingSesion (JSESSIONID)" />
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
    <%-- ✅ NEW: Update SN (Batch) --%>
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

    <!-- ========== IM 异常单 ========== -->
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
            <option value="success">Mark as Success</option>
            <option value="failed">Mark as Failed</option>
        </select>
        <br /><br />

        <input type="submit" value="Update Install Status" />
    </form>
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
    <script>
        document.querySelectorAll('form').forEach(form => {
            form.addEventListener('submit', function() {
                const submitBtn = this.querySelector('input[type="submit"]');
                if (submitBtn) {
                    submitBtn.disabled = true;
                    submitBtn.value = 'Processing...';
                }
            });
        });
    </script>
    <script>
        function updatePlaceholder(system) {
            var input = document.getElementById('user_ids');
            if (input) {
                if (system) {
                    input.placeholder = "e.g. user1,user2,user3 (" + system + ")";
                } else {
                    input.placeholder = "e.g. user1,user2,user3";
                }
            }
        }
    </script>
    <script>
        const toggle = document.getElementById('togglePassword');
        const password = document.getElementById('password');

        toggle.addEventListener('click', () => {
            const type = password.getAttribute('type') === 'password' ? 'text' : 'password';
            password.setAttribute('type', type);

            // 可切换眼睛图标（可选）
            toggle.textContent = type === 'password' ? '👁️' : '🙈';
        });
    </script>

</body>
</html>