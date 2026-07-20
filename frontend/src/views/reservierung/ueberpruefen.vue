<template>
  <div class="min-h-screen bg-white">
    <!-- Hauptinhalt: max-w-xl begrenzt die Breite auf Tablet/Desktop -->
    <div v-if="booking" class="p-6 max-w-xl mx-auto">
      
      <!-- Hundekarte: mb-8 bleibt, Breite wird durch den Container begrenzt -->
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

      <!-- Zeitdaten -->
      <div class="space-y-4 mb-10 text-lg">
        <div class="flex">
          <span class="w-24 font-semibold text-gray-600">Datum:</span>
          <span>{{ booking.date }}</span>
        </div>
        <div class="flex">
          <span class="w-24 font-semibold text-gray-600">Uhrzeit:</span>
          <span>{{ booking.start }} - {{ booking.end }} Uhr</span>
        </div>
      </div>

      <!-- Bestätigen Button -->
      <!-- w-full auf Handy, md:w-64 auf Tablet/Desktop für eine kompakte Größe -->
      <div class="flex justify-center md:justify-start">
        <button @click="confirmBooking" 
                class="w-full md:w-64 bg-[#9CA99A] py-4 rounded-full text-black font-bold shadow-md">
          Termin Bestätigen
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const booking = ref(null)

onMounted(() => {
  const data = localStorage.getItem('bookingFinal')
  if (data) {
    booking.value = JSON.parse(data)
  } else {
    router.push('/reservierung/zeitwahl')
  }
})

const confirmBooking = () => {
  router.push('/reservierung/erfolgreichReserviert');
}
</script>