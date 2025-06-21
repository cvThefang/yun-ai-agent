<template>
  <div class="super-agent">
    <h1>Super Agent</h1>
    <div class="chat-container">
      <ChatBox />
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import ChatBox from '@/components/ChatBox.vue';

export default defineComponent({
  name: 'SuperAgentView',
  components: {
    ChatBox
  }
});
</script>

<style scoped>
.super-agent {
  padding: 20px;
  max-width: 1000px;
  margin: 0 auto;
}

.chat-container {
  margin-top: 30px;
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
  height: 70vh;
}
</style>