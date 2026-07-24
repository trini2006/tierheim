<template>
  <div class="w-full min-h-screen bg-white px-6 py-8 text-gray-800">
    <div class="w-full max-w-xl mx-auto space-y-10">
      
      <!-- Sektion: Persönliches (nur sichtbar, wenn die vorherige Seite KEIN Admin-Pfad war) -->
      <section v-if="!wasAdmin">
        <div class="flex items-center justify-between mb-2">
          <div class="flex items-center gap-3">
            <span class="text-2xl">👤</span>
            <h2 class="text-xl font-bold">Persönliches</h2>
          </div>
          <div class="w-4 h-4 bg-orange-500 rounded-full"></div>
        </div>
        <div class="px-2">
          <p class="text-gray-600 text-sm">Mitgliednummer: 1234567890</p>
        </div>
      </section>

      <!-- Sektion 1: Allgemein -->
      <section>
        <div class="flex items-center gap-3 mb-6">
          <span class="text-2xl">🔧</span>
          <h2 class="text-xl font-bold">Allgemein</h2>
        </div>
        <div class="flex items-center justify-between px-2">
          <span class="font-medium text-gray-800">Schriftgröße</span>
          <div class="bg-gray-100 p-1 rounded-full flex gap-1">
            <button @click="fontSize = 'normal'" :class="[fontSize === 'normal' ? 'bg-[#536B53] text-white' : 'hover:bg-gray-200']" class="px-5 py-1.5 rounded-full text-sm font-medium transition">Normal</button>
            <button @click="fontSize = 'groß'" :class="[fontSize === 'groß' ? 'bg-[#536B53] text-white' : 'hover:bg-gray-200']" class="px-5 py-1.5 rounded-full text-sm font-medium transition">Groß</button>
          </div>
        </div>
      </section>

      <!-- Sektion 2: Benachrichtigungen -->
      <section>
        <div class="flex items-center gap-3 mb-6">
          <span class="text-2xl">🔔</span>
          <h2 class="text-xl font-bold">Benachrichtigungen</h2>
        </div>
        <div class="space-y-4 px-2">
          <div class="flex items-center justify-between">
            <span class="text-gray-800">Nachrichtenton</span>
            <div class="bg-gray-100 p-1 rounded-full flex gap-1">
              <button @click="sound = true" :class="[sound ? 'bg-[#536B53] text-white' : 'hover:bg-gray-200']" class="px-5 py-1.5 rounded-full text-sm font-medium transition">Ja</button>
              <button @click="sound = false" :class="[!sound ? 'bg-[#536B53] text-white' : 'hover:bg-gray-200']" class="px-5 py-1.5 rounded-full text-sm font-medium transition">Nein</button>
            </div>
          </div>
          <div class="flex items-center justify-between">
            <span class="text-gray-800">Erinnerung</span>
            <div class="bg-gray-100 p-1 rounded-full flex gap-1">
              <button @click="reminder = true" :class="[reminder ? 'bg-[#536B53] text-white' : 'hover:bg-gray-200']" class="px-5 py-1.5 rounded-full text-sm font-medium transition">Ja</button>
              <button @click="reminder = false" :class="[!reminder ? 'bg-[#536B53] text-white' : 'hover:bg-gray-200']" class="px-5 py-1.5 rounded-full text-sm font-medium transition">Nein</button>
            </div>
          </div>
        </div>
      </section>

      <!-- Sektion 3: App-Sprache -->
      <section>
        <div class="flex items-center gap-3 mb-6">
          <span class="text-2xl">🌐</span>
          <h2 class="text-xl font-bold">App-Sprache</h2>
        </div>
        <div class="px-2">
          <p class="text-gray-700">Deutsch</p>
        </div>
      </section>

      <!-- Trennlinie -->
      <hr class="border-gray-200 my-6" />

      <!-- Sektion 4: Abmelden / Logout -->
      <section class="px-2">
        <button 
          @click="ausloggen"
          class="w-full py-3 px-4 bg-red-50 hover:bg-red-100 text-red-700 font-semibold rounded-2xl border border-red-200 transition-colors flex items-center justify-center gap-2"
        >
          <span>🚪</span> Abmelden
        </button>
      </section>

    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { previousPath } from '@/router.js' 

const router = useRouter()

// Korrigierter Check (einfacher Slash statt doppeltem Slash)
const wasAdmin = computed(() => previousPath.value.includes('app/admin'))

const fontSize = ref('normal')
const sound = ref(true)
const reminder = ref(true)

const ausloggen = () => {
  // Löscht die gespeicherte Rolle und leitet zum Login zurück
  localStorage.removeItem('userRole')
  router.push('/')
}
</script>