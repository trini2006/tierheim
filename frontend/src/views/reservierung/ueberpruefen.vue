<!-- views/reservierung/ueberpruefen.vue -->
<template>
  <div class="min-h-screen bg-white">
    <div v-if="booking" class="p-6 max-w-xl mx-auto">
      
      <!-- Hundekarte -->
      <div class="bg-[#BFCABF] p-4 rounded-2xl mb-8 flex items-center gap-4">
        <img :src="booking.dog.image" class="w-24 h-24 rounded-lg object-cover" />
        <div class="flex-grow">
          <div class="flex justify-between items-start">
            <h2 class="text-xl font-bold">{{ booking.dog.name }}</h2>
            <div :class="['w-3 h-3 rounded-full', booking.dog.color]"></div>
          </div>
          <p class="text-sm text-gray-800">{{ booking.dog.breed }}</p>
          <p class="text-sm text-gray-800">{{ booking.dog.age }} Jahre, {{ booking.dog.gender }}</p>
          <div class="flex gap-2 mt-2">
            <span v-for="tag in booking.dog.tags" :key="tag" 
                  class="bg-white/60 px-2 py-0.5 rounded-full text-xs">
              {{ tag }}
            </span>
          </div>
        </div>
      </div>

      <!-- Zeitdaten & Admin-Mitgliedsnummer -->
      <div class="space-y-4 mb-10 text-lg">
        <div class="flex">
          <span class="w-32 font-semibold text-gray-600">Datum:</span>
          <span>{{ booking.date || 'Mi 01.07.' }}</span>
        </div>
        <div class="flex">
          <span class="w-32 font-semibold text-gray-600">Uhrzeit:</span>
          <span>{{ booking.start && booking.end ? `${booking.start} - ${booking.end} Uhr` : '10:00 - 10:30 Uhr' }}</span>
        </div>

        <!-- Wird NUR im Admin-Bereich angezeigt -->
        <div v-if="isAdminMode" class="flex">
          <span class="w-32 font-semibold text-gray-600">Mitglied:</span>
          <span class="font-bold text-gray-800">{{ booking.telefon || booking.mitgliedId || 'Unbekannt' }}</span>
        </div>
      </div>

      <!-- Bestätigen Button -->
      <div class="flex justify-center md:justify-start">
        <button @click="confirmBooking" 
                class="w-full md:w-64 bg-[#9CA99A] hover:bg-[#8b9889] py-4 rounded-full text-black font-bold shadow-md transition-colors">
          Termin Bestätigen
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()
const booking = ref(null)

// Prüft, ob wir uns im Admin-Bereich befinden
const isAdminMode = computed(() => route.path.startsWith('/app/admin'))

onMounted(() => {
  const data = localStorage.getItem('bookingFinal')
  if (data) {
    booking.value = JSON.parse(data)
  } else {
    // Falls keine Daten da sind, zurück zur entsprechenden Startseite
    router.push(isAdminMode.value ? '/app/admin' : '/app/reservierung/zeitwahl')
  }
})

const confirmBooking = () => {
  if (isAdminMode.value) {
    // Hier kannst du für Admin einen eigenen Erfolgsweg oder eine Weiterleitung definieren
    router.push('/app/admin/erfolgreichReserviert')
  } else {
    router.push('/app/reservierung/erfolgreichReserviert')
  }
}
</script>