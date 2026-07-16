<!--muss noch für admin gemacht werden, aber die vorherige
      seiten sind noch nicht fertig-->

<template>
  <div class="flex w-full h-full p-4 md:p-8">
    <!-- LINKS: Die Liste (auf Desktop sichtbar) -->
    <div class="hidden md:block w-1/3 pr-8 border-r">
      <h3 class="text-xl font-bold text-gray-800 mb-6 font-sans">Unsere Hunde</h3>
      <div class="space-y-3">
        <div
          v-for="h in hundeListe"
          :key="h.id"
          @click="router.push(`/hund/${h.id}`)"
          :class="['p-3 rounded-full cursor-pointer transition-all flex items-center justify-between', 
                  route.params.id === h.id ? 'bg-[#C5D0C5]' : 'bg-gray-100 hover:bg-gray-200']"
        >
          <span class="font-bold text-sm">{{ h.name }}</span>
          <span v-if="route.params.id === h.id">></span>
        </div>
      </div>
    </div>

    <!-- RECHTS: Die Details (dein Design) -->
    <div class="w-full md:w-2/3 md:pl-8">
      <div class="max-w-md mx-auto">
        <!-- Bildbereich -->
        <div class="flex flex-col items-center">
          <img :src="hund.bild" class="w-full h-64 object-cover rounded-2xl shadow-md">
          <div class="flex gap-2 mt-4">
            <div class="w-16 h-16 bg-gray-200 rounded-lg"></div>
            <div class="w-16 h-16 bg-gray-200 rounded-lg flex items-center justify-center font-bold">+1</div>
          </div>
        </div>

        <!-- Beschreibung Box -->
        <div class="bg-[#C5D0C5] p-6 rounded-3xl mt-8">
          <h2 class="text-xl font-bold mb-4">Beschreibung</h2>
          <div class="grid grid-cols-2 gap-y-2 text-sm">
            <p class="font-bold">Alter:</p><p>{{ hund.alter }}</p>
            <p class="font-bold">Rasse:</p><p>{{ hund.rasse }}</p>
          </div>
          <div class="flex gap-2 mt-4">
            <span class="bg-white/50 px-3 py-1 rounded-full text-xs">Freundlich</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const hundeListe = ref([
  { id: 'wambo', name: 'Wambo' },
  { id: 'aris', name: 'Aris' },
  { id: 'mira', name: 'Mira' },
  { id: 'richy', name: 'Richy' },
  { id: 'xina', name: 'Xina' },
])

// Simulierte Daten - hier später echte Daten laden
const hund = ref({ name: 'Wambo', alter: '2 Jahre', rasse: 'Husky-Chow Mix', bild: '' })

// Überwacht die URL-Änderung, um neue Hundedaten zu laden
watch(() => route.params.id, (newId) => {
  // Hier könntest du die Daten basierend auf der neuen ID aktualisieren
  console.log("Neuer Hund geladen:", newId)
}, { immediate: true })
</script>