<template>
  <div class="chat-box">
    <div class="messages" ref="messagesContainer">
      <ChatMessage 
        v-for="(message, index) in messages" 
        :key="index" 
        :message="message" 
      />
    </div>
    <div class="input-area">
      <textarea 
        v-model="userInput" 
        placeholder="Type your message here..." 
        @keydown.enter.prevent="sendMessage"
      ></textarea>
      <button @click="sendMessage" :disabled="isLoading">Send</button>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed, nextTick, onMounted, watch } from 'vue';
import { useRoute } from 'vue-router';
import ChatMessage from './ChatMessage.vue';
import { sendChatMessage } from '@/services/api';

interface Message {
  content: string;
  isUser: boolean;
}

export default defineComponent({
  name: 'ChatBox',
  components: {
    ChatMessage
  },
  setup() {
    const route = useRoute();
    const messages = ref<Message[]>([]);
    const userInput = ref('');
    const isLoading = ref(false);
    const messagesContainer = ref<HTMLElement | null>(null);
    
    const agentType = computed(() => {
      if (route.path.includes('super-agent')) {
        return 'super-agent';
      } else if (route.path.includes('love-master')) {
        return 'love-master';
      }
      return 'unknown';
    });

    const sendMessage = async () => {
      if (!userInput.value.trim() || isLoading.value) return;
      
      // Add user message
      const userMessage = {
        content: userInput.value,
        isUser: true
      };
      messages.value.push(userMessage);
      
      // Clear input and show loading state
      const inputText = userInput.value;
      userInput.value = '';
      isLoading.value = true;
      
      try {
        // Send message to API
        const response = await sendChatMessage(inputText, agentType.value);
        
        // Add AI response
        messages.value.push({
          content: response.data.message,
          isUser: false
        });
      } catch (error) {
        console.error('Error sending message:', error);
        messages.value.push({
          content: 'Sorry, there was an error processing your request.',
          isUser: false
        });
      } finally {
        isLoading.value = false;
        scrollToBottom();
      }
    };
    
    const scrollToBottom = async () => {
      await nextTick();
      if (messagesContainer.value) {
        messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight;
      }
    };
    
    onMounted(() => {
      // Add welcome message based on agent type
      if (agentType.value === 'super-agent') {
        messages.value.push({
          content: 'Welcome to Super Agent! How can I assist you today?',
          isUser: false
        });
      } else if (agentType.value === 'love-master') {
        messages.value.push({
          content: 'Hello, I\'m your Love Master. Feel free to ask me anything about relationships and love.',
          isUser: false
        });
      }
    });
    
    watch(messages, () => {
      scrollToBottom();
    });
    
    return {
      messages,
      userInput,
      isLoading,
      sendMessage,
      messagesContainer
    };
  }
});
</script>

<style scoped>
.chat-box {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
}

.input-area {
  display: flex;
  border-top: 1px solid #ddd;
  padding: 10px;
}

textarea {
  flex: 1;
  height: 40px;
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 8px;
  resize: none;
  font-family: inherit;
  font-size: 14px;
}

button {
  margin-left: 10px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 0 20px;
  cursor: pointer;
}

button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}
</style>