<template>
  <!-- w-full sorgt dafür, dass der Flex-Container die volle Breite nutzt -->
  <div class="flex items-center justify-center min-h-screen w-full">
    
    <!-- LADEZUSTAND -->
    <div v-if="loading" class="bg-gray-200 text-gray-800 p-8 rounded-2xl shadow-2xl text-center m-4">
      <div class="animate-spin text-4xl mb-2">⏳</div>
      <h2 class="text-3xl font-bold mb-2">Wird reserviert...</h2>
      <p class="text-lg">Bitte einen Moment Geduld.</p>
    </div>

    <!-- ERFOLG BANNER -->
    <div v-else-if="success" class="bg-green-700 text-white p-8 rounded-2xl shadow-2xl text-center animate-bounce m-4">
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
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const loading = ref(true)
const success = ref(false)

onMounted(async () => {
  try {
    // Gespeicherte Daten aus dem localStorage holen
    const terminData = JSON.parse(localStorage.getItem('terminData') || '{}')
    const hundData = JSON.parse(localStorage.getItem('finalerTermin') || '{}') // Falls woanders gespeichert

    // Payload für ReservierungDTO zusammenbauen
    const payload = {
      mitgliedId: terminData.mitgliedId || terminData.mitgliedsnummer,
      hundId: hundData.hundId || terminData.hundId,
      datum: terminData.datum, // Format 'YYYY-MM-DD'
      zeitAb: terminData.von || terminData.start, // Format 'HH:mm:ss' oder 'HH:mm'
      zeitBis: terminData.bis || terminData.end
    }

    // Absenden an den Endpunkt @PostMapping("/new")
    const res = await fetch('/new', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    })

    if (res.ok) {
      success.value = true
      loading.value = false

      setTimeout(() => {
        localStorage.removeItem('terminData')
        localStorage.removeItem('finalerTermin')
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

    // Bei Fehler nach kurzer Zeit zum Anfang des Reservierungsprozesses leiten
    setTimeout(() => {
      if (route.path.startsWith('/app/admin')) {
        router.push('/app/admin/termin-mitglied') // Admin Start vom Termin-Prozess
      } else {
        router.push('/app/zeitwahl') // Benutzer Start vom Termin-Prozess (Pfad anpassen falls nötig)
      }
    }, 2500)
  }
})
</script>