const API = 'http://localhost:8080';
let token = null;
let userId = null;
let pollInterval = null;

function showLogin() {
    document.getElementById('loginView').classList.remove('hidden');
    document.getElementById('registerView').classList.add('hidden');
    document.getElementById('chatView').classList.add('hidden');
}

function showRegister() {
    document.getElementById('registerView').classList.remove('hidden');
    document.getElementById('loginView').classList.add('hidden');
    document.getElementById('chatView').classList.add('hidden');
}

function showChat() {
    document.getElementById('chatView').classList.remove('hidden');
    document.getElementById('loginView').classList.add('hidden');
    document.getElementById('registerView').classList.add('hidden');
    loadMessages();
    pollInterval = setInterval(loadMessages, 3000);
}

async function register() {
    const body = {
        firstName: document.getElementById('regFirstName').value,
        lastName: document.getElementById('regLastName').value,
        email: document.getElementById('regEmail').value,
        username: document.getElementById('regUsername').value,
        password: document.getElementById('regPassword').value
    };
    try {
        const res = await fetch(`${API}/users/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });
        if (res.ok) {
            document.getElementById('registerSuccess').textContent = 'Konto skapat! Du kan nu logga in.';
            document.getElementById('registerError').textContent = '';
            setTimeout(showLogin, 1500);
        } else {
            document.getElementById('registerError').textContent = 'Något gick fel. Försök igen.';
        }
    } catch (e) {
        document.getElementById('registerError').textContent = 'Kunde inte ansluta till servern.';
    }
}

async function login() {
    const body = {
        username: document.getElementById('loginUsername').value,
        password: document.getElementById('loginPassword').value
    };
    try {
        const res = await fetch(`${API}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });
        if (res.ok) {
            const data = await res.json();
            token = data.token;
            const payload = JSON.parse(atob(token.split('.')[1]));
            userId = payload.sub;
            showChat();
        } else {
            document.getElementById('loginError').textContent = 'Fel användarnamn eller lösenord.';
        }
    } catch (e) {
        document.getElementById('loginError').textContent = 'Kunde inte ansluta till servern.';
    }
}

async function sendMessage() {
    const input = document.getElementById('messageInput');
    const content = input.value.trim();
    if (!content) return;
    try {
        await fetch(`${API}/messages`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({ senderId: userId, content })
        });
        input.value = '';
        loadMessages();
    } catch (e) {
        console.error('Kunde inte skicka meddelande', e);
    }
}

async function loadMessages() {
    try {
        const res = await fetch(`${API}/messages`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        if (res.ok) {
            const messages = await res.json();
            const list = document.getElementById('messageList');
            list.innerHTML = messages.map(m => `
                <div class="message">
                    <div>${m.content}</div>
                    <div class="time">${new Date(m.sentAt).toLocaleTimeString('sv-SE')}</div>
                </div>
            `).join('');
            list.scrollTop = list.scrollHeight;
        }
    } catch (e) {
        console.error('Kunde inte hämta meddelanden', e);
    }
}

function handleKey(event) {
    if (event.key === 'Enter') sendMessage();
}

function logout() {
    token = null;
    userId = null;
    clearInterval(pollInterval);
    showLogin();
}