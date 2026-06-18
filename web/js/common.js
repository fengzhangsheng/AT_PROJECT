// 公共 JavaScript 功能

function preventDoubleSubmit(form) {  // ✅ 修正：函数名无空格
    const submitBtn = form.querySelector('input[type="submit"]');
    if (submitBtn && submitBtn.disabled) return false;
    if (submitBtn) {
        submitBtn.disabled = true;
        submitBtn.value = 'Processing...';
    }
    return true;
}

function normalizeTextareaInput(textarea) {
    const lines = textarea.value
        .split('\n')
        .map(line => line.trim())
        .filter(line => line.length > 0);
    textarea.value = lines.join('\n');
}

function updatePlaceholder(system) {
    const input = document.getElementById('user_ids');
    if (input) {
        input.placeholder = system
            ? `e.g. user1,user2,user3 (${system})`
            : "e.g. user1,user2,user3";
    }
}

// 等 DOM 加载完成后再执行所有初始化
document.addEventListener('DOMContentLoaded', function () {

    // 1. 表单提交统一处理（合并防重 + 清理）
    document.querySelectorAll('form').forEach(form => {
        form.addEventListener('submit', function (e) {
            // 清理所有 textarea
            form.querySelectorAll('textarea').forEach(normalizeTextareaInput);
            // 防重复提交
            // if (!preventDoubleSubmit(form)) {  // ✅ 修正：调用正确的函数名
            //     e.preventDefault();
            // }
        });
    });

    // 2. 密码可见切换
    const toggle = document.getElementById('togglePassword');
    const password = document.getElementById('password');
    if (toggle && password) {
        toggle.addEventListener('click', () => {
            const type = password.getAttribute('type') === 'password' ? 'text' : 'password';
            password.setAttribute('type', type);
            toggle.textContent = type === 'password' ? '👁️' : '🙈';
        });
    }

    // 3. 导航高亮
    const currentPath = window.location.pathname;
    document.querySelectorAll('.sidebar a').forEach(link => {
        const href = link.getAttribute('href');
        if (href && currentPath.endsWith(href.split('/').pop())) {
            link.classList.add('active');
        }
    });

    // 注意：updatePlaceholder 是函数，需在需要的地方手动调用
    // 例如：某个下拉框 onchange 时调用 updatePlaceholder(selectedSystem);
});