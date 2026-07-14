<!-- views/admin/hauptseiten/start.vue -->
<template>
  <div class="grid grid-cols-1 md:grid-cols-2 gap-8 max-w-6xl mx-auto items-start p-4 md:p-6">
    <!-- LINKER BEREICH: Menü -->
    <!-- Mobile: nur sichtbar, wenn keine Unterseite aktiv ist -->
    <div
      :class="isIndexRoute ? 'block' : 'hidden md:block'"
      class="space-y-3 w-full max-w-md mx-auto md:mx-0"
    >
      <!-- Heute -->
      <button
        @click="navigate('')"
        :class="[
          'flex items-center justify-between p-4 rounded-3xl shadow-sm border transition-colors w-full',
          isActive('')
            ? 'bg-[#b8c9b4] border-emerald-700'
            : 'bg-[#D3DDD1] border-gray-200 hover:bg-[#c2cebf]',
        ]"
      >
        <div class="flex items-center gap-3">
          <svg class="w-7 h-7 text-gray-900 flex-shrink-0" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
            <rect x="3" y="5" width="18" height="16" rx="2" />
            <path d="M3 10h18M8 3v4M16 3v4" />
            <path d="M9 14.5l2 2 4-4" />
          </svg>
          <span class="font-bold text-gray-800 text-sm">Heute</span>
        </div>
        <svg
          v-if="!isActive('')"
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

      <!-- Statistik -->
      <button
        @click="navigate('statistik')"
        :class="[
          'flex items-center justify-between p-4 rounded-3xl shadow-sm border transition-colors w-full',
          isActive('statistik')
            ? 'bg-[#b8c9b4] border-emerald-700'
            : 'bg-[#D3DDD1] border-gray-200 hover:bg-[#c2cebf]',
        ]"
      >
        <div class="flex items-center gap-3">
          <svg class="w-7 h-7 text-emerald-700 flex-shrink-0" viewBox="0 0 24 24">
            <rect x="3" y="12" width="4" height="9" rx="1" fill="currentColor" opacity="0.5" />
            <rect x="10" y="7" width="4" height="14" rx="1" fill="currentColor" opacity="0.75" />
            <rect x="17" y="3" width="4" height="18" rx="1" fill="currentColor" />
          </svg>
          <span class="font-bold text-gray-800 text-sm">Statistik</span>
        </div>
        <svg
          v-if="!isActive('statistik')"
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

      <!-- Nachrichten -->
      <button
        @click="navigate('nachrichten')"
        :class="[
          'flex items-center justify-between p-4 rounded-3xl shadow-sm border transition-colors w-full',
          isActive('nachrichten')
            ? 'bg-[#b8c9b4] border-emerald-700'
            : 'bg-[#D3DDD1] border-gray-200 hover:bg-[#c2cebf]',
        ]"
      >
        <div class="flex items-center gap-3 min-w-0">
          <div class="relative flex-shrink-0">
            <svg class="w-7 h-7 text-gray-900" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
              <rect x="2" y="5" width="20" height="14" rx="2" />
              <path d="M3 6l9 7 9-7" />
            </svg>
            <span
              v-if="ungeleseneNachrichten > 0"
              class="absolute -top-1 -right-1 bg-red-500 text-white text-[9px] font-bold w-4 h-4 rounded-full flex items-center justify-center border border-white"
            >
              {{ ungeleseneNachrichten }}
            </span>
          </div>
          <div class="text-left leading-tight min-w-0">
            <span class="font-bold text-gray-800 block text-sm">Nachrichten</span>
            <span v-if="neuesteNachricht" class="text-xs text-red-600 font-semibold truncate block max-w-[180px]">
              {{ neuesteNachricht }}
            </span>
          </div>
        </div>
        <svg
          v-if="!isActive('nachrichten')"
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

      <!-- Veranstaltungen -->
      <button
        @click="navigate('veranstaltungen')"
        :class="[
          'flex items-center justify-between p-4 rounded-3xl shadow-sm border transition-colors w-full',
          isActive('veranstaltungen')
            ? 'bg-[#b8c9b4] border-emerald-700'
            : 'bg-[#D3DDD1] border-gray-200 hover:bg-[#c2cebf]',
        ]"
      >
        <div class="flex items-center gap-3">
          <svg class="w-7 h-7 text-gray-900 flex-shrink-0" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
            <rect x="3" y="5" width="18" height="16" rx="2" />
            <path d="M3 10h18M8 3v4M16 3v4" />
          </svg>
          <span class="font-bold text-gray-800 text-sm">Veranstaltungen</span>
        </div>
        <svg
          v-if="!isActive('veranstaltungen')"
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

      <!-- Reservierungen -->
      <button
        @click="navigate('reservierungen')"
        :class="[
          'flex items-center justify-between p-4 rounded-3xl shadow-sm border transition-colors w-full',
          isActive('reservierungen')
            ? 'bg-[#b8c9b4] border-emerald-700'
            : 'bg-[#D3DDD1] border-gray-200 hover:bg-[#c2cebf]',
        ]"
      >
        <div class="flex items-center gap-3">
          <svg class="w-7 h-7 text-gray-900 flex-shrink-0" viewBox="0 0 24 24" fill="currentColor">
            <circle cx="6" cy="5" r="2" /><circle cx="11" cy="3.5" r="2" />
            <circle cx="16" cy="5" r="2" /><circle cx="19" cy="9" r="2" />
            <path d="M12 10c-4 0-7 3-7 6.5 0 2 1.5 3.5 3.3 3.5 1.3 0 1.7-.7 3.7-.7s2.4.7 3.7.7c1.8 0 3.3-1.5 3.3-3.5 0-3.5-3-6.5-7-6.5z" />
          </svg>
          <span class="font-bold text-gray-800 text-sm">Reservierungen</span>
        </div>
        <svg
          v-if="!isActive('reservierungen')"
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

      <!-- Mitglieder -->
      <button
        @click="navigate('mitglieder')"
        :class="[
          'flex items-center justify-between p-4 rounded-3xl shadow-sm border transition-colors w-full',
          isActive('mitglieder')
            ? 'bg-[#b8c9b4] border-emerald-700'
            : 'bg-[#D3DDD1] border-gray-200 hover:bg-[#c2cebf]',
        ]"
      >
        <div class="flex items-center gap-3">
          <svg class="w-7 h-7 text-gray-900 flex-shrink-0" viewBox="0 0 24 24" fill="currentColor">
            <circle cx="9" cy="7" r="3" /><circle cx="17" cy="8" r="2.5" />
            <path d="M2 20c0-4 3-6.5 7-6.5s7 2.5 7 6.5" />
            <path d="M15 14c3 0 6 1.8 7 5.2" />
          </svg>
          <span class="font-bold text-gray-800 text-sm">Mitglieder</span>
        </div>
        <svg
          v-if="!isActive('mitglieder')"
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

    <!-- RECHTER BEREICH: Detail-Inhalt -->
    <!-- Mobile: nur sichtbar, wenn eine Unterseite aktiv ist -->
    <div
      :class="isIndexRoute ? 'hidden md:block' : 'block'"
      class="w-full max-w-md mx-auto md:mx-0 mt-3 md:mt-0"
    >
      <h2 class="text-lg font-bold text-gray-800 mb-4 text-left">
        {{ computedHeader }}
      </h2>

      <!-- "Heute" ist die Index-Route -> Inhalt direkt hier eingebaut -->
      <div v-if="isIndexRoute">
        <p v-if="!hatHeuteTermine" class="text-gray-500">
          Aktuell keine geplanten Gassi-Geher...
        </p>
        <ul v-else class="space-y-1">
          <li v-for="termin in gassiGeherHeute" :key="termin.id" class="text-gray-800">
            {{ termin.uhrzeit }} – {{ termin.hundName }} mit {{ termin.mitgliedName }}
          </li>
        </ul>
      </div>

      <!-- Alle anderen Unterseiten -->
      <RouterView v-else />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { RouterView, useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

// Aktive Route === Index-Route "/admin" (keine Unterseite gewählt -> "Heute" wird gezeigt)
const isIndexRoute = computed(() => route.path === '/admin')

function isActive(subPath) {
  return subPath === '' ? route.path === '/admin' : route.path === `/admin/${subPath}`
}

function navigate(subPath) {
  router.push(subPath === '' ? '/admin' : `/admin/${subPath}`)
}

const computedHeader = computed(() => {
  if (route.path === '/admin/statistik') return 'Statistik'
  if (route.path === '/admin/nachrichten') return 'Nachrichten'
  if (route.path === '/admin/veranstaltungen') return 'Veranstaltungen'
  if (route.path === '/admin/reservierungen') return 'Reservierungen'
  if (route.path === '/admin/mitglieder') return 'Mitglieder'
  return 'Heute'
})

// Nachrichten-Badge (wird im Menü-Button angezeigt)
const ungeleseneNachrichten = ref(0)
const neuesteNachricht = ref('')

// Heutige geplante Gassi-Geher-Termine (Inline-Inhalt rechts)
const gassiGeherHeute = ref([])
const hatHeuteTermine = computed(() => gassiGeherHeute.value.length > 0)

async function ladeNachrichtenBadge() {
  try {
    const res = await fetch('/api/admin/nachrichten?ungelesen=true')
    if (res.ok) {
      const daten = await res.json()
      ungeleseneNachrichten.value = daten.anzahl ?? 0
      neuesteNachricht.value = daten.neueste ?? ''
    }
  } catch (fehler) {
    console.error('Nachrichten-Badge konnte nicht geladen werden:', fehler)
  }
}

async function ladeHeutigeTermine() {
  try {
    const res = await fetch('/api/admin/termine/heute')
    if (res.ok) {
      gassiGeherHeute.value = await res.json()
    }
  } catch (fehler) {
    console.error('Termine konnten nicht geladen werden:', fehler)
  }
}

onMounted(() => {
  ladeNachrichtenBadge()
  ladeHeutigeTermine()
})


//SPÄTER EVENTUELL: SEITE OPTIMIEREN/ BUTTONS & NFORMATION ZENTRIEREN
</script>