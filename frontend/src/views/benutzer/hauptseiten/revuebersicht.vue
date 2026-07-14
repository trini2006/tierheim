<template>
  <div class="w-full text-gray-800 bg-white flex flex-col items-center">
    <!-- Content-Bereich: Passt sich flexibel an die Spalte oder den Bildschirm an -->
    <main class="w-full px-4 py-6 flex flex-col items-center max-w-md mx-auto">
      <!-- Grüne Hundekarte -->
      <div class="w-full bg-[#BFCCB5] rounded-2xl p-4 flex gap-4 items-center shadow-sm mb-6">
        <div
          class="w-20 h-20 md:w-24 md:h-24 rounded-xl bg-[#94A684]/40 flex flex-col items-center justify-center text-gray-800"
        >
          <span class="text-2xl md:text-3xl">🐾</span>
          <span class="text-[10px] font-semibold uppercase tracking-wider text-gray-700 mt-1"
            >Dark</span
          >
        </div>
        <div class="flex-1 relative">
          <span class="absolute top-0 right-0 w-3 h-3 rounded-full bg-orange-500"></span>
          <h2 class="font-bold text-base md:text-lg text-gray-900 mb-0.5">Dark</h2>
          <p class="text-xs text-gray-700">Mischling</p>
          <p class="text-xs text-gray-700 mb-2">7 Jahre, Rüde</p>
          <div class="flex flex-wrap gap-1.5">
            <span class="text-[10px] bg-white/60 text-gray-800 px-2 py-0.5 rounded-full"
              >ruhig</span
            >
            <span class="text-[10px] bg-white/60 text-gray-800 px-2 py-0.5 rounded-full"
              >zurückhaltend</span
            >
          </div>
        </div>
      </div>

      <!-- Termin-Details -->
      <div class="w-full max-w-[260px] space-y-2.5 mb-8 text-left">
        <div class="flex justify-between items-center">
          <span class="font-bold text-xs md:text-sm text-gray-700 w-16">Datum:</span>
          <span class="text-xs md:text-sm text-gray-900 font-medium flex-1 pl-2"
            >Mittwoch, 01.07.</span
          >
        </div>
        <div class="flex justify-between items-center">
          <span class="font-bold text-xs md:text-sm text-gray-700 w-16">Uhrzeit:</span>
          <span class="text-xs md:text-sm text-gray-900 font-medium flex-1 pl-2"
            >10:00 - 11:00 Uhr</span
          >
        </div>
      </div>

      <!-- Stornieren Button -->
      <button
        @click="stornieren"
        class="w-full max-w-[200px] bg-[#FF8B8B] hover:bg-[#ff7575] text-gray-900 font-bold py-2.5 px-4 rounded-full text-xs shadow-sm transition-all text-center hover:scale-[1.02] active:scale-[0.98]"
      >
        Termin stornieren
      </button>
    </main>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

// Erkennt, ob die Seite eingebettet auf dem Desktop genutzt wird oder als eigene Mobile-Seite
defineProps({
  isEmbedded: {
    type: Boolean,
    default: false,
  },
})

const router = useRouter()

// Falls du goBack an anderer Stelle im Script nicht mehr brauchst, kannst du es so lassen oder löschen
const goBack = () => {
  router.back()
}

const stornieren = () => {
  alert('Termin stornieren wurde geklickt.')
}
</script>
