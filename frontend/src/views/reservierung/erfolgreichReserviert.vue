<template>
  <!-- w-full sorgt dafür, dass der Flex-Container die volle Breite nutzt -->
  <div class="flex items-center justify-center min-h-screen w-full">
    
    <!-- Das Banner -->
    <div class="bg-green-700 text-white p-8 rounded-2xl shadow-2xl text-center animate-bounce m-4">
      <h2 class="text-3xl font-bold mb-2">Erfolg!</h2>
      <p class="text-lg">Sie haben erfolgreich reserviert.</p>
    </div>
    
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

onMounted(() => {
  setTimeout(() => {
    router.push('/')
  }, 2000)
})
</script>