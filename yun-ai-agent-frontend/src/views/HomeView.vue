<template>
  <div class="home">
    <h1>云 AI Agent</h1>
    <div class="service-options">
      <router-link to="/super-agent" class="service-option">
        <h2>Super Agent</h2>
        <p>强大的AI工具箱</p>
      </router-link>
      <router-link to="/love-master" class="service-option">
        <h2>Love Master</h2>
        <p>爱情顾问</p>
      </router-link>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'HomeView',
});
</script>

<style scoped>
.home {
  padding: 20px;
}

.service-options {
  display: flex;
  justify-content: center;
  gap: 30px;
  margin-top: 40px;
}

.service-option {
  background-color: #f5f5f5;
  border-radius: 10px;
  padding: 20px;
  width: 200px;
  text-decoration: none;
  color: #333;
  transition: transform 0.3s, box-shadow 0.3s;
}

.service-option:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0,0,0,0.1);
}

h2 {
  margin-bottom: 10px;
}

p {
  margin: 0;
  color: #666;
}
</style>