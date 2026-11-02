<template>
  <div class="flex items-center justify-center min-h-screen w-full bg-gray-50">
    <div class="bg-green-700 text-white p-8 rounded-2xl shadow-2xl text-center m-4">
      <h2 class="text-3xl font-bold mb-2">Erfolg!</h2>
      <p class="text-lg">Sie haben erfolgreich reserviert.</p>
    </div>
  </div>
</template>

<script setup>
import { onMounted, onUnmounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()
let timerId = null

onMounted(() => {
  timerId = setTimeout(() => {
    router.push(route.path.startsWith('/app/admin') ? '/app/admin' : '/app')
  }, 2000)
})

onUnmounted(() => clearTimeout(timerId))
</script>
