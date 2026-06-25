// Ai-ChatBot Frontend Controller Logic

// Core variables & state
let token = localStorage.getItem('jwt_token') || '';
let userId = parseInt(localStorage.getItem('user_id')) || 0;
let sessionId = parseInt(localStorage.getItem('session_id')) || 0;
let username = localStorage.getItem('username') || 'Guest User';
let email = localStorage.getItem('email') || 'guest@ai-chatbot.local';
let apiBaseUrl = localStorage.getItem('api_base_url') || '';
let localHistory = [];

// Initialize application
document.addEventListener('DOMContentLoaded', () => {
    // Load config display
    document.getElementById('settings-api-url').value = apiBaseUrl;
    document.getElementById('settings-user-id').value = userId;
    document.getElementById('settings-session-id').value = sessionId;
    
    updateConnectionDetails();

    if (token) {
        showScreen('app-screen');
        loadChatHistory();
    } else {
        showScreen('auth-screen');
    }

    // Auto-resize textarea listener
    const textarea = document.getElementById('chat-textarea');
    textarea.addEventListener('input', () => {
        textarea.style.height = 'auto';
        textarea.style.height = (textarea.scrollHeight - 4) + 'px';
        
        // Character counter
        const len = textarea.value.length;
        document.getElementById('char-counter').textContent = `${len} / 2000`;
    });
});

// Update display metrics on the sidebar
function updateConnectionDetails() {
    document.getElementById('display-user-id').textContent = userId;
    document.getElementById('display-session-id').textContent = sessionId;
    document.getElementById('display-username').textContent = username;
    document.getElementById('display-email').textContent = email;
    
    // Set avatar letters
    const letters = username.split(' ').map(n => n[0]).join('').substring(0, 2).toUpperCase();
    document.getElementById('avatar-letters').textContent = letters || 'US';

    // Test health check (dummy verification of API)
    if (apiBaseUrl) {
        document.getElementById('display-api-status').textContent = "Synced Node";
        document.getElementById('display-api-status').className = "val status-ok";
    } else {
        document.getElementById('display-api-status').textContent = "Local Host";
        document.getElementById('display-api-status').className = "val status-ok";
    }
}

// Route manager
function showScreen(screenId) {
    document.querySelectorAll('.screen-section').forEach(s => s.classList.remove('active'));
    document.getElementById(screenId).classList.add('active');
}

// Auth Tab Switching
function switchAuthTab(tab) {
    document.querySelectorAll('.auth-tab').forEach(t => t.classList.remove('active'));
    document.querySelectorAll('.auth-form').forEach(f => f.classList.remove('active'));
    
    if (tab === 'login') {
        document.getElementById('tab-login').classList.add('active');
        document.getElementById('form-login').classList.add('active');
    } else {
        document.getElementById('tab-register').classList.add('active');
        document.getElementById('form-register').classList.add('active');
    }
}

// Decodes standard HS256 JWT payloads client-side
function decodeJwt(token) {
    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));
        return JSON.parse(jsonPayload);
    } catch (e) {
        console.error("JWT decoding failed", e);
        return null;
    }
}

// Helper to make authenticated fetch requests
async function makeRequest(endpoint, options = {}) {
    const url = `${apiBaseUrl}${endpoint}`;
    
    options.headers = options.headers || {};
    if (token) {
        options.headers['Authorization'] = `Bearer ${token}`;
    }
    if (!(options.body instanceof FormData)) {
        options.headers['Content-Type'] = 'application/json';
    }

    try {
        const response = await fetch(url, options);
        if (!response.ok) {
            const errText = await response.text();
            throw new Error(errText || `Server responded with HTTP status ${response.status}`);
        }
        return response;
    } catch (error) {
        console.error(`Request to ${url} failed`, error);
        throw error;
    }
}

// Handle login submissions
async function handleLogin(e) {
    e.preventDefault();
    const emailInput = document.getElementById('login-email').value;
    const passwordInput = document.getElementById('login-password').value;
    const submitBtn = document.getElementById('btn-login-submit');

    submitBtn.disabled = true;
    submitBtn.style.opacity = '0.7';

    try {
        const res = await makeRequest('/api/auth/login', {
            method: 'POST',
            body: JSON.stringify({ email: emailInput, password: passwordInput })
        });
        
        const data = await res.json();
        
        token = data.token;
        localStorage.setItem('jwt_token', token);
        userId = Number(data.userId || userId || 0);
        sessionId = Number(data.sessionId || sessionId || 0);
        
        // Extract username/email from token or default
        const decoded = decodeJwt(token);
        email = decoded?.sub || emailInput;
        username = email.split('@')[0];
        
        localStorage.setItem('email', email);
        localStorage.setItem('username', username);
        localStorage.setItem('user_id', userId);
        localStorage.setItem('session_id', sessionId);
        
        showToast('Login Successful!', 'success');
        updateConnectionDetails();
        showScreen('app-screen');
        loadChatHistory();
        
    } catch (err) {
        showToast(err.message || 'Login failed. Please check credentials.', 'error');
    } finally {
        submitBtn.disabled = false;
        submitBtn.style.opacity = '1';
    }
}

// Handle registration
async function handleRegister(e) {
    e.preventDefault();
    const nameInput = document.getElementById('register-username').value;
    const emailInput = document.getElementById('register-email').value;
    const passwordInput = document.getElementById('register-password').value;
    const submitBtn = document.getElementById('btn-register-submit');

    submitBtn.disabled = true;
    submitBtn.style.opacity = '0.7';

    try {
        const res = await makeRequest('/api/auth/register', {
            method: 'POST',
            body: JSON.stringify({ username: nameInput, email: emailInput, password: passwordInput })
        });
        
        showToast('Registration Successful! Check your console/email for the token.', 'success');
        switchAuthTab('login');
        
    } catch (err) {
        showToast(err.message || 'Registration failed.', 'error');
    } finally {
        submitBtn.disabled = false;
        submitBtn.style.opacity = '1';
    }
}

// Handle Token Verification
async function handleVerifyToken() {
    const tokenVal = document.getElementById('verify-token-input').value.trim();
    if (!tokenVal) {
        showToast('Please enter a verification token first.', 'warning');
        return;
    }

    try {
        const res = await makeRequest(`/api/auth/verify?token=${encodeURIComponent(tokenVal)}`);
        const text = await res.text();
        showToast(text || 'Verified Successfully!', 'success');
        document.getElementById('verify-token-input').value = '';
    } catch (err) {
        showToast(err.message || 'Verification failed.', 'error');
    }
}

// Log out user
function handleLogout() {
    token = '';
    localStorage.removeItem('jwt_token');
    localHistory = [];
    showScreen('auth-screen');
    showToast('Logged out successfully.', 'info');
}

// Configure settings modal
function openSettings() {
    document.getElementById('settings-modal').classList.add('active');
}

function closeSettings() {
    document.getElementById('settings-modal').classList.remove('active');
}

function saveSettings() {
    apiBaseUrl = document.getElementById('settings-api-url').value.trim();
    userId = parseInt(document.getElementById('settings-user-id').value) || 0;
    sessionId = parseInt(document.getElementById('settings-session-id').value) || 0;

    localStorage.setItem('api_base_url', apiBaseUrl);
    localStorage.setItem('user_id', userId);
    localStorage.setItem('session_id', sessionId);

    closeSettings();
    updateConnectionDetails();
    showToast('Settings saved. Refreshing history...', 'success');
    loadChatHistory();
}

// History modals
function closeSummary() {
    document.getElementById('summary-modal').classList.remove('active');
}

// Toast System
function showToast(msg, type = 'info') {
    const container = document.getElementById('toast-container');
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.innerHTML = `
        <span>${msg}</span>
        <button class="toast-close" onclick="this.parentElement.remove()">&times;</button>
    `;
    container.appendChild(toast);
    
    // Auto remove
    setTimeout(() => {
        toast.style.animation = 'toastSlideIn 0.3s reverse ease forwards';
        setTimeout(() => toast.remove(), 300);
    }, 4000);
}

// Render dynamic chat bubbles
function appendMessage(sender, text, sentiment = '') {
    const list = document.getElementById('dynamic-messages-list');
    const row = document.createElement('div');
    row.className = `message-row ${sender === 'user' ? 'user' : 'system'}`;
    
    // Format text with basic markdown code-blocks
    let formattedText = escapeHtml(text);
    // Replace markdown code blocks
    formattedText = formattedText.replace(/```([\s\S]*?)```/g, '<pre><code>$1</code></pre>');
    // Replace inline code blocks
    formattedText = formattedText.replace(/`([^`\n]+)`/g, '<code>$1</code>');
    // Replace linebreaks with <br>
    formattedText = formattedText.replaceAll('\n', '<br>');

    let sentimentBadgeHtml = '';
    if (sender === 'bot' && sentiment) {
        sentimentBadgeHtml = `<span class="inner-sentiment-badge ${sentiment.toLowerCase()}">Sentiment: ${sentiment}</span>`;
    }

    if (sender === 'user') {
        row.innerHTML = `
            <div class="message-bubble">
                <p>${formattedText}</p>
            </div>
        `;
    } else {
        row.innerHTML = `
            <div class="bot-avatar">
                <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M12 2L2 7L12 12L22 7L12 2Z" fill="currentColor"/>
                </svg>
            </div>
            <div class="message-bubble">
                <p>${formattedText}</p>
                ${sentimentBadgeHtml}
            </div>
        `;
    }

    list.appendChild(row);
    scrollToBottom();
}

function scrollToBottom() {
    const container = document.getElementById('chat-messages-container');
    container.scrollTop = container.scrollHeight;
}

// Safe escape utility
function escapeHtml(unsafe) {
    return unsafe
         .replace(/&/g, "&amp;")
         .replace(/</g, "&lt;")
         .replace(/>/g, "&gt;")
         .replace(/"/g, "&quot;")
         .replace(/'/g, "&#039;");
}

// Caching local storage history or downloading it from database
async function loadChatHistory() {
    const list = document.getElementById('dynamic-messages-list');
    list.innerHTML = '';

    // Load from local storage
    const cacheKey = `ai_chatbot_history_${userId}`;
    const cached = localStorage.getItem(cacheKey);
    
    if (cached) {
        localHistory = JSON.parse(cached);
        if (localHistory.length > 0) {
            document.getElementById('suggestions-box').classList.add('hidden');
            localHistory.forEach(msg => {
                appendMessage(msg.role, msg.content, msg.sentiment);
            });
            return;
        }
    }

    // Fallback to fetch from database
    try {
        const res = await makeRequest(`/api/chat/history/${userId}`);
        const data = await res.json();
        
        if (data && data.length > 0) {
            document.getElementById('suggestions-box').classList.add('hidden');
            
            // Build local history structure (note: db only holds bot responses in return payload history)
            localHistory = [];
            data.forEach(item => {
                // Since user prompt is missing, we populate standard user placeholder if reconstruction is needed,
                // or just display the bot responses.
                localHistory.push({
                    role: 'bot',
                    content: item.response,
                    sentiment: item.sentiment || 'Neutral'
                });
                appendMessage('bot', item.response, item.sentiment);
            });
            localStorage.setItem(cacheKey, JSON.stringify(localHistory));
        } else {
            document.getElementById('suggestions-box').classList.remove('hidden');
        }
    } catch (err) {
        console.warn("Could not load backend chat history, keeping blank chat", err);
        document.getElementById('suggestions-box').classList.remove('hidden');
    }
}

// Send chat message
async function handleSendChat(e) {
    if (e) e.preventDefault();
    
    const textarea = document.getElementById('chat-textarea');
    const text = textarea.value.trim();
    if (!text) return;
    if (!userId) {
        showToast('Please log in again so your account session can be refreshed.', 'warning');
        showScreen('auth-screen');
        return;
    }

    // Reset textarea
    textarea.value = '';
    textarea.style.height = 'auto';
    document.getElementById('char-counter').textContent = '0 / 2000';
    document.getElementById('suggestions-box').classList.add('hidden');

    // Append user message to screen
    appendMessage('user', text);
    localHistory.push({ role: 'user', content: text });
    localStorage.setItem(`ai_chatbot_history_${userId}`, JSON.stringify(localHistory));

    // Show typing loader
    const typingIndicator = document.getElementById('chat-typing-indicator');
    typingIndicator.classList.remove('hidden');
    scrollToBottom();

    try {
        const res = await makeRequest('/api/chat', {
            method: 'POST',
            body: JSON.stringify({
                message: text,
                userId: userId,
                sessionId: sessionId
            })
        });

        const data = await res.json();
        
        // Hide loader
        typingIndicator.classList.add('hidden');

        // Append bot message
        appendMessage('bot', data.response, data.sentiment);
        localHistory.push({ role: 'bot', content: data.response, sentiment: data.sentiment });
        localStorage.setItem(`ai_chatbot_history_${userId}`, JSON.stringify(localHistory));

        // Update header sentiment badge
        const sentimentIndicator = document.getElementById('sentiment-indicator');
        sentimentIndicator.textContent = `Sentiment: ${data.sentiment || 'Neutral'}`;
        sentimentIndicator.classList.remove('hidden');

    } catch (err) {
        typingIndicator.classList.add('hidden');
        showToast('Error getting bot response. Check server connection.', 'error');
        appendMessage('bot', 'Sorry, I failed to process your request because the server returned an error. Please verify the active User ID and Session ID configurations in settings, or check your PostgreSQL status.');
    }
}

// Handle sending quick start suggestions
function sendSuggestion(text) {
    document.getElementById('chat-textarea').value = text;
    handleSendChat();
}

// Clear screen history
function clearLocalHistory() {
    if (confirm("Are you sure you want to clear your current local screen history? This won't affect database logs.")) {
        localHistory = [];
        localStorage.removeItem(`ai_chatbot_history_${userId}`);
        document.getElementById('dynamic-messages-list').innerHTML = '';
        document.getElementById('suggestions-box').classList.remove('hidden');
        document.getElementById('sentiment-indicator').classList.add('hidden');
        showToast('Chat history cleared from client.', 'info');
    }
}

// Start new chat session
function startNewChat() {
    localHistory = [];
    localStorage.removeItem(`ai_chatbot_history_${userId}`);
    document.getElementById('dynamic-messages-list').innerHTML = '';
    document.getElementById('suggestions-box').classList.remove('hidden');
    document.getElementById('sentiment-indicator').classList.add('hidden');
    
    // Let the backend create the next valid database session.
    sessionId = 0;
    document.getElementById('settings-session-id').value = sessionId;
    localStorage.setItem('session_id', sessionId);
    updateConnectionDetails();

    showToast('New conversation initialized.', 'success');
}

// Document Upload triggers
function triggerFileInput() {
    document.getElementById('file-uploader').click();
}

async function handleFileUpload(e) {
    const file = e.target.files[0];
    if (!file) return;

    // Verify file size < 10MB
    if (file.size > 10 * 1024 * 1024) {
        showToast('File is too large. Max size allowed is 10MB.', 'error');
        return;
    }

    const statusBar = document.getElementById('upload-status-bar');
    const fill = document.getElementById('upload-progress-fill');
    const textStatus = document.getElementById('upload-status-text');

    statusBar.classList.remove('hidden');
    fill.style.width = '20%';
    textStatus.textContent = 'Preparing upload...';

    const formData = new FormData();
    formData.append('file', file);
    formData.append('userId', userId);

    try {
        fill.style.width = '60%';
        textStatus.textContent = 'Sending bits...';

        const res = await makeRequest('/api/document/upload', {
            method: 'POST',
            body: formData
        });

        const msgText = await res.text();
        
        fill.style.width = '100%';
        textStatus.textContent = 'Complete!';
        showToast(msgText || 'Document uploaded and analyzed successfully!', 'success');

        // Append notification context message in chat
        appendMessage('bot', `System Analysis complete. File [${file.name}] successfully processed for vector database index retrieval. You can now query details about this file.`);
        
        setTimeout(() => {
            statusBar.classList.add('hidden');
            fill.style.width = '0';
        }, 3000);

    } catch (err) {
        statusBar.classList.add('hidden');
        showToast(err.message || 'File upload failed.', 'error');
    } finally {
        document.getElementById('file-uploader').value = '';
    }
}

// Export Chat History as PDF File
async function exportPdf() {
    showToast('Generating PDF file export...', 'info');
    
    try {
        const url = `${apiBaseUrl}/api/chat/export/pdf/${userId}`;
        const headers = {};
        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }

        const response = await fetch(url, { headers });
        if (!response.ok) {
            throw new Error('PDF generation failed on server');
        }

        const blob = await response.blob();
        const downloadUrl = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = downloadUrl;
        link.download = `chat-history-user-${userId}.pdf`;
        document.body.appendChild(link);
        link.click();
        link.remove();
        window.URL.revokeObjectURL(downloadUrl);
        
        showToast('PDF downloaded successfully!', 'success');
    } catch (err) {
        showToast(err.message || 'Could not export chat to PDF.', 'error');
    }
}

// Generate Chat Summary
async function generateSummary() {
    showToast('Analyzing and compiling conversation summary...', 'info');
    
    try {
        const res = await makeRequest(`/api/chat/summary/${userId}`, {
            method: 'POST'
        });
        const summaryText = await res.text();
        
        // Show summary modal
        document.getElementById('summary-user-display').textContent = `User ID: ${userId}`;
        document.getElementById('summary-date-display').textContent = `Generated Date: ${new Date().toLocaleDateString()} ${new Date().toLocaleTimeString()}`;
        document.getElementById('summary-text-container').innerHTML = `<p>${summaryText.replaceAll('\n', '<br>')}</p>`;
        
        document.getElementById('summary-modal').classList.add('active');
    } catch (err) {
        showToast(err.message || 'Could not compile summary.', 'error');
    }
}

// Close summary modal
function closeSummary() {
    document.getElementById('summary-modal').classList.remove('active');
}

// Handle Enter and Shift+Enter in chat text area
function handleTextareaKeydown(e) {
    if (e.key === 'Enter' && !e.shiftKey) {
        e.preventDefault();
        handleSendChat();
    }
}


