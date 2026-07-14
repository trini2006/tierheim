<template>
  <!-- Grid: Wechselt jetzt bereits ab Tablet-Breite (md statt lg) in die 2-Spalten-Ansicht -->
  <div class="grid grid-cols-1 md:grid-cols-2 gap-8 max-w-6xl mx-auto items-start p-4 md:p-6">
    <!-- LINKER BEREICH: Buttons -->
    <div class="space-y-8 w-full max-w-md mx-auto md:mx-0">
      <!-- 1. NACHRICHTEN BUTTON -->
      <div>
        <button
          @click="navigate('nachrichten')"
          :class="[
            'flex items-center justify-between p-3 rounded-full shadow-sm border transition-colors w-full',
            isActive('nachrichten')
              ? 'bg-[#b8c9b4] border-emerald-700'
              : 'bg-[#D3DDD1] border-gray-200 hover:bg-[#c2cebf]',
          ]"
        >
          <div class="flex items-center gap-3">
            <div class="relative">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                stroke-width="1.5"
                stroke="currentColor"
                class="w-7 h-7 text-gray-800"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  d="M21.75 6.75v10.5a2.25 2.25 0 0 1-2.25 2.25h-15a2.25 2.25 0 0 1-2.25-2.25V6.75m19.5 0A2.25 2.25 0 0 0 19.5 4.5h-15a2.25 2.25 0 0 0-2.25 2.25m19.5 0v.243a2.25 2.25 0 0 1-1.07 1.916l-7.5 4.615a2.25 2.25 0 0 1-2.36 0L3.32 8.91a2.25 2.25 0 0 1-1.07-1.916V6.75"
                />
              </svg>
              <span
                class="absolute -top-1 -right-1 bg-red-500 text-white text-[9px] font-bold w-4 h-4 rounded-full flex items-center justify-center border border-white"
                >1</span
              >
            </div>
            <div class="text-left leading-tight">
              <span class="font-bold text-gray-800 block text-sm">Nachrichten</span>
              <span class="text-xs text-red-600 font-semibold">Hund Akito wurde gesperrt! ...</span>
            </div>
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

      <!-- 2. RESERVIERUNGEN BUTTON -->
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

        <button
          @click="navigate('reservierungen')"
          :class="[
            'flex items-center justify-between p-3 rounded-full shadow-sm border transition-colors w-full',
            isActive('reservierungen')
              ? 'bg-[#b8c9b4] border-emerald-700'
              : 'bg-[#D3DDD1] border-gray-200 hover:bg-[#c2cebf]',
          ]"
        >
          <div class="flex items-center gap-3">
            <div
              class="w-8 h-8 rounded-full bg-emerald-800 text-white flex items-center justify-center text-sm"
            >
              🐾
            </div>
            <div class="text-left leading-tight">
              <span class="font-semibold text-gray-800 text-sm block">Dark am 01.07.2026</span>
            </div>
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

      <!-- 3. VERANSTALTUNGEN -->
      <div class="space-y-3">
        <div class="flex items-center gap-2 text-gray-800 font-bold">
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
              d="M6.75 3v2.25M17.25 3v2.25M3 18.75V7.5a2.25 2.25 0 0 1 2.25-2.25h13.5A2.25 2.25 0 0 1 21 7.5v11.25m-18 0A2.25 2.25 0 0 0 5.25 21h13.5A2.25 2.25 0 0 0 21 18.75m-18 0v-7.5A2.25 2.25 0 0 1 5.25 9h13.5A2.25 2.25 0 0 1 21 11.25v7.5"
            />
          </svg>
          <h2 class="text-sm tracking-wide uppercase text-gray-600 text-left">Veranstaltungen</h2>
        </div>
        <p class="text-sm text-gray-500 italic pl-1 text-left">
          Aktuell keine geplanten Veranstaltungen...
        </p>
      </div>
    </div>

    <!-- RECHTER BEREICH: Platzhalter für die Detailansichten auf Tablet & Desktop -->
    <div class="w-full max-w-md mx-auto md:mx-0 mt-8 md:mt-0 hidden md:block">
      <!-- Dynamische Überschrift -->
      <h2 class="text-lg font-bold text-gray-800 mb-4 text-left">
        {{ computedHeader }}
      </h2>

      <!-- Hier werden deine Views wie z.B. die Revuebersicht auf Desktop/Tablet reingeladen -->
      <RouterView />
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const isActive = (path) => {
  return route.path.endsWith(`/start/${path}`) || route.path.endsWith(`/${path}`)
}

const computedHeader = computed(() => {
  if (route.path.endsWith('/nachrichten')) {
    return 'Nachrichten'
  }
  if (route.path.endsWith('/reservierungen')) {
    return 'Reservierungsübersicht'
  }
  return 'Nächster Termin'
})

const navigate = (subPath) => {
  const isMobile = window.matchMedia('(max-width: 767px)').matches

  if (isMobile) {
    // Smartphone: Leitet wie von dir gewünscht auf die vollen Mobile-Seiten weiter
    if (subPath === 'nachrichten') router.push('/benachrichtigungen')
    if (subPath === 'reservierungen') router.push('/revuebersicht')
  } else {
    // Tablet & Desktop: Lädt die Route rechts nebenan im <RouterView /> Platzhalter
    router.push(`/${subPath}`)
  }
}
</script>
