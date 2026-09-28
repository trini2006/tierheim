<template>
  <div class="flex items-center justify-center min-h-screen w-full bg-gray-50">
    
    <!-- LADEZUSTAND -->
    <div v-if="loading" class="bg-gray-700 text-white p-8 rounded-2xl shadow-2xl text-center m-4">
      <h2 class="text-3xl font-bold mb-2">Wird geladen...</h2>
      <p class="text-lg">Bitte einen Moment Geduld.</p>
    </div>

    <!-- ERFOLG BANNER -->
    <div v-else-if="success" class="bg-green-700 text-white p-8 rounded-2xl shadow-2xl text-center m-4">
      <h2 class="text-3xl font-bold mb-2">Erfolg!</h2>
      <p class="text-lg">Sie haben erfolgreich reserviert.</p>
    </div>

    <!-- FEHLER BANNER -->
    <div v-else class="bg-red-600 text-white p-8 rounded-2xl shadow-2xl text-center m-4">
      <h2 class="text-3xl font-bold mb-2">Fehlgeschlagen!</h2>
      <p class="text-lg">Reservierung konnte nicht gespeichert werden.</p>
    </div>
    
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const loading = ref(true)
const success = ref(false)
let timerId = null // Variable zum Speichern des Timers

onMounted(async () => {
  try {
    const bookingFinal = JSON.parse(localStorage.getItem('bookingFinal') || '{}')

    const payload = {
      mitgliedId: bookingFinal.mitgliedId,
      hundId: bookingFinal.dog?.id,
      datum: bookingFinal.datum,
      zeitAb: bookingFinal.von ? (bookingFinal.von.length === 5 ? bookingFinal.von + ':00' : bookingFinal.von) : '10:00:00',
      zeitBis: bookingFinal.bis ? (bookingFinal.bis.length === 5 ? bookingFinal.bis + ':00' : bookingFinal.bis) : '10:30:00'
    }

    const res = await fetch('/new', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    })

    if (res.ok) {
      success.value = true
      loading.value = false

      timerId = setTimeout(() => {
        localStorage.removeItem('terminData')
        localStorage.removeItem('bookingFinal')

        if (route.path.startsWith('/app/admin')) {
          router.push('/app/admin')
        } else {
          router.push('/app')
        }
      }, 2000)
    } else {
      throw new Error('Server antwortete mit Fehler')
    }
  } catch (e) {
    console.error('Reservierungsfehler:', e)
    success.value = false
    loading.value = false

    // Wenn ein Fehler auftritt, leiten wir standardmäßig nach 3 Sekunden weiter,
    // ABER nur, wenn der User nicht vorher manuell wegnavigiert.
    timerId = setTimeout(() => {
      // Wenn der Nutzer bereits auf der Home-Seite ist, machen wir gar nichts mehr
      if (route.path === '/app/admin' || route.path === '/app') return

      if (route.path.startsWith('/app/admin')) {
        router.push('/app/admin')
      } else {
        router.push('/app')
      }
    }, 3000)
  }
})

// WICHTIG: Sobald die Komponente zerstört wird (z.B. weil du auf Home klickst),
// wird der laufende Timer sofort gelöscht. So wirst du nie wieder ungewollt weggezogen!
onUnmounted(() => {
  if (timerId) {
    clearTimeout(timerId)
  }
})
</script>