<template>
  <!-- Der Hauptcontainer wird untereinander (flex-col) statt nebeneinander ausgerichtet -->
  <div class="flex flex-col min-h-screen bg-gray-50">
    <!-- ========================================================= -->
    <!-- MOBILE TOP-BAR (NUR auf Mobile sichtbar, blendet ab md: aus) -->
    <!-- ========================================================= -->
    <div
      :class="[
        'md:hidden fixed top-0 left-0 right-0 h-14 text-white flex items-center justify-center font-bold text-lg shadow-md z-50 transition-colors duration-300',
        isAdmin ? 'bg-[#8B2626]' : 'bg-[#536B53]',
      ]"
    >
      <!-- Zurück-Pfeil: Nur sichtbar, wenn wir NICHT auf der Startseite sind -->
      <button
        v-if="showBackButton"
        @click="goBack"
        class="absolute left-4 top-1/2 -translate-y-1/2 p-1 active:scale-95 transition-transform focus:outline-none"
      >
        <!-- Ein cleaner Pfeil nach links -->
        <svg
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          stroke-width="2.5"
          stroke="currentColor"
          class="w-6 h-6"
        >
          <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 19.5L8.25 12l7.5-7.5" />
        </svg>
      </button>

      <span>{{ mobileTitle }}</span>
    </div>

    <!-- ========================================================= -->
    <!-- DESKTOP & TABLET TOP-NAVIGATION (Sichtbar ab md: 768px) -->
    <!-- ========================================================= -->
    <nav
      :class="[
        'hidden md:flex items-center justify-between w-full text-white px-8 py-4 border-b border-gray-700 sticky top-0 z-50 shadow-md transition-colors duration-300',
        isAdmin ? 'bg-[#8B2626]' : 'bg-[#536B53]',
      ]"
    >
      <!-- Logo / Name links -->
      <div class="flex items-center gap-3">
        <span class="text-2xl">🐾</span>
        <h1 class="text-xl font-bold">
          {{ isAdmin ? 'Tierheim Weiden' : 'Tierheim Weiden' }}
        </h1>
      </div>

      <!-- Navigations-Links rechts -->
      <div class="flex gap-4">
        <RouterLink
          to="/"
          class="px-4 py-2 rounded-lg hover:bg-black/20 transition"
          :exact-active-class="isAdmin ? 'bg-red-700' : 'bg-green-600'"
        >
          🏠 Startseite
        </RouterLink>
        <RouterLink
          to="/tierheim"
          class="px-4 py-2 rounded-lg hover:bg-black/20 transition"
          :active-class="isAdmin ? 'bg-red-700' : 'bg-green-600'"
        >
          🐾 Tierheim
        </RouterLink>
        <RouterLink
          to="/einstellungen"
          class="px-4 py-2 rounded-lg hover:bg-black/20 transition"
          :active-class="isAdmin ? 'bg-red-700' : 'bg-green-600'"
        >
          ⚙️ Einstellungen
        </RouterLink>
      </div>
    </nav>

    <!-- ========================================================= -->
    <!-- DER DYNAMISCHE INHALT (Für alle Bildschirmgrößen) -->
    <!-- ========================================================= -->
    <main class="flex-grow p-4 pt-18 md:pt-8 md:p-8 max-w-7xl mx-auto w-full pb-24 md:pb-8">
      <RouterView />
    </main>

    <!-- ========================================================= -->
    <!-- MOBILE BOTTOM-BAR (Wird ab Tablet-Größe 'md:' versteckt) -->
    <!-- ========================================================= -->
    <nav
      class="md:hidden fixed bottom-0 left-0 right-0 h-16 bg-white border-t border-gray-200 flex justify-around items-center z-50 shadow-lg"
    >
      <RouterLink
        to="/"
        class="flex flex-col items-center text-gray-600"
        :exact-active-class="isAdmin ? 'text-red-600' : 'text-green-600'"
      >
        <span class="text-xl">🏠</span>
        <span class="text-xs">Start</span>
      </RouterLink>

      <RouterLink
        to="/tierheim"
        class="flex flex-col items-center text-gray-600"
        :active-class="isAdmin ? 'text-red-600' : 'text-green-600'"
      >
        <span class="text-xl">🐾</span>
        <span class="text-xs">Tierheim</span>
      </RouterLink>

      <RouterLink
        to="/einstellungen"
        class="flex flex-col items-center text-gray-600"
        :active-class="isAdmin ? 'text-red-600' : 'text-green-600'"
      >
        <span class="text-xl">⚙️</span>
        <span class="text-xs">Einstellungen</span>
      </RouterLink>
    </nav>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

// Prüft live, ob der aktuelle Pfad das Wort 'admin' enthält
const isAdmin = computed(() => {
  return route.path.toLowerCase().includes('/admin')
})

// Prüft, ob wir NICHT auf der Startseite (egal ob User oder Admin) sind
const showBackButton = computed(() => {
  const path = route.path.toLowerCase()
  return path !== '/' && path !== '/admin'
})

// Schickt den Benutzer im Verlauf einen Schritt zurück
const goBack = () => {
  router.back()
}

// Berechnet die Überschrift für die mobile Top-Bar dynamisch
const mobileTitle = computed(() => {
  // 1. Priorität: Wenn in der router.js ein meta.title gesetzt wurde, nimm diesen!
  if (route.meta && route.meta.title) {
    return route.meta.title
  }

  // 2. Priorität: Intelligentes Fallback basierend auf deiner URL-Struktur
  const path = route.path.toLowerCase()

  if (path.endsWith('/benachrichtigungen') || path.endsWith('/nachrichten')) return 'Nachrichten'
  if (path.endsWith('/tierheim')) return 'Tierheim'
  if (path.endsWith('/einstellungen')) return 'Einstellungen'
  if (
    path.endsWith('/reservierungsuebersicht') ||
    path.endsWith('/reservierungen') ||
    path.endsWith('/revuebersicht')
  )
    return 'Reservierungsübersicht'

  if (path === '/' || path === '/admin') return 'Willkommen'

  if (path.includes('/reservieren')) return 'Reservierung'
  if (path.includes('/hund/')) return 'Details'

  return 'Tierheim Weiden'
})
</script>
