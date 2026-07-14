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
      {{ mobileTitle }}
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
          {{ isAdmin ? 'Tierheim Weiden (Admin)' : 'Tierheim Weiden' }}
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
    <!-- pt-16 auf Mobile hinzugefügt, damit der Inhalt nicht unter der neuen Mobile-Top-Bar klebt -->
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
import { useRoute } from 'vue-router'

const route = useRoute()

// Prüft live, ob der aktuelle Pfad das Wort 'admin' enthält
const isAdmin = computed(() => {
  return route.path.toLowerCase().includes('/admin')
})

// Berechnet die Überschrift für die mobile Top-Bar anhand der URL
const mobileTitle = computed(() => {
  const path = route.path.toLowerCase()

  if (path.endsWith('/benachrichtigungen') || path.endsWith('/nachrichten')) {
    return 'Nachrichten'
  }
  if (path.endsWith('/tierheim')) {
    return 'Tierheim'
  }
  if (path.endsWith('/einstellungen')) {
    return 'Einstellungen'
  }
  if (path.endsWith('/reservierungsuebersicht') || path.endsWith('/reservierungen')) {
    return 'Reservierungsübersicht'
  }

  // Wenn der Pfad genau "/" ist (oder ein Admin-Gegenstück) -> Willkommen
  if (path === '/' || path === '/admin') {
    return 'Willkommen'
  }

  // Fallback, falls du dich im Buchungs-Prozess oder auf einer Detailseite befindest
  if (path.includes('/reservieren')) return 'Reservierung'
  if (path.includes('/hund/')) return 'Details'

  return 'Tierheim Weiden'
})
</script>
