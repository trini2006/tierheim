<template>
  <div class="flex flex-col min-h-screen bg-gray-50">
    <!-- ========================================================= -->
    <!-- DESKTOP & TABLET HEADER -->
    <!-- ========================================================= -->
    <header
      :class="[
        'hidden md:block w-full text-white relative z-50 shadow-md transition-colors duration-300',
        isAdmin ? 'bg-[#8B2626]' : 'bg-[#536B53]',
      ]"
    >
      <div class="max-w-7xl mx-auto px-8 h-20 flex items-center justify-between">
        <!-- Überschrift jetzt linksbündig ohne Logo-Platzhalter -->
        <h1 class="text-2xl font-bold tracking-wide">
          {{ route.meta.title || 'Tierheim Weiden' }}
        </h1>

        <nav class="flex items-center gap-8">
          <RouterLink
            :to="isAdmin ? '/admin' : '/'"
            class="p-2 rounded-full hover:bg-black/10 transition"
          >
            <svg class="w-9 h-9" fill="currentColor" viewBox="0 0 24 24">
              <path d="M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z" />
            </svg>
          </RouterLink>

          <RouterLink
            :to="isAdmin ? '/admin/hunde' : '/tierheim'"
            class="p-2 rounded-full hover:bg-black/10 transition"
          >
            <svg class="w-9 h-9" fill="currentColor" viewBox="0 0 24 24">
              <path
                v-if="isAdmin"
                d="M12 14c1.66 0 3-1.34 3-3s-1.34-3-3-3-3 1.34-3 3 1.34 3 3 3zm5.5-3c.83 0 1.5-.67 1.5-1.5S18.33 8 17.5 8 16 8.67 16 9.5s.67 1.5 1.5 1.5zM6.5 11c.83 0 1.5-.67 1.5-1.5S7.33 8 6.5 8 5 8.67 5 9.5s.67 1.5 1.5 1.5zM12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2z"
              />
              <path
                v-else
                d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 17.93c-3.95-.49-7-3.85-7-7.93 0-.62.08-1.21.21-1.79L9 15v1c0 1.1.9 2 2 2v1.93z"
              />
            </svg>
          </RouterLink>

          <RouterLink
            :to="isAdmin ? '/beides/systemeinstellungen' : '/einstellungen'"
            class="p-2 rounded-full hover:bg-black/10 transition"
          >
            <svg class="w-9 h-9" fill="currentColor" viewBox="0 0 24 24">
              <path
                d="M19.14 12.94c.04-.3.06-.61.06-.94 0-.32-.02-.64-.07-.94l2.03-1.58c.18-.14.23-.41.12-.61l-1.92-3.32c-.12-.22-.37-.29-.59-.22l-2.39.96c-.5-.38-1.03-.7-1.62-.94l-.36-2.54c-.04-.24-.24-.41-.48-.41h-3.84c-.24 0-.43.17-.47.41l-.36 2.54c-.59.24-1.13.57-1.62.94l-2.39-.96c-.22-.08-.47 0-.59.22L2.74 8.87c-.12.21-.08.47.12.61l2.03 1.58c-.05.3-.09.63-.09.94s.02.64.07.94l-2.03 1.58c-.18.14-.23.41-.12.61l1.92 3.32c.12.22.37.29.59.22l2.39-.96c.5.38 1.03.7 1.62.94l.36 2.54c.05.24.24.41.48.41h3.84c.24 0 .44-.17.47-.41l.36-2.54c.59-.24 1.13-.56 1.62-.94l2.39.96c.22.08.47 0 .59-.22l1.92-3.32c.12-.22.07-.47-.12-.61l-2.01-1.58zM12 15.6c-1.98 0-3.6-1.62-3.6-3.6s1.62-3.6 3.6-3.6 3.6 1.62 3.6 3.6-1.62 3.6-3.6 3.6z"
              />
            </svg>
          </RouterLink>

          <RouterLink v-if="isAdmin" to="/admin/profileinstellungen" class="ml-4">
            <div
              class="w-12 h-12 rounded-full border-2 border-white bg-green-700 flex items-center justify-center text-white font-bold hover:scale-105 transition-transform"
            >
              AD
            </div>
          </RouterLink>
        </nav>
      </div>
    </header>

    <!-- MOBILE TOP-BAR -->
    <div
      :class="[
        'md:hidden fixed top-0 left-0 right-0 h-14 text-white flex items-center justify-center font-bold text-lg shadow-md z-50 transition-colors duration-300',
        isAdmin ? 'bg-[#8B2626]' : 'bg-[#536B53]',
      ]"
    >
      <span>{{ mobileTitle }}</span>
    </div>

    <main class="flex-grow w-full max-w-7xl mx-auto p-4 pt-18 md:pt-8 pb-24">
      <RouterView />
    </main>

    <!-- MOBILE BOTTOM-BAR -->
    <nav
      class="md:hidden fixed bottom-0 left-0 right-0 h-16 bg-white border-t flex justify-around items-center z-50"
    >
      <RouterLink :to="isAdmin ? '/admin' : '/'" class="flex flex-col items-center text-gray-600">
        <span class="text-xl">🏠</span><span class="text-xs">Start</span>
      </RouterLink>
      <RouterLink
        :to="isAdmin ? '/admin/hunde' : '/tierheim'"
        class="flex flex-col items-center text-gray-600"
      >
        <span class="text-xl">🐾</span
        ><span class="text-xs">{{ isAdmin ? 'Hunde' : 'Tierheim' }}</span>
      </RouterLink>
      <RouterLink
        :to="isAdmin ? '/beides/systemeinstellungen' : '/einstellungen'"
        class="flex flex-col items-center text-gray-600"
      >
        <span class="text-xl">⚙️</span><span class="text-xs">Einstellungen</span>
      </RouterLink>
    </nav>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const isAdmin = computed(() => route.path.toLowerCase().startsWith('/admin'))

const mobileTitle = computed(() => {
  if (route.meta?.title) return route.meta.title
  return 'Tierheim Weiden'
})
</script>
