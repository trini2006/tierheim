<template>
  <div class="grid grid-cols-1 md:grid-cols-2 gap-8 max-w-6xl mx-auto items-start p-4 md:p-6">
    
    <!-- LINKER BEREICH: Menü & Mobile "Heute"-Ansicht -->
    <div :class="isIndexRoute ? 'block' : 'hidden md:block'" class="space-y-3 w-full max-w-md mx-auto md:mx-0">
      <button 
        v-for="item in menuItems" 
        :key="item.path" 
        @click="handleNavigate(item)" 
        :class="buttonClass(item.path)"
      >
        <span class="font-bold text-gray-800 text-sm">{{ item.name }}</span>
      </button>

      <!-- "Heute" Bereich (nur auf Mobile sichtbar, wenn man sich auf der Admin-Startseite befindet) -->
      <div class="block md:hidden mt-8 bg-white p-6 rounded-3xl border border-gray-100 shadow-sm">
        <h2 class="text-lg font-bold text-gray-800 mb-4">Heute</h2>
        <p v-if="!hatHeuteTermine" class="text-gray-500">Aktuell keine geplanten Gassi-Geher...</p>
        <ul v-else class="space-y-2">
          <li v-for="termin in gassiGeherHeute" :key="termin.id" class="text-gray-800 border-b pb-2">
            {{ termin.uhrzeit }} – {{ termin.hundName }} mit {{ termin.mitgliedName }}
          </li>
        </ul>
      </div>
    </div>

    <!-- RECHTER BEREICH: RouterView für Unterseiten & Desktop "Heute"-Kasten -->
    <div :class="isIndexRoute ? 'hidden md:block' : 'block'" class="w-full max-w-md mx-auto md:mx-0">
      <RouterView />

      <!-- "Heute" Bereich (nur auf Desktop/Tablet sichtbar, wenn auf der Admin-Startseite) -->
      <div v-if="isIndexRoute" class="hidden md:block bg-white p-6 rounded-3xl border border-gray-100 shadow-sm">
        <h2 class="text-xl font-bold text-gray-800 mb-4">Heute</h2>
        <p v-if="!hatHeuteTermine" class="text-gray-500">Aktuell keine geplanten Gassi-Geher...</p>
        <ul v-else class="space-y-2">
          <li v-for="termin in gassiGeherHeute" :key="termin.id" class="text-gray-800 border-b pb-2">
            {{ termin.uhrzeit }} – {{ termin.hundName }} mit {{ termin.mitgliedName }}
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute, RouterView } from 'vue-router'

const router = useRouter()
const route = useRoute()

// Prüft, ob man exakt auf der Hauptseite des Admins ist (/app/admin)
const isIndexRoute = computed(() => route.path === '/app/admin')

const menuItems = [
  { name: 'Statistik', path: '/app/admin/statistik' },  
  { name: 'Nachrichten', path: '/app/admin/nachrichten' },
  { name: 'Veranstaltungen', path: '/app/admin/veranstaltungen' },
  { name: 'Reservierungen', path: '/app/admin/reservierungen' },
  { name: 'Reservierung hinzufügen', path: '/app/admin/termin-mitglied' },
  { name: 'Mitglieder', path: '/app/admin/mitglieder' },
]

function buttonClass(path) {
  const active = route.path === path
  return [
    'flex items-center justify-between p-4 rounded-3xl shadow-sm border transition-colors w-full',
    active ? 'bg-[#b8c9b4] border-emerald-700' : 'bg-[#D3DDD1] border-gray-200 hover:bg-[#c2cebf]'
  ]
}

function handleNavigate(item) {
  router.push(item.path)
}

const gassiGeherHeute = ref([])
const hatHeuteTermine = computed(() => gassiGeherHeute.value.length > 0)

async function ladeHeutigeTermine() {
  try {
    const res = await fetch('/api/admin/termine/heute')
    
    const contentType = res.headers.get('content-type')
    if (res.ok && contentType && contentType.includes('application/json')) {
      gassiGeherHeute.value = await res.json()
    } else {
      gassiGeherHeute.value = []
    }
  } catch (e) {
    gassiGeherHeute.value = []
  }
}

onMounted(ladeHeutigeTermine)
</script>