// ============================================================
//  SchoLife Admin Portal — main.js
//  Place in: src/main/resources/static/main.js
// ============================================================

// ── 1. PAGE LOAD ANIMATIONS ─────────────────────────────────
document.addEventListener('DOMContentLoaded', () => {
    initAnimations();
    initSidebar();
    initSearch();
    initToasts();
    initModals();
    initTabs();
    initCharts();
    initGreeting();
    initHttpMethods();
});

// Staggered fade-up on load
function initAnimations() {
    const elements = document.querySelectorAll('.fu');
    elements.forEach((el, i) => {
        el.style.opacity = '0';
        el.style.transform = 'translateY(16px)';
        el.style.transition = 'opacity 0.4s ease, transform 0.4s ease';
        setTimeout(() => {
            el.style.opacity = '1';
            el.style.transform = 'translateY(0)';
        }, i * 80);
    });

    // Stat card number count-up animation
    document.querySelectorAll('.sc-value').forEach(el => {
        const target = parseInt(el.textContent.replace(/,/g, '')) || 0;
        if (!target) return;
        let current = 0;
        const step = Math.ceil(target / 40);
        const timer = setInterval(() => {
            current = Math.min(current + step, target);
            el.textContent = current.toLocaleString();
            if (current >= target) clearInterval(timer);
        }, 30);
    });

    // Enrollment bar fill animation
    document.querySelectorAll('.enroll-fill').forEach(el => {
        const width = el.style.width;
        el.style.width = '0%';
        el.style.transition = 'width 0.8s ease';
        setTimeout(() => { el.style.width = width; }, 400);
    });

    // Stat card hover effect
    document.querySelectorAll('.stat-card').forEach(card => {
        card.addEventListener('mouseenter', () => {
            card.style.transform = 'translateY(-3px)';
            card.style.boxShadow = '0 8px 30px rgba(123,28,53,0.12)';
            card.style.transition = 'all 0.2s ease';
        });
        card.addEventListener('mouseleave', () => {
            card.style.transform = 'translateY(0)';
            card.style.boxShadow = '';
        });
    });
}

// ── 2. SIDEBAR ───────────────────────────────────────────────
function initSidebar() {
    // Highlight current page in sidebar
    const currentPath = window.location.pathname;
    document.querySelectorAll('.nav-item').forEach(link => {
        link.classList.remove('active');
        if (link.getAttribute('href') === currentPath) {
            link.classList.add('active');
        }
    });

    // Mobile toggle (if sidebar toggle button exists)
    const toggleBtn = document.getElementById('sidebarToggle');
    const sidebar   = document.querySelector('.sidebar');
    if (toggleBtn && sidebar) {
        toggleBtn.addEventListener('click', () => {
            sidebar.classList.toggle('sidebar--open');
        });
    }
}

// ── 3. SEARCH ────────────────────────────────────────────────
function initSearch() {
    const searchInput = document.querySelector('.topbar-search input');
    if (!searchInput) return;

    searchInput.addEventListener('input', (e) => {
        const q = e.target.value.toLowerCase().trim();
        if (!q) {
            document.querySelectorAll('.data-table tbody tr').forEach(r => r.style.display = '');
            return;
        }
        document.querySelectorAll('.data-table tbody tr').forEach(row => {
            row.style.display = row.textContent.toLowerCase().includes(q) ? '' : 'none';
        });
    });

    // Keyboard shortcut: Ctrl+K to focus search
    document.addEventListener('keydown', (e) => {
        if ((e.ctrlKey || e.metaKey) && e.key === 'k') {
            e.preventDefault();
            searchInput.focus();
            searchInput.select();
        }
    });
}

// ── 4. TOAST NOTIFICATIONS ───────────────────────────────────
function initToasts() {
    // Create toast container if it doesn't exist
    if (!document.getElementById('toastContainer')) {
        const container = document.createElement('div');
        container.id = 'toastContainer';
        container.style.cssText = `
      position: fixed; bottom: 1.5rem; right: 1.5rem;
      display: flex; flex-direction: column; gap: 0.7rem;
      z-index: 9999; pointer-events: none;
    `;
        document.body.appendChild(container);
    }
}

// Call this anywhere: showToast('Saved!', 'success')
function showToast(message, type = 'success') {
    const container = document.getElementById('toastContainer');
    if (!container) return;

    const colors = {
        success: { bg: '#f0fdf4', border: '#bbf7d0', text: '#16a34a', icon: '✅' },
        error:   { bg: '#fef2f2', border: '#fecaca', text: '#dc2626', icon: '❌' },
        warning: { bg: '#fffbeb', border: '#fde68a', text: '#d97706', icon: '⚠️' },
        info:    { bg: '#eff6ff', border: '#bfdbfe', text: '#2563eb', icon: 'ℹ️' },
    };
    const c = colors[type] || colors.info;

    const toast = document.createElement('div');
    toast.style.cssText = `
    background: ${c.bg}; border: 1.5px solid ${c.border}; color: ${c.text};
    padding: 0.85rem 1.2rem; border-radius: 12px;
    font-family: 'DM Sans', sans-serif; font-size: 0.88rem; font-weight: 600;
    display: flex; align-items: center; gap: 0.6rem;
    box-shadow: 0 4px 20px rgba(0,0,0,0.1);
    pointer-events: all; cursor: pointer;
    opacity: 0; transform: translateX(20px);
    transition: all 0.3s ease; min-width: 260px;
  `;
    toast.innerHTML = `<span>${c.icon}</span><span>${message}</span>`;
    container.appendChild(toast);

    requestAnimationFrame(() => {
        toast.style.opacity = '1';
        toast.style.transform = 'translateX(0)';
    });

    const remove = () => {
        toast.style.opacity = '0';
        toast.style.transform = 'translateX(20px)';
        setTimeout(() => toast.remove(), 300);
    };
    toast.addEventListener('click', remove);
    setTimeout(remove, 3500);
}

// ── 5. MODALS ────────────────────────────────────────────────
function initModals() {
    // Close modal when clicking overlay
    document.querySelectorAll('.modal-overlay').forEach(overlay => {
        overlay.addEventListener('click', (e) => {
            if (e.target === overlay) closeModal(overlay.id);
        });
    });

    // Close on Escape key
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') {
            document.querySelectorAll('.modal-overlay.open').forEach(m => {
                closeModal(m.id);
            });
        }
    });
}

function openModal(id) {
    const modal = document.getElementById(id);
    if (!modal) return;
    modal.classList.add('open');
    modal.style.animation = 'modalIn 0.25s ease';
    document.body.style.overflow = 'hidden';
}

function closeModal(id) {
    const modal = document.getElementById(id);
    if (!modal) return;
    modal.classList.remove('open');
    document.body.style.overflow = '';
}

// ── 6. FILTER TABS ───────────────────────────────────────────
function initTabs() {
    document.querySelectorAll('.filter-tabs').forEach(group => {
        group.querySelectorAll('.ftab').forEach(tab => {
            tab.addEventListener('click', () => {
                group.querySelectorAll('.ftab').forEach(t => t.classList.remove('active'));
                tab.classList.add('active');
                const cat = tab.dataset.filter || '';
                filterTable(cat);
            });
        });
    });
}

function filterTable(cat) {
    document.querySelectorAll('.data-table tbody tr').forEach(row => {
        row.style.display = (!cat || row.dataset.cat === cat) ? '' : 'none';
    });
}

// ── 7. ENROLLMENT CHART (Chart.js) ───────────────────────────
function initCharts() {
    const canvas = document.getElementById('enrollmentChart');
    if (!canvas || typeof Chart === 'undefined') return;

    new Chart(canvas, {
        type: 'doughnut',
        data: {
            labels: ['Engineering', 'Business', 'Education', 'Arts & Sci', 'Nursing', 'Computing'],
            datasets: [{
                data: [385, 308, 249, 198, 108, 150],
                backgroundColor: ['#7B1C35','#9B2D46','#C9930A','#b87209','#16a34a','#2563eb'],
                borderWidth: 0,
                hoverOffset: 8,
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { position: 'bottom', labels: { font: { family: 'DM Sans' } } }
            },
            cutout: '65%',
        }
    });
}

// ── 8. GREETING — Hello (username) ──────────────────────────
function initGreeting() {
    const greetEl = document.getElementById('userGreeting');
    if (!greetEl) return;

    // Get username from data attribute set by Thymeleaf
    const name = greetEl.dataset.name || 'Admin';
    const hour = new Date().getHours();
    let timeGreet = 'Good morning';
    if (hour >= 12 && hour < 17) timeGreet = 'Good afternoon';
    if (hour >= 17)              timeGreet = 'Good evening';

    greetEl.textContent = `${timeGreet}, ${name}! 👋`;

    // Animate greeting
    greetEl.style.opacity = '0';
    greetEl.style.transform = 'translateY(-8px)';
    greetEl.style.transition = 'all 0.5s ease';
    setTimeout(() => {
        greetEl.style.opacity = '1';
        greetEl.style.transform = 'translateY(0)';
    }, 200);
}

// ── 9. HTTP METHODS — CRUD via fetch ─────────────────────────
function initHttpMethods() {
    // Attach delete buttons
    document.querySelectorAll('[data-delete-url]').forEach(btn => {
        btn.addEventListener('click', async () => {
            const url  = btn.dataset.deleteUrl;
            const name = btn.dataset.name || 'this item';
            if (!confirm(`Delete ${name}?`)) return;

            try {
                const res = await ScholifeAPI.delete(url);
                if (res.ok) {
                    btn.closest('tr')?.remove();
                    showToast(`${name} deleted successfully.`, 'success');
                } else {
                    showToast('Delete failed. Please try again.', 'error');
                }
            } catch {
                showToast('Connection error.', 'error');
            }
        });
    });

    // Attach status toggle buttons
    document.querySelectorAll('[data-toggle-status]').forEach(btn => {
        btn.addEventListener('click', async () => {
            const url = btn.dataset.toggleStatus;
            try {
                const res = await ScholifeAPI.patch(url, {});
                const data = await res.json();
                showToast(`Status updated to ${data.status}.`, 'success');
                setTimeout(() => location.reload(), 1000);
            } catch {
                showToast('Update failed.', 'error');
            }
        });
    });
}

// ── 10. ScholifeAPI — HTTP Methods wrapper ───────────────────
const ScholifeAPI = {

    // GET — fetch data
    get: (url) =>
        fetch(url, {
            method: 'GET',
            headers: { 'Accept': 'application/json' },
        }),

    // POST — create new record
    post: (url, body) =>
        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept':       'application/json',
                'X-CSRF-TOKEN': getCsrfToken(),
            },
            body: JSON.stringify(body),
        }),

    // PUT — update entire record
    put: (url, body) =>
        fetch(url, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Accept':       'application/json',
                'X-CSRF-TOKEN': getCsrfToken(),
            },
            body: JSON.stringify(body),
        }),

    // PATCH — partial update (e.g. status toggle)
    patch: (url, body) =>
        fetch(url, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
                'Accept':       'application/json',
                'X-CSRF-TOKEN': getCsrfToken(),
            },
            body: JSON.stringify(body),
        }),

    // DELETE — remove record
    delete: (url) =>
        fetch(url, {
            method: 'DELETE',
            headers: {
                'Accept':       'application/json',
                'X-CSRF-TOKEN': getCsrfToken(),
            },
        }),
};

// Get CSRF token from meta tag (set by Thymeleaf in HTML head)
function getCsrfToken() {
    return document.querySelector('meta[name="_csrf"]')?.getAttribute('content') || '';
}

// ── 11. STUDENT API CALLS ────────────────────────────────────
const StudentAPI = {

    // GET all students
    getAll: async () => {
        const res = await ScholifeAPI.get('/api/students');
        return res.json();
    },

    // GET one student
    getById: async (id) => {
        const res = await ScholifeAPI.get(`/api/students/${id}`);
        return res.json();
    },

    // POST — add new student (used by Add Student modal)
    create: async (studentData) => {
        const res = await ScholifeAPI.post('/api/students', studentData);
        const data = await res.json();
        if (res.ok) {
            showToast(`Student ${studentData.firstName} added!`, 'success');
            return { success: true, data };
        } else {
            showToast(data.message || 'Failed to add student.', 'error');
            return { success: false, data };
        }
    },

    // PUT — update student
    update: async (id, studentData) => {
        const res = await ScholifeAPI.put(`/api/students/${id}`, studentData);
        const data = await res.json();
        if (res.ok) {
            showToast('Student updated!', 'success');
            return { success: true, data };
        } else {
            showToast(data.message || 'Update failed.', 'error');
            return { success: false, data };
        }
    },

    // DELETE — remove student
    delete: async (id, name) => {
        if (!confirm(`Delete student ${name}?`)) return { success: false };
        const res = await ScholifeAPI.delete(`/api/students/${id}`);
        if (res.ok) {
            showToast(`${name} removed.`, 'success');
            return { success: true };
        } else {
            showToast('Delete failed.', 'error');
            return { success: false };
        }
    },
};

// ── 12. ORGANIZATION API CALLS ───────────────────────────────
const OrganizationAPI = {
    getAll:  async ()       => (await ScholifeAPI.get('/api/organizations')).json(),
    create:  async (data)   => (await ScholifeAPI.post('/api/organizations', data)).json(),
    update:  async (id, d)  => (await ScholifeAPI.put(`/api/organizations/${id}`, d)).json(),
    delete:  async (id)     => ScholifeAPI.delete(`/api/organizations/${id}`),
};

// ── 13. ADMIN API CALLS ──────────────────────────────────────
const AdminAPI = {
    getAll:  async ()       => (await ScholifeAPI.get('/api/admins')).json(),
    create:  async (data)   => (await ScholifeAPI.post('/api/admins', data)).json(),
    update:  async (id, d)  => (await ScholifeAPI.put(`/api/admins/${id}`, d)).json(),
    delete:  async (id)     => ScholifeAPI.delete(`/api/admins/${id}`),
};

// ── 14. ROLE CARD SELECTION ──────────────────────────────────
function selectRole(el) {
    document.querySelectorAll('.role-card').forEach(c => c.classList.remove('selected'));
    el.classList.add('selected');
}

// ── 15. PASSWORD STRENGTH METER ──────────────────────────────
function checkStrength(val) {
    const bars  = ['sb1','sb2','sb3','sb4'].map(id => document.getElementById(id));
    const label = document.getElementById('slabel');
    if (!bars[0]) return;
    bars.forEach(b => { if (b) b.className = 'sbar'; });
    if (!val) { if (label) label.textContent = ''; return; }
    let score = 0;
    if (val.length >= 8)          score++;
    if (/[A-Z]/.test(val))        score++;
    if (/[0-9]/.test(val))        score++;
    if (/[^A-Za-z0-9]/.test(val)) score++;
    const cls    = ['weak','fair','good','strong'][score - 1] || 'weak';
    const labels = ['Weak','Fair','Good','Strong'];
    for (let i = 0; i < score; i++) if (bars[i]) bars[i].classList.add(cls);
    if (label) {
        label.textContent = labels[score - 1] || '';
        label.className   = 'strength-label ' + cls;
    }
}

// ── 16. PASSWORD TOGGLE ──────────────────────────────────────
function togglePw(id) {
    const f = document.getElementById(id);
    if (!f) return;
    f.type = f.type === 'password' ? 'text' : 'password';
}

// ── 17. DEMO FILL (login page) ───────────────────────────────
function fillDemo() {
    const email = document.getElementById('emailField');
    const pw    = document.getElementById('pwField');
    if (email) email.value = 'admin@scholife.edu';
    if (pw)    pw.value    = 'Admin@1234';
}

// ── 18. CSS ANIMATIONS (injected) ───────────────────────────
const style = document.createElement('style');
style.textContent = `
  @keyframes modalIn {
    from { opacity: 0; transform: scale(0.96) translateY(-10px); }
    to   { opacity: 1; transform: scale(1) translateY(0); }
  }
  @keyframes slideIn {
    from { opacity: 0; transform: translateX(30px); }
    to   { opacity: 1; transform: translateX(0); }
  }
  @keyframes fadeUp {
    from { opacity: 0; transform: translateY(12px); }
    to   { opacity: 1; transform: translateY(0); }
  }
  .nav-item { transition: background 0.15s, color 0.15s; }
  .btn      { transition: all 0.15s; }
  .card     { transition: box-shadow 0.2s; }
  .modal-overlay { transition: opacity 0.2s; }
  .modal-overlay:not(.open) { opacity: 0; pointer-events: none; }
  .modal-overlay.open { opacity: 1; }
`;
document.head.appendChild(style);