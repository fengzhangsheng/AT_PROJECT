<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="shortcut icon" href="<%=request.getContextPath()%>/images/favicon.ico">
    <link rel="icon" href="<%=request.getContextPath()%>/images/favicon.ico">
    <title>Welcome - OM Tools</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            background: linear-gradient(135deg, #e6f7ff, #00b0ff);
            min-height: 100vh;
            position: relative;
            overflow: hidden;
            font-family: 'Segoe UI', Arial, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        /* 流动波浪 */
        .wave {
            position: absolute;
            bottom: -50px;
            left: 0;
            width: 200%;
            height: 150px;
            background: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1200 120'%3E%3Cpath d='M0,60 Q300,10 600,60 T1200,60 L1200,120 L0,120 Z' fill='none' stroke='rgba(255,255,255,0.15)' stroke-width='1'/%3E%3C/svg%3E") repeat-x;
            animation: wave 12s infinite linear;
            z-index: 1;
        }

        @keyframes wave {
            0% { transform: translateX(0); }
            100% { transform: translateX(-50%); }
        }

        /* 光点粒子 */
        .particle {
            position: absolute;
            border-radius: 50%;
            background: rgba(255, 255, 255, 0.7);
            filter: blur(0.5px);
            z-index: 2;
        }

        /* 科技图标容器 */
        .tech-icon {
            position: absolute;
            opacity: 0.25;
            z-index: 2;
            pointer-events: none; /* 不阻挡点击 */
        }

        /* 按钮 —— 完全嵌入背景 */
        .enter-btn {
            position: relative;
            padding: 16px 52px;
            background: rgba(255, 255, 255, 0.18);
            backdrop-filter: blur(12px);
            border: 1px solid rgba(255, 255, 255, 0.35);
            color: white;
            text-decoration: none;
            font-size: 19px;
            font-weight: 600;
            border-radius: 10px;
            transition: all 0.3s ease;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.12);
            cursor: pointer;
            z-index: 10;
            letter-spacing: 0.5px;
        }

        .enter-btn:hover {
            background: rgba(255, 255, 255, 0.25);
            transform: translateY(-3px);
            box-shadow: 0 6px 25px rgba(0, 0, 0, 0.18);
        }
    </style>
</head>
<body>
<!-- 波浪 -->
<div class="wave"></div>

<!-- 按钮 -->
<a href="${pageContext.request.contextPath}/pages/om_tools.jsp" class="enter-btn">Enter System</a>


<script>
    // === 1. 生成随机光点 ===
    for (let i = 0; i < 40; i++) {
        const p = document.createElement('div');
        p.className = 'particle';
        p.style.left = `${Math.random() * 100}%`;
        p.style.top = `${Math.random() * 100}%`;
        p.style.width = p.style.height = `${Math.random() * 3 + 2}px`;
        p.style.opacity = Math.random() * 0.5 + 0.2;
        document.body.appendChild(p);
    }

    // === 2. 内联 SVG 图标定义（无网络依赖）===
    const icons = [
        `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="32" height="32"><path fill="white" d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5c-1.38 0-2.5-1.12-2.5-2.5s1.12-2.5 2.5-2.5 2.5 1.12 2.5 2.5-1.12 2.5-2.5 2.5z"/></svg>`, // Wi-Fi
        `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="32" height="32"><path fill="white" d="M18 8h-2V5c0-1.1-.9-2-2-2H8c-1.1 0-2 .9-2 2v3H2v2h2v10h12V10h2V8zM9 16h6v-2H9v2zm0-4h6V8H9v4z"/></svg>`, // Lock
        `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="32" height="32"><path fill="white" d="M19.35 10.04A7.49 7.49 0 0 0 12 4C9.11 4 6.6 5.64 5.35 8.04A5.994 5.994 0 0 0 0 14c0 3.31 2.69 6 6 6h13c2.76 0 5-2.24 5-5 0-2.64-2.05-4.78-4.65-4.96z"/></svg>`, // Cloud
        `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="32" height="32"><path fill="white" d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-1 17.93c-3.95-.49-7-3.85-7-7.93 0-.62.08-1.21.21-1.79L9 15v1c0 1.1.9 2 2 2v1.93zm6.9-2.54c-.24-.83-.74-1.54-1.4-2.08L14 14v-1c0-1.1-.9-2-2-2H8.21l-.21.93c-.5.21-.97.48-1.4.79 1.37 1.37 3.22 2.21 5.4 2.21 2.18 0 4.03-.84 5.4-2.21z"/></svg>` // Earth
    ];

    // === 3. 随机放置图标 ===
    const positions = [
        { x: '10%', y: '15%' },
        { x: '85%', y: '20%' },
        { x: '15%', y: '80%' },
        { x: '80%', y: '75%' }
    ];

    icons.forEach((svgStr, i) => {
        if (i < positions.length) {
            const div = document.createElement('div');
            div.className = 'tech-icon';
            div.innerHTML = svgStr;
            div.style.left = positions[i].x;
            div.style.top = positions[i].y;
            document.body.appendChild(div);
        }
    });
</script>
</body>
</html>