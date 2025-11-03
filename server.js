const express = require('express');
const cors = require('cors');
const jwt = require('jsonwebtoken');
const bodyParser = require('body-parser');

const app = express();
app.use(express.json());
app.get('/health', (_, res) => res.json({ ok: true }));

/** SOLO ESTE USUARIO PODRÁ INICIAR SESIÓN */
const USER = {
  id: 1,
  username: 'demo',
  password: 'demo123',
  email: 'demo@cineplus.cl',
  firstName: 'Demo',
  lastName: 'User'
};

// Secretos SOLO para mock (en prod usa env vars seguras)
const ACCESS_SECRET = 'dev-access-secret';
const REFRESH_SECRET = 'dev-refresh-secret';

// helpers
function signAccess(payload) {
  // vida corta para probar refresh
  return jwt.sign(payload, ACCESS_SECRET, { expiresIn: '15m' });
}
function signRefresh(payload) {
  return jwt.sign({ ...payload, type: 'refresh' }, REFRESH_SECRET, { expiresIn: '7d' });
}
function authMiddleware(req, res, next) {
  const h = req.headers.authorization || '';
  const [, token] = h.split(' ');
  if (!token) return res.status(401).json({ error: 'No token' });

  try {
    const decoded = jwt.verify(token, ACCESS_SECRET);
    req.user = decoded;
    return next();
  } catch (e) {
    return res.status(401).json({ error: 'Invalid token' });
  }
}

// LOGIN: solo (demo/demo123) devuelve 200 + tokens
app.post('/user/login', (req, res) => {
  const { username, password } = req.body || {};
  if (username !== USER.username || password !== USER.password) {
    return res.status(401).json({ error: 'Credenciales inválidas' });
  }
  const accessToken = signAccess({ sub: USER.id, username: USER.username });
  const refreshToken = signRefresh({ sub: USER.id, username: USER.username });
  return res.json({
    id: USER.id,
    username: USER.username,
    email: USER.email,
    firstName: USER.firstName,
    lastName: USER.lastName,
    accessToken,
    refreshToken
  });
});

// ME: protegido por Bearer accessToken
app.get('/user/me', authMiddleware, (req, res) => {
  // puedes validar que sub/coincida con USER.id si quieres
  return res.json({
    id: USER.id,
    username: USER.username,
    email: USER.email,
    firstName: USER.firstName,
    lastName: USER.lastName
  });
});

// REFRESH: recibe refreshToken y emite nuevo accessToken
app.post('/auth/refresh', (req, res) => {
  const { refreshToken } = req.body || {};
  if (!refreshToken) return res.status(400).json({ error: 'Falta refreshToken' });

  try {
    const decoded = jwt.verify(refreshToken, REFRESH_SECRET);
    if (decoded.type !== 'refresh') throw new Error('Not a refresh token');

    // (opcional) validar que el usuario exista (aquí solo permitimos USER)
    if (decoded.sub !== USER.id) return res.status(401).json({ error: 'Usuario inválido' });

    const newAccess = signAccess({ sub: USER.id, username: USER.username });
    // (opcional) rotar refresh, para el mock devolvemos el mismo
    return res.json({ token: newAccess, refreshToken });
  } catch (e) {
    return res.status(401).json({ error: 'Refresh inválido' });
  }
});

app.listen(3000, () => console.log('Mock auth server on http://localhost:3000'));
