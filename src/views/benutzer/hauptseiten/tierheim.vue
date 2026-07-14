<template>
  <div
    class="w-full bg-white rounded-3xl shadow-sm border border-gray-100 p-6 md:p-12 min-h-[70vh]"
  >
    <!-- Auf Mobile zeigen wir das Logo zentriert an. Auf Desktop blendet es sich aus, da es dort im Header überlappt -->
    <div class="flex md:hidden justify-center mb-6">
      <div
        class="w-24 h-24 bg-white rounded-full p-2 shadow-md flex items-center justify-center border border-gray-200"
      >
        <div
          class="w-full h-full rounded-full flex items-center justify-center bg-gray-50 text-center p-2"
        >
          <span class="text-xs font-bold text-gray-500 leading-tight"
            >Tierheim<br />Weiden Logo</span
          >
        </div>
      </div>
    </div>

    <!-- ZWEISPALTIGES RASTER (Wechselt ab md: zu 2 Spalten) -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-10 lg:gap-16 items-start">
      <!-- LINKE SPALTE: Kontaktdaten & Gassi-Zeiten -->
      <div class="space-y-8">
        <!-- Kontaktdaten -->
        <div>
          <h3 class="text-xl font-bold text-gray-900 mb-4 font-sans">Kontaktdaten</h3>
          <div class="text-left space-y-2 text-sm md:text-base text-gray-700 leading-relaxed">
            <p class="font-medium">Schustermooslohe 94</p>
            <p class="font-medium">92637 Weiden i. d. OPf.</p>
            <p class="pt-2">0961 25780</p>
            <p>0175 4715971 (Notfall)</p>
            <p class="pt-2">
              <a
                href="https://tierheim-weiden.de"
                target="_blank"
                class="hover:underline text-emerald-800 font-semibold"
              >
                tierheim-weiden.de
              </a>
            </p>
          </div>
        </div>

        <!-- Gassi Zeiten -->
        <div>
          <h3 class="text-xl font-bold text-gray-900 mb-4 font-sans">Gassi Zeiten</h3>
          <div class="text-sm md:text-base text-gray-700 space-y-2 max-w-sm">
            <div class="flex justify-between">
              <span class="font-bold w-12">Mo</span>
              <span>10:00 - 14:00 Uhr</span>
            </div>
            <div class="flex justify-between">
              <span class="font-bold w-12">Mi</span>
              <span>10:00 - 14:00 Uhr</span>
            </div>
            <div class="flex justify-between">
              <span class="font-bold w-12">Fr</span>
              <span>10:00 - 14:00 Uhr</span>
            </div>
            <div class="flex justify-between">
              <span class="font-bold w-12">Sa</span>
              <span>10:00 - 11:30 Uhr</span>
            </div>
          </div>
        </div>

        <!-- Button "zu den Terminen" -->
        <div class="pt-2">
          <button
            @click="goToReservations"
            class="flex items-center gap-2 bg-[#94A684] hover:bg-[#839573] text-gray-900 font-bold px-6 py-3 rounded-full text-sm shadow-sm transition-all active:scale-95"
          >
            <span>📅</span>
            <span>zu den Terminen</span>
          </button>
        </div>
      </div>

      <!-- RECHTE SPALTE: Unsere Hunde -->
      <div class="space-y-6">
        <!-- Überschrift Hunde-Liste -->
        <div class="flex items-center gap-3">
          <span class="text-xl">🐕</span>
          <h3 class="text-xl font-bold text-gray-800 font-sans">Unsere Hunde</h3>
        </div>

        <!-- Dynamische Liste der Hunde -->
        <div class="space-y-3.5">
          <div
            v-for="hund in hunde"
            :key="hund.id"
            @click="viewDogDetails(hund.id)"
            class="w-full bg-[#E3E9DD] hover:bg-[#d6decb] rounded-full p-2 pl-3 pr-6 flex items-center justify-between cursor-pointer shadow-sm transition-all hover:translate-x-1 active:scale-[0.99]"
          >
            <div class="flex items-center gap-4">
              <!-- Kreisrundes Avatar-Bild für jeden Hund -->
              <div
                class="w-11 h-11 rounded-full bg-white overflow-hidden flex items-center justify-center border border-gray-200"
              >
                <!-- Später durch Hundebild ersetzen, z.B. :src="hund.bild" -->
                <span class="text-xl">🐶</span>
              </div>
              <span class="font-bold text-gray-800 text-sm md:text-base">{{ hund.name }}</span>
            </div>

            <!-- Pfeil nach rechts -->
            <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke-width="3"
              stroke="currentColor"
              class="w-4 h-4 text-gray-600"
            >
              <path stroke-linecap="round" stroke-linejoin="round" d="M8.25 4.5l7.5 7.5-7.5 7.5" />
            </svg>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// Die vollständige Hundeliste aus deinem Screenshot
const hunde = ref([
  { id: 'wambo', name: 'Wambo' },
  { id: 'aris', name: 'Aris' },
  { id: 'mira', name: 'Mira' },
  { id: 'richy', name: 'Richy' },
  { id: 'xina', name: 'Xina' },
])

const goToReservations = () => {
  router.push('/')
}

const viewDogDetails = (id) => {
  router.push(`/hund/${id}`)
}
</script>
