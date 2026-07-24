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
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

onMounted(() => {
  setTimeout(() => {
    // Aufräumen der gespeicherten Buchungsdaten
    localStorage.removeItem('terminData')
    localStorage.removeItem('bookingFinal')

    // Automatische Weiterleitung je nach Admin- oder Benutzer-Pfad
    if (route.path.startsWith('/app/admin')) {
      router.push('/app/admin')
    } else {
      router.push('/app')
    }
  }, 2000)
})
</script>