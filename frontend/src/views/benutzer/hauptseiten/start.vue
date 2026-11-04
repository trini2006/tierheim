<template>
  <div class="grid grid-cols-1 md:grid-cols-2 gap-8 max-w-6xl mx-auto items-start p-4 md:p-6">
    <!-- LINKER BEREICH -->
    <div class="space-y-8 w-full max-w-md mx-auto md:mx-0">
      <!-- 1. NACHRICHTEN BUTTON
      <button
        @click="navigate('nachrichten')"
        :class="[
          'flex items-center justify-between p-3 rounded-full shadow-sm border transition-colors w-full',
          activeTab === 'nachrichten' ? 'bg-[#b8c9b4] border-emerald-700' : 'bg-[#D3DDD1] border-gray-200 hover:bg-[#c2cebf]'
        ]"
      >
        <div class="flex items-center gap-3">
          <div class="relative">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-7 h-7 text-gray-800">
              <path stroke-linecap="round" stroke-linejoin="round" d="M21.75 6.75v10.5a2.25 2.25 0 0 1-2.25 2.25h-15a2.25 2.25 0 0 1-2.25-2.25V6.75m19.5 0A2.25 2.25 0 0 0 19.5 4.5h-15a2.25 2.25 0 0 0-2.25 2.25m19.5 0v.243a2.25 2.25 0 0 1-1.07 1.916l-7.5 4.615a2.25 2.25 0 0 1-2.36 0L3.32 8.91a2.25 2.25 0 0 1-1.07-1.916V6.75" />
            </svg>
            <span class="absolute -top-1 -right-1 bg-red-500 text-white text-[9px] font-bold w-4 h-4 rounded-full flex items-center justify-center border border-white">1</span>
          </div>
          <div class="text-left leading-tight">
            <span class="font-bold text-gray-800 block text-sm">Nachrichten</span>
            <span class="text-xs text-red-600 font-semibold">Hund Akito wurde gesperrt! ...</span>
          </div>
        </div>
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2.5" stroke="currentColor" class="w-4 h-4 text-gray-800 mr-1"><path stroke-linecap="round" stroke-linejoin="round" d="m8.25 4.5 7.5 7.5-7.5 7.5" /></svg>
      </button>-->

      <!-- 2. RESERVIERUNGEN BEREICH -->
      <div class="space-y-3">
        <div class="flex items-center gap-2 text-gray-800 font-bold mb-1">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            stroke-width="2"
            stroke="currentColor"
            class="w-5 h-5"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              d="M12 6v6h4.5m4.5 0a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"
            />
          </svg>
          <h2 class="text-sm tracking-wide uppercase text-gray-600 text-left">Reservierungen</h2>
        </div>

        <!-- Dynamische Liste der Reservierungen -->
        <p v-if="store.fehler" class="text-sm text-red-700 pl-1 text-left">{{ store.fehler }}</p>
        <div v-else-if="store.alle.length > 0" class="space-y-3">
          <button
            v-for="res in store.alle"
            :key="res.id"
            @click="waehle(res.id)"
            :class="[
              'flex items-center justify-between p-3 rounded-full shadow-sm border transition-colors w-full',
              istAusgewaehlt(res)
                ? 'bg-[#b8c9b4] border-emerald-700'
                : 'bg-[#D3DDD1] border-gray-200 hover:bg-[#c2cebf]',
            ]"
          >
            <div class="flex items-center gap-3">
              <div
                :class="[
                  'w-8 h-8 rounded-full text-white flex items-center justify-center text-sm',
                  res.status === 'STORNIERT' ? 'bg-red-800' : 'bg-emerald-800',
                ]"
              >
                {{ res.status === 'STORNIERT' ? '🚫' : '🐾' }}
              </div>
              <span
                :class="[
                  'font-semibold text-gray-800 text-sm',
                  res.status === 'STORNIERT' ? 'line-through decoration-red-600 decoration-2' : '',
                ]"
              >
                {{ res.hund?.name || 'Hund' }} am {{ formatiereDatumAnzeige(res.datum) }}
              </span>
            </div>
            <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke-width="2.5"
              stroke="currentColor"
              class="w-4 h-4 text-gray-800 mr-1"
            >
              <path stroke-linecap="round" stroke-linejoin="round" d="m8.25 4.5 7.5 7.5-7.5 7.5" />
            </svg>
          </button>
        </div>

        <p v-else-if="store.geladen" class="text-sm text-gray-500 italic pl-1 text-left">
          Aktuell keine Reservierungen vorhanden...
        </p>
        <p v-else class="text-sm text-gray-500 pl-1 text-left">Lade...</p>
      </div>

      <!-- 3. VERANSTALTUNGEN
      <div class="space-y-3">
        <div class="flex items-center gap-2 text-gray-800 font-bold">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-5 h-5"><path stroke-linecap="round" stroke-linejoin="round" d="M6.75 3v2.25M17.25 3v2.25M3 18.75V7.5a2.25 2.25 0 0 1 2.25-2.25h13.5A2.25 2.25 0 0 1 21 7.5v11.25m-18 0A2.25 2.25 0 0 0 5.25 21h13.5A2.25 2.25 0 0 0 21 18.75m-18 0v-7.5A2.25 2.25 0 0 1 5.25 9h13.5A2.25 2.25 0 0 1 21 11.25v7.5" /></svg>
          <h2 class="text-sm tracking-wide uppercase text-gray-600 text-left">Veranstaltungen</h2>
        </div>
        <p class="text-sm text-gray-500 italic pl-1 text-left">Aktuell keine geplanten Veranstaltungen...</p>
      </div>-->
    </div>

    <!-- RECHTER BEREICH -->
    <div class="w-full max-w-md mx-auto md:mx-0 mt-8 md:mt-0 hidden md:block">
      <h2 class="text-lg font-bold text-gray-800 mb-4 text-left">
        {{ store.ausgewaehltId ? 'Termin' : 'Nächster Termin' }}
      </h2>
      <ReservierungsDetail :reservierung="store.angezeigte" />
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useReservierungenStore } from '@/stores/reservierungen.js'
import { formatiereDatumKurz as formatiereDatumAnzeige } from '@/components/utils/datum.js'
import ReservierungsDetail from '@/views/benutzer/hauptseiten/ReservierungsDetail.vue'
import { useIstMobil } from '@/composables/useIstMobil.js'

const router = useRouter()
const store = useReservierungenStore()

// einziger Ladepunkt der Seite; Beim erstmaligen Laden wieder "nächster Termin"
onMounted(() => {
  store.waehle(null)
  store.lade()
})

const istMobil = useIstMobil()
const waehle = (id) => {
  if (istMobil.value)
    router.push(`/app/reservierungen/${id}`) // Mobil: eigene Seite
  else store.waehle(id) // Desktop: rechts anzeigen
}

const istAusgewaehlt = (res) => !istMobil.value && res.id === store.angezeigte?.id

const resetToDefault = () => store.waehle(null)
defineExpose({ resetToDefault })
</script>
