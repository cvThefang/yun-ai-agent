import axios from 'axios';

const api = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json'
  }
});

export const sendChatMessage = (message: string, agentType: string) => {
  return api.post('/chat', {
    message,
    agentType
  });
};

export default api;