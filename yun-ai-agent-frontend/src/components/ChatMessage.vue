<template>
  <div class="message" :class="{ 'user-message': message.isUser, 'ai-message': !message.isUser }">
    <div class="avatar">
      <span>{{ message.isUser ? 'You' : agentName }}</span>
    </div>
    <div class="content">
      {{ message.content }}
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, computed } from 'vue';
import { useRoute } from 'vue-router';

export default defineComponent({
  name: 'ChatMessage',
  props: {
    message: {
      type: Object,
      required: true
    }
  },
  setup() {
    const route = useRoute();
    
    const agentName = computed(() => {
      if (route.path.includes('super-agent')) {
        return 'SA';
      } else if (route.path.includes('love-master')) {
        return 'LM';
      }
      return 'AI';
    });
    
    return {
      agentName
    };
  }
});
</script>

<style scoped>
.message {
  margin-bottom: 15px;
  display: flex;
  align-items: flex-start;
}

.user-message {
  flex-direction: row-reverse;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 10px;
  font-size: 12px;
  font-weight: bold;
}

.user-message .avatar {
  background-color: #e2f5e2;
  color: #4CAF50;
}

.ai-message .avatar {
  background-color: #e3f2fd;
  color: #2196F3;
}

.content {
  max-width: 70%;
  padding: 10px 15px;
  border-radius: 10px;
  word-break: break-word;
  white-space: pre-wrap;
}

.user-message .content {
  background-color: #e2f5e2;
  text-align: right;
}

.ai-message .content {
  background-color: #f5f5f5;
  text-align: left;
}
</style>